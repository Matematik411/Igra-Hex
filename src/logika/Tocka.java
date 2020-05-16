package logika;

import java.util.HashSet;
import java.util.Set;

import splosno.Koordinati;



public class Tocka {

	public Set<Tocka> sosedje;
	public Polje polje;
	public Koordinati koordinati;
	public Tocka predhodnji;
	public boolean videna;

	
	public Tocka (Koordinati koordinati) {	
		this.sosedje = new HashSet<Tocka>();
		this.koordinati = koordinati;
		this.polje = Polje.PRAZNO;
		this.videna = false;
	}
	
	public Tocka (Koordinati koordinati, Polje polje) {	
		this.sosedje = new HashSet<Tocka>();
		this.koordinati = koordinati;
		this.polje = polje;
		this.videna = false;
	}
	
	
}
