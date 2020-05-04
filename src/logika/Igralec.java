package logika;

public enum Igralec {
	Rdeè, Moder;
	
	public Igralec nasprotnik() {
		if (this == Rdeè) return Moder;
		else return Rdeè;
	}
	
	public Polje getPolje() {
		if (this == Rdeè) return Polje.Rdeè;
		else return Polje.Moder;
	}
}
