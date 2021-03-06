package gui;
/*
 * Tu se nahaja opis igralnega polja, po katerem se igra.
 * 
 * Glavni del polja je mreza iz sestkotnikov, ki je obrobljena z barvama igralcev,
 * na koncih, ki jih zelita povezati s potjo.
 * 
 * Osvetljena je zadnji postavljeni zeton, v trenutku zmage, pa se zmagovalna pot pobarva.
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.JPanel;

import vodja.Vodja; 

import logika.Igra;
import logika.Igralec;
import logika.Tocka;
import splosno.Koordinati;

@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel implements MouseListener {
	
	// Napovedane kolicine s katerimi tekom datoteke racunam.
	// a = stranica sestkotnika mreze
	private double a;
	// k = polovica sirine sestkotnika v navpicnem polozaju
	private double k;
	// Beli prostor (ws) je prostor okoli narisane figure, da le ta ni cisto ob robu.
	private double ws;
	
	// Relativna sirina crte glede na a.
	private final static double LINE_WIDTH = 0.10;
	// Relativni prostor okoli zetonov glede na a.
	private final static double PADDING = 0.10;

	// Konstruktor, ki zgradi igralno polje velikosti getPreferredSize().
	public IgralnoPolje() {
		setBackground(Color.WHITE);
		this.addMouseListener(this);	
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(850, 500);
	}

	
	// Stranico izracunamo glede na velikost odprtega okna.
	private void stranica() {
		// Poracunamo kaksna je lahko stranica, da bo na polju vse potrebno.
		double sirina = (2 * getWidth()) / (Math.sqrt(3.0) * (3 * (Igra.N+2) - 2));
		double visina = (2 * getHeight()) / (3 * (Igra.N+2) - 2);
		
		// Izberemo manjso od izracunanih vrednosti in poracunamo spremenljivke.
		a =  Math.min(sirina, visina);
		k = Math.sqrt(3) * a * 0.5;
		ws = 2 * a;
	}
	
	// Metoda, ki narise zeton v i-ti stolpec in j-to vrstico mreze.
	private void narisiZeton(Graphics2D g2, int i, int j, Igralec p) {
		// Izracunam premer zetona.
		double d = 2 * a * (1.0 - LINE_WIDTH - 2.0 * PADDING);
		
		// Nato se sredisce.
		int xSred = (int) (ws + (2 * i + j + 1) * k);
		int ySred = (int) (ws*0.5 + (1.5 * j + 1) * a);
		double x = xSred - d * 0.5;
		double y = ySred - d * 0.5;
		
		// Izberem usrezno barvo in narisem zeton.
		if (p == Igralec.Rdec) g2.setColor(Color.RED);
		else g2.setColor(Color.BLUE);
		g2.fillOval((int)x, (int)y, (int)d , (int)d);
		
		// Zadnji postavljeni zeton osvetlim.
		if (Vodja.igra.zadnja != null && 
				Vodja.igra.zadnja.koordinati.getX() == i && 
				Vodja.igra.zadnja.koordinati.getY() == j) {
			g2.setStroke(new BasicStroke((float) (1.5 * a * LINE_WIDTH)));
			g2.setColor(Color.ORANGE);
			g2.drawOval((int)x, (int)y, (int)d, (int)d);
		}
	}
	
	// Sledi 6 metod, ki narisejo mrezo in obarvana ombocja ob robih.
	private int[][] navpicnaCrta(int i, int n) {
		int d = 2 * n;
		
		int[][] crta = new int[2][d];
		int[] xKoord = new int[d];
		int[] yKoord = new int[d];
		for (int j = 0; j < n; j++) {
			xKoord[2*j] = (int) (k * (2 * i + j) + ws);
			xKoord[2*j + 1] = (int) (k * (2 * i + j) + ws);
			yKoord[2*j] = (int) (a * (0.5 + 1.5 * j) + ws*0.5);
			yKoord[2*j + 1] = (int) (a * (1.5 + 1.5 * j) + ws*0.5);
		}
		crta[0] = xKoord;
		crta[1] = yKoord;
		return crta;
	}
	
	private int[][] leviModri(int n) {
		int d = 2 * n + 3;
		
		int[][] crta = new int[2][d];
		int[] xKoord = new int[d];
		int[] yKoord = new int[d];
		for (int j = 0; j < n; j++) {
			xKoord[2*j] = (int) (k * j + ws);
			xKoord[2*j + 1] = (int) (k * j + ws);
			yKoord[2*j] = (int) (a * (0.5 + 1.5 * j) + ws*0.5);
			yKoord[2*j + 1] = (int) (a * (1.5 + 1.5 * j) + ws*0.5);
		}
		
		xKoord[2*n] = (int) (k * (Igra.N - 0.5) + ws);
		yKoord[2*n] = (int) (a * (1.5 * Igra.N + 0.25) + ws*0.5);
		
		xKoord[2*n+1] = (int) (k * (Igra.N - 1) + ws);
		yKoord[2*n+1] = (int) (a * (1.5 * Igra.N + 1) + ws*0.5);
		
		xKoord[2*n+2] = (int) (ws - 2 * k);
		yKoord[2*n+2] = (int) (ws*0.5 - 0.5 * a);
		
		crta[0] = xKoord;
		crta[1] = yKoord;
		return crta;
	}
	
	private int[][] desniModri(int n) {
		int d = 2 * n + 3;
		
		int[][] crta = new int[2][d];
		int[] xKoord = new int[d];
		int[] yKoord = new int[d];
		for (int j = 0; j < n; j++) {
			xKoord[2*j] = (int) (k * (2 * n + j) + ws);
			xKoord[2*j + 1] = (int) (k * (2 * n + j) + ws);
			yKoord[2*j] = (int) (a * (0.5 + 1.5 * j) + ws*0.5);
			yKoord[2*j + 1] = (int) (a * (1.5 + 1.5 * j) + ws*0.5);
		}
		
		xKoord[2*n] = (int) (k * (3 * n + 1) + ws);
		yKoord[2*n] = (int) (a * (1 + 1.5 * n) + ws*0.5);
		
		xKoord[2*n+1] = (int) (2 * n * k + ws);
		yKoord[2*n+1] = (int) (ws*0.5 - 0.5 * a);
		
		xKoord[2*n+2] = (int) (k * (n * 2 - 0.5) + ws);
		yKoord[2*n+2] = (int) (ws*0.5 + 0.25 * a);
		
		crta[0] = xKoord;
		crta[1] = yKoord;
		return crta;
	}
	
	private int[][] vodoravnaCrta(int j, int n) {
		int d = 2 * n + 1;
		
		int[][] crta = new int[2][d];
		int[] xKoord = new int[d];
		int[] yKoord = new int[d];
		for (int i = 0; i < n; i++) {
			xKoord[2 * i] = (int) (k * (j + 2 * i) + ws);
			xKoord[2 * i + 1] = (int) (k * (j + 2 * i + 1) + ws); 
			yKoord[2 * i] = (int) (0.5 * a * (3 * j + 1) + ws*0.5);
			yKoord[2 * i + 1] = (int) (1.5 * a * j + ws*0.5);
		}
		xKoord[d - 1] = (int) (k * (j + 2 * n) + ws);
		yKoord[d - 1] = (int) (0.5 * a * (3 * j + 1) + ws*0.5);
		crta[0] = xKoord;
		crta[1] = yKoord;
		return crta;
	}
	
	private int[][] zgorajRdeci(int n) {
		int d = 2 * n + 3;
		
		int[][] crta = new int[2][d];
		int[] xKoord = new int[d];
		int[] yKoord = new int[d];
		for (int i = 0; i < n; i++) {
			xKoord[2 * i] = (int) (k * 2 * i + ws);
			xKoord[2 * i + 1] = (int) (k * (2 * i + 1) + ws); 
			yKoord[2 * i] = (int) (0.5 * a + ws*0.5);
			yKoord[2 * i + 1] = (int) (ws*0.5);
		}
		
		xKoord[2*n] = (int) (k * (n * 2 - 0.5) + ws);
		yKoord[2*n] = (int) (ws*0.5 + 0.25 * a);
		
		xKoord[2*n+1] = (int) (2 * n * k + ws);
		yKoord[2*n+1] = (int) (ws*0.5 - 0.5 * a);
		
		xKoord[2*n+2] = (int) (ws - 2 * k);
		yKoord[2*n+2] = (int) (ws*0.5 - 0.5 * a);
		
		crta[0] = xKoord;
		crta[1] = yKoord;
		return crta;
	}
	
	private int[][] spodajRdeci(int n) {
		int d = 2 * n + 3;
		
		int[][] crta = new int[2][d];
		int[] xKoord = new int[d];
		int[] yKoord = new int[d];
		for (int i = 0; i < n; i++) {
			xKoord[2 * i] = (int) (k * (n + 2 * i) + ws);
			xKoord[2 * i + 1] = (int) (k * (n + 2 * i + 1) + ws); 
			yKoord[2 * i] = (int) (0.5 * a * (3 * n + 1) + ws*0.5);
			yKoord[2 * i + 1] = (int) (1.5 * a * n + ws*0.5);
		}
		
		xKoord[2*n] = (int) (k * (3 * n + 1) + ws);
		yKoord[2*n] = (int) (a * (1 + 1.5 * n) + ws*0.5);
		
		xKoord[2*n+1] = (int) (k * (Igra.N - 1) + ws);
		yKoord[2*n+1] = (int) (a * (1.5 * Igra.N + 1) + ws*0.5);
		
		xKoord[2*n+2] = (int) (k * (Igra.N - 0.5) + ws);
		yKoord[2*n+2] = (int) (a * (1.5 * Igra.N + 0.25) + ws*0.5);
		
		crta[0] = xKoord;
		crta[1] = yKoord;
		return crta;
	}
	
	// To pa je metoda, ki vrne sestkotnik v mrezi, za namene barvanja zmagovalne poti.
	private int[][] sestkotnik(int i, int j) {
		int[][] sestkotnik = new int[2][6];
		
		int[] xKoord = new int[6];
		int xSred = (int) (ws + (2 * i + j + 1) * k);

		xKoord[0] = (int) (xSred + k);
		xKoord[1] = xSred;
		xKoord[2] = (int) (xSred - k);
		xKoord[3] = (int) (xSred - k);
		xKoord[4] = xSred;
		xKoord[5] = (int) (xSred + k);
		
		int ySred = (int) (ws*0.5 + (1.5 * j + 1) * a);
		int[] yKoord = new int[6];
		yKoord[0] = (int) (ySred + 0.5 * a);
		yKoord[1] = (int) (ySred + a);
		yKoord[2] = (int) (ySred + 0.5 * a);
		yKoord[3] = (int) (ySred - 0.5 * a);
		yKoord[4] = (int) (ySred - a);
		yKoord[5] = (int) (ySred - 0.5 * a);
		
		sestkotnik[0] = xKoord;
		sestkotnik[1] = yKoord;
		return sestkotnik;
	}
	
	// Sledi glavna metoda razreda, ki se klice ob vsakem osvezevanju in na pravilen
	// nacin klice vse prejsnje, iz njih dobi elemente, te pa nato narise na polje.
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		// Na zacetku izracunamo stranico.
		stranica();

		// V primeru, da imamo zmagovalno crto, njeno ozadje pobarvamo.
		Set<Tocka> t = null;
		if (Vodja.igra != null) {t = Vodja.igra.konec;}
		if (t != null) {
			g2.setColor(new Color(255, 215, 0));
			for (Tocka P : t) {
				int i = P.koordinati.getX();
				int j = P.koordinati.getY();
				int[][] sestkotnik = sestkotnik(i, j);
				g2.fillPolygon(sestkotnik[0], sestkotnik[1], 6);
			}
		}
		
		// Zdaj s pravilnimi argumenti klicemo metode, ki vracajo dele mreze.

		// Najprej modri robni obmocji.
		g2.setColor(Color.BLUE);
		int[][] crta = leviModri(Igra.N);
		g2.fillPolygon(crta[0], crta[1], 2 * Igra.N + 3);
		crta = desniModri(Igra.N);
		g2.fillPolygon(crta[0], crta[1], 2 * Igra.N + 3);

		// Nato rdeci robni obmocji.
		g2.setColor(Color.RED);
		crta = zgorajRdeci(Igra.N);
		g2.fillPolygon(crta[0], crta[1], 2 * Igra.N + 3);
		crta = spodajRdeci(Igra.N);
		g2.fillPolygon(crta[0], crta[1], 2 * Igra.N + 3);

		// In se crte mreze.
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((float) (a * LINE_WIDTH)));
		for (int i = 0; i <= Igra.N; i++) {
			crta = navpicnaCrta(i, Igra.N);
			g2.drawPolyline(crta[0], crta[1], 2 * Igra.N);
			
			crta = vodoravnaCrta(i, Igra.N);
			g2.drawPolyline(crta[0], crta[1], 2 * Igra.N);	 
		}
		// Ostaneta se dve robni crti, ki nista zajeti v zankah.
		g2.drawLine((int) (k * (Igra.N - 1) + ws), (int) (Igra.N * 1.5 * a + ws*0.5),
			(int) (k * Igra.N + ws), (int) (a * (1.5 * Igra.N + 0.5) + ws*0.5));
		g2.drawLine((int) ((2 * Igra.N - 1) * k + ws), (int) (ws*0.5), 
				(int) (2 * Igra.N * k + ws), (int) (a * 0.5 + ws*0.5));
		
		// Na polje moramo dodati se zetone.
		Tocka[][] plosca;;
		if (Vodja.igra != null) {
			plosca = Vodja.igra.getPlosca();
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					switch(plosca[j][i].polje) {
					case Moder: narisiZeton(g2, i, j, Igralec.Moder); break;
					case Rdec: narisiZeton(g2, i, j, Igralec.Rdec); break;
					default: break;
					}
				}
			}
		}	
		
	}
	
	// Naslednja metoda zazna, v katero polje mreze je kliknil clovek za svojo potezo.
	@Override
	public void mouseClicked(MouseEvent e) {
		// Mreza je aktivna le v primeru, ko je na potezi clovek.
		if (Vodja.clovekNaVrsti) {
			// Preberemo x, y koordinati klika, ki se nato pretvorita v i, j koordinati
			// mreze. Pri tem je potrebno obravnavati vec moznosti.
			int x = (int) (e.getX() - ws);
			int y = (int) (e.getY() - ws*0.5);

			int navpicno = (int) ( y / (0.5 * a));
			int vodoravno = (int) (x / k);

			// 1. moznosti - smo v srednjem delu sestkotnika po visini.
			if (navpicno % 3 > 0) {
				int j = navpicno / 3;
				vodoravno -= j;
				int i = vodoravno / 2;
				double d = Math.abs(k * (2 * i + 1 + j) - x);
				if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N &&
						(k - d) > (a *LINE_WIDTH * 0.5)) {
					Vodja.clovekovaPoteza(new Koordinati(i, j));
				}
			}
			
			// Sicer smo v pasu kjer crte tvorijo lomljenko.
			// 2. - ce se navpicno in vodoravno sesteje v sodo stevilo, imamo primer /.
			else if ((navpicno + vodoravno) % 2 == 0) {
				int relX = (int) (x % k);
				int relY = (int) (y % (0.5 * a));

				// Lahko se nahajamo nad crto.
				if (relY < ((-0.5 * a) / k) * relX + (0.5 - LINE_WIDTH * 0.5) * a) {
					int j = (navpicno / 3) - 1;
					int i = (vodoravno - j) / 2;
					if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N) {
						Vodja.clovekovaPoteza(new Koordinati(i, j));
					}
					
				}
				// Ali pod njo.
				else if (relY > ((-0.5 * a) / k) * relX + (0.5 + LINE_WIDTH * 0.5) * a) {
					int j = navpicno / 3;
					int i = ((vodoravno - j) / 2) ;
					if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N) {
						Vodja.clovekovaPoteza(new Koordinati(i, j));
					}
					
				}
			}
			
			// 3. - ostal je primer \.
			else {
				int relX = (int) (x % k);
				int relY = (int) (y % (0.5 * a));			
				
				// Lahko smo nad crto.
				if (relY < ((0.5 * a) / k) * relX - a * LINE_WIDTH * 0.5) {
					int j = (navpicno / 3) - 1;
					int i = (vodoravno - j) / 2;
					if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N) {
						Vodja.clovekovaPoteza(new Koordinati(i, j));
					}
					
				}
				// Ali pod njo.
				else if (relY > ((0.5 * a) / k) * relX + a * LINE_WIDTH * 0.5) {
					int j = navpicno / 3;
					int i = ((vodoravno - j) / 2) ;
					if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N) {
						Vodja.clovekovaPoteza(new Koordinati(i, j));
					}
					
				}
			}

		}
	}

	// Preostalih metod ne uporabljamo.
	@Override
	public void mousePressed(MouseEvent e) {		
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}
	
}
