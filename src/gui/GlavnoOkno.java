package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import vodja.Vodja;
import vodja.Vodja.VrstaIgralca;
import logika.Igra;
import logika.Igralec;
import splosno.KdoIgra;


@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	
	// polje, kjer igramo igro
	private IgralnoPolje polje;

	
	//Statusna vrstica v spodnjem delu okna
	private JLabel status;
	
	// Izbire v menujih
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;
	//private JMenuItem imeRdecega;
	//private JMenuItem imeModrega;
	private JMenuItem v5, v6, v7, v8, v9, v10, v11, v12, v13;

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
		
//		imeRdecega = new JMenuItem("Nastavi ime rdecega");
//		nastavitve.add(imeRdecega);
//		imeRdecega.addActionListener(this);
//		
//		imeModrega = new JMenuItem("Nastavi ime modrega");
//		nastavitve.add(imeModrega);
//		imeModrega.addActionListener(this);
		
		JMenu velikostIgre = new JMenu("Nastavi velikost igralne mreze");
		nastavitve.add(velikostIgre);
		
		v5 = new JMenuItem("5");
		velikostIgre.add(v5);
		v5.addActionListener(this);
		v6 = new JMenuItem("6");
		velikostIgre.add(v6);
		v6.addActionListener(this);
		v7 = new JMenuItem("7");
		velikostIgre.add(v7);
		v7.addActionListener(this);
		v8 = new JMenuItem("8");
		velikostIgre.add(v8);
		v8.addActionListener(this);
		v9 = new JMenuItem("9");
		velikostIgre.add(v9);
		v9.addActionListener(this);
		v10 = new JMenuItem("10");
		velikostIgre.add(v10);
		v10.addActionListener(this);
		v11 = new JMenuItem("11");
		velikostIgre.add(v11);
		v11.addActionListener(this);
		v12 = new JMenuItem("12");
		velikostIgre.add(v12);
		v12.addActionListener(this);
		v13 = new JMenuItem("13");
		velikostIgre.add(v13);
		v13.addActionListener(this);

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
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
							    status.getFont().getStyle(),
							    20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		status.setText("Izberite igro!");
		
	}
	
	
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
		//} else if (e.getSource() == imeRdecega) {
		//} else if (e.getSource() == imeModrega) {
		} else {
			if (e.getSource() == v5) {
				Igra.N = 5;
			} else if (e.getSource() == v6) {
				Igra.N = 6;
			} else if (e.getSource() == v7) {
				Igra.N = 7;
			} else if (e.getSource() == v8) {
				Igra.N = 8;
			} else if (e.getSource() == v9) {
				Igra.N = 9;
			} else if (e.getSource() == v10) {
				Igra.N = 10;
			} else if (e.getSource() == v11) {
				Igra.N = 11;
			} else if (e.getSource() == v12) {
				Igra.N = 12;
			} else if (e.getSource() == v13) {
				Igra.N = 13;
			}
			Vodja.igra = null;
			Vodja.clovekNaVrsti = false;
			osveziGUI();
		}
	}
	
	// GUI = graphical user interface
	public void osveziGUI() {
		if (Vodja.igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch(Vodja.igra.stanje()) {
			case V_TEKU: 
				status.setText("Na potezi je " + Vodja.igra.naPotezi() + 
						" - " + Vodja.kdoIgra.get(Vodja.igra.naPotezi()).ime()); 
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
