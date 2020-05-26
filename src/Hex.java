/*
 * Aplikacija za igro Hex. Narejena kot projektna naloga pri predmetu Programiranje 2, 
 * v studijskem letu 2019/2020.
 * 
 * Aplikacija omogoca igranje med dvema igralcema, igranje proti racunalniku in igro 
 * med dvema racunalnikoma.
 * 
 * V tej datoteki se nahaja metoda main, ki zazene program.
 * 
 * Program se odvija v JFrame oknu, v njej se igra igra hex. Datoteke za aplikacijo so
 * razporejene po paketih:
 * - gui: vsebuje datoteke, ki urejajo graficni vmesnik, preko katerega uporabnik igra igro
 * - inteligenca: vsebuje datoteke, ki upravljajo racunalnik, ko le ta igra igro.
 * - logika: vsebuje datoteke s pravili igre.
 * - splosno: vsebuje vnaprej predpisane datoteke, za kompatibilnost programa s programi 
 * 	 drugih ekip pri predmetu Programiranje 2.
 * - vodja: vsebuje datoteko, ki nadzira potek igre.
 * 
 * Aplikacijo sva naredila Andraz Maier in Nejc Zajc.
 * pomlad 2020
 */

import gui.GlavnoOkno;
import vodja.Vodja;

public class Hex {
	public static void main(String[] args) {
		GlavnoOkno glavno_okno = new GlavnoOkno();
		glavno_okno.pack();
		glavno_okno.setVisible(true);
		Vodja.okno = glavno_okno;
	}

}
