package inteligenca;

import splosno.Koordinati;

public class Vrednost {
	
	public int vrednost;
	public Koordinati koordinati;
	public Vrednost pointer;
	public Skok skok;
	
	public Vrednost(Koordinati koordinati) {
		this.koordinati = koordinati;
		this.vrednost = Integer.MAX_VALUE;
		
	}
	
}
