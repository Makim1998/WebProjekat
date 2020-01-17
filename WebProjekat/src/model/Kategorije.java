package model;

import java.util.ArrayList;

public class Kategorije {
	public ArrayList<KategorijaVM> kategorije;

	public ArrayList<KategorijaVM> getKategorije() {
		return kategorije;
	}

	public void setKategorije(ArrayList<KategorijaVM> kategorije) {
		this.kategorije = kategorije;
	}

	public Kategorije() {
		kategorije = new  ArrayList<KategorijaVM>();

	}
	public KategorijaVM getKategorija(String ime) {
		for(KategorijaVM k : kategorije) {
			if(k.getIme().equals(ime)) {
				return k;
			}
		}
		return null;
	}
	

}
