package logika;

import java.util.HashSet;
import java.util.Set;

import koordinati.Koordinati;



public class Tocka {

	public Set<Tocka> sosedje;
	public Polje polje;
	public Koordinati koordinati;
	public Set<Tocka> predhodnji;
	public boolean videna;

	
	public Tocka (Koordinati koordinati) {	
		this.sosedje = new HashSet<Tocka>();
		this.koordinati = koordinati;
		this.polje = Polje.PRAZNO;
		this.predhodnji = new HashSet<Tocka>();
		this.videna = false;
	}
	
	
	
}
