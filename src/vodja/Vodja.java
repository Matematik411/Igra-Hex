package vodja;


import java.util.Random;

import gui.GlavnoOkno;

import java.util.Map;
import java.util.List;

import logika.Igra;
import logika.Igralec;
import koordinati.Koordinati;

public class Vodja {
	
	public static enum VrstaIgralca { Ra�unalnik, �lovek; }
	public static GlavnoOkno okno;
	public static Igra igra = null;
	public static boolean clovekNaVrsti = false;
	
	//??
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	
	public static void igramoNovoIgro () {
		igra = new Igra ();
		igramo ();
	}
	
	
	public static void igramo () {
		okno.osveziGUI();

		switch (igra.stanje()) {
		case ZMAGA_RDE�: 
			break;
		case ZMAGA_MODER: 
			break;
		case V_TEKU: 
			Igralec igralec = igra.naPotezi;
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case �lovek: 
				clovekNaVrsti = true;
				break;
			case Ra�unalnik:
				racunalnikovaPoteza();
				break;
			}
		}
	}
	
	private static Random random = new Random ();
	
	public static void racunalnikovaPoteza() {
		List<Koordinati> moznePoteze = igra.poteze();
		int randomIndex = random.nextInt(moznePoteze.size());
		Koordinati poteza = moznePoteze.get(randomIndex);
		igra.odigraj(poteza);
		igramo();	
	}
	
	
	public static void clovekovaPoteza(Koordinati poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo ();
	}
	
}
