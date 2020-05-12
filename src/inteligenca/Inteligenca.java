package inteligenca;

import logika.Igra;
import splosno.Koordinati;
import splosno.KdoIgra;

public abstract class Inteligenca extends KdoIgra {
	
	public Inteligenca (String ime) {
		super(ime);
	}
	
	public abstract Koordinati izberiPotezo (Igra igra);

}
