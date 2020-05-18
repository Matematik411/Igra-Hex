package logika;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import splosno.Koordinati;





public class Igra {
	
	// Standardna velikost igralne plošèe je 11x11
	public static int N = 7;
	
	// Zmagovalna èrta
	public Set<Tocka> konec;
	
	public Set<Tocka> rdece;
	public Set<Tocka> modre;
	
	// Igralno polje
	private Tocka[][] plosca;
	
	// mislim da dela tud kr tk, ker te toèke so tk stalne
	private Tocka rdeca_zgoraj = new Tocka(new Koordinati(-1, 0), Polje.Rdec);
	private Tocka rdeca_spodaj = new Tocka(new Koordinati(N, 0), Polje.Rdec);
	private Tocka modra_levo = new Tocka(new Koordinati(0, -1), Polje.Moder);
	private Tocka modra_desno = new Tocka(new Koordinati(0, N), Polje.Moder);
	
//	private Tocka rdeca_zgoraj;
//	private Tocka rdeca_spodaj;
//	private Tocka modra_levo;
//	private Tocka modra_desno;
	
	// Igralec, ki je trenutno na potezi.
	// Vrednost je poljubna, lahko je napaèna, èe je igre konec.
	public Igralec naPotezi;
	
	
	// Konstruktor za splosno igro
	public Igra() {
		this(N);
	}
	
	// Konstruktor za igro NxN
	public Igra(int M) {
		this.plosca = new Tocka[M][M];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				plosca[i][j] = new Tocka(new Koordinati(j, i));
			}
		}
		this.naPotezi = Igralec.Rdec;
		N = M;
		this.konec = new HashSet<Tocka>();
		this.rdece = new HashSet<Tocka>();
		this.modre = new HashSet<Tocka>();
		
//		this.rdeca_zgoraj = new Tocka(new Koordinati(-1, 0), Polje.Rdec);
//		this.rdeca_spodaj = new Tocka(new Koordinati(N, 0), Polje.Rdec);
//		this.modra_levo = new Tocka(new Koordinati(0, -1), Polje.Moder);
//		this.modra_desno = new Tocka(new Koordinati(0, N), Polje.Moder);
	}
	
	

	
	public Igra(Igra igra) {
		this.naPotezi = igra.naPotezi();
//		this.rdeca_zgoraj = new Tocka(new Koordinati(-1, 0), Polje.Rdec);
//		this.rdeca_spodaj = new Tocka(new Koordinati(N, 0), Polje.Rdec);
//		this.modra_levo = new Tocka(new Koordinati(0, -1), Polje.Moder);
//		this.modra_desno = new Tocka(new Koordinati(0, N), Polje.Moder);
		this.konec = new HashSet<Tocka>();
		this.rdece = new HashSet<Tocka>();
		this.modre = new HashSet<Tocka>();
		this.plosca = new Tocka[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.plosca[i][j] = new Tocka(igra.plosca[i][j].koordinati, igra.plosca[i][j].polje);
				this.plosca[i][j].sosedje = new HashSet<Tocka>();
			}
		}
		
		// zamenjati bi jih tako mogel tu, ko je vse zgrajeno, tako da ne bo veliko bolje
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Tocka t = this.plosca[i][j];
				if (t.polje != Polje.PRAZNO) {
					Polje p = t.polje;
					t.polje = Polje.PRAZNO;
					odigraj(t.koordinati, p);
				}
				
			}
		}

	}
	
	
	// Vrne igralno plošèo
	public Tocka[][] getPlosca () {
		return this.plosca;
	}
	
	// Vrne igralca
	public Igralec naPotezi() {
		return this.naPotezi;
	}
	
	
	// Vrne seznam možnih potez
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
	
	
	private void dodaj_povezavo(Tocka tocka1, Tocka tocka2) {
		if (tocka1.polje == tocka2.polje && tocka1.polje != Polje.PRAZNO) {
			tocka1.sosedje.add(tocka2);
			tocka2.sosedje.add(tocka1);
		}
	}
	
	public boolean odigraj(Koordinati p) {
		return odigraj(p, this.naPotezi.getPolje());
	}
	
	
	// Vseeno nam je ali je kordinata v mejah
	public boolean odigraj(Koordinati p, Polje barva) {	
		int x = p.getX();
		int y = p.getY();
		Tocka glavna = this.plosca[y][x];
		//Polje barva = this.naPotezi.getPolje();
		if (glavna.polje == Polje.PRAZNO) {
			glavna.polje = barva;
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
			
			this.naPotezi = this.naPotezi.nasprotnik();
			return true;
		}
		else {
			return false;
		}
	}

	
	private void pocisti() {
		for (Tocka tocka : this.rdece) {
			tocka.videna = false;
			tocka.predhodnji = null;
		}
		for (Tocka tocka : this.modre) {
			tocka.videna = false;
			tocka.predhodnji = null;
		}
	}
	
	
	private void zmagovalna_pot(Tocka tocka) {
		Tocka prejsnja = tocka.predhodnji;
		if (prejsnja != this.rdeca_zgoraj && prejsnja != this.modra_levo) {
			this.konec.add(prejsnja);
			zmagovalna_pot(prejsnja);
		}
	}
	
	
	// implementiran DFS za preverjanje ali smo prisli cez igralno mrezo
	private void DFS(Tocka zacetek) {
		zacetek.videna = true;
		for (Tocka sosed : zacetek.sosedje) {
			if(sosed!=null && !sosed.videna) {
				sosed.predhodnji = zacetek;
				if (sosed == this.rdeca_spodaj) zmagovalna_pot(rdeca_spodaj);
				if (sosed == this.modra_desno) zmagovalna_pot(modra_desno);
				DFS(sosed);
			}
		}
	}
	
	
	// preveri ali je igre konec, vrne Stanje igre
	public Stanje stanje() {
		this.pocisti();
		DFS(rdeca_zgoraj);
		if (this.konec.size() > 0) return Stanje.ZMAGA_RDEC;
		this.pocisti();
		DFS(modra_levo);
		if (this.konec.size() > 0) return Stanje.ZMAGA_MODER;		
		return Stanje.V_TEKU;
	}
	

		
		
		
	
	
	
	

	
	
	
	
	
}
