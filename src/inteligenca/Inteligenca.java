package inteligenca;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import logika.Igra;
import logika.Igralec;
import logika.Stanje;
import splosno.Koordinati;
import splosno.KdoIgra;


public class Inteligenca extends KdoIgra {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // vrednost ob zmagi
	private static final int ZGUBA = -ZMAGA;
	private int globina;
	private int ai;
	
	public Inteligenca() {
		super("Rehoboam");
		this.globina = 7;
		this.ai = 2;
	}
	
	
	public void nastaviGlobino(int d) {
		this.globina = d;
	}
	
	public void nastaviAI(int i) {
		this.ai = i;
	}
	
	public Koordinati izberiPotezo (Igra igra) {
		OcenjenaPoteza najboljsaPoteza;
		if (ai == 0) {
			najboljsaPoteza = minimax(igra, this.globina);
		} else if (ai == 1) {
			List<OcenjenaPoteza> ocenjenePoteze = najboljsePoteze(igra, this.globina);
			Random RANDOM = new Random();
			int i = RANDOM.nextInt(ocenjenePoteze.size());	
			najboljsaPoteza = ocenjenePoteze.get(i);
		} else {
			najboljsaPoteza = alphaBetaMinimax(igra, this.globina, ZGUBA, ZMAGA, igra.naPotezi());
		}
		return najboljsaPoteza.poteza;
	}
	
	// -------------------MINIMAX--------------------------
	// vrne najboljso ocenjeno potezo z vidika igralca na potezi
	public OcenjenaPoteza minimax(Igra igra, int globina) {
		OcenjenaPoteza najboljsaPoteza = null;
		List<Koordinati> moznePoteze = igra.poteze();
		
		for (Koordinati p: moznePoteze) {

			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
			int ocena;
			
			// ne zazna primera, ko bi nasprotnik zmagal v eni potezi in zato tega ne prepreci
			switch (kopijaIgre.stanje()) {
			case ZMAGA_RDEC: 
			case ZMAGA_MODER: ocena = ZMAGA; break;

			default:
				// nekdo je na potezi
				if (globina == 1) ocena = OceniPozicijo.oceniPozicijo(kopijaIgre, igra.naPotezi());
				// globina > 1
				else ocena = -minimax(kopijaIgre, globina-1).ocena;
			}
		
			if (najboljsaPoteza == null
				|| ocena > najboljsaPoteza.ocena) {
				najboljsaPoteza = new OcenjenaPoteza(p, ocena);	
			}
		}

		return najboljsaPoteza;
	}
		
	// ------------ RANDOM MINIMAX --------------------------
	// vrne seznam vseh potez, ki imajo najvecjo vrednost z vidika trenutnega igralca na potezi
	public static List<OcenjenaPoteza> najboljsePoteze(Igra igra, int globina) {
		NajboljseOcenjenePoteze najboljsePoteze = new NajboljseOcenjenePoteze();
		List<Koordinati> moznePoteze = igra.poteze();
		for (Koordinati p: moznePoteze) {
			Igra tempIgra = new Igra(igra); 
			tempIgra.odigraj (p);	//poskusimo vsako potezo v novi kopiji igre
			int ocena;
			switch (tempIgra.stanje()) {
			case ZMAGA_RDEC:
			case ZMAGA_MODER: ocena = ZMAGA; break; // p je zmagovalna poteza
			default: //nekdo je na potezi
				if (globina==1) ocena = OceniPozicijo.oceniPozicijo(tempIgra,igra.naPotezi());
				else ocena = -najboljsePoteze(tempIgra,globina-1).get(0).ocena; // - ker je drug igralec 
			}
			najboljsePoteze.addIfBest(new OcenjenaPoteza(p, ocena));			
		}
		return najboljsePoteze.list();
	}

	
	//-------------ALPHABETA-----------------
	public static OcenjenaPoteza alphaBetaMinimax(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		
		int ocena;
		if (igra.naPotezi() == jaz) {ocena = ZGUBA;} else {ocena = ZMAGA;}
		List<Koordinati> moznePoteze = IzbiraPotez.izbiraPotezVse(igra, jaz, globina);	
		//List<Koordinati> najbolsePoteze = new LinkedList<Koordinati>();
		Koordinati najboljsaPoteza = moznePoteze.get(0); 
		
		for (Koordinati p: moznePoteze) {

			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
			int ocenaPoteze;
			

			switch (kopijaIgre.stanje()) {
			
			case ZMAGA_RDEC: {
				System.out.println("Rdeci-zmaga");
				ocenaPoteze = (jaz == Igralec.Rdec ? ZMAGA : ZGUBA);
				break;
			}
			
			case ZMAGA_MODER: {
				System.out.println("Modri-zmaga");
				ocenaPoteze = (jaz == Igralec.Moder ? ZMAGA : ZGUBA); 				
				break;
			}
			
			default:
				// nekdo je na potezi
				if (globina == 1) ocenaPoteze = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				// globina > 1
				else ocenaPoteze = alphaBetaMinimax(kopijaIgre, globina-1, alpha, beta, jaz).ocena;
			}
			
		
			if (igra.naPotezi() == jaz) {
				if (ocenaPoteze == ocena) {}//najbolsePoteze.add(p);
				else if (ocenaPoteze > ocena) {
					ocena = ocenaPoteze;
					najboljsaPoteza = p;
					//najbolsePoteze.clear();
					//najbolsePoteze.add(p);
					alpha = Math.max(ocena, alpha);
				}
			}
			
			else {
				if (ocenaPoteze == ocena) {}//najbolsePoteze.add(p);
				else if (ocenaPoteze < ocena) {
					ocena = ocenaPoteze;
					najboljsaPoteza = p;
					//najbolsePoteze.clear();
					//najbolsePoteze.add(p);
					beta = Math.min(ocena, beta);
				}
			}
			
			if (alpha >= beta) return new OcenjenaPoteza(najboljsaPoteza, ocena);
		
		}
		
		//Random RANDOM = new Random();
		//int i = RANDOM.nextInt(najbolsePoteze.size());
		//najboljsaPoteza = najbolsePoteze.get(i);
		
		return new OcenjenaPoteza(najboljsaPoteza, ocena);
	}



	
	

}
