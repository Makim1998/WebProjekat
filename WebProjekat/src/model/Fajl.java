package model;
import java.io.FileReader;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
public class Fajl {
	private static Gson g = new GsonBuilder().setPrettyPrinting().create();
	
	public static void main(String[] args) {
		Korisnik k = new Korisnik("admin@gmail.com","admin","Marko","Mijatovic","AMD",Uloga.SUPER_ADMIN);
		Korisnik k1 = new Korisnik("ana@gmail.com","ana","Ana","Mijatovic","Intel",Uloga.KORISNIK);
		Korisnik k2 = new Korisnik("marko@gmail.com","ana","Ana","Mijatovic","Nvidia",Uloga.ADMIN);
		ArrayList<Korisnik> korisnici = new ArrayList<Korisnik>();
		korisnici.add(k);
		korisnici.add(k1);
		korisnici.add(k2);
		String dir =  System.getProperty("user.dir");
		System.out.println(dir + "\\resources\\korisnici.txt");
		try {
			FileWriter fw = new FileWriter("data.txt");
			String data = g.toJson(korisnici);
			System.out.println(data);
			
			System.out.println(data);
			fw.write(data);
			fw.close();
			JsonReader jr = new JsonReader( new FileReader("data.txt"));
			ArrayList<Korisnik> orisnici = g.fromJson(jr, new TypeToken<ArrayList<Korisnik>>(){}.getType());
			for(Korisnik ko:orisnici) {
				System.out.println(ko);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
