package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Tocka;

public class OceniPozicijo {
	
	// zacetna verzija
	// Metoda oceniPozicijo za igro Hex
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		int ocena = 0;
		if (jaz == Igralec.Rdec) {
			
			for (Tocka t : igra.rdece) ocena += 1 + t.sosedje.size();

		}
		else {
			for (Tocka t : igra.modre) ocena += 1 + t.sosedje.size();

		}
		return ocena;	
	}
	
	
	

}


