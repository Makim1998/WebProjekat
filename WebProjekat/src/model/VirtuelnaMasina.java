package model;

import java.util.ArrayList;
import java.util.Date;

public class VirtuelnaMasina {
	
	private String ime;
	private KategorijaVM kategorija;
	private String organizacija;
	private ArrayList<String> diskovi;
	private Date datumPaljenja;
	private Date datumGasenja;
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public KategorijaVM getKategorija() {
		return kategorija;
	}
	public void setKategorija(KategorijaVM kategorija) {
		this.kategorija = kategorija;
	}
	public ArrayList<String> getDiskovi() {
		return diskovi;
	}
	public void setDiskovi(ArrayList<String> diskovi) {
		this.diskovi = diskovi;
	}
	public Date getDatumPaljenja() {
		return datumPaljenja;
	}
	public void setDatumPaljenja(Date datumPaljenja) {
		this.datumPaljenja = datumPaljenja;
	}
	public Date getDatumGasenja() {
		return datumGasenja;
	}
	public void setDatumGasenja(Date datumGasenja) {
		this.datumGasenja = datumGasenja;
	}
	
	public VirtuelnaMasina(String ime, KategorijaVM kategorija, ArrayList<String> diskovi, Date datumPaljenja,
			Date datumGasenja) {
		this.ime = ime;
		this.kategorija = kategorija;
		this.diskovi = diskovi;
		this.datumPaljenja = datumPaljenja;
		this.datumGasenja = datumGasenja;
	}
	
	public VirtuelnaMasina() {
	}
	
	public String getOrganizacija() {
		return organizacija;
	}
	
	public void setOrganizacija(String organizacija) {
		this.organizacija = organizacija;
	}	

	public void addDisk(String imeDiska) {
		diskovi.add(imeDiska);
	}
	
	public void removeDisk(String imeDiska) {
		int index = diskovi.indexOf(imeDiska);
		diskovi.remove(index);
	}
	
}
