package model;

import java.util.ArrayList;
<<<<<<< HEAD
=======

public class Organizacija {
	
	private String ime;
	private String opis;
	private String logo;
	private ArrayList<String> listaKorisnika;
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public ArrayList<String> getListaKorisnika() {
		return listaKorisnika;
	}
	public void setListaKorisnika(ArrayList<String> listaKorisnika) {
		this.listaKorisnika = listaKorisnika;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Organizacija))
			return false;
		Organizacija org = (Organizacija)arg0;
		return this.ime.equals(org.ime);
	}
	
	@Override
	public String toString() {
		return "Organizacija: [ime=" + this.ime + ", opis=" + this.opis
				+ ", logo" + this.logo + "]";
	}
>>>>>>> branch 'master' of https://github.com/Makim1998/WebProjekat.git

public class Organizacija {
	public String ime;
	public String opis;
	public ArrayList<String> korisnici;
	public ArrayList<String> vm;
	
}
