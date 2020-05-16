package logika;

public enum Igralec {
	Rdec, Moder;
	
	public Igralec nasprotnik() {
		if (this == Rdec) return Moder;
		else return Rdec;
	}
	
	public Polje getPolje() {
		if (this == Rdec) return Polje.Rdec;
		else return Polje.Moder;
	}
}
