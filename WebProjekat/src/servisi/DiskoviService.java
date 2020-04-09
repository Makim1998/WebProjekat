package servisi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import json.DiskJson;
import model.Disk;
import model.Diskovi;
import model.Korisnik;
import model.Organizacija;
import model.Organizacije;
import model.VirtuelnaMasina;
import model.VirtuelneMasine;

@Path("/diskovi")
public class DiskoviService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	@Context
	HttpServletResponse response;
	
	private static Gson g = new Gson();
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Disk> getDiskovi() {
		Diskovi diskovi = (Diskovi)ctx.getAttribute("diskovi");
		if (diskovi == null) {
			String ctxPath = ctx.getRealPath("") + "//data//diskovi.txt";
			try {
				JsonReader reader = new JsonReader(new FileReader(ctxPath));
				diskovi = new Diskovi();
				diskovi.setDiskovi(g.fromJson(reader, new TypeToken<ArrayList<Disk>>(){}.getType()));
				ctx.setAttribute("diskovi", diskovi);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		response.setStatus(200);
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovan");
		if (korisnik.getUloga().toString().equals("super admin"))
			return diskovi.getDiskovi();
		else {
			ArrayList<Disk> ret = new ArrayList<Disk>();
			for (Disk d: diskovi.getDiskovi()) {
				if (d.getOrganizacija().equals(korisnik.organizacija))
					ret.add(d);
			}
			return ret;
		}
	}
	
	@POST
	@Path("/dodaj")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Disk dodajDisk(DiskJson noviDisk) {
		Disk disk = new Disk();
		disk.setIme(noviDisk.ime);
		disk.setTip(noviDisk.tip);
		disk.setKapacitet(noviDisk.kapacitet);
		VirtuelneMasine masine = (VirtuelneMasine)ctx.getAttribute("masine");
		if (masine.getMasina(noviDisk.virtuelnaMasina) == null) {
			System.out.println("Ne postoji takva virtuelna masina!");
			response.setStatus(400);
			return null;
		}
		disk.setVirtuelnaMasina(noviDisk.virtuelnaMasina);
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovan");
		disk.setOrganizacija(korisnik.getOrganizacija());
		Organizacije organizacije = (Organizacije) ctx.getAttribute("organizacije");
		Diskovi diskovi = (Diskovi) ctx.getAttribute("diskovi");
		if (diskovi.getDisk(disk.getIme()) != null) {
			System.out.println("Vec postoji takav disk!");
			response.setStatus(400);
			return null;
		}
		if (korisnik.getUloga().toString().equals("korisnik")) {
			System.out.println("Nemate pravo da dodajete disk!");
			response.setStatus(403);
			return null;
		}
		if (korisnik.getUloga().toString().equals("admin")) {
			Organizacija organizacija = organizacije.getOrganizacija(korisnik.getOrganizacija());
			if (!organizacija.getListaKorisnika().contains(korisnik.getEmail())) {
				System.out.println("Ne pripadate datoj organizaciji!");
				response.setStatus(403);
				return null;
			}
		}
		diskovi.diskovi.add(disk);
		ctx.setAttribute("diskovi", diskovi);
		try {
			FileWriter writer = new FileWriter(ctx.getRealPath("") + "\\data\\diskovi.txt", false);
			String data = g.toJson(diskovi);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VirtuelnaMasina masina = masine.getMasina(noviDisk.virtuelnaMasina);
		masina.addDisk(noviDisk.ime);
		int index = masine.masine.indexOf(masina);
		masine.masine.set(index, masina);
		ctx.setAttribute("masine", masine);
		try {
			FileWriter writer = new FileWriter(ctx.getRealPath("") + "\\data\\masine.txt", false);
			String data = g.toJson(masine);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setStatus(200);
		return disk;
	}
	
	@GET
	@Path("/izmeni")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String izmeniDisk(DiskJson zaIzmenu) {
		String id = zaIzmenu.ime;
		Diskovi diskovi = (Diskovi) ctx.getAttribute("diskovi");
		Disk izmenjena = diskovi.getDisk(id);
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovan");
		if (izmenjena == null) {
			System.out.println("Ne postoji takav disk");
			response.setStatus(400);
			return "Ne postoji takav disk";
		}
		int index = diskovi.diskovi.indexOf(izmenjena);
		izmenjena.setIme(zaIzmenu.novoIme);
		izmenjena.setKapacitet(zaIzmenu.kapacitet);
		izmenjena.setTip(zaIzmenu.tip);
		VirtuelneMasine masine = (VirtuelneMasine)ctx.getAttribute("masine");
		if (masine.getMasina(zaIzmenu.virtuelnaMasina) == null) {
			System.out.println("Ne postoji takva virtuelna masina!");
			response.setStatus(400);
			return null;
		}
		izmenjena.setVirtuelnaMasina(zaIzmenu.virtuelnaMasina);
		Organizacije organizacije = (Organizacije)ctx.getAttribute("organizacije");
		izmenjena.setOrganizacija(korisnik.getOrganizacija());
		if (korisnik.getUloga().toString().equals("korisnik")) {
			System.out.println("Nemate pravo da menjate disk!");
			response.setStatus(403);
			return null;
		}
		if (korisnik.getUloga().toString().equals("admin")) {
			Organizacija organizacija = organizacije.getOrganizacija(korisnik.getOrganizacija());
			if (!organizacija.getListaKorisnika().contains(korisnik.getEmail())) {
				System.out.println("Ne pripadate datoj organizaciji!");
				response.setStatus(403);
				return null;
			}
		}
		diskovi.diskovi.set(index, izmenjena);
		ctx.setAttribute("diskovi", diskovi);
		try {
			FileWriter writer = new FileWriter(ctx.getRealPath("") + "\\data\\diskovi.txt", false);
			String data = g.toJson(diskovi);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VirtuelnaMasina masina = masine.getMasina(zaIzmenu.virtuelnaMasina);
		masina.addDisk(zaIzmenu.novoIme);
		int index2 = masine.masine.indexOf(masina);
		masine.masine.set(index2, masina);
		ctx.setAttribute("masine", masine);
		try {
			FileWriter writer = new FileWriter(ctx.getRealPath("") + "\\data\\masine.txt", false);
			String data = g.toJson(masine);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		response.setStatus(200);
		return "OK";
	}
	
	@POST
	@Path("/brisanje")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String obrisiDisk(String ime) {
		Diskovi diskovi = (Diskovi)ctx.getAttribute("diskovi");
		if(diskovi.getDisk(ime) == null) {
			System.out.println("Neispravan disk");
			response.setStatus(400);
			return "Notok";
		};
		Korisnik korisnik = (Korisnik)request.getSession().getAttribute("ulogovan");
		if (!korisnik.getUloga().toString().equals("super admin")) {
			response.setStatus(403);
			return "Nemate pravo na brisanje diska!";
		}
		Disk zaBrisanje = diskovi.getDisk(ime);
		int index = diskovi.diskovi.indexOf(zaBrisanje);
		diskovi.diskovi.remove(index);
		ctx.setAttribute("diskovi", diskovi);
		try {
			FileWriter writer = new FileWriter(ctx.getRealPath("") + "\\data\\diskovi.txt", false);
			String data = g.toJson(diskovi);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VirtuelneMasine masine = (VirtuelneMasine)ctx.getAttribute("masine");
		VirtuelnaMasina masina = masine.getMasina(diskovi.getDisk(ime).getVirtuelnaMasina());
		masina.removeDisk(diskovi.getDisk(ime).getIme());
		int index2 = masine.masine.indexOf(masina);
		masine.masine.set(index2, masina);
		ctx.setAttribute("masine", masine);
		try {
			FileWriter writer = new FileWriter(ctx.getRealPath("") + "\\data\\masine.txt", false);
			String data = g.toJson(masine);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		response.setStatus(200);
		return "OK";
	}
	
}
