package main.evolution.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A population used in a genetic algorithm. It holds the genoms and knows the best genom.
 * 
 * @author Felix
 * @param <T> Implementation of a genom
 *
 */
public class Population<T extends GenericGenom<? extends AbstractGene<?>>> {
	
	private List<T> genoms;
	private T best;
	
	/**
	 * Creates an empty population
	 */
	public Population() {
		genoms = new ArrayList<>();
	}
	
	public List<T> getGenoms() {
		return genoms;
	}
	
	/**
	 * Add a genom to the list of genoms for this population
	 * @param genom the new genom
	 */
	public void addGenom(T genom) {
		this.genoms.add(genom);
	}

	public T getBest() {
		return best;
	}

	public void setBest(T best) {
		this.best = best;
	}
	
	/**
	 * Sort the genoms of this population according to their fitness.
	 * The will be sorted in ascending order.
	 */
	public void sortGenoms() {
		Collections.sort(genoms);
	}
}
