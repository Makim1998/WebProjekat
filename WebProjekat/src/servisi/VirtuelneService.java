package servisi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import json.VM;
import model.Disk;
import model.Diskovi;
import model.KategorijaVM;
import model.Kategorije;
import model.Korisnik;
import model.Organizacije;
import model.VirtuelnaMasina;
import model.VirtuelneMasine;


@Path("/masine")
public class VirtuelneService {
	
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
	public Collection<VirtuelnaMasina> getVMasine() {
		VirtuelneMasine masine = (VirtuelneMasine)ctx.getAttribute("masine");
		if (masine == null) {
			String ctxPath = ctx.getRealPath("") + "//data//masine.txt";
			try {
				JsonReader reader = new JsonReader(new FileReader(ctxPath));
				masine = new VirtuelneMasine();
				masine.setMasine(g.fromJson(reader, new TypeToken<ArrayList<VirtuelnaMasina>>(){}.getType()));
				ctx.setAttribute("masine", masine);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		response.setStatus(200);
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovan");
		if (korisnik.getUloga().toString().equals("super admin"))
			return masine.getMasine();
		else {
			ArrayList<VirtuelnaMasina> ret = new ArrayList<VirtuelnaMasina>();
			for (VirtuelnaMasina vm: masine.getMasine()) {
				if (vm.getOrganizacija().equals(korisnik.getOrganizacija()))
					ret.add(vm);
			}
			return ret;
		}
	}
	
	private KategorijaVM getKategorija(String imeKat) {
		String ctxPath = ctx.getRealPath("") + "//data//kategorije.txt";
		Kategorije kategorije = new Kategorije();
		try {
			if (ctx.getAttribute("kategorije") == null) {
				JsonReader reader = new JsonReader(new FileReader(ctxPath));
				kategorije.setKategorije(g.fromJson(reader, new TypeToken<ArrayList<KategorijaVM>>(){}.getType()));
				ctx.setAttribute("kategorije", kategorije);
			}else {
				kategorije = (Kategorije)ctx.getAttribute("kategorije");
			}
			return kategorije.getKategorija(imeKat);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@POST
	@Path("/dodaj")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public VirtuelnaMasina dodajMasina(VM novaVM) throws ParseException {
		Korisnik korisnik = (Korisnik)request.getSession().getAttribute("ulogovan");
		if (korisnik.getUloga().toString().equals("korisnik")) {
			System.out.println("Nemate pravo da dodajete virtuelnu masinu");
			response.setStatus(403);
			return null;
		}
		VirtuelnaMasina masina = new VirtuelnaMasina();
		masina.setIme(novaVM.ime);
		if (getKategorija(novaVM.kategorija) == null) {
			System.out.println("Ne postoji takva kategorija!");
			response.setStatus(400);
			return null;
		}
		masina.setKategorija(getKategorija(novaVM.kategorija));
		if (korisnik.getUloga().toString().equals("admin")) {
			if (!korisnik.getOrganizacija().equals(novaVM.organizacija)) {
				System.out.println("Ne pripadate datoj organizaciji");
				response.setStatus(403);
				return null;
			}
		}
		Organizacije organizacije = (Organizacije)ctx.getAttribute("organizacije");
		if (organizacije.getOrganizacija(novaVM.organizacija) == null) {
			System.out.println("Ne postoji takva organizacija!");
			response.setStatus(400);
			return null;
		}
		masina.setOrganizacija(novaVM.organizacija);
		masina.setDiskovi(new ArrayList<String>());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		masina.setDatumPaljenja(sdf.parse(sdf.format(new Date())));
		masina.setDatumGasenja(sdf.parse(sdf.format(new Date())));
		VirtuelneMasine masine = (VirtuelneMasine) ctx.getAttribute("masine");
		if (masine.getMasina(masina.getIme()) != null) {
			System.out.println("Vec postoji takva virtuelna masina!");
			response.setStatus(400);
			return null;
		}
		masine.masine.add(masina);
		ctx.setAttribute("masine", masine);
		response.setStatus(200);
		try {
			FileWriter writer = new FileWriter(ctx.getRealPath("") + "\\data\\masine.txt", false);
			String data = g.toJson(masine);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return masina;
	}
	
	@GET
	@Path("/izmeni/{nazivVM}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String izmeniMasina(@PathParam("nazivVM") String id, VM zaIzmenu) {
		Korisnik korisnik = (Korisnik)request.getSession().getAttribute("ulogovan");
		if (korisnik.getUloga().toString().equals("korisnik")) {
			System.out.println("Nemate pravo da dodajete virtuelnu masinu");
			response.setStatus(403);
			return null;
		}
		VirtuelneMasine masine = (VirtuelneMasine) ctx.getAttribute("masine");
		VirtuelnaMasina izmenjena = masine.getMasina(id);
		if (izmenjena == null) {
			System.out.println("Ne postoji takva virtuelna masina");
			return "Ne postoji takva virtuelna masina";
		}
		int index = masine.masine.indexOf(izmenjena);
		izmenjena.setIme(zaIzmenu.ime);
		if (getKategorija(zaIzmenu.kategorija) == null) {
			System.out.println("Ne postoji takva kategorija!");
			response.setStatus(400);
			return null;
		}
		izmenjena.setKategorija(getKategorija(zaIzmenu.kategorija));
		if (korisnik.getUloga().toString().equals("admin")) {
			if (!korisnik.getOrganizacija().equals(zaIzmenu.organizacija)) {
				System.out.println("Ne pripadate datoj organizaciji");
				response.setStatus(403);
				return null;
			}
		}
		izmenjena.setKategorija(getKategorija(zaIzmenu.kategorija));
		Organizacije organizacije = (Organizacije)ctx.getAttribute("organizacije");
		if (organizacije.getOrganizacija(zaIzmenu.organizacija) == null) {
			System.out.println("Ne postoji takva organizacija!");
			response.setStatus(400);
			return null;
		}
		izmenjena.setOrganizacija(zaIzmenu.organizacija);
		masine.masine.set(index, izmenjena);
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
		return "OK";
	}
	
	@POST
	@Path("/brisanje")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String obrisiMasina(String ime) {
		Korisnik korisnik = (Korisnik)request.getSession().getAttribute("ulogovan");
		if (korisnik.getUloga().toString().equals("korisnik")) {
			System.out.println("Nemate pravo da brisete virtuelnu masinu");
			response.setStatus(403);
			return null;
		}
		VirtuelneMasine masine = (VirtuelneMasine) ctx.getAttribute("masine");
		if(masine.getMasina(ime) == null) {
			System.out.println("Neispravna virtuelna masina");
			response.setStatus(400);
			return "Notok";
		};
		VirtuelnaMasina zaBrisanje = masine.getMasina(ime);
		int index = masine.masine.indexOf(zaBrisanje);
		masine.masine.remove(index);
		Diskovi diskovi = new Diskovi();
		if (ctx.getAttribute("diskovi") == null) {
			JsonReader reader;
			try {
				reader = new JsonReader(new FileReader(ctx.getRealPath("") + "//data//diskovi.txt"));
				diskovi = new Diskovi();
				diskovi.setDiskovi(g.fromJson(reader, new TypeToken<ArrayList<Disk>>(){}.getType()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			diskovi = (Diskovi)ctx.getAttribute("diskovi");
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
		for (Disk disk: diskovi.getDiskovi()) {
			if (disk.getVirtuelnaMasina().equals(zaBrisanje.getIme()))
				diskovi.diskovi.remove(diskovi.diskovi.indexOf(disk));
		}
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
		response.setStatus(200);
		return "OK";
	}
	
	@GET
	@Path("/aktivacija")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String aktivirajVM(String ime) {
		Korisnik korisnik = (Korisnik)request.getSession().getAttribute("ulogovan");
		if (korisnik.getUloga().toString().equals("korisnik")) {
			System.out.println("Nemate pravo da brisete virtuelnu masinu");
			response.setStatus(403);
			return "Nemate pravo da brisete virtuelnu masinu";
		}
		VirtuelneMasine masine = (VirtuelneMasine) ctx.getAttribute("masine");
		if(masine.getMasina(ime) == null) {
			System.out.println("Neispravna virtuelna masina");
			response.setStatus(400);
			return "Notok";
		};
		VirtuelnaMasina aktivirana = masine.getMasina(ime);
		int index = masine.masine.indexOf(aktivirana);
		if (aktivirana.getDatumPaljenja().before(aktivirana.getDatumGasenja()))
			aktivirana.setDatumPaljenja(new Date());
		else
			aktivirana.setDatumGasenja(new Date());
		masine.masine.set(index, aktivirana);
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
		return "OK";
	}

}
