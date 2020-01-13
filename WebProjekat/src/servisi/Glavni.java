package servisi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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

import json.KorisnikLog;
import model.Korisnici;
import model.Korisnik;


@Path("/aplikacija")
public class Glavni {
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	private static Gson g = new Gson();

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik login(KorisnikLog korisnik) {
		System.out.println("Login");
		Korisnik ulogovan = validiraj(korisnik);
		request.getSession().setAttribute("ulogovan", ulogovan);
		return ulogovan;
		
	}
	
	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik getUser() {
		Korisnik ulogovan = (Korisnik) request.getSession().getAttribute("ulogovan");
		System.out.println(ulogovan);
		return ulogovan;
		
	}
	@GET
	@Path("/user")
	@Produces(MediaType.TEXT_PLAIN)
	public String logout() {
		request.getSession().invalidate();
		return "OK";
		
	}
	
	private Korisnik validiraj(KorisnikLog korisnik) {
		for(Korisnik k: getKorisnici().korisnici) {
			if(k.getEmail().equals(korisnik.username) && k.getLozinka().equals(korisnik.password)) {
				return k;
			}
		}
		return null;
	}

	private  Korisnici getKorisnici(){
		Korisnici  korisnici =  (Korisnici) ctx.getAttribute("korisnici");
		if(korisnici == null ) {
			try {
				String dir =  ctx.getRealPath("");
				System.out.println(dir + "\\data\\korisnici.txt");
				JsonReader reader = new JsonReader(new FileReader(ctx.getRealPath("") + "\\data\\korisnici.txt"));
				korisnici = new Korisnici();
				korisnici.setKorisnici( g.fromJson(reader, new TypeToken<ArrayList<Korisnik>>(){}.getType()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			ctx.setAttribute("korisnici", korisnici);
		}
		return korisnici;
	}
	
}
