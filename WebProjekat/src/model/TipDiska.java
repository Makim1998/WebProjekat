package model;

public enum TipDiska {
	SSD,
	HDD;
	
	@Override
	public String toString() {
		String tip;
		switch(this) {
		case SSD: tip = "SSD"; break;
		case HDD: tip = "HDD"; break;
		default: tip = "nema tip"; break;
		}
		return tip;
	}
}
