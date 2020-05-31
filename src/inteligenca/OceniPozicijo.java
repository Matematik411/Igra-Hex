
package inteligenca;

import java.util.HashSet;
import java.util.Set;

import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Tocka;
import splosno.Koordinati;

public class OceniPozicijo {
	
	
	
	
	public static Pot oceni_rdec(Igra igra) {
		Tocka[][] plosca = igra.getPlosca();
		int N = plosca[0].length;
		
		Set<Vrednost> rdeca_pot = new HashSet<Vrednost>();
		Vrednost[][] tabela_dolzin_rdeci =  new Vrednost[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tabela_dolzin_rdeci[i][j] = new Vrednost(new Koordinati(j, i));
			}
		}
		
		// 1. vrstica
		for (int j = 0; j < N; j++) {
			if (plosca[0][j].polje == Polje.Rdec) tabela_dolzin_rdeci[0][j].vrednost = 0;
			if (plosca[0][j].polje == Polje.PRAZNO) tabela_dolzin_rdeci[0][j].vrednost = 1;
		}
		
		// ostalo
		for (int i = 1; i < N; i++) {
			for (int j = 0; j < N; j++) {
			
				int levo_levo_zg;
				int levo_zg;
				int desno_desno_zg;
				int desno_zg;
				int zgoraj;
				
				try {
					if (plosca[i - 1][j].polje == Polje.PRAZNO && plosca[i][j - 1].polje == Polje.PRAZNO) {
						levo_levo_zg = tabela_dolzin_rdeci[i - 1][j - 1].vrednost;
					}
					else levo_levo_zg = 1000000;
				} catch (ArrayIndexOutOfBoundsException e) { levo_levo_zg = 1000000; }
				
				try { levo_zg = tabela_dolzin_rdeci[i - 1][j].vrednost; } 
				catch (ArrayIndexOutOfBoundsException e) { levo_zg = 1000000; }
				try { desno_zg = tabela_dolzin_rdeci[i - 1][j + 1].vrednost; } 
				catch (ArrayIndexOutOfBoundsException e) { desno_zg = 1000000; }
				try {
					if (plosca[i][j + 1].polje == Polje.PRAZNO && plosca[i - 1][j + 1].polje == Polje.PRAZNO) {
						desno_desno_zg = tabela_dolzin_rdeci[i - 1][j + 2].vrednost;
					}
					else desno_desno_zg = 1000000;
				} catch (ArrayIndexOutOfBoundsException e) { desno_desno_zg = 1000000; }
				try {
					if (plosca[i - 1][j].polje == Polje.PRAZNO && plosca[i - 1][j + 1].polje == Polje.PRAZNO) {
						zgoraj = tabela_dolzin_rdeci[i - 2][j + 1].vrednost;
					}
					else zgoraj = 1000000;
				} catch (ArrayIndexOutOfBoundsException e) { zgoraj = 1000000; }
				
				
				Vrednost pointer = null;
				Skok skok = null;
				int vrednost_polja;
				vrednost_polja = Math.min(levo_levo_zg, Math.min(levo_zg, Math.min(desno_zg, Math.min(desno_desno_zg, zgoraj))));
				
				if (vrednost_polja != 1000000) {
					if (vrednost_polja == levo_levo_zg) {
						pointer = tabela_dolzin_rdeci[i - 1][j - 1];
						skok = Skok.Skok1;
					}
					if (vrednost_polja == levo_zg) pointer = tabela_dolzin_rdeci[i - 1][j];
					if (vrednost_polja == desno_desno_zg) {
						pointer = tabela_dolzin_rdeci[i - 1][j + 2];
						skok = Skok.Skok3;
					}
					if (vrednost_polja == desno_zg) pointer = tabela_dolzin_rdeci[i - 1][j + 1];
					if (vrednost_polja == zgoraj) {
						pointer = tabela_dolzin_rdeci[i - 2][j + 1];
						skok = Skok.Skok2;
					}
					if (plosca[i][j].polje == Polje.PRAZNO) vrednost_polja += 1;
					if (plosca[i][j].polje == Polje.Moder) vrednost_polja = 1000000;
				}
				tabela_dolzin_rdeci[i][j].vrednost = vrednost_polja;
				tabela_dolzin_rdeci[i][j].pointer = pointer;
				tabela_dolzin_rdeci[i][j].skok = skok;
			}
			
			
			// Ponovni pregled
			for (int j = 0; j < N; j++) {
				int levo;
				int desno;
				int vrednost_polja = tabela_dolzin_rdeci[i][j].vrednost;
				Vrednost pointer = tabela_dolzin_rdeci[i][j].pointer;
				Skok skok = tabela_dolzin_rdeci[i][j].skok;
				Vrednost nov_pointer = pointer;
				
				
				try { levo = tabela_dolzin_rdeci[i][j - 1].vrednost; } 
				catch (ArrayIndexOutOfBoundsException e) { levo = 1000000; }
				try { desno = tabela_dolzin_rdeci[i][j + 1].vrednost; } 
				catch (ArrayIndexOutOfBoundsException e) { desno = 1000000; }
				
				if (levo != 1000000 && levo < vrednost_polja) {
					if (plosca[i][j].polje == Polje.Rdec) vrednost_polja = levo;
					if (plosca[i][j].polje == Polje.PRAZNO) vrednost_polja = levo + 1;
					nov_pointer = tabela_dolzin_rdeci[i][j - 1];
					skok = null;
				}
				if (desno != 1000000 && desno < vrednost_polja) {
					if (plosca[i][j].polje == Polje.Rdec) vrednost_polja = desno;
					if (plosca[i][j].polje == Polje.PRAZNO) vrednost_polja = desno + 1;
					nov_pointer = tabela_dolzin_rdeci[i][j + 1];
					skok = null;
				}
				tabela_dolzin_rdeci[i][j].vrednost = vrednost_polja;
				tabela_dolzin_rdeci[i][j].pointer = nov_pointer;
				tabela_dolzin_rdeci[i][j].skok = skok;
			}
		}
		
