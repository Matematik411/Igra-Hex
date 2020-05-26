package inteligenca;

import java.util.List;

import logika.Igra;
import logika.Igralec;
import splosno.Koordinati;
import splosno.KdoIgra;


public class Inteligenca extends KdoIgra {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // vrednost ob zmagi
	private static final int PORAZ = -ZMAGA;
	private static int glob;
	public static boolean nacinLokalno = true;
	public static boolean nacinGlobalno;
	
	public Inteligenca() {
		super("ZajcMaier");
		this.glob = 3;
		this.nacinGlobalno = true;
	}
	
	
	public Koordinati izberiPotezo (Igra igra) {
		OcenjenaPoteza najboljsaPoteza;
		najboljsaPoteza = alphaBetaMinimax(igra, this.glob, PORAZ, ZMAGA, igra.naPotezi());
		return najboljsaPoteza.poteza;
	}

	
	//-------------ALPHABETA-----------------
	public static OcenjenaPoteza alphaBetaMinimax(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		if (igra.zadnja == null) {
			return new OcenjenaPoteza(new Koordinati(igra.velikost / 2, igra.velikost / 2), 0);
		}
		
		int ocena;
		if (igra.naPotezi() == jaz) {ocena = PORAZ;} else {ocena = ZMAGA;}
		List<Koordinati> moznePoteze = IzbiraPotez.izbiraPotezVse(igra, jaz, globina);	
		
		if (globina == glob && !nacinLokalno) nacinGlobalno = false;
		if (globina == 1 && nacinGlobalno) nacinLokalno = true;
		
		if (moznePoteze.size() == 0) {
			moznePoteze = igra.poteze();
			if (globina > 3) globina = 3;
		}
		
		
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
		
		
		if (ocena == ZMAGA) {
			for (int g = 1; g < globina; g++) {
				OcenjenaPoteza boljsa = alphaBetaMinimax(igra, g, PORAZ, ZMAGA, jaz);
				if (boljsa.ocena == ZMAGA) return boljsa;
			}
		}
		
		return new OcenjenaPoteza(najboljsaPoteza, ocena);
	}



	
	

}
