package model;

import java.util.ArrayList;

public class Korisnici {
	public ArrayList<Korisnik> korisnici;

	public ArrayList<Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(ArrayList<Korisnik> korisnici) {
		this.korisnici = korisnici;
	}
	
	public Korisnici() {
		korisnici = new ArrayList<Korisnik>();
	}
	
	public Korisnik getKorisnik(String username) {
		for(Korisnik k : korisnici) {
			if(k.email.equals(username)) {
				return k;
			}
		}
		return null;
	}
}
