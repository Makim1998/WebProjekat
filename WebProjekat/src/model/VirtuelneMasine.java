package model;

import java.util.ArrayList;

public class VirtuelneMasine {
	
	public ArrayList<VirtuelnaMasina> masine;

	public ArrayList<VirtuelnaMasina> getMasine() {
		return masine;
	}

	public void setMasine(ArrayList<VirtuelnaMasina> masine) {
		this.masine = masine;
	}

	public VirtuelneMasine(ArrayList<VirtuelnaMasina> masine) {
		super();
		this.masine = masine;
	}

	public VirtuelneMasine() {
	}
	
	public VirtuelnaMasina getMasina(String imeVM) {
		for (VirtuelnaMasina vm: masine) {
			if (vm.getIme().equals(imeVM))
				return vm;
		}
		return null;
	}

}
