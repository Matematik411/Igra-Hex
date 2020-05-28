package logika;
/*
 * Razred z objektom cakalne vrste, ki je uporabljen pri iskanju zmagovalne poti v mrezi
 * z BFS algoritmom.
 */
import java.util.ArrayList;

public class Queue {
	// Napovedana vrsta, vsebovani objekti bodo Tocke.
	private ArrayList<Tocka> holder;
	
	// Preprost konstruktor, ki ustvari nov objekt.
	public Queue() {
		holder = new ArrayList<Tocka>();
	}

	// Metoda za vstavljanje Tock na zacetek. Metoda prestavi vse ostale za eno polje naprej.
	public void vstavi(Tocka t) {
		holder.add(0, t);
	}
	
	// Metoda vrne Tocko, ki je ze najdlje v vrsti in jo odstrani iz vrste.
	public Tocka vzami() {
		int v = holder.size();
		return holder.remove(v - 1);
	}
	
	// Zadnja je metoda, ki preveri ali je vrsta prazna.
	public boolean jePrazen() {
		return holder.size() == 0;
	}
}
