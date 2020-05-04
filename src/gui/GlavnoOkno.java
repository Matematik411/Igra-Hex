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
import logika.Igralec;



/**
 * Glavno okno aplikacije hrani trenutno stanje igre in nadzoruje potek
 * igre.
 * 
 * @author AS
 *
 */
@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	/**
	 * JPanel, v katerega igramo
	 */
	private IgralnoPolje polje;

	
	//Statusna vrstica v spodnjem delu okna
	private JLabel status;
	
	// Izbire v menujih
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;
	private JMenuItem imeRdecega;
	private JMenuItem imeModrega;
	private JMenuItem velikostIgre;

	/**
	 * Ustvari novo glavno okno in pricni igrati igro.
	 */
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
		
		imeRdecega = new JMenuItem("Nastavi ime rdecega");
		nastavitve.add(imeRdecega);
		imeRdecega.addActionListener(this);
		
		imeModrega = new JMenuItem("Nastavi ime modrega");
		nastavitve.add(imeModrega);
		imeModrega.addActionListener(this);
		
		velikostIgre = new JMenuItem("Nastavi velikost igralne mreze");
		nastavitve.add(velikostIgre);
		velikostIgre.addActionListener(this);

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
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Raèunalnik); 
			Vodja.vrstaIgralca.put(Igralec.Rdeè, VrstaIgralca.Èlovek);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Rdeè, VrstaIgralca.Raèunalnik); 
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Èlovek);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraClovekClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Rdeè, VrstaIgralca.Èlovek); 
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Èlovek);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.Rdeè, VrstaIgralca.Raèunalnik); 
			Vodja.vrstaIgralca.put(Igralec.Moder, VrstaIgralca.Raèunalnik);
			Vodja.igramoNovoIgro();
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
				status.setText("Na potezi je " + Vodja.igra.naPotezi + 
						" - " + Vodja.vrstaIgralca.get(Vodja.igra.naPotezi)); 
				break;
			case ZMAGA_RDEÈ: 
				status.setText("Zmagal je RDEC - " + 
						Vodja.vrstaIgralca.get(Vodja.igra.naPotezi.nasprotnik()));
				break;
			case ZMAGA_MODER: 
				status.setText("Zmagal je MODER - " + 
						Vodja.vrstaIgralca.get(Vodja.igra.naPotezi.nasprotnik()));
				break;
			}
		}
		polje.repaint();
	}
	



}
