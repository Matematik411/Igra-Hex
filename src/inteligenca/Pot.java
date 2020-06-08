package inteligenca;
/*
 * Objekt razreda Pot je koncni produkt funkcij oceni_moder in oceni_rdec v razredu OceniPozicijo.
 * Shrani vrednost najbolse poti, ki se kasneje pretvori v vrednost za minimax in pa mnozico koordinat
 * najbolse poti, ki jo potrebujemo v IzbiraPotez. (Ta mnozica ne vsebuje vseh koordinat na poti, ampak le
 * tiste, ki jih nujno potrebujemo. ce imamo npr. do neke tocke dve mozni poti (Tu pridejo v igro objekti razreda Skok)
 * ni v mnozici nobene. sele v IzbiraPotez se jih s pomocjo Skok-ov rekonstruira.
 */

import java.util.Set;

public class Pot {

	public int vrednost;
	public Set<Vrednost> najbolsa_pot;
	
	public Pot(int vrednost, Set<Vrednost> najbolsa_pot) {
		this.vrednost = vrednost;
		this.najbolsa_pot = najbolsa_pot;
	}
	
}