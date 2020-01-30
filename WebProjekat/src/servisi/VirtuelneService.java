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
import model.KategorijaVM;
import model.Kategorije;
import model.VirtuelnaMasina;
import model.VirtuelneMasine;


@Path("/masine")
public class VirtuelneService {
	
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	private static Gson g = new Gson();
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<VirtuelnaMasina> getDiskovi() {
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
		return masine.getMasine();
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
	public VirtuelnaMasina dodajDisk(VM novaVM) throws ParseException {
		VirtuelnaMasina masina = new VirtuelnaMasina();
		masina.setIme(novaVM.ime);
		masina.setKategorija(getKategorija(novaVM.kategorija));
		masina.setOrganizacija(novaVM.organizacija);
		masina.setDiskovi(new ArrayList<String>());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		masina.setDatumPaljenja(sdf.parse(sdf.format(new Date())));
		masina.setDatumGasenja(sdf.parse(sdf.format(new Date())));
		VirtuelneMasine masine = (VirtuelneMasine) ctx.getAttribute("masine");
		if (masine.getMasina(masina.getIme()) != null) {
			System.out.println("Vec postoji takva virtuelna masina!");
			return null;
		}
		masine.masine.add(masina);
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
		return masina;
	}
	
	@GET
	@Path("/izmeni/{nazivVM}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String izmeniDisk(@PathParam("nazivVM") String id, VM zaIzmenu) {
		VirtuelneMasine masine = (VirtuelneMasine) ctx.getAttribute("masine");
		VirtuelnaMasina izmenjena = masine.getMasina(id);
		if (izmenjena == null) {
			System.out.println("Ne postoji takva virtuelna masina");
			return "Ne postoji takva virtuelna masina";
		}
		int index = masine.masine.indexOf(izmenjena);
		izmenjena.setIme(zaIzmenu.ime);
		izmenjena.setKategorija(getKategorija(zaIzmenu.kategorija));
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
		return "OK";
	}
	
	@POST
	@Path("/brisanje")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String obrisiDisk(String ime) {
		VirtuelneMasine masine = (VirtuelneMasine) ctx.getAttribute("masine");
		if(masine.getMasina(ime) == null) {
			System.out.println("Neispravna virtuelna masina");
			return "Notok";
		};
		VirtuelnaMasina zaBrisanje = masine.getMasina(ime);
		int index = masine.masine.indexOf(zaBrisanje);
		masine.masine.remove(index);
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
		return "OK";
	}
	
	@GET
	@Path("/aktivacija")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String aktivirajVM(String ime) {
		VirtuelneMasine masine = (VirtuelneMasine) ctx.getAttribute("masine");
		if(masine.getMasina(ime) == null) {
			System.out.println("Neispravna virtuelna masina");
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
		return "OK";
	}

}
