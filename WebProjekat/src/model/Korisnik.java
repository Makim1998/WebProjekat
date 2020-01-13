package model;

public class Korisnik {

	public String email;
	public String lozinka;
	public String ime;
	public String prezime;
	public String organizacija;
	public Uloga uloga;
	
	public Korisnik() {
		super();
	}

	public Korisnik(String email, String lozinka, String ime, String prezime, String organizacija,Uloga uloga) {
		super();
		this.email = email;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.organizacija = organizacija;
		this.uloga = uloga;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getOrganizacija() {
		return organizacija;
	}

	public void setOrganizacija(String organizacija) {
		this.organizacija = organizacija;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		if(!(arg0 instanceof Korisnik)) {
			return false;
		}
		Korisnik k2 = (Korisnik) arg0;
		return this.email.equals(k2.email);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Korisnik [email=" + email + ", ime=" + ime + ", prezime=" + prezime 
				+ ", organizacija" + ", uloga=" + uloga.toString() 
				+ "]";
	}
	
	
}