		// Koncna vrednost
		int najmanjsa_rdeca = 1000000;
		Vrednost pointer_rdeca = null;
		for (int j = 0; j < N; j++) {
			int vrednost = tabela_dolzin_rdeci[N - 1][j].vrednost;
			if (najmanjsa_rdeca > vrednost) {
				najmanjsa_rdeca = vrednost;
				pointer_rdeca = tabela_dolzin_rdeci[N - 1][j];
			}	
		}
		
	
		// Izracun poti
		Vrednost spremenljivka = pointer_rdeca;
		while (spremenljivka != null) {
			rdeca_pot.add(spremenljivka);
			spremenljivka = spremenljivka.pointer;
		}
		
		return new Pot(najmanjsa_rdeca, rdeca_pot);
	}
	
	
	public static Pot oceni_moder(Igra igra) {
		Tocka[][] plosca = igra.getPlosca();
		int N = plosca[0].length;
		
		Set<Vrednost> modra_pot = new HashSet<Vrednost>();
		Vrednost[][] tabela_dolzin_modri =  new Vrednost[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tabela_dolzin_modri[i][j] = new Vrednost(new Koordinati(j, i));
			}
		}
		
		// 1. vrstica
		for (int i = 0; i < N; i++) {
			if (plosca[i][0].polje == Polje.Moder) tabela_dolzin_modri[i][0].vrednost = 0;
			if (plosca[i][0].polje == Polje.PRAZNO) tabela_dolzin_modri[i][0].vrednost = 1;
		}
		
		// ostalo
		for (int j = 1; j < N; j++) {
			for (int i = 0; i < N; i++) {
			
				int gor_gor_lev;
				int gor_lev;
				int dol_lev;
				int dol_dol_lev;
				int levo;
				
				try {
					if (plosca[i - 1][j].polje == Polje.PRAZNO && plosca[i][j - 1].polje == Polje.PRAZNO) {
						gor_gor_lev = tabela_dolzin_modri[i - 1][j - 1].vrednost;
					}
					else gor_gor_lev = 1000000;
				} catch (ArrayIndexOutOfBoundsException e) { gor_gor_lev = 1000000; }
				
				try { gor_lev = tabela_dolzin_modri[i][j - 1].vrednost; } 
				catch (ArrayIndexOutOfBoundsException e) { gor_lev = 1000000; }
				try { dol_lev = tabela_dolzin_modri[i + 1][j - 1].vrednost; } 
				catch (ArrayIndexOutOfBoundsException e) { dol_lev = 1000000; }
				try {
					if (plosca[i + 1][j - 1].polje == Polje.PRAZNO && plosca[i + 1][j].polje == Polje.PRAZNO) {
						dol_dol_lev = tabela_dolzin_modri[i + 2][j - 1].vrednost;
					}
					else dol_dol_lev = 1000000;
				} catch (ArrayIndexOutOfBoundsException e) { dol_dol_lev = 1000000; }
				try {
					if (plosca[i + 1][j - 1].polje == Polje.PRAZNO && plosca[i][j - 1].polje == Polje.PRAZNO) {
						levo = tabela_dolzin_modri[i + 1][j - 2].vrednost;
					}
					else levo = 1000000;
				} catch (ArrayIndexOutOfBoundsException e) { levo = 1000000; }
				
				
				Vrednost pointer = null;
				Skok skok = null;
				int vrednost_polja;
				vrednost_polja = Math.min(gor_gor_lev, Math.min(gor_lev, Math.min(dol_lev, Math.min(dol_dol_lev, levo))));
				
				if (vrednost_polja != 1000000) {
					if (vrednost_polja == gor_gor_lev) {
						pointer = tabela_dolzin_modri[i - 1][j - 1];
						skok = Skok.Skok1;
					}
					if (vrednost_polja == gor_lev) pointer = tabela_dolzin_modri[i][j - 1];
					if (vrednost_polja == dol_lev) pointer = tabela_dolzin_modri[i + 1][j - 1];
					if (vrednost_polja == dol_dol_lev) {
						pointer = tabela_dolzin_modri[i + 2][j - 1];
						skok = Skok.Skok3;
					}
					if (vrednost_polja == levo) {
						pointer = tabela_dolzin_modri[i + 1][j - 2];
						skok = Skok.Skok2;
					}
					if (plosca[i][j].polje == Polje.PRAZNO) vrednost_polja += 1;
					if (plosca[i][j].polje == Polje.Rdec) vrednost_polja = 1000000;
				}
				tabela_dolzin_modri[i][j].vrednost = vrednost_polja;
				tabela_dolzin_modri[i][j].pointer = pointer;
				tabela_dolzin_modri[i][j].skok = skok;
			}
			
			
			// Ponovni pregled
			for (int i = 0; i < N; i++) {
				int gor;
				int dol;
				int vrednost_polja = tabela_dolzin_modri[i][j].vrednost;
				Vrednost pointer = tabela_dolzin_modri[i][j].pointer;
				Skok skok = tabela_dolzin_modri[i][j].skok;
				Vrednost nov_pointer = pointer;
				
				try { gor = tabela_dolzin_modri[i - 1][j].vrednost; } 
				catch (ArrayIndexOutOfBoundsException e) { gor = 1000000; }
				try { dol = tabela_dolzin_modri[i + 1][j].vrednost; } 
				catch (ArrayIndexOutOfBoundsException e) { dol = 1000000; }
				
				if (dol != 1000000 && dol < vrednost_polja) {
					if (plosca[i][j].polje == Polje.Moder) vrednost_polja = dol;
					if (plosca[i][j].polje == Polje.PRAZNO) vrednost_polja = dol + 1;
					nov_pointer = tabela_dolzin_modri[i + 1][j];
					skok = null;
				}
				if (gor != 1000000 && gor < vrednost_polja) {
					if (plosca[i][j].polje == Polje.Moder) vrednost_polja = gor;
					if (plosca[i][j].polje == Polje.PRAZNO) vrednost_polja = gor + 1;
					nov_pointer = tabela_dolzin_modri[i - 1][j];
					skok = null;
				}
				tabela_dolzin_modri[i][j].vrednost = vrednost_polja;
				tabela_dolzin_modri[i][j].pointer = nov_pointer;
				tabela_dolzin_modri[i][j].skok = skok;
			}
		}
		
		// Koncna vrednost
		int najmanjsa_modra = 1000000;
		Vrednost pointer_modra = null;
		for (int i = 0; i < N; i++) {
			int vrednost = tabela_dolzin_modri[i][N - 1].vrednost;
			if (najmanjsa_modra > vrednost) {
				najmanjsa_modra = vrednost;
				pointer_modra = tabela_dolzin_modri[i][N - 1];
			}	
		}
		
	
		// Izracun poti
		Vrednost spremenljivka = pointer_modra;
		while (spremenljivka != null) {
			modra_pot.add(spremenljivka);
			spremenljivka = spremenljivka.pointer;
		}
		
		return new Pot(najmanjsa_modra, modra_pot);
	}

	
	
	
	public static int oceniPozicijo(Igra igra, Igralec jaz, int globina) {
		int N = igra.velikost;
		int vrednost_rdeci;
		int vrednost_modri;

		if (Inteligenca.nacinLokalno == true) {
			vrednost_rdeci = oceni_rdec(igra).vrednost - 1;
			vrednost_modri = oceni_moder(igra).vrednost - 1;	
		}
		else {
			vrednost_rdeci = BfsIskanje.BfsIskanjePotiRdec(igra).size() - 1;
			vrednost_modri = BfsIskanje.BfsIskanjePotiModer(igra).size() - 1;
		}

		int a = 1000000 / (N * N);
		int b = -a;
		
		double alpha = (double) a;
		double n = (double) N;
		
	
		if (jaz == Igralec.Rdec) {
			if (vrednost_modri == 1000000 || vrednost_rdeci == 0) return 1000000;
			if (vrednost_rdeci == 1000000 || vrednost_modri == 0) return -1000000;
			
//			if (vrednost_modri > vrednost_rdeci) {
//				double z = (2/(n * n - 2) * 1 - 1 / (n * n - 2) * 2 + 1 - 2/(n * n - 2) * vrednost_rdeci + 1 / (n * n - 2) * vrednost_modri) / (2 / (n * n * alpha));
//			
//				return (int) z;
//			} else if (vrednost_modri < vrednost_rdeci) {
//				double z = ((2/3) * 2 - (1/3) * 1 - (2/3) * vrednost_rdeci + (1/3) * vrednost_modri ) / 
//						(2 * (n * n - 2)/(3 * n * n * alpha));
//						
//				return (int) z;
//			} else return 0;
			return -(a * vrednost_rdeci + b * vrednost_modri ) + 10 * vrednost_rdeci;
			
		}
		else {
			if (vrednost_modri == 1000000 || vrednost_rdeci == 0) return -1000000;
			if (vrednost_rdeci == 1000000 || vrednost_modri == 0) return 1000000;
			
//			if (vrednost_modri > vrednost_rdeci) {
//				double z = -(2/(n * n - 2) * 1 - 1 / (n * n - 2) * 2 + 1 - 2/(n * n - 2) * vrednost_rdeci + 1 / (n * n - 2) * vrednost_modri) / (2 / (n * n * alpha));
//			
//				return (int) z;
//			} else if (vrednost_modri < vrednost_rdeci) {
//				double z = -((2/3) * 2 - (1/3) * 1 - (2/3) * vrednost_rdeci + (1/3) * vrednost_modri ) / 
//						(2 * (n * n - 2)/(3 * n * n * alpha));
//						
//				return (int) z;
//			} else return 0;
			return (a * vrednost_rdeci + b * vrednost_modri ) + 10 * vrednost_modri;
		}
	}
}










