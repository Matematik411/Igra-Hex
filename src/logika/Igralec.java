package logika;

public enum Igralec {
	Rde�, Moder;
	
	public Igralec nasprotnik() {
		if (this == Rde�) return Moder;
		else return Rde�;
	}
	
	public Polje getPolje() {
		if (this == Rde�) return Polje.Rde�;
		else return Polje.Moder;
	}
}
