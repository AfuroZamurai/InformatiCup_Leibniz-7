package main.evolution.ga;

import java.util.ArrayList;
import java.util.List;

/**
 * A population used in a genetic algorithm. It holds the genoms and knows the best genom.
 * 
 * @author Felix
 *
 */
public class Population {
	
	private List<Genom> genoms;
	private Genom best;
	
	public Population() {
		genoms = new ArrayList<>();
	}

	public List<Genom> getGenoms() {
		return genoms;
	}

	public void addGenom(Genom genom) {
		this.genoms.add(genom);
	}

	public Genom getBest() {
		return best;
	}

	public void setBest(Genom best) {
		this.best = best;
	}

}
