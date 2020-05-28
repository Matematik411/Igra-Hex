package inteligenca;
/*
 * Razred za iskanje poti z BFS iskanjem.
 * 
 * To iskanje se uporablja, ko glavni algoritem veè ne najde zmagovalne poti, saj
 * ta zavije nazaj. Ta algoritem pa zazna tudi take poti.
 * 
 * Glavni metodi razreda poisceta najkrajsi poti in ju vrneta kot seznama Koordinat.
 * 
 * Vselej ko se to iskanje klice, bosta obe poti se obstajali.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import logika.Queue;
import logika.Tocka;
import splosno.Koordinati;
import logika.Igra;
import logika.Igralec;
import logika.Polje;

public class BfsIskanje {
	
	// Napovemo dva slovarja, kjer se shranjujo vrednosti pomembne za algoritem.
	// Globina je stevilo potrebnih dodanih zetonov do teh koordinat od spodaj (rdeci)
	// ali iz desne (modri).
	private static HashMap<Koordinati, Integer> globine; 
	// V tem slovarju pa so shranjene koordinate, od kod pride najkrajsa pot
	// do te tocke.
	private static HashMap<Koordinati, Koordinati> predhodnji;
	
	// Definiramo si veliko stevilo, ki bo pomenilo, da to te tocke ne moremo priti.
	private final static int MAX = Integer.MAX_VALUE;
	
	public static int BfsOceni(Igra igra, Igralec jaz) {
		int N = igra.velikost;
		
		int rdeca = BfsIskanjePotiRdec(igra).size();
		int modra = BfsIskanjePotiModer(igra).size();
		
		int a = Integer.MAX_VALUE / (N * N);
		int b = -a;
	
		if (jaz == Igralec.Rdec) {
			return -(a * rdeca + b * modra );
		}
		else {
			return (a * rdeca + b * modra );
		}
	}
	
	public static List<Koordinati> BfsIskanjePoti(Igra igra, Igralec jaz) {
		if (jaz == Igralec.Rdec) return BfsIskanjePotiRdec(igra);
		else return BfsIskanjePotiModer(igra);
	}
	
	// Za vsako barvo je definirana svoja metoda, ki poisce in vrno najkrajso pot.
	public static List<Koordinati> BfsIskanjePotiRdec(Igra igra) {
		// Najprej pridobimo iz igrane igre trenutno stanje na mrezi.
		Tocka[][] plosca = igra.getPlosca();
				
		// Nato zapolnimo slovarja globin in predhodnih tock, glede na zacetek v 
		// rdeci tocki pod mrezo.
		BFSGlobine(new Tocka(new Koordinati(0, igra.velikost), Polje.Rdec), plosca);
		
		// Zdaj se sprehodimo cez zgornjo vrstico in poiscemo, do katerega iz teh polj
		// lahko pridemo z najmanj potezami.
		
		// Za primer, ko ne najdemo boljse poteze, za polozaj, ki bo zacetna tocka poti,
		// vzamemo kar tocko v kotu.
		Koordinati polozaj = plosca[0][0].koordinati; 
		int dolzina = MAX;
		
		for (int j = 0; j < igra.velikost; j++) {
			// Iz slovarja preberemo globino tocke (ce ta ne obstaja priredimo vrednost MAX).
			int novaDolzina = globine.getOrDefault(plosca[0][j].koordinati, MAX);
			// Ce je dobljena globina boljsa od trenutne, zamenjamo polozaj s to tocko.
			if (novaDolzina < dolzina) {
				dolzina = novaDolzina;
				polozaj = plosca[0][j].koordinati;
			}
		}
		
		// Definiramo si seznam Koordinat, v katerega bomo vpisali najkrajso pot.
		List<Koordinati> najkrajsaPot = new LinkedList<Koordinati>();
		// V seznam vpisemo tiste koordinate, katerih polja so prazna.
		while (predhodnji.containsKey(polozaj)) {
			if (plosca[polozaj.getY()][polozaj.getX()].polje == Polje.PRAZNO) {
				najkrajsaPot.add(polozaj);
			}
			polozaj = predhodnji.get(polozaj);
		}

		// Ko tako zapolnimo cel seznam, ga vrnemo.
		return najkrajsaPot;
		}
	
	// Sledi se analogna metoda za primer, ko iscemo pot za modrega.	
	public static List<Koordinati> BfsIskanjePotiModer(Igra igra) {
		Tocka[][] plosca = igra.getPlosca();

		BFSGlobine(new Tocka(new Koordinati(igra.velikost, 0), Polje.Moder), plosca);
		
		Koordinati polozaj = plosca[0][0].koordinati; 
		int dolzina = MAX;
		
		for (int i = 0; i < igra.velikost; i++) {
			int novaDolzina = globine.getOrDefault(plosca[i][0].koordinati, MAX);
			if (novaDolzina < dolzina) {
				dolzina = novaDolzina;
				polozaj = plosca[i][0].koordinati;
			}
		}
		
		List<Koordinati> najkrajsaPot = new LinkedList<Koordinati>();
		while (predhodnji.containsKey(polozaj)) {
			if (plosca[polozaj.getY()][polozaj.getX()].polje == Polje.PRAZNO) {
				najkrajsaPot.add(polozaj);
			}
			polozaj = predhodnji.get(polozaj);
		}

		return najkrajsaPot;
	}

	// Metoda, ki seizpolne slovarja glede na trenutno stanje ne mrezi.
	private static void BFSGlobine(Tocka zacetek, Tocka[][] plosca) {
		// Definiramo si nova slovarja.
		globine = new HashMap<Koordinati, Integer>();
		predhodnji = new HashMap<Koordinati, Koordinati>();
		
		// Napovemo stevilo, s katerim bomo racunali globino do izbrane tocke.
		int vrednost;
		
		// Tudi tu si pomagamo z vrsto Tock. Vanjo vstavimo najprej zacetno tocko.
		Queue q = new Queue();
		q.vstavi(zacetek);
		
		// Zacetni tocki priredimo globino 0.
		globine.put(zacetek.koordinati, 0);
		
		// Dokler nismo pregledali vseh tock v vrsti, se izvaja algoritem.
		while (!q.jePrazen()) {
			Tocka t = q.vzami();
			// Ce tocka ni nasprotne barve, jo bomo obravnavali in ji priredili
			// ustrezno globino. Ce je nasprotne barve, cez njo pot ne bo mogla
			// prehajati, torej jo kar izpustimo.
			if (t.polje == zacetek.polje || t.polje == Polje.PRAZNO ) {
				// Poiscemo sosede trenutne tocke, kot vse okoliske tocke.
				Set<Tocka> sosedje = okoli(t.koordinati, plosca);	
				
				// Iz teh sosedov smo lahko prisli na to tocko. Glede na njih priredimo
				// trenutni tocki vrednost.
				for (Tocka p : sosedje) {
					switch (p.polje) {
					// Ce je sosed iste barve, priredimo kar njegovo globino, sicer pa MAX.
					case Rdec: {
						if (zacetek.polje == Polje.Rdec) vrednost = globine.get(t.koordinati);
						else vrednost = MAX;
						break;
					}
					case Moder: {
						if (zacetek.polje == Polje.Moder) vrednost = globine.get(t.koordinati);
						else vrednost = MAX;
						break; 
					}
					// V primeru, ko pa je tisto polje prazno, priredimo njegovo 
					// globino + 1, saj rabimo na tisto polje se postaviti zeton.
					default: vrednost = globine.get(t.koordinati) + 1;
					}
					
					// Ko smo dobili potencialno vrednost za naso tocko, jo vstavimo kot
					// globino.
					// To se zgodi v primeru, ko globine se nismo imeli, ali pa je trenutna
					// vrednost boljsa (strogo manjsa) od predhodnje.
					if (!globine.containsKey(p.koordinati)) {
						q.vstavi(p);
						globine.put(p.koordinati, vrednost);
						predhodnji.put(p.koordinati, t.koordinati);
					}
					else {
						if (vrednost < globine.get(p.koordinati)) {
							q.vstavi(p);
							globine.put(p.koordinati, vrednost);
							predhodnji.put(p.koordinati, t.koordinati);
						}
					}
				}		
			}
		}

	}
	
	// Metoda, ki danim koordinatam priredi mnozico okoliskih tock, ki v mrezi obstajajo.
	private static Set<Tocka> okoli(Koordinati p, Tocka[][] plosca) {
		// Izracunamo trenutno tocko in velikost mreze.
		Set<Tocka> okoli = new HashSet<Tocka>();
		int x = p.getX();
		int y = p.getY();
		int N = plosca[0].length;
		
		// V primeru spodnje rdece tocke, so okoliske tocke vse iz zadnje vrstice.
		if (p.getY() == N) {
			for (int j = 0; j < N; j++) {
				Tocka tocka = plosca[N - 1][j];
				okoli.add(tocka);
			}
			return okoli;
		} 
		// V primeru desne modre tocke, pa so okoliske tocke vse iz zadnjega stolpca.
		else if (p.getX() == N) {
			for (int i = 0; i < N; i++) {
				Tocka tocka = plosca[i][N - 1];
				okoli.add(tocka);
			}
			return okoli;
		}

		// Sicer za 6 okoliskih tock le preverimo ali v mrezi obstajajo.
		try {
			Tocka tocka = plosca[y - 1][x];
			okoli.add(tocka);
		} catch (ArrayIndexOutOfBoundsException e) {}
		try {
			Tocka tocka = plosca[y - 1][x + 1];
			okoli.add(tocka);
		} catch (ArrayIndexOutOfBoundsException e) {}
		try {
			Tocka tocka = plosca[y][x + 1];
			okoli.add(tocka);
		} catch (ArrayIndexOutOfBoundsException e) {}
		try {
			Tocka tocka = plosca[y + 1][x];
			okoli.add(tocka);
		} catch (ArrayIndexOutOfBoundsException e) {}
		try {
			Tocka tocka = plosca[y + 1][x - 1];
			okoli.add(tocka);
		} catch (ArrayIndexOutOfBoundsException e) {}
		try {
			Tocka tocka = plosca[y][x - 1];
			okoli.add(tocka);
		} catch (ArrayIndexOutOfBoundsException e) {}
	
		// To mnozico okoliskih tock vrnemo.
		return okoli;
	}
}
