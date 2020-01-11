package model;

public enum Uloga {
	SUPER_ADMIN,
	KORISNIK,
	ADMIN;

	@Override
	public String toString() {
		String uloga = "";
		switch(this) {
		case SUPER_ADMIN:
			uloga = "super admin";
			break;
		case KORISNIK:
			uloga = "korisnik";
			break;
		case ADMIN:
			uloga = "admin";
			break;
		default:
			uloga = "nema";
			break;
			
		}
		return uloga;
	}
	
	
}

