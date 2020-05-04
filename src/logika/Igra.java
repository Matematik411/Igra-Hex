package logika;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import koordinati.Koordinati;





public class Igra {
	
	// Standardna velikost igralne plo��e je 11x11
	public static int N = 11;
	
	// Zmagovalna �rta
	public Set<Tocka> konec;
	
	public Set<Tocka> rdece;
	public Set<Tocka> modre;
	
	// Igralno polje
	private Tocka[][] plosca;
	
	// Igralec, ki je trenutno na potezi.
	// Vrednost je poljubna, lahko je napa�na, �e je igre konec.
	public Igralec naPotezi;
	
	
	// Konstruktor za igro 11x11
	public Igra() {
		N = 11;
		this.plosca = new Tocka[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = new Tocka(new Koordinati(i, j));
			}
		}
		this.naPotezi = Igralec.Rde�;
		this.konec = new HashSet<Tocka>();
		this.rdece = new HashSet<Tocka>();
		this.modre = new HashSet<Tocka>();
	}
	
	// Konstruktor za igro NxN
	public Igra(int M) {
		this.plosca = new Tocka[M][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = new Tocka(new Koordinati(i, j));
			}
		}
		this.naPotezi = Igralec.Rde�;
		N = M;
		this.konec = new HashSet<Tocka>();
		this.rdece = new HashSet<Tocka>();
		this.modre = new HashSet<Tocka>();
	}
	
	// Vrne igralno plo��o
	public Tocka[][] getPlosca () {
		return this.plosca;
	}
	
	// Vrne seznam mo�nih potez
	public List<Koordinati> poteze() {
		LinkedList<Koordinati> seznam = new LinkedList<Koordinati>(); //Predvidevam da je linked list kot Ocamel list ???
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (this.plosca[i][j].polje == Polje.PRAZNO) {
					seznam.add(new Koordinati(i, j));
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
	
	
	
	
	// Vseeno nam je ali je kordinata v mejah
	public boolean odigraj(Koordinati p) {	
		int x = p.getX();
		int y = p.getY();
		Tocka glavna = this.plosca[x][y];
		Polje barva = this.naPotezi.getPolje();
		if (glavna.polje == Polje.PRAZNO) {
			glavna.polje = barva;
			if (barva == Polje.Rde�) this.rdece.add(glavna);
			if (barva == Polje.Moder) this.modre.add(glavna);
			try {
				Tocka tocka1 = this.plosca[x][y - 1];
				dodaj_povezavo(tocka1, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka2 = this.plosca[x][y + 1];
				dodaj_povezavo(tocka2, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka3 = this.plosca[x - 1][y];
				dodaj_povezavo(tocka3, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka4 = this.plosca[x - 1][y + 1];
				dodaj_povezavo(tocka4, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka5 = this.plosca[x + 1][y - 1];
				dodaj_povezavo(tocka5, glavna);
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				Tocka tocka6 = this.plosca[x + 1][y];
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
			tocka.predhodnji.clear();
		}
		for (Tocka tocka : this.modre) {
			tocka.videna = false;
			tocka.predhodnji.clear();
		}
	}
	
	
	private void DFS(Tocka zacetek) {
		zacetek.videna = true;
		zacetek.predhodnji.add(zacetek);
		for (Tocka sosed : zacetek.sosedje) {
			if(sosed!=null && sosed.videna) {
				sosed.predhodnji = zacetek.predhodnji;
				sosed.predhodnji.add(sosed);
				if (sosed.polje == Polje.Rde�) {
					if (sosed.koordinati.getY() == N) {
						this.konec = sosed.predhodnji;
					}
				}
				if (sosed.polje == Polje.Moder) {
					if (sosed.koordinati.getX() == N) {
						this.konec = sosed.predhodnji;
					}
				}
				DFS(sosed);
			}
		}
	}
	
	
	
	public Stanje stanje() {
		this.pocisti();
		for (int i = 0; i < N; i++) {
			Tocka tocka = this.plosca[0][i];
			DFS(tocka);
			if (this.konec.size() > 0) return Stanje.ZMAGA_RDE�;
		}
		for (int i = 0; i < N; i++) {
			Tocka tocka = this.plosca[i][0];
			DFS(tocka);
			if (this.konec.size() > 0) return Stanje.ZMAGA_MODER;
		}
		return Stanje.V_TEKU;
	}
	
		
		
		
		
		
	
	
	
	

	
	
	
	
	
}
