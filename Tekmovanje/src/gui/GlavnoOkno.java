package gui;
/*
 * V tej datoteki je narejen JFrame, ki se zazene ob zagonu aplikacije. Preko njega 
 * upravljamo vso dogajanje. 
 * Poleg igralnega polja, ki je implementiran v datoteki "IgralnoPolje.java" je v oknu 
 * tudi vrstica z menijem in izpisna vrstica, ki razlaga trenutno stanje na igralnem polju.
 */

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import vodja.Vodja;
import vodja.Vodja.VrstaIgralca;
import logika.Igra;
import logika.Igralec;
import splosno.KdoIgra;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	
	// To je glavno igralno polje, ki ga v tej datoteki le klicemo.
	private IgralnoPolje polje;
	
	// Nad igralnim poljem je vrstica k meniji, pod njim pa polje, ki vsebuje izpisno vrstico
	// vrstico in dodatne gumbe pri igri med dvema clovekoma.
	private JMenuBar menuBar;
	private JPanel dodatno;
	
	// Napovedane moznosti v menijih.
	private JMenu igraMenu;
	private JMenu nastavitve;
	private JMenu velikostIgre;
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;
	private JMenuItem navodila;
	private JMenuItem v7, v9, v11, v13;
	
	// Napovedane se moznosti iz spodnjega polja.
	private JLabel status;
	private JButton menjaj;
	private JButton razveljavi;
	
	// Glavna metoda, ki ustvari okno, ki vsebuje vso dogajanje.
	public GlavnoOkno() {
		
		// Nastavlimo osnove.
		this.setTitle("Igra hex");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
	
		// Vspostavimo vrstico z meniji.
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// V to vrstico dodamo meni, kjer izberemo nacin igre.
		igraMenu = new JMenu("Nova igra");
		menuBar.add(igraMenu);
		
		igraClovekRacunalnik = new JMenuItem("èlovek (rdeè) proti raèunalniku");
		igraMenu.add(igraClovekRacunalnik);
		igraClovekRacunalnik.addActionListener(this);
		
		igraRacunalnikClovek = new JMenuItem("raèunalnik (rdeè) proti èloveku");
		igraMenu.add(igraRacunalnikClovek);
		igraRacunalnikClovek.addActionListener(this);
		
		igraClovekClovek = new JMenuItem("èlovek (rdeè) proti èloveku");
		igraMenu.add(igraClovekClovek);
		igraClovekClovek.addActionListener(this);
		
		igraRacunalnikRacunalnik = new JMenuItem("raèunalnik (rdeè) proti raèunalniku");
		igraMenu.add(igraRacunalnikRacunalnik);
		igraRacunalnikRacunalnik.addActionListener(this);
		
		// Nato v vrstico dodamo se meni z nastavitvami.
		nastavitve = new JMenu("Nastavitve");
		menuBar.add(nastavitve);
		
		velikostIgre = new JMenu("Nastavi velikost igralne mreze.");
		nastavitve.add(velikostIgre);
		
		v7 = new JMenuItem("mreza 7x7");
		velikostIgre.add(v7);
		v7.addActionListener(this);
		v9 = new JMenuItem("mreza 9x9");
		velikostIgre.add(v9);
		v9.addActionListener(this);
		v11 = new JMenuItem("mreza 11x11 (originalna velikost)");
		velikostIgre.add(v11);
		v11.addActionListener(this);
		v13 = new JMenuItem("mreza 13x13");
		velikostIgre.add(v13);
		v13.addActionListener(this);
		
		navodila = new JMenuItem("veè o igri");
		nastavitve.add(navodila);
		navodila.addActionListener(this);

		// Igralno polje le klicemo iz drugega razreda.
		polje = new IgralnoPolje();

		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		getContentPane().add(polje, polje_layout);
		
		// Vspostavimo se spodnjo polje.
		dodatno = new JPanel();

		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(dodatno, status_layout);
		
		// Izpisna vrstica.
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
			    status.getFont().getStyle(),
			    20));
		status.setText("Izberite igro!");
		
		// Do dodatnih moznosti ob igranju med dvema clovekoma dostopamo preko gumbov.
		menjaj = new JButton("Menjaj barvo!");
		dodatno.add(menjaj);
		menjaj.addActionListener(this);
		
		razveljavi = new JButton("Razveljavi zadnjo potezo!");
		dodatno.add(razveljavi);
		razveljavi.addActionListener(this);

		dodatno.add(status);
		dodatno.add(menjaj);
		dodatno.add(razveljavi);

		// Ti moznosti nista na voljo vedno, zato zaenkrat nastavimo, da nista vidna.
		menjaj.setVisible(false);
		razveljavi.setVisible(false);

	}
	
	// V tem nizu je zapisan izpis, ki ga uporabnik vidi, ce zeli prebrati vec o igri.
	String opisIgre = "Igra Hex je zelo zanimiva in pouèna igra, ki veliko nauèi igralca!";
	
	
	// Zdaj je definiran osnovni izgled okna. 
	// Nastavimo katere akcije se bodo sprozile ob klikih na gumbe.
	@Override
	public void actionPerformed(ActionEvent e) {
		// Gumbi v meniju za izbiro igre zazenejo novo igro.
		if (e.getSource() == igraClovekRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Racunalnik); 
			Vodja.vrstaIgralca.put(Igralec.Rdec, VrstaIgralca.Clovek);
			Vodja.kdoIgra = new EnumMap<Igralec,KdoIgra>(Igralec.class);
			Vodja.kdoIgra.put(Igralec.Rdec, new KdoIgra("Uporabnik")); 
			Vodja.kdoIgra.put(Igralec.Moder, Vodja.racunalnikovaInteligenca);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Rdec, VrstaIgralca.Racunalnik); 
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Clovek);
			Vodja.kdoIgra = new EnumMap<Igralec,KdoIgra>(Igralec.class);
			Vodja.kdoIgra.put(Igralec.Rdec, Vodja.racunalnikovaInteligenca); 
			Vodja.kdoIgra.put(Igralec.Moder, new KdoIgra("Uporabnik"));
			Vodja.igramoNovoIgro();
			Vodja.clovekNaVrsti = false;
		} else if (e.getSource() == igraClovekClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Rdec, VrstaIgralca.Clovek); 
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Clovek);
			Vodja.kdoIgra = new EnumMap<Igralec,KdoIgra>(Igralec.class);
			Vodja.kdoIgra.put(Igralec.Rdec, new KdoIgra("Uporabnik")); 
			Vodja.kdoIgra.put(Igralec.Moder, new KdoIgra("Uporabnik"));
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Rdec, VrstaIgralca.Racunalnik); 
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Racunalnik);
			Vodja.kdoIgra = new EnumMap<Igralec,KdoIgra>(Igralec.class);
			Vodja.kdoIgra.put(Igralec.Rdec, Vodja.racunalnikovaInteligenca); 
			Vodja.kdoIgra.put(Igralec.Moder, Vodja.racunalnikovaInteligenca);
			Vodja.igramoNovoIgro();
			Vodja.clovekNaVrsti = false;
		} 
		// Dodatni gumbi zazenejo ustrezne metode na igri v poteku.
		else if (e.getSource() == menjaj) {
			Vodja.menjajBarvo();
		} else if (e.getSource() == razveljavi) {
			Vodja.razveljaviPotezo();
		} else if (e.getSource() == navodila) {
			JOptionPane.showMessageDialog(null, opisIgre);
		} 
		// Sprememba velikosti polja prav tako konca trenutno igro.
		else {
			if (e.getSource() == v7) {
				Igra.N = 7;
			} else if (e.getSource() == v9) {
				Igra.N = 9;
			} else if (e.getSource() == v11) {
				Igra.N = 11;
			} else if (e.getSource() == v13) {
				Igra.N = 13;
			}
			Vodja.igra = null;
			Vodja.clovekNaVrsti = false;
		}
		// Ob vsaki sprozeni akciji se mora okno osveziti.
		osveziGUI();
	}
	
	// Definirati moramo se kako se okno osvezi.
	public void osveziGUI() {
		// Dodatna gumba nastavimo v splosnem na nevidna.
		menjaj.setVisible(false);
		razveljavi.setVisible(false);
		
		// Polja so odvisna od trenutne igre, zato najprej preverimo, ali se sploh izvaja.
		if (Vodja.igra == null) {
			status.setText("Igra trenutno ne poteka.");
		}
		else {
			// Ko se igra izvaja so izpisi odvisni od trenutnega stanja.
			switch(Vodja.igra.stanje()) {
			case V_TEKU: 
				// Ko igra tece izpisemo igralca v izpisni vrstici.
				status.setText("Trenutno igra " + Vodja.igra.naPotezi() + 
						", torej " + Vodja.kdoIgra.get(Vodja.igra.naPotezi()).ime());

				// Ce igrata dva cloveka, v pravih trenutnik pokazemo dodatna gumba.
				if (Vodja.vrstaIgralca.get(Igralec.Rdec) == VrstaIgralca.Clovek && 
						Vodja.vrstaIgralca.get(Igralec.Moder) == VrstaIgralca.Clovek) {
					razveljavi.setVisible(true);
					
					if (Vodja.igra.modre.isEmpty() && !Vodja.igra.rdece.isEmpty()) {
						menjaj.setVisible(true);
					}
					if (Vodja.igra.zadnja == null ||
							(!Vodja.igra.modre.isEmpty() && Vodja.igra.rdece.isEmpty())) 
						razveljavi.setVisible(false);

				}
				break;
			// Ob koncu igre izpisemo zmagovalca.
			case ZMAGA_RDEC: 
				status.setText("Zmagovalec je RDECI igralec, torej " + 
						Vodja.kdoIgra.get(Vodja.igra.naPotezi().nasprotnik()).ime());
				break;
			case ZMAGA_MODER: 
				status.setText("Zmagovalec je MODER igralec, torej " + 
						Vodja.kdoIgra.get(Vodja.igra.naPotezi().nasprotnik()).ime());
				break;
			}
		}
		// Prav tako moramo vsakic osveziti igralno polje.
		polje.repaint();
	}
}
