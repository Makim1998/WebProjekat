package servisi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
import com.google.gson.stream.JsonWriter;

import json.DiskJson;
import model.Disk;
import model.Diskovi;

@Path("/diskovi")
public class DiskoviService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
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
		return diskovi.getDiskovi();
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
		disk.setVirtuelnaMasina(noviDisk.virtuelnaMasina);
		Diskovi diskovi = (Diskovi) ctx.getAttribute("diskovi");
		if (diskovi.getDisk(disk.getIme()) != null) {
			System.out.println("Vec postoji takav disk!");
			return null;
		}
		diskovi.diskovi.add(disk);
		ctx.setAttribute("diskovi", diskovi);
		try {
			JsonWriter writer = new JsonWriter(new FileWriter(ctx.getRealPath("") + "\\data\\diskovi.txt", false));
			g.toJson(writer, new TypeToken<ArrayList<Disk>>(){}.getType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return disk;
	}
	
	@GET
	@Path("/izmeni/{nazivDiska}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String izmeniDisk(@PathParam("nazivDiska") String id, DiskJson zaIzmenu) {
		Diskovi diskovi = (Diskovi) ctx.getAttribute("diskovi");
		Disk izmenjena = diskovi.getDisk(id);
		if (izmenjena == null) {
			System.out.println("Ne postoji takav disk");
			return "Ne postoji takav disk";
		}
		int index = diskovi.diskovi.indexOf(izmenjena);
		izmenjena.setIme(zaIzmenu.ime);
		izmenjena.setKapacitet(zaIzmenu.kapacitet);
		izmenjena.setTip(zaIzmenu.tip);
		izmenjena.setVirtuelnaMasina(zaIzmenu.virtuelnaMasina);
		diskovi.diskovi.set(index, izmenjena);
		ctx.setAttribute("diskovi", diskovi);
		try {
			JsonWriter writer = new JsonWriter(new FileWriter(ctx.getRealPath("") + "\\data\\diskovi.txt", false));
			g.toJson(writer, new TypeToken<ArrayList<Disk>>(){}.getType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			return "Notok";
		};
		Disk zaBrisanje = diskovi.getDisk(ime);
		int index = diskovi.diskovi.indexOf(zaBrisanje);
		diskovi.diskovi.remove(index);
		ctx.setAttribute("diskovi", diskovi);
		try {
			JsonWriter writer = new JsonWriter(new FileWriter(ctx.getRealPath("") + "\\data\\diskovi.txt", false));
			g.toJson(writer, new TypeToken<ArrayList<Disk>>(){}.getType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK";
	}
	
}
