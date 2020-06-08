package inteligenca;
/*
 * Algoritem za igranje racunalnika temelji na Minimax metodi z alphabeta dodatkom.
 * Algoritem za oceno pozicije je kar zahteven, zato je optimalna globina za hitro delovanje 
 * in dobro igro 5. 
 */

import java.util.List;

import logika.Igra;
import logika.Igralec;
import splosno.Koordinati;
import splosno.KdoIgra;


public class Inteligenca extends KdoIgra {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // Vrednost ob zmagi
	private static final int PORAZ = -ZMAGA;
	private static int globina;
	// Spodnja dva nacina uravnavata, katera od dveh ocen pozicij se bo izvedla.
	// Default ocena deluje bolje na zacetku igre, a ima pomankljivosti, zato se sredi igre pretvori na drugo funkcijo.
	// Ko se zamenja globalno tako ostane celo igro, ce pa se sprememni lokalno se lahko v igri vrne nazaj na prvotno funkcijo.
	public static boolean nacinLokalno = true;
	public static boolean nacinGlobalno = true;
	
	public Inteligenca() {
		super("Rehoboam");
		Inteligenca.globina = 5;

	}
	
	// Izbere najbolso potezo s pomocjo alphabete.
	public Koordinati izberiPotezo (Igra igra) {
		OcenjenaPoteza najboljsaPoteza;
		najboljsaPoteza = alphaBetaMinimax(igra, Inteligenca.globina, PORAZ, ZMAGA, igra.naPotezi());
		return najboljsaPoteza.poteza;
	}

	
	//-------------ALPHABETA-----------------
	public static OcenjenaPoteza alphaBetaMinimax(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		// Ce je raCunalnik prvi na potezi postavi svoj zeton v sredino.
		if (igra.zadnja == null) {
			return new OcenjenaPoteza(new Koordinati(Igra.N / 2, Igra.N / 2), 0);
		}
		
		int ocena;
		if (igra.naPotezi() == jaz) {ocena = PORAZ;} else {ocena = ZMAGA;}
		// Da ne gleda vedno vseh potez mu damo le poteze, ki jih je vredno preveriti
		List<Koordinati> moznePoteze = IzbiraPotez.izbiraPotezVse(igra, jaz, globina);	
		
		// Preverja katero funkcijo uporablja in nastavlja parametre za naprej.
		if (globina == Inteligenca.globina && !nacinLokalno) nacinGlobalno = false;
		if (globina == 1 && nacinGlobalno) nacinLokalno = true;
		
		// Bolj preventivno, ce se zgodi, da nima moznih potez (kar se ne bi smelo zgoditi)
		// pregleda vse poteze, a le na globini 3.
		if (moznePoteze.size() == 0) {
			moznePoteze = igra.poteze();
			if (globina > 3) globina = 3;
		}
		
		// Od tu naprej je standardni alphabeta algoritem.
		Koordinati najboljsaPoteza = moznePoteze.get(0); 
		
		for (Koordinati p: moznePoteze) {

			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
			int ocenaPoteze;
			

			switch (kopijaIgre.stanje()) {
			
			case ZMAGA_RDEC: {
				if (jaz == Igralec.Rdec) ocenaPoteze = ZMAGA;
				else ocenaPoteze = PORAZ;
				break;
			}
			
			case ZMAGA_MODER: {
				if (jaz == Igralec.Moder) ocenaPoteze = ZMAGA;
				else ocenaPoteze = PORAZ;				
				break;
			}
			
			default:
				// nekdo je na potezi
				if (globina == 1) {
					ocenaPoteze = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz, globina);
				}
				else {
					ocenaPoteze = alphaBetaMinimax(kopijaIgre, globina-1, alpha, beta, jaz).ocena;
				}
			}
			
			
			
		
			if (igra.naPotezi() == jaz) {
				if (ocenaPoteze > ocena) {
					ocena = ocenaPoteze;
					najboljsaPoteza = p;
					alpha = Math.max(ocena, alpha);
				}
			}
			
			else {

				if (ocenaPoteze < ocena) {
					ocena = ocenaPoteze;
					najboljsaPoteza = p;
					beta = Math.min(ocena, beta);
				}
			}
			
			if (alpha >= beta) return new OcenjenaPoteza(najboljsaPoteza, ocena);
		
		}
		
		return new OcenjenaPoteza(najboljsaPoteza, ocena);
	}


}
