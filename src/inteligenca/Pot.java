package inteligenca;

import java.util.Set;

import splosno.Koordinati;

public class Pot {

	public int vrednost;
	public Set<Koordinati> najbolsa_pot;
	
	public Pot(int vrednost, Set<Koordinati> najbolsa_pot) {
		this.vrednost = vrednost;
		this.najbolsa_pot = najbolsa_pot;
	}
	
	
}
