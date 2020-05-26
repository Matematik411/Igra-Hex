package logika;
/*
 * Enum tip, ki vsebuje mozna igralca igre.
 * 
 * Pravtako vsebuje preprosti metodi, ki vrneta trenutnega nasprotnika v igri
 * in barvo igralca, ki je na potezi.
 */
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
