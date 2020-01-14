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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import model.Korisnici;
import model.Korisnik;


@Path("/korisnici")
public class KorisniciService {
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	private static Gson g = new Gson();
	
	@GET
	@Path("/getKorisnici")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Korisnik> getJustProducts() {
		System.out.println("Korisnici");
		return getKorisnici().korisnici;
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik addKorisnik(Korisnik k) {
		System.out.println("Dodavanje korisnika");
		System.out.println(k);
		if(getKorisnici().getKorisnik(k.email) != null) {
			System.out.println("Neispravan korisnik");
			return null;
		};
		System.out.println("Ispravan korisnik");
		((Korisnici) ctx.getAttribute("korisnici")).korisnici.add(k);
		return k;
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
