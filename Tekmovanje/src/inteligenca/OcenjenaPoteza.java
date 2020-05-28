package inteligenca;

import splosno.Koordinati;

public class OcenjenaPoteza {
	
	Koordinati poteza;
	int ocena;
	
	public OcenjenaPoteza (Koordinati poteza, int ocena) {
		this.poteza = poteza;
		this.ocena = ocena;
	}
	
	public int compareTo (OcenjenaPoteza op) {
		if (this.ocena < op.ocena) return -1;
		else if (this.ocena > op.ocena) return 1;
		else return 0;
	}

}
