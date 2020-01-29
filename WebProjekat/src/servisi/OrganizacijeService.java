package servisi;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

import json.OrganizacijaInfo;
import model.Organizacija;
import model.Organizacije;

@Path("/organizacije")
public class OrganizacijeService {
	
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	private static Gson g = new Gson();
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Organizacija> getOrganizacije() {
		Organizacije organizacije = (Organizacije)ctx.getAttribute("organizacije");
		if (organizacije == null) {
			String ctxPath = ctx.getRealPath("") + "//data//organizacije.txt";
			try {
				JsonReader reader = new JsonReader(new FileReader(ctxPath));
				organizacije = new Organizacije();
				organizacije.setOrganizacije(g.fromJson(reader, new TypeToken<ArrayList<Organizacija>>(){}.getType()));
				ctx.setAttribute("organizacije", organizacije);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return organizacije.getOrganizacije();
	}
	
	@POST
	@Path("/dodaj")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Organizacija dodajOrg(OrganizacijaInfo novaOrg) {
		Organizacija organizacija = new Organizacija();
		organizacija.setIme(novaOrg.ime);
		organizacija.setLogo(novaOrg.logo);
		organizacija.setOpis(novaOrg.opis);
		organizacija.setListaKorisnika(new ArrayList<String>());
		Organizacije organizacije = (Organizacije) ctx.getAttribute("organizacije");
		if (organizacije.getOrganizacija(organizacija.getIme()) != null) {
			System.out.println("Vec postoji takva organizacija!");
			return null;
		}
		organizacije.organizacije.add(organizacija);
		ctx.setAttribute("organizacije", organizacije);
		return organizacija;
	}

	@GET
	@Path("/izmeni/{nazivOrg}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String izmeniOrg(@PathParam("nazivOrg") String id, OrganizacijaInfo zaIzmenu) {
		Organizacije organizacije = (Organizacije) ctx.getAttribute("organizacije");
		Organizacija izmenjena = organizacije.getOrganizacija(id);
		if (izmenjena == null) {
			System.out.println("Ne postoji takva organizacija");
			return "Ne postoji takva organizacija";
		}
		int index = organizacije.organizacije.indexOf(izmenjena);
		izmenjena.setIme(zaIzmenu.ime);
		izmenjena.setLogo(zaIzmenu.logo);
		izmenjena.setOpis(zaIzmenu.opis);
		izmenjena.setListaKorisnika(new ArrayList<String>());
		organizacije.organizacije.set(index, izmenjena);
		ctx.setAttribute("organizacije", organizacije);
		return "OK";
	}
}