package logika;
/*
 * Razred, ki ustvari igro in vsebuje metode, za njeno igranje.
 * 
 * Tu se ne ve, kdo igra igro (clovek ali racunalnik), igra tece z izvajanjem potez in
 * usklajevanjem njenih lastnosti na vsaki potezi.
 */

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import splosno.Koordinati;

public class Igra {
	
	// Napovedane spremeljivke, ki jih uporablja igra.
	// Standardna velikost igralne plosce je 11x11.
	public static int N = 11;

	// Igralno polje je mreza.
	private Tocka[][] plosca;
	
	// Vseskozi hranimo tocke iste barve v svoji mnozici.
	public Set<Tocka> rdece;
	public Set<Tocka> modre;
	
	// Zmagovalna èrta, ki se definira v primeru zmage nekega igralca.
	public Set<Tocka> konec;

	// Hranimo tudi zadnjo odigrano potezo.
	public Tocka zadnja;
	
	// Izven vsake stranice mreze definiramo nove tocke, ki so v pomoc pri iskanju zmage.
	private Tocka rdeca_zgoraj;
	private Tocka rdeca_spodaj;
	private Tocka modra_levo;
	private Tocka modra_desno;

	
	// Igralec, ki je trenutno na potezi.
	public Igralec naPotezi;
	
	
	// Konstruktor za splosno igro. Ustvari prazno mrezo in nastavi vrednosti na zacetne.
	public Igra() {
		this(N);
	}
	
