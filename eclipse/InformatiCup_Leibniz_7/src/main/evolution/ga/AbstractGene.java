package main.evolution.ga;

import java.util.ArrayList;
import java.util.List;

import main.utils.Evolutionhelper;

/**
 * Representation of a single gene. A gene consists of a number of integer values.
 * The values are randomly initialized from 0 to MAX_GENE_VALUE.
 * 
 * @author Felix
 *
 */
public abstract class AbstractGene<T> {
	protected List<T> values;
	protected int length;
	
	/**
	 * Creates a new gene with a given number of values. The values will be initialized during creation.
	 * 
	 * @param length the number of values this gene will have
	 */
	public AbstractGene(int length) {
		values = new ArrayList<>();
		this.length = length;
		initializeGene();
	}
	
	public AbstractGene(List<T> values) {
		this.values = values;
		this.length = values.size();
	}
	
	abstract protected void initializeGene();
	
	/**
	 * Replaces one value of this gene.
	 * @param index position of the value which will be replaced
	 * @param newValue the new value
	 */
	public void replaceValue(int index, T newValue) {
		values.set(index, newValue);
	}

	public List<T> getValues() {
		return values;
	}
	
	public int getGeneLength( ) {
		return length;
	}
}
