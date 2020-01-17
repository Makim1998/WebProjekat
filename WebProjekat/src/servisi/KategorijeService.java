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

import model.KategorijaVM;
import model.Kategorije;

@Path("/kategorije")
public class KategorijeService {
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	private static Gson g = new Gson();
	
	@GET
	@Path("/getKategorije")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<KategorijaVM> getJustValues() {
		System.out.println("Kategorije");
		return getKategorije().kategorije;
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public KategorijaVM addKategorija(KategorijaVM k) {
		System.out.println("Dodavanje korisnika");
		System.out.println(k);
		if(getKategorije().getKategorija(k.getIme()) != null) {
			System.out.println("Neispravna kategorija");
			return null;
		};
		System.out.println("Ispravna kategorija");
		((Kategorije) ctx.getAttribute("kategorije")).kategorije.add(k);
		return k;
	}
	private  Kategorije getKategorije(){
		Kategorije  kategorije =  (Kategorije) ctx.getAttribute("kategorije");
		if(kategorije == null ) {
			try {
				String dir =  ctx.getRealPath("");
				//System.out.println(dir + "\\data\\korisnici.txt");
				JsonReader reader = new JsonReader(new FileReader(ctx.getRealPath("") + "\\data\\kategorije.txt"));
				kategorije = new Kategorije();
				kategorije.setKategorije( g.fromJson(reader, new TypeToken<ArrayList<KategorijaVM>>(){}.getType()));
				ctx.setAttribute("kategorije", kategorije);
				for(KategorijaVM kat: kategorije.getKategorije()) {
					System.out.println(kat.getRAM());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		return kategorije;
		
	}
}
