package main.evolution.ga;

import java.util.ArrayList;
import java.util.List;

/**
 * A population used in a genetic algorithm. It holds the genoms and knows the best genom.
 * 
 * @author Felix
 * @param <T>
 *
 */
public class Population<T extends GenericGenom> {
	
	private List<T> genoms;
	private T best;
	
	public Population() {
		genoms = new ArrayList<>();
	}

	public List<T> getGenoms() {
		return genoms;
	}

	public void addGenom(T genom) {
		this.genoms.add(genom);
	}

	public T getBest() {
		return best;
	}

	public void setBest(T best) {
		this.best = best;
	}

}
