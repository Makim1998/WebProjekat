package servisi;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

import json.OrganizacijaInfo;
import model.Korisnik;
import model.Organizacija;
import model.Organizacije;

@Path("/organizacije")
public class OrganizacijeService {
	
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
		response.setStatus(200);
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovan");
		if (korisnik.getUloga().toString().equals("super admin"))
			return organizacije.getOrganizacije();
		else {
			return new ArrayList<Organizacija>();
		}
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
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovan");
		if (!korisnik.getUloga().toString().equals("super admin")) {
			System.out.println("Ne mozete da dodajete organizacije");
			response.setStatus(403);
			return null;
		}
		Organizacije organizacije = (Organizacije) ctx.getAttribute("organizacije");
		if (organizacije.getOrganizacija(organizacija.getIme()) != null) {
			System.out.println("Vec postoji takva organizacija!");
			response.setStatus(400);
			return null;
		}
		organizacije.organizacije.add(organizacija);
		ctx.setAttribute("organizacije", organizacije);
		response.setStatus(200);
		return organizacija;
	}

	@GET
	@Path("/izmeni")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String izmeniOrg(OrganizacijaInfo zaIzmenu) {
		String id = zaIzmenu.ime;
		Organizacije organizacije = (Organizacije) ctx.getAttribute("organizacije");
		Organizacija izmenjena = organizacije.getOrganizacija(id);
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovan");
		if (korisnik.uloga.toString().equals("korisnik")) {
			response.setStatus(403);
			return "Nemate pravo na izmenu organizacije!";
		}
		if (korisnik.uloga.toString().equals("admin")) {
			if (!izmenjena.hasKorisnik(korisnik.getEmail())) {
				response.setStatus(403);
				return "Ne pripadate datoj organizaciji!";
			}
		}
		if (izmenjena == null) {
			System.out.println("Ne postoji takva organizacija");
			response.setStatus(400);
			return "Ne postoji takva organizacija";
		}
		for (Organizacija o: organizacije.getOrganizacije()) {
			if (o.getIme().equals(zaIzmenu.novoIme) && !o.getIme().equals(id)) {
				System.out.println("Vec postoji organizacija sa tim imenom");
				response.setStatus(400);
				return "Vec postoji organizacija sa tim imenom";
			}
		}
		int index = organizacije.organizacije.indexOf(izmenjena);
		izmenjena.setIme(zaIzmenu.novoIme);
		izmenjena.setLogo(zaIzmenu.logo);
		izmenjena.setOpis(zaIzmenu.opis);
		organizacije.organizacije.set(index, izmenjena);
		ctx.setAttribute("organizacije", organizacije);
		response.setStatus(200);
		return "OK";
	}
}
