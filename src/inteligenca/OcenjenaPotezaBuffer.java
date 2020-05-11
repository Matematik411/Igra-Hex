package inteligenca;

import java.util.List;
import java.util.LinkedList;
import inteligenca.OcenjenaPoteza;

public class OcenjenaPotezaBuffer {
	
	private int velikost;
	
	private LinkedList<OcenjenaPoteza> buffer;
		
	public OcenjenaPotezaBuffer(int velikost) {
		this.velikost = velikost;
		this.buffer = new LinkedList<OcenjenaPoteza> ();
	}
	
	public void add(OcenjenaPoteza ocenjenaPoteza) {
		int i = 0;
		for (OcenjenaPoteza op:buffer) {
			if (ocenjenaPoteza.compareTo(op) != 1) i++;
			else break; // izstopimo iz zanke	
		}
		if (i < velikost) buffer.add(i,ocenjenaPoteza);
		if (buffer.size() > velikost) buffer.removeLast();
	}
	
	
	public List<OcenjenaPoteza> list() {
		return (List<OcenjenaPoteza>) buffer;
	}

}
