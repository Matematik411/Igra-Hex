package inteligenca;

import java.util.List;
import java.util.Random;

import logika.Igra;
import splosno.Koordinati;
import splosno.KdoIgra;


public class Inteligenca extends KdoIgra {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // vrednost ob zmagi
	private int globina;
	private int ai;
	
	public Inteligenca (String ime) {
		super(ime);
		this.globina = 2;
		this.ai = 0;
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
		} else {
			List<OcenjenaPoteza> ocenjenePoteze = najboljsePoteze(igra, 2);
			Random RANDOM = new Random();
			int i = RANDOM.nextInt(ocenjenePoteze.size());	
			najboljsaPoteza = ocenjenePoteze.get(i);
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

			// ne zazna primera, ko bi nasprotnik zmagal v eni potezi in zato tega ne prepreèi
			switch (kopijaIgre.stanje()) {
			case ZMAGA_RDEÈ: 
			case ZMAGA_MODER: {ocena = ZMAGA; 
			//System.out.println(p);
			//System.out.println(ocena);
			}
			default:
				// nekdo je na potezi
				if (globina == 1) ocena = OceniPozicijo.oceniPozicijo(kopijaIgre, igra.naPotezi());
				// globina > 1
				else ocena = -minimax(kopijaIgre, globina-1).ocena;
			}
			//System.out.println(p);
			//System.out.println(ocena);
			
			if (najboljsaPoteza == null || ocena > najboljsaPoteza.ocena) {
				najboljsaPoteza = new OcenjenaPoteza(p, ocena);	
			}
		}
		return najboljsaPoteza;
	}
		
	// ------------ RANDOM MINIMAX --------------------------
	// vrne seznam vseh potez, ki imajo najvecjo vrednost z vidike trenutnega igralca na potezi
	public static List<OcenjenaPoteza> najboljsePoteze(Igra igra, int globina) {
		NajboljseOcenjenePoteze najboljsePoteze = new NajboljseOcenjenePoteze();
		List<Koordinati> moznePoteze = igra.poteze();
		for (Koordinati p: moznePoteze) {
			Igra tempIgra = new Igra(igra); 
			tempIgra.odigraj (p);	//poskusimo vsako potezo v novi kopiji igre
			int ocena;
			switch (tempIgra.stanje()) {
			case ZMAGA_RDEÈ:
			case ZMAGA_MODER: ocena = ZMAGA; break; // p je zmagovalna poteza
			default: //nekdo je na potezi
				if (globina==1) ocena = OceniPozicijo.oceniPozicijo(tempIgra,igra.naPotezi());
				else ocena = -najboljsePoteze(tempIgra,globina-1).get(0).ocena; // - ker je drug igralec 
			}
			najboljsePoteze.addIfBest(new OcenjenaPoteza(p, ocena));			
		}
		return najboljsePoteze.list();
	}


}
