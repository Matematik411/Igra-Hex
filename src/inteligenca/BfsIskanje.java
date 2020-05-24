package inteligenca;


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
	public static boolean test = false;
	
	// hashmap globin in predhodnjih
	private static HashMap<Koordinati, Integer> globine; 
	private static HashMap<Koordinati, Koordinati> predhodnji;
	
	private static final int MAX = Integer.MAX_VALUE; // vrednost ob zmagi
	
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
	
	
	public static List<Koordinati> BfsIskanjePotiRdec(Igra igra) {
		Tocka[][] plosca = igra.getPlosca();
				
		BFSGlobine(new Tocka(new Koordinati(0, igra.velikost), Polje.Rdec), plosca);
		
		Koordinati polozaj = plosca[0][0].koordinati; 
		int dolzina = MAX;
		
		for (int j = 0; j < igra.velikost; j++) {
			int novaDolzina = globine.getOrDefault(plosca[0][j].koordinati, MAX);
			if (novaDolzina < dolzina) {
				dolzina = novaDolzina;
				polozaj = plosca[0][j].koordinati;
			}
		}
		
		List<Koordinati> najkrajsaPot = new LinkedList<Koordinati>();
		while (predhodnji.containsKey(polozaj)) {
			if (plosca[polozaj.getY()][polozaj.getX()].polje == Polje.PRAZNO) {
				najkrajsaPot.add(polozaj);
			}
			polozaj = predhodnji.get(polozaj);
		}
//		System.out.println(globine);
//		System.out.println(predhodnji);
//		System.out.println(najkrajsaPot);
		return najkrajsaPot;
		}
		
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
//		System.out.println(globine);
//		System.out.println(predhodnji);
//		System.out.println(najkrajsaPot);
		if (najkrajsaPot.size() < 3) test = true;
		return najkrajsaPot;
	}

	// implementiran BFS
	private static void BFSGlobine(Tocka zacetek, Tocka[][] plosca) {
		globine = new HashMap<Koordinati, Integer>();  
		predhodnji = new HashMap<Koordinati, Koordinati>();
		
		int vrednost;
		Queue q = new Queue();
		q.vstavi(zacetek);
		
		globine.put(zacetek.koordinati, 0);
		
		while (!q.jePrazen()) {
			Tocka t = q.vzami();
			if (t.polje == zacetek.polje || t.polje == Polje.PRAZNO ) {
				Set<Tocka> sosedje = okoli(t.koordinati, plosca);	
				
				for (Tocka p : sosedje) {
					switch (p.polje) {
					case Rdec: vrednost = (zacetek.polje == Polje.Rdec ? globine.get(t.koordinati) : MAX); break;
					case Moder: vrednost = (zacetek.polje == Polje.Moder ? globine.get(t.koordinati) : MAX); break; 
					
					default: vrednost = globine.get(t.koordinati) + 1;
					}
					
					if (!globine.containsKey(p.koordinati)) {
						q.vstavi(p);
						globine.put(p.koordinati, vrednost);
						predhodnji.put(p.koordinati, t.koordinati);
					}
					else {
						if (vrednost < globine.get(p.koordinati)) {
							//q.vstavi(p);
							globine.put(p.koordinati, vrednost);
							predhodnji.put(p.koordinati, t.koordinati);
						}
					}
				}		
			}
		}


	}
	
	private static Set<Tocka> okoli(Koordinati p, Tocka[][] plosca) {
		Set<Tocka> okoli = new HashSet<Tocka>();
		int x = p.getX();
		int y = p.getY();
		int N = plosca[0].length;
		
		if (p.getY() == N) {
			for (int j = 0; j < N; j++) {
				Tocka tocka = plosca[N - 1][j];
				okoli.add(tocka);
			}
			return okoli;
		} 
		else if (p.getX() == N) {
			for (int i = 0; i < N; i++) {
				Tocka tocka = plosca[i][N - 1];
				okoli.add(tocka);
			}
			return okoli;
		}

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
	
		return okoli;
	}
}
