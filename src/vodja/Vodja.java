package vodja;
/*
 * Ta datoteka upravlja potek igre.
 * 
 * Vsako potezo torej klice ustreznega igralca, prav tako ob koncu konca igro.
 */

import javax.swing.SwingWorker;

import gui.GlavnoOkno;
import inteligenca.Inteligenca;

import java.util.Map;

import logika.Igra;
import logika.Igralec;
import splosno.KdoIgra;
import splosno.Koordinati;

public class Vodja {
	
	// Napovedane komponentne.
	public static enum VrstaIgralca { Racunalnik, Clovek; }
	public static GlavnoOkno okno;
	public static Igra igra = null;
	public static boolean clovekNaVrsti = false;
	
	// Slovarja, ki vsebujeta podatke o igralcih.
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	public static Map<Igralec,KdoIgra> kdoIgra;
	
	// Metodi, ki zazeneta novo igro.
	public static void igramoNovoIgro () {
		igra = new Igra(Igra.N);
		System.out.println(Inteligenca.nacinGlobalno);
		System.out.println(Inteligenca.nacinLokalno);

		//Inteligenca.nacinGlobalno = true;
		//Inteligenca.nacinLokalno = true;
		igramo();
	}
	
	public static void igramoNovoIgro(int n) {
		
		igra = new Igra(n);
		//System.out.println(Inteligenca.nacinGlobalno);
		//System.out.println(Inteligenca.nacinLokalno);
		//Inteligenca.nacinGlobalno = true;
		//Inteligenca.nacinLokalno = true;
		igramo();
	}
	
	// Glavna metoda, ki glede na stanje igre ustrezno razdeli naloge.
	public static void igramo () {
		// Osvezi okno, da prikaze trenutno stanje.
		okno.osveziGUI();

		// Potek odvisen od stanja igre.
		switch (igra.stanje()) {
		case ZMAGA_RDEC: 
			break;
		case ZMAGA_MODER: 
			break;
		case V_TEKU: 
			// Ko igra se poteka, poisca trenutnega igralca na potezi in ga vprasa za potezo.
			Igralec igralec = igra.naPotezi;
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case Clovek: 
				clovekNaVrsti = true;
				break;
			case Racunalnik:
				racunalnikovaPoteza();
				break;
			}
		}
	}
	
	// Metodi za racunalnikovo igro.
	// Klic, ki vspostavi racunalnik ob zagonu nove igre.
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca();
	
	// Ta metoda, pa na vsaki potezi, poklice razred "Inteligenca", ki vrne potezo.
	public static void racunalnikovaPoteza() {
		Igra zacetnaIgra = igra;
		// V drugi veji poisce potezo, ki jo nato tudi odigra.
		SwingWorker<Koordinati, Void> worker = 
				new SwingWorker<Koordinati, Void> () {
			@Override
			protected Koordinati doInBackground() {
				Koordinati poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				return poteza;
			}
			@Override
			protected void done () {
				Koordinati poteza = null;
				try {poteza = get();} catch (Exception e) {};
				// Preveriti moramo, ali je igra v vmesnem casu ostala ista.
				if (igra == zacetnaIgra) {
					igra.odigraj(poteza);
					igramo();
				}
			}
		};
		worker.execute();
	}
	
	// Sledijo se metode, ki se ticejo clovekovih potez.
	// Metoda za clovekovo potezo.	
	public static void clovekovaPoteza(Koordinati poteza) {
		// Tukaj je igralno polje aktivno za klike, ko dobimo potezo, ga zopet ugasnemo.
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo();
	}
	
	// Klica naslednjih metod, sta mozna le v primeru, ko igrata dva cloveka.
	public static void razveljaviPotezo() {
		igra.razveljavi();
	}
	
	public static void menjajBarvo() {
		igra.menjaj();
	}
	
}