	// Konstruktor za igro MxM.
	public Igra(int M) {
		this.plosca = new Tocka[M][M];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				plosca[i][j] = new Tocka(new Koordinati(j, i));
			}
		}
		this.rdeca_zgoraj = new Tocka(new Koordinati(0, -1), Polje.Rdec);
		this.rdeca_spodaj = new Tocka(new Koordinati(0, N), Polje.Rdec);
		this.modra_levo = new Tocka(new Koordinati(-1, 0), Polje.Moder);
		this.modra_desno = new Tocka(new Koordinati(N, 0), Polje.Moder);
		this.naPotezi = Igralec.Rdec;
		N = M;
		this.konec = new HashSet<Tocka>();
		this.rdece = new HashSet<Tocka>();
		this.modre = new HashSet<Tocka>();
		this.zadnja = null;

	}
	
	// Konstruktor za kopijo igre, ki je nujen za simuliranje novih iger, pri 
	// igranju racunalnika.
	public Igra(Igra igra) {
		this.naPotezi = igra.naPotezi();
		this.konec = new HashSet<Tocka>();
		this.rdece = new HashSet<Tocka>();
		this.modre = new HashSet<Tocka>();
		this.rdeca_zgoraj = new Tocka(new Koordinati(0, -1), Polje.Rdec);
		this.rdeca_spodaj = new Tocka(new Koordinati(0, N), Polje.Rdec);
		this.modra_levo = new Tocka(new Koordinati(-1, 0), Polje.Moder);
		this.modra_desno = new Tocka(new Koordinati(N, 0), Polje.Moder);
		this.plosca = new Tocka[N][N];
		Koordinati z = igra.zadnja.koordinati;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.plosca[i][j] = new Tocka(igra.plosca[i][j].koordinati, igra.plosca[i][j].polje);
				this.plosca[i][j].sosedje = new HashSet<Tocka>();
			}
		}
		
		// Trenutne tocke so se stare, zato jih zamenjam z novimi tako, da jih odigram.
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Tocka t = this.plosca[i][j];
				if (t.polje != Polje.PRAZNO) {
					Polje p = t.polje;
					t.polje = Polje.PRAZNO;
					odigraj(t.koordinati, p);
				}
				if (z == new Koordinati(z.getX(), z.getY())) this.zadnja = t;
				
			}
		}
		
		// Igralec se je morda spremenil, zato ga zopet pravilno nastavimo.
		this.naPotezi = igra.naPotezi();
	}
	
	
	// Metoda, ki vrne igralno mrezo.
	public Tocka[][] getPlosca () {
		return this.plosca;
	}
	
	// Metoda, ki vrne igralca, ki je trenutno na potezi.
	public Igralec naPotezi() {
		return this.naPotezi;
	}
	
	
	// Metoda, ki vrne seznam moznih potez.
	public List<Koordinati> poteze() {
		LinkedList<Koordinati> seznam = new LinkedList<Koordinati>(); 
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (this.plosca[i][j].polje == Polje.PRAZNO) {
					seznam.add(new Koordinati(j, i));
				}
			}
		}
		return seznam;
	}
	
	// V mnozico sosedov Tock, z vsako potezo vstavimo morebitne nove.
	private void dodaj_povezavo(Tocka tocka1, Tocka tocka2) {
		if (tocka1.polje == tocka2.polje && tocka1.polje != Polje.PRAZNO) {
			tocka1.sosedje.add(tocka2);
			tocka2.sosedje.add(tocka1);
		}
	}
	
	// Glavna metoda, ki poskusi odigrati potezo s koordinatama p. V primeru uspesno
	// odigrane poteze vrne vrednost true, sicer false.
	public boolean odigraj(Koordinati p) {
		return odigraj(p, this.naPotezi.getPolje());
	}
	
	
	// Metoda, ki odigra potezo z vnaprej doloceno barvo. 
	public boolean odigraj(Koordinati p, Polje barva) {	
		// Prebere vnesene koordinate in preveri, kaksne barve je to polje v mrezi.
		int x = p.getX();
		int y = p.getY();
		Tocka glavna = this.plosca[y][x];
		
		// Ce je polje prazno, lahko odigra potezo.
		if (glavna.polje == Polje.PRAZNO) {
			// Najprej spremeni barvo polja.
			glavna.polje = barva;
			
			// Nato se doda vse ustrezne sosede.
			if (barva == Polje.Rdec) {
				this.rdece.add(glavna);
				if (y == 0) dodaj_povezavo(glavna, this.rdeca_zgoraj);
				if (y == N - 1) dodaj_povezavo(glavna, this.rdeca_spodaj);
			}
			if (barva == Polje.Moder) { 
				this.modre.add(glavna);
				if (x == 0) dodaj_povezavo(glavna, this.modra_levo);
				if (x == N - 1) dodaj_povezavo(glavna, this.modra_desno);
			}
			try {
				Tocka tocka1 = this.plosca[y - 1][x];
				dodaj_povezavo(tocka1, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka2 = this.plosca[y + 1][x];
				dodaj_povezavo(tocka2, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka3 = this.plosca[y][x - 1];
				dodaj_povezavo(tocka3, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka4 = this.plosca[y + 1][x - 1];
				dodaj_povezavo(tocka4, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka5 = this.plosca[y - 1][x + 1];
				dodaj_povezavo(tocka5, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka6 = this.plosca[y][x + 1];
				dodaj_povezavo(tocka6, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			
			// Za konec spremeni aktivnega igralca. 
			this.naPotezi = this.naPotezi.nasprotnik();
			// To potezo nastavi kot zadnjo odigrano.
			this.zadnja = glavna;
			// Ker se je poteza uspesno izvedla vrne metoda true.
			return true;
		}
		// V primeru, ko pa je to polje polno, se poteza ne izvede, metoda pa vrne false.
		else {
			return false;
		}
	}

	// Metoda za razveljavitev zadnje poteze (na voljo le, ko igrata dva cloveka).
	public void razveljavi() {
		// Zadnjo tocko odstrani iz vseh sosedov ...
		for (Tocka t : this.zadnja.sosedje) {
			t.sosedje.remove(this.zadnja);
		}
		// ... iz mnozice tock te barve ...
		if (this.zadnja.polje == Polje.Rdec) {
			this.rdece.remove(this.zadnja);
		} 
		else this.modre.remove(this.zadnja);
		//... in nastavi to polje kot prazno.
		this.plosca[this.zadnja.koordinati.getY()][this.zadnja.koordinati.getX()] 
				= new Tocka(this.zadnja.koordinati);
		
		// Vrniti mora se potezo igralcu, ki jo je naredil (je ravno trenutni nasprotnik).
		this.naPotezi = this.naPotezi.nasprotnik();
		// Zbrise se potezo iz spremeljivke "zadnja", saj se zdaj sploh ni izvedla
		this.zadnja = null;
	}
	
	// Metoda za enkratno menjavo barve (na voljo le, ko igrata dva cloveka).
	// Ta metoda se lahko zgodi le, ko je na mrezi natanko en rdec zeton.
	public void menjaj() {
		// Poisce koordinate rdecega zetona.
		Koordinati prva = this.zadnja.koordinati;
		// Ta zeton zbrise.
		razveljavi();
		// In simetricno potezo odigra kot modri.
		this.naPotezi = this.naPotezi.nasprotnik();
		odigraj(new Koordinati(prva.getY(), prva.getX()));
	}
	
	// Metoda, ki jo klice algoritem BFS in sluzi ciscenju puscic na predhodna polja v poti.
	private void pocisti() {
		// Za vse Tocke izbrise predhodne tocke in nastavi, da se niso videne, ker je 
		// to ostalo iz predhodnih pregledov algoritma BFS.
		for (Tocka tocka : this.rdece) {
			tocka.videna = false;
			tocka.predhodnji = null;
		}
		for (Tocka tocka : this.modre) {
			tocka.videna = false;
			tocka.predhodnji = null;
		}
		rdeca_zgoraj.videna =  false;
		rdeca_zgoraj.predhodnji = null;
		rdeca_spodaj.videna =  false;
		rdeca_spodaj.predhodnji = null;
		modra_levo.videna =  false;
		modra_levo.predhodnji = null;
		modra_desno.videna =  false;
		modra_desno.predhodnji = null;
	}
	
	// Implementiran BFS (Breadth-first search) algoritem za iskanje zmagovalne poti.
	// Algoritem shrani v spremenljivko konec najkrajso zmagovalno pot, ce ta obstaja.
	private void BFS(Tocka zacetek) {
		// Algoritem bo iskal pot od spodaj navzgor (za rdecega)
		// ali od desne proti levi (za modrega).
		
		// Spremenljivka "videna" gleda, ali smo do te tocke ze kdaj prisli.
		// Spremeljivka "predhodnji" pa od kod smo prisli tja prvic, tako se tudi ustvari
		// najkrajsa pot od enega konca mreze do drugega.
		
		// Definira novo cakalno vrsto v katero da zacetno tocko.
		Queue q = new Queue();
		q.vstavi(zacetek);
		
		// To tocko prav tako oznaci za videno.
		zacetek.videna = true;
		
		// Dokler se niso bile pregledane vse tocke v vrsti se algoritem izvaja.
		while (!q.jePrazen()) {
			// Vzame se najstarejsa tocka iz vrste ...
			Tocka t = q.vzami();
			Set<Tocka> sosedje = t.sosedje;
			
			// ... in z zanko vse njene sosede nastavimo na videne, kot predhodnjo
			// tocko pa nastavimo to tocko samo.
			for (Tocka p : sosedje) {
				if (!p.videna) {
					q.vstavi(p);
					p.videna = true;
					p.predhodnji = t;
				}
			}
		}
		
		// Ko se je algoritem koncal, so v vseh dostopnih tockah iz zacetne tocke shranjene
		// tudi koordinate kako smo tja prisli. 
		// Pri preverjanju ali obstaja pot to vrha, zacnemo zgoraj in sledimo 
		// predhodnim puscicam.
		
		// Definiramo novo mnozico, v kateri bodo tocke iz zmagovalne poti.
		Set<Tocka> koncni = new HashSet<Tocka>();
		
		// Zmagovalna pot bo obstajala, ko bo imela koncna tocka (zgornja za rdecega in 
		// leva za modrega) kaj shranjeno v "predhodnji", saj to pomeni, da jo je pot od
		// zacetne tocke dosegla.
		
		// Najprej preverimo za rdeco zgornjo tocko.
		if (rdeca_zgoraj.predhodnji != null) {
			
			// Ko pot obstaja gremo v nasprotni smeri in pridemo po najkrajsi do dna.
			Tocka t = rdeca_zgoraj.predhodnji;
			while (t != null) {
				koncni.add(t);
				t = t.predhodnji;
			}
			
			// Le odstranimo zacetno tocko, saj izven tabele in mnozico shranimo v "konec".
			koncni.remove(rdeca_spodaj);
			this.konec = koncni;
		} 
		
		// Preverimo analogno se za modrega. (Tu gre zdaj od leve proti desni.)
		else if (modra_levo.predhodnji != null) {
			Tocka t = modra_levo.predhodnji;
			while (t != null) {
				koncni.add(t);
				t = t.predhodnji;
			}
			
			koncni.remove(modra_desno);
			this.konec = koncni;
		}
	}
	
	
	// Metoda, ki preverja stanje igre na nacin, da klice BFS algoritem.
	public Stanje stanje() {
		
		// Najprej preveri, ali BFS najde rdeco pot cez mrezo.
		BFS(rdeca_spodaj);
		if (this.konec.size() > 0) return Stanje.ZMAGA_RDEC;
		// Ce poti ne najde, mora pocistiti dele poti, ki so nastali.
		this.pocisti();
		
		// Nato poskusi najti se modro pot cez mrezo.
		BFS(modra_desno);
		if (this.konec.size() > 0) return Stanje.ZMAGA_MODER;	
		// Tudi tu pocisti za sabo, ce ni nicesar nasla.
		this.pocisti();
		
		// V primeru, ko zmagovalna pot ni bila najdena, le ta ne obstaja, 
		// torej igra se tece
		return Stanje.V_TEKU;
	}
	
}
