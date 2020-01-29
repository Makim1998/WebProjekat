package model;

import java.util.ArrayList;

public class Diskovi {
	
	public ArrayList<Disk> diskovi;

	public ArrayList<Disk> getDiskovi() {
		return diskovi;
	}

	public void setDiskovi(ArrayList<Disk> diskovi) {
		this.diskovi = diskovi;
	}
	
	public Diskovi() {
	}

	public Diskovi(ArrayList<Disk> diskovi) {
		super();
		this.diskovi = diskovi;
	}
	
	public Disk getDisk(String imeDiska) {
		for (Disk d: diskovi) {
			if (d.getIme().equals(imeDiska))
				return d;
		}
		return null;
	}

}
