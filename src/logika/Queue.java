package logika;

import java.util.ArrayList;

public class Queue {
	private ArrayList<Tocka> holder;
	
	public Queue() {
		holder = new ArrayList<Tocka>();
	}

	public void vstavi(Tocka t) {
		holder.add(0, t);
	}
	
	public Tocka vzami() {
		int v = holder.size();
		return holder.remove(v - 1);
	}
	
	public Tocka zadnji() {
		return holder.get(0);
	}
	
	public boolean jePrazen() {
		return holder.size() == 0;
	}
}
