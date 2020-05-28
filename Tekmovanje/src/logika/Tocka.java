package logika;
/*
 * Razred za posamezne tocke igralne mreze.
 * 
 * Ima atribute koordinat, barve, mnozico sosednjih tock iste barve in polji "predhodnji"
 * in "videna", ki sluzita algoritmu iskanja zmagovalne poti.
 */

import java.util.HashSet;
import java.util.Set;

import splosno.Koordinati;

public class Tocka {

	// Napovedani atributi tock.
	public Set<Tocka> sosedje;
	public Polje polje;
	public Koordinati koordinati;
	public Tocka predhodnji;
	public boolean videna;

	// Konstruktor, ki ustvari tocko in nastavi njene atribute na zacetne vrednosti.
	public Tocka (Koordinati koordinati) {	
		this.sosedje = new HashSet<Tocka>();
		this.koordinati = koordinati;
		this.polje = Polje.PRAZNO;
		this.videna = false;
	}
	
	// Ce vnaprej poznamo barvo tocke, klicemo to verzijo konstruktorja.
	public Tocka (Koordinati koordinati, Polje polje) {	
		this.sosedje = new HashSet<Tocka>();
		this.koordinati = koordinati;
		this.polje = polje;
		this.videna = false;
	}
	
	
}
