package inteligenca;
/*
 * Razred vsebuje funkcije potrebne za izbiro optimalnih potez.
 */

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
	
	// Pomozna funkcija.
	private static boolean preveriBarvo(Tocka tocka1, Tocka tocka2) {
		if (tocka1.polje == Polje.PRAZNO && tocka2.polje == Polje.PRAZNO) return true;
		else return false;
	}
	
	// Pomozna funkcija, ki preveri, da so koordinate moznih potez res se prazne.
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
	
	// Glavna funkcija za izbiro potez.
	public static List<Koordinati> izbiraPotezVse(Igra igra, Igralec igralec, int globina) {
		LinkedList<Koordinati> seznam = new LinkedList<Koordinati>();
		Tocka[][] plosca = igra.getPlosca();
		Tocka zadnja = igra.zadnja;
		
		// Naredi objekt razred Pot.
		Pot potOcena = new Pot(1, new HashSet<Vrednost>());
		Set<Vrednost> pot;
		
		// Izbere poteze za modrega.
		if (igralec == Igralec.Moder) { 
			// Pogleda s katero funkcijo je Ocenil pozicijo.
			if (Inteligenca.nacinLokalno) {
				potOcena = OceniPozicijo.oceni_moder(igra); 
				pot = potOcena.najbolsa_pot;
			}
			else {
				pot = potOcena.najbolsa_pot;
			}
			
			// Preveri ali se igra ze bliza koncu, takrat se namrec izbira potez spremeni.
			List<Koordinati> test = new LinkedList<Koordinati>();
			for (Vrednost vre : pot) {
				test.add(vre.koordinati);
			}
			
			// Ce se res bliza koncu sprozi BFS oceni pozicijo namesto default funkcije, 
			// ki je v OceniPozicijo. (To naredi nacinLokalno.)
			if (preveriPrazno(igra, test).size() < 4) {
				potOcena.vrednost = 1;
				Inteligenca.nacinLokalno = false;
				List<Koordinati> modra_pot = BfsIskanje.BfsIskanjePotiModer(igra);
				for (Koordinati koordinata : modra_pot) {
					pot.add(new Vrednost(koordinata));
				}
			}
			int vrednost = potOcena.vrednost;
			
			// To pomeni, da imamo zmago ze v rokah, pokriti moramo le se polja,
			// kjer imamo dve moznosti, a je za nasprotnika neobranljivo.
			// Ta polja moramo konstruirati (o tem pisem v razredu Pot).
			if (vrednost == 0) {
				for (Vrednost vre : pot) {
					int j = vre.koordinati.getX();
					int i = vre.koordinati.getY();
					
					// Preverimo vse tri mozne skoke in dodamo prave koordinate, da bo nasa pot povezana mnozica.
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
				return preveriPrazno(igra, seznam);
			}
			
			// Ce zmage se nimamo v rokah.
			else {
				int i = zadnja.koordinati.getY();
				int j = zadnja.koordinati.getX();
				
				// Ko delamo Skoke, imamo na voljo dve poti, ki jih nasprotnik ne more obeh hkrati blokirati.
				// A ce se odloci in nam eno pot blokira moramo mi zapreti drugo in tako povezati polji, ki sta
				// bili dotlej nepovezani (a neobranljivi).
				// Tu preverimo vse moznosti.
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
				
				// Ko te moznosti imamo, preverimo ali obstajajo in ce obstajajo imajo najvisjo prioriteto,
				// zato jih vrnemo kot edine moznosti za izbiro potez.
				List<Koordinati> prazno = preveriPrazno(igra, seznam);
				if (prazno.size() > 0) {
					return prazno;
				}
				
				// Ce teh tezav nimamo vrnemo, razlicno glede na globino (tu je sicer if (globina > 0), kar je vedno res,
				// a bi lahko dodatno optimizirali, ce bi se to izkazalo za potrebno) in glede na to kako blizu koncu smo.
				// Naceloma mu dodamo za izbiro potez: takojsnje sosede, kake sosede, ki so oddaljeni za 2 polji,
				// njegovo zmagovalno pot in zmagovalno pot nasprotnika, pridobljeno z BFS funkcijo ocena poti.
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
				// Njegova zmagovalna pot.
				for (Vrednost vre : pot) {
					seznam.add(vre.koordinati);
				}
				
				// Nasprotnikova zmagovalna pot.
				for (Koordinati p : BfsIskanje.BfsIskanjePotiRdec(igra)) {
					seznam.add(p);
				}

				// Nazadnje vrnemo preverjen seznam potez.
				return preveriPrazno(igra, seznam);
			}
			
			
		}
		
		// Tu je vse povsem analogno kot za modrega igralca, le indeksi koordinat so drugacni.
		if (igralec == Igralec.Rdec) { 
			if (Inteligenca.nacinLokalno) {
				potOcena = OceniPozicijo.oceni_rdec(igra); 
				pot = potOcena.najbolsa_pot;
			}
			else {
				pot = potOcena.najbolsa_pot;
			}

			List<Koordinati> test = new LinkedList<Koordinati>();
			for (Vrednost vre : pot) {
				test.add(vre.koordinati);
			}

			if (preveriPrazno(igra,test).size() < 4) {
				potOcena.vrednost = 1;
				Inteligenca.nacinLokalno = false;
				List<Koordinati> rdeca_pot = BfsIskanje.BfsIskanjePotiRdec(igra);
				for (Koordinati koordinata : rdeca_pot) {
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

				for (Koordinati p : BfsIskanje.BfsIskanjePotiModer(igra)) {
					seznam.add(p);
				}
				
				return preveriPrazno(igra, seznam);
				
			}
		}
		// Do sem se ne da priti.
		return preveriPrazno(igra, seznam);	
	}
	

	
}
