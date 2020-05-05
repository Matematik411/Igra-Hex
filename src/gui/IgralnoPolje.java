package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.JPanel;

import vodja.Vodja; //ce ne importamo vedno rabimo vodja.Vodja, zdaj le Vodja...

import logika.Igra;
import logika.Igralec;
//import logika.Polje;
import logika.Tocka;
import koordinati.Koordinati;

/**
 * Pravokotno obmocje, v katerem je narisano igralno polje.
 */
@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel implements MouseListener {
	
	private double a;
	private double k;
	private double ws; 
	
	// Relativna sirina crte
	private final static double LINE_WIDTH = 0.05;
	// Relativni prostor okoli zetonov
	private final static double PADDING = 0.10;
	// Relativni prostor okoli mreze
	private final static double AROUND = 0.05;
	
	public IgralnoPolje() {
		setBackground(Color.WHITE);
		this.addMouseListener(this);
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700, 500);
	}

	
	// sirina enega kvadratka
	private void stranica() {
		//return Math.min(getWidth(), getHeight()) / Igra.N;
		double sirina = (2 * getWidth() * (1 - AROUND)) / (Math.sqrt(3.0) * (3 * Igra.N - 1));
		double visina = (2 * getHeight() * (1 - AROUND)) / (3 * Igra.N + 1);
		a =  Math.min(sirina, visina);
		k = Math.sqrt(3) * a * 0.5;
		ws = Math.min(getHeight(), getWidth()) * 0.5 * AROUND;
	}
	


	private void narisiZeton(Graphics2D g2, int i, int j, Igralec p) {
		// premer zetona
		double d = 2 * a * (1.0 - LINE_WIDTH - 2.0 * PADDING);
		// sredisce
		int xSred = (int) (ws + (2 * i + j + 1) * k);
		int ySred = (int) (ws + (1.5 * j + 1) * a);
		double x = xSred - d * 0.5;
		double y = ySred - d * 0.5;
		if (p == Igralec.Rdeè) g2.setColor(Color.GREEN);
		else g2.setColor(Color.BLUE);
		
		//g2.setStroke(new BasicStroke((float) (a * LINE_WIDTH)));
		g2.fillOval((int)x, (int)y, (int)d , (int)d);
	}
	
	private int[][] navpicnaCrta(int i, int n) {
		int d = 2 * n;
		
		int[][] crta = new int[2][d];
		int[] xKoord = new int[d];
		int[] yKoord = new int[d];
		for (int j = 0; j < n; j++) {
			xKoord[2*j] = (int) (k * (2 * i + j) + ws);
			xKoord[2*j + 1] = (int) (k * (2 * i + j) + ws);
			yKoord[2*j] = (int) (a * (0.5 + 1.5 * j) + ws);
			yKoord[2*j + 1] = (int) (a * (1.5 + 1.5 * j) + ws);
		}
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
			yKoord[2 * i] = (int) (0.5 * a * (3 * j + 1) + ws);
			yKoord[2 * i + 1] = (int) (1.5 * a * j + ws);
		}
		xKoord[d - 1] = (int) (k * (j + 2 * n) + ws);
		yKoord[d - 1] = (int) (0.5 * a * (3 * j + 1) + ws);
		crta[0] = xKoord;
		crta[1] = yKoord;
		return crta;
	}
	
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
		
		int ySred = (int) (ws + (1.5 * j + 1) * a);
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
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		stranica();

		// ce imamo zmagovalno terico, njeno ozadje pobarvamo
		Set<Tocka> t = null;
		if (Vodja.igra != null) {t = Vodja.igra.konec;}
		if (t != null) {
			g2.setColor(new Color(255, 215, 0));
			for (Tocka P : t) {
				System.out.println(P.koordinati);
//				int j = P.koordinati.y;
//				int[][] sestkotnik = sestkotnik(i, j);
//				g2.fillPolygon(sestkotnik[0], sestkotnik[1], 6);
			}
		}
		
		// crte
		// najprej robne, ki so posebnih barv in krepke
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke((float) (3 * a * LINE_WIDTH)));
		int[][] crta = navpicnaCrta(0, Igra.N);
		g2.drawPolyline(crta[0], crta[1], 2 * Igra.N);
		crta = navpicnaCrta(Igra.N, Igra.N);
		g2.drawPolyline(crta[0], crta[1], 2 * Igra.N);
		// crti v kotih desno-zgoraj in levo-spodaj narisemo posebej
		g2.drawLine((int) (k * (Igra.N - 1) + ws), (int) (Igra.N * 1.5 * a + ws),
				(int) (k * Igra.N + ws), (int) (a * (1.5 * Igra.N + 0.5) + ws));
		
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke((float) (3 * a * LINE_WIDTH)));
		crta = vodoravnaCrta(0, Igra.N);
		g2.drawPolyline(crta[0], crta[1], 2 * Igra.N);
		crta = vodoravnaCrta(Igra.N, Igra.N);
		g2.drawPolyline(crta[0], crta[1], 2 * Igra.N);
		// crti v kotih desno-zgoraj in levo-spodaj narisemo posebej
		g2.drawLine((int) ((2 * Igra.N - 1) * k + ws), (int) ws, 
				(int) (2 * Igra.N * k + ws), (int) (a * 0.5 + ws));
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((float) (a * LINE_WIDTH)));
		for (int i = 1; i < Igra.N; i++) {
			crta = navpicnaCrta(i, Igra.N);
			g2.drawPolyline(crta[0], crta[1], 2 * Igra.N);
			
			crta = vodoravnaCrta(i, Igra.N);
			g2.drawPolyline(crta[0], crta[1], 2 * Igra.N);	 
		}

		
		// krizci in krozci
		Tocka[][] plosca;;
		if (Vodja.igra != null) {
			plosca = Vodja.igra.getPlosca();
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					switch(plosca[j][i].polje) {
					case Moder: narisiZeton(g2, i, j, Igralec.Moder); break;
					case Rdeè: narisiZeton(g2, i, j, Igralec.Rdeè); break;
					default: break;
					}
				}
			}
		}	
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (Vodja.clovekNaVrsti) {
			int x = (int) (e.getX() - ws);
			int y = (int) (e.getY() - ws);

			
			int navpicno = (int) ( y / (0.5 * a));
			int vodoravno = (int) (x / k);

			// ce smo v srednjem delu sestkotnika po visini
			if (navpicno % 3 > 0) {
				int j = navpicno / 3;
				vodoravno -= j;
				int i = vodoravno / 2;
				double d = Math.abs(k * (2 * i + 1 + j) - x);
				if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N &&
						(k - d) > (a *LINE_WIDTH * 0.5)) {
					Vodja.clovekovaPoteza (new Koordinati(i, j));
				}
			}
			
			// sicer smo v cik-cak pasu
			// ce se navpicno in vodoravno sesteje v sodo imamo primer /
			else if ((navpicno + vodoravno) % 2 == 0) {
				int relX = (int) (x % k);
				int relY = (int) (y % (0.5 * a));

				// smo nad crto
				if (relY < ((-0.5 * a) / k) * relX + (0.5 - LINE_WIDTH * 0.5) * a) {
					int j = (navpicno / 3) - 1;
					int i = (vodoravno - j) / 2;
					if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N) {
						Vodja.clovekovaPoteza (new Koordinati(i, j));
					}
					
				}
				// smo pod crto
				else if (relY > ((-0.5 * a) / k) * relX + (0.5 + LINE_WIDTH * 0.5) * a) {
					int j = navpicno / 3;
					int i = ((vodoravno - j) / 2) ;
					if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N) {
						Vodja.clovekovaPoteza (new Koordinati(i, j));
					}
					
				}
			}
			
			// sicer smo v primeru \
			else {
				int relX = (int) (x % k);
				int relY = (int) (y % (0.5 * a));			
				
				// smo nad crto
				if (relY < ((0.5 * a) / k) * relX - a * LINE_WIDTH * 0.5) {
					int j = (navpicno / 3) - 1;
					int i = (vodoravno - j) / 2;
					if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N) {
						Vodja.clovekovaPoteza (new Koordinati(i, j));
					}
					
				}
				// smo pod crto
				else if (relY > ((0.5 * a) / k) * relX + a * LINE_WIDTH * 0.5) {
					int j = navpicno / 3;
					int i = ((vodoravno - j) / 2) ;
					if (0 <= i && i < Igra.N && 0 <= j && j < Igra.N) {
						Vodja.clovekovaPoteza (new Koordinati(i, j));
					}
					
				}
			}

		}
	}

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
