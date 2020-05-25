package gui;

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
	
	// polje, kjer igramo igro
	private IgralnoPolje polje;

	// za dodatne nastavitve naredimo nov JPanel
	private JPanel dodatno;
	
	//Statusna vrstica v spodnjem delu okna
	private JLabel status;
	

	
	// Izbire v menujih
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;
	
	private JButton menjaj;
	private JButton razveljavi;
	private JMenuItem navodila;

	//private JMenuItem imeRdecega;
	//private JMenuItem imeModrega;
	private JMenuItem v7, v9, v11, v13;

	// ustvari igro
	public GlavnoOkno() {
		
		this.setTitle("Igra hex");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
	
		// menu
		JMenuBar menu_bar = new JMenuBar();
		this.setJMenuBar(menu_bar);
		JMenu igra_menu = new JMenu("Nova igra");
		menu_bar.add(igra_menu);
		
		igraClovekRacunalnik = new JMenuItem("Clovek - racunalnik");
		igra_menu.add(igraClovekRacunalnik);
		igraClovekRacunalnik.addActionListener(this);
		
		igraRacunalnikClovek = new JMenuItem("Racunalnik - clovek");
		igra_menu.add(igraRacunalnikClovek);
		igraRacunalnikClovek.addActionListener(this);
		
		igraClovekClovek = new JMenuItem("Clovek - clovek");
		igra_menu.add(igraClovekClovek);
		igraClovekClovek.addActionListener(this);
		
		igraRacunalnikRacunalnik = new JMenuItem("Racunalnik - racunalnik");
		igra_menu.add(igraRacunalnikRacunalnik);
		igraRacunalnikRacunalnik.addActionListener(this);
		
		// nastavitve
		JMenu nastavitve = new JMenu("Nastavitve");
		menu_bar.add(nastavitve);
		
		JMenu velikostIgre = new JMenu("Nastavi velikost igralne mreze");
		nastavitve.add(velikostIgre);
		

		v7 = new JMenuItem("7");
		velikostIgre.add(v7);
		v7.addActionListener(this);
		v9 = new JMenuItem("9");
		velikostIgre.add(v9);
		v9.addActionListener(this);
		v11 = new JMenuItem("11");
		velikostIgre.add(v11);
		v11.addActionListener(this);
		v13 = new JMenuItem("13");
		velikostIgre.add(v13);
		v13.addActionListener(this);
		
		navodila = new JMenuItem("o igri");
		nastavitve.add(navodila);
		navodila.addActionListener(this);

		// igralno polje
		polje = new IgralnoPolje();

		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		getContentPane().add(polje, polje_layout); // da vzame polje in layout skupaj
		
		// statusna vrstica za sporocila
		dodatno = new JPanel();

		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
			    status.getFont().getStyle(),
			    20));
		status.setText("Izberite igro!");
		
		menjaj = new JButton("Menjaj barvo!");
		dodatno.add(menjaj);
		menjaj.addActionListener(this);
		
		razveljavi = new JButton("Razveljavi zadnjo potezo!");
		dodatno.add(razveljavi);
		razveljavi.addActionListener(this);

		dodatno.add(status);
		dodatno.add(menjaj);
		dodatno.add(razveljavi);
		getContentPane().add(dodatno, status_layout);
		menjaj.setVisible(false);
		razveljavi.setVisible(false);

	}
	
	String opisIgre = "tu napiseva ker zeliva o igri";
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == igraClovekRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Racunalnik); 
			Vodja.vrstaIgralca.put(Igralec.Rdec, VrstaIgralca.Clovek);
			Vodja.kdoIgra = new EnumMap<Igralec,KdoIgra>(Igralec.class);
			Vodja.kdoIgra.put(Igralec.Rdec, new KdoIgra("Clovek")); 
			Vodja.kdoIgra.put(Igralec.Moder, Vodja.racunalnikovaInteligenca);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Rdec, VrstaIgralca.Racunalnik); 
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Clovek);
			Vodja.kdoIgra = new EnumMap<Igralec,KdoIgra>(Igralec.class);
			Vodja.kdoIgra.put(Igralec.Rdec, Vodja.racunalnikovaInteligenca); 
			Vodja.kdoIgra.put(Igralec.Moder, new KdoIgra("Clovek"));
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraClovekClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Rdec, VrstaIgralca.Clovek); 
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Clovek);
			Vodja.kdoIgra = new EnumMap<Igralec,KdoIgra>(Igralec.class);
			Vodja.kdoIgra.put(Igralec.Rdec, new KdoIgra("Clovek")); 
			Vodja.kdoIgra.put(Igralec.Moder, new KdoIgra("Clovek"));
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Rdec, VrstaIgralca.Racunalnik); 
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Racunalnik);
			Vodja.kdoIgra = new EnumMap<Igralec,KdoIgra>(Igralec.class);
			Vodja.kdoIgra.put(Igralec.Rdec, Vodja.racunalnikovaInteligenca); 
			Vodja.kdoIgra.put(Igralec.Moder, Vodja.racunalnikovaInteligenca);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == menjaj) {
			Vodja.menjajBarvo();
		} else if (e.getSource() == razveljavi) {
			Vodja.razveljaviPotezo();
		} else if (e.getSource() == navodila) {
			JOptionPane.showMessageDialog(null, opisIgre);
		} else {
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
		osveziGUI();
	}
	
	// GUI = graphical user interface
	public void osveziGUI() {
		menjaj.setVisible(false);
		razveljavi.setVisible(false);
		if (Vodja.igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch(Vodja.igra.stanje()) {
			case V_TEKU: 
				status.setText("Na potezi je " + Vodja.igra.naPotezi() + 
						" - " + Vodja.kdoIgra.get(Vodja.igra.naPotezi()).ime());

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
			case ZMAGA_RDEC: 
				status.setText("Zmagal je RDEC - " + 
						Vodja.kdoIgra.get(Vodja.igra.naPotezi().nasprotnik()).ime());
				break;
			case ZMAGA_MODER: 
				status.setText("Zmagal je MODER - " + 
						Vodja.kdoIgra.get(Vodja.igra.naPotezi().nasprotnik()).ime());
				break;
			}
		}
		polje.repaint();
	}
	



}
