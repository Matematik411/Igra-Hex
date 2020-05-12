package vodja;


import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import gui.GlavnoOkno;
import inteligenca.Inteligenca;
import inteligenca.Minimax;
import inteligenca.RandomMinimax;

import java.util.Map;
import java.util.List;

import logika.Igra;
import logika.Igralec;
import splosno.Koordinati;

public class Vodja {
	
	public static enum VrstaIgralca { Raèunalnik, Èlovek; }
	public static GlavnoOkno okno;
	public static Igra igra = null;
	public static boolean clovekNaVrsti = false;
	
	//??
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	
	public static void igramoNovoIgro () {
		igra = new Igra();
		igramo();
	}
	
	public static void igramoNovoIgro(int n) {
		igra = new Igra(n);
		igramo();
	}
	
	
	public static void igramo () {
		okno.osveziGUI();

		switch (igra.stanje()) {
		case ZMAGA_RDEÈ: 
			break;
		case ZMAGA_MODER: 
			break;
		case V_TEKU: 
			Igralec igralec = igra.naPotezi;
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case Èlovek: 
				clovekNaVrsti = true;
				break;
			case Raèunalnik:
				racunalnikovaPoteza();
				break;
			}
		}
	}
	
//	private static Random random = new Random ();
//	
//	public static void racunalnikovaPoteza() {
//		List<Koordinati> moznePoteze = igra.poteze();
//		int randomIndex = random.nextInt(moznePoteze.size());
//		Koordinati poteza = moznePoteze.get(randomIndex);
//		igra.odigraj(poteza);
//		igramo();	
//	}

	
	public static Inteligenca racunalnikovaInteligenca = new Minimax(2);
	
	public static void racunalnikovaPoteza() {
		Igra zacetnaIgra = igra;
		SwingWorker<Koordinati, Void> worker = 
				new SwingWorker<Koordinati, Void> () {
			@Override
			protected Koordinati doInBackground() {
				Koordinati poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				try {TimeUnit.SECONDS.sleep(2);} catch (Exception e) {};
				return poteza;
			}
			@Override
			protected void done () {
				Koordinati poteza = null;
				try {poteza = get();} catch (Exception e) {};
				if (igra == zacetnaIgra) {
					igra.odigraj(poteza);
					igramo ();
				}
			}
		};
		worker.execute();
	}
	
	
	public static void clovekovaPoteza(Koordinati poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo();
	}
	
}
