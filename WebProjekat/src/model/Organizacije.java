package model;

import java.util.ArrayList;

public class Organizacije {
	
	public ArrayList<Organizacija> organizacije;

	public ArrayList<Organizacija> getOrganizacije() {
		return organizacije;
	}

	public void setOrganizacije(ArrayList<Organizacija> organizacije) {
		this.organizacije = organizacije;
	}

	public Organizacije(ArrayList<Organizacija> organizacije) {
		this.organizacije = organizacije;
	}

	public Organizacije() {
	}
	
	public Organizacija getOrganizacija(String imeOrg) {
		for (Organizacija org: organizacije) {
			if (org.getIme().equals(imeOrg))
				return org;
		}
		return null;
	}
	
}
