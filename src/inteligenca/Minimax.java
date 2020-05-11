package inteligenca;

import java.util.List;

import logika.Igra;
//import logika.Igralec;
import logika.Igralec;
import koordinati.Koordinati;

import inteligenca.OceniPozicijo;

public class Minimax extends Inteligenca {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // vrednost ob zmagi


	
	private int globina;
	
	public Minimax (int globina) {
		super("minimax globina " + globina);
		this.globina = globina;
	}
	
	@Override
	public Koordinati izberiPotezo (Igra igra) {
		OcenjenaPoteza najboljsaPoteza = minimax(igra, this.globina);
		return najboljsaPoteza.poteza;	
	}
	
	// vrne najboljso ocenjeno potezo z vidike igralca jaz
	public OcenjenaPoteza minimax(Igra igra, int globina) {
		OcenjenaPoteza najboljsaPoteza = null;
		List<Koordinati> moznePoteze = igra.poteze();
		for (Koordinati p: moznePoteze) {
			//System.out.println(p);
			Igra kopijaIgre = new Igra(igra);
			//System.out.println(kopijaIgre);
			
			kopijaIgre.odigraj(p);
			//System.out.println("test");
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_RDEÈ:;
			case ZMAGA_MODER: ocena = ZMAGA; break;

			default:
				// nekdo je na potezi
				if (globina == 1) ocena = OceniPozicijo.oceniPozicijo(kopijaIgre, igra.naPotezi());
				// globina > 1
				else ocena = -minimax(kopijaIgre, globina-1).ocena;
			}

			if (najboljsaPoteza == null || ocena > najboljsaPoteza.ocena) {
				najboljsaPoteza = new OcenjenaPoteza(p, ocena);	

			}
		}
		return najboljsaPoteza;
	}
	
	
}