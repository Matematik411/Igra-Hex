package inteligenca;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Tocka;
import splosno.Koordinati;

public class IzbiraPotez {
	
	
	private static boolean preveriBarvo(Tocka tocka1, Tocka tocka2) {
		if (tocka1.polje == Polje.PRAZNO && tocka2.polje == Polje.PRAZNO) return true;
		else return false;
	}
	
	
	private static List<Koordinati> preveriPrazno(Igra igra, List<Koordinati> seznam) {
		Tocka[][] plosca = igra.getPlosca();
		LinkedList<Koordinati> prazne = new LinkedList<Koordinati>();
		
		for (Koordinati koordinate : seznam) {
			int i = koordinate.getY();
			int j = koordinate.getX();
			try {
				 if (plosca[i][j].polje == Polje.PRAZNO) prazne.add(koordinate);
			} catch (ArrayIndexOutOfBoundsException e) {}
		}

		return prazne;
	}
	
	
	public static List<Koordinati> izbiraPotezVse(Igra igra, Igralec igralec, int globina) {
		LinkedList<Koordinati> seznam = new LinkedList<Koordinati>();
		Tocka[][] plosca = igra.getPlosca();
		Tocka zadnja = igra.zadnja;
		
		Pot potOcena = new Pot(1, new HashSet<Vrednost>());
		Set<Vrednost> pot;
		if (igralec == Igralec.Moder) { 
			if (Inteligenca.nacinLokalno) {
				potOcena = OceniPozicijo.oceni_moder(igra); 
				pot = potOcena.najbolsa_pot;
			}
			else {
				pot = potOcena.najbolsa_pot;
			}

			if (pot.size() == 0) {
				potOcena.vrednost = 1;
				Inteligenca.nacinLokalno = false;
				System.out.println("MODRI");
				List<Koordinati> modra_pot = BfsIskanje.BfsIskanjePotiModer(igra);
				System.out.println(modra_pot);
				System.out.println(modra_pot.size());
				for (Koordinati koordinata : modra_pot) {
					pot.add(new Vrednost(koordinata));
				}
			}
			int vrednost = potOcena.vrednost;
			
			if (vrednost == 0) {
				for (Vrednost vre : pot) {
					int j = vre.koordinati.getX();
					int i = vre.koordinati.getY();
					
					if (vre.skok == Skok.Skok1) {
						Tocka Tocka1 = plosca[i][j - 1];
						Tocka Tocka2 = plosca[i - 1][j];
						if (preveriBarvo(Tocka1, Tocka2)) {
							seznam.add(Tocka1.koordinati);
							seznam.add(Tocka2.koordinati);
						}
					}
					if (vre.skok == Skok.Skok2) {
						Tocka Tocka1 = plosca[i + 1][j - 1];
						Tocka Tocka2 = plosca[i][j - 1];
						if (preveriBarvo(Tocka1, Tocka2)) {
							seznam.add(Tocka1.koordinati);
							seznam.add(Tocka2.koordinati);
						}
					}
					if (vre.skok == Skok.Skok3) {
						Tocka Tocka1 = plosca[i + 1][j];
						Tocka Tocka2 = plosca[i + 1][j - 1];
						if (preveriBarvo(Tocka1, Tocka2)) {
							seznam.add(Tocka1.koordinati);
							seznam.add(Tocka2.koordinati);
						}
					}
				}
				if (globina == 5) 	{
					System.out.println("preveri_prazno");
					System.out.println(preveriPrazno(igra, seznam));
				}
				return preveriPrazno(igra, seznam);
			}
			
			else {
				int i = zadnja.koordinati.getY();
				int j = zadnja.koordinati.getX();
				
				try {
					if (plosca[i - 1][j + 1].polje == Polje.Moder && plosca[i + 1][j].polje == Polje.Moder) {
						seznam.add(new Koordinati(j + 1, i));
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i - 1][j + 1].polje == Polje.Moder && plosca[i][j - 1].polje == Polje.Moder) {
						seznam.add(new Koordinati(j, i - 1));
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i + 1][j].polje == Polje.Moder && plosca[i][j - 1].polje == Polje.Moder) {
						seznam.add(new Koordinati(j - 1, i + 1));
					}
				}  catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i - 1][j].polje == Polje.Moder && plosca[i + 1][j - 1].polje == Polje.Moder) {
						seznam.add(new Koordinati(j - 1, i));
					}
				}  catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i - 1][j].polje == Polje.Moder && plosca[i][j + 1].polje == Polje.Moder) {
						seznam.add(new Koordinati(j + 1, i - 1));
					}
				}  catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i][j + 1].polje == Polje.Moder && plosca[i + 1][j - 1].polje == Polje.Moder) {
						seznam.add(new Koordinati(j, i + 1));
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
				
				List<Koordinati> prazno = preveriPrazno(igra, seznam);
				if (prazno.size() > 0) {
					if (globina == 5) 	{
						System.out.println("sosedi");
						System.out.println(preveriPrazno(igra, seznam));
					}
					return prazno;
				}
				
				if (pot.size() > 2) {	
					seznam.add(new Koordinati(j, i - 1));
					seznam.add(new Koordinati(j - 1, i + 1));
					seznam.add(new Koordinati(j + 1, i - 1));
					seznam.add(new Koordinati(j, i + 1));
					
					if (globina > 0) {
						seznam.add(new Koordinati(j + 1, i));
						seznam.add(new Koordinati(j - 1, i));
					}
					if (globina > 0) {
						seznam.add(new Koordinati(j + 1, i - 2));
						seznam.add(new Koordinati(j - 1, i + 2));
						seznam.add(new Koordinati(j + 1, i + 1));
						seznam.add(new Koordinati(j - 1, i - 1));
					}
				}
				for (Vrednost vre : pot) {
					seznam.add(vre.koordinati);
				}
				if (globina == 5) 	System.out.println(preveriPrazno(igra, seznam));

				return preveriPrazno(igra, seznam);
			}
			
			
		}
		
		if (igralec == Igralec.Rdec) { 
			if (Inteligenca.nacinLokalno) {
				potOcena = OceniPozicijo.oceni_rdec(igra); 
				pot = potOcena.najbolsa_pot;
			}
			else {
				pot = potOcena.najbolsa_pot;
			}

			if (pot.size() == 0) {
				potOcena.vrednost = 1;
				Inteligenca.nacinLokalno = false;
				System.out.println("RDECIIII");
				List<Koordinati> modra_pot = BfsIskanje.BfsIskanjePotiRdec(igra);
				System.out.println(modra_pot);
				System.out.println(modra_pot.size());
				for (Koordinati koordinata : modra_pot) {
					pot.add(new Vrednost(koordinata));
				}
			}
			int vrednost = potOcena.vrednost;
			
			if (vrednost == 0) {
				for (Vrednost vre : pot) {
					int j = vre.koordinati.getX();
					int i = vre.koordinati.getY();
					
					if (vre.skok == Skok.Skok1) {
						Tocka Tocka1 = plosca[i][j - 1];
						Tocka Tocka2 = plosca[i - 1][j];
						if (preveriBarvo(Tocka1, Tocka2)) {
							seznam.add(Tocka1.koordinati);
							seznam.add(Tocka2.koordinati);
						}
					}
					if (vre.skok == Skok.Skok2) {
						Tocka Tocka1 = plosca[i - 1][j + 1];
						Tocka Tocka2 = plosca[i - 1][j];
						if (preveriBarvo(Tocka1, Tocka2)) {
							seznam.add(Tocka1.koordinati);
							seznam.add(Tocka2.koordinati);
						}
					}
					if (vre.skok == Skok.Skok3) {
						Tocka Tocka1 = plosca[i - 1][j + 1];
						Tocka Tocka2 = plosca[i][j + 1];
						if (preveriBarvo(Tocka1, Tocka2)) {
							seznam.add(Tocka1.koordinati);
							seznam.add(Tocka2.koordinati);
						}
					}
				}
				return preveriPrazno(igra, seznam);
			}
			
			else {
				int i = zadnja.koordinati.getY();
				int j = zadnja.koordinati.getX();
				
				try {
					if (plosca[i - 1][j + 1].polje == Polje.Rdec && plosca[i + 1][j].polje == Polje.Rdec) {
						seznam.add(new Koordinati(j + 1, i));
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i - 1][j + 1].polje == Polje.Rdec && plosca[i][j - 1].polje == Polje.Rdec) {
						seznam.add(new Koordinati(j, i - 1));
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i + 1][j].polje == Polje.Rdec && plosca[i][j - 1].polje == Polje.Rdec) {
						seznam.add(new Koordinati(j - 1, i + 1));
					}
				}  catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i - 1][j].polje == Polje.Rdec && plosca[i + 1][j - 1].polje == Polje.Rdec) {
						seznam.add(new Koordinati(j - 1, i));
					}
				}  catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i - 1][j].polje == Polje.Rdec && plosca[i][j + 1].polje == Polje.Rdec) {
						seznam.add(new Koordinati(j + 1, i - 1));
					}
				}  catch (ArrayIndexOutOfBoundsException e) {}
				try {
					if (plosca[i][j + 1].polje == Polje.Rdec && plosca[i + 1][j - 1].polje == Polje.Rdec) {
						seznam.add(new Koordinati(j, i + 1));
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
				
				List<Koordinati> prazno = preveriPrazno(igra, seznam);
				if (prazno.size() > 0) return prazno;
				
				
				if (pot.size() > 2) {
					seznam.add(new Koordinati(j + 1, i));
					seznam.add(new Koordinati(j - 1, i + 1));
					seznam.add(new Koordinati(j - 1, i));
					seznam.add(new Koordinati(j + 1, i - 1));
					
					if (globina > 0) {
					seznam.add(new Koordinati(j, i + 1));
					seznam.add(new Koordinati(j, i - 1));
					}
					if (globina > 0) {
						seznam.add(new Koordinati(j - 2, i + 1));
						seznam.add(new Koordinati(j - 1, i - 1));
						seznam.add(new Koordinati(j + 1, i + 1));
						seznam.add(new Koordinati(j + 2, i - 1));
					}	
				}
				for (Vrednost vre : pot) {
					seznam.add(vre.koordinati);
				}
				
				return preveriPrazno(igra, seznam);
				
			}
		}
		// Do sem se neda priti
		return preveriPrazno(igra, seznam);	
	}
	

	
}
