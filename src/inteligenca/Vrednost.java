package inteligenca;
/*
 * Objekte razreda Vrednost uporabljamo za dinamicno iskanje najkrajsih poti v OceniPozicijo.
 * V sebi skriva koordinate polja, koliko zetonov je potrebnih, da pridemo od tega polja do konca,
 * pointer, ki nam pove iz katerega zetona smo prisli (to je zato, da lahko potem skonstruiramo najbolso pot)
 * in objekt razreda Skok, ki nam pove ali je nas prejsnji zeton na poti oddaljen za 2 polji, a nam nasprotnik ne more 
 * blokirati poti do njega.
 */

import splosno.Koordinati;

public class Vrednost {
	
	public int vrednost;
	public Koordinati koordinati;
	public Vrednost pointer;
	public Skok skok;
	
	public Vrednost(Koordinati koordinati) {
		this.koordinati = koordinati;
		this.vrednost = Integer.MAX_VALUE;
		
	}
	
}
