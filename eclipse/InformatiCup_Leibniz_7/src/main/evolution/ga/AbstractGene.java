package main.evolution.ga;

import java.util.ArrayList;
import java.util.List;

import main.utils.Evolutionhelper;

/**
 * Abstract implementation of a gene. A gene has a list of values.
 * 
 * @author Felix
 *
 * @param <T> The type of the values
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
	
	/**
	 * Creates a new gene from a list of given values.
	 * @param values the values this new gene will have
	 */
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
	
	/**
	 * Retrieves the number of gene values this gene holds.
	 * @return the length of the list of values
	 */
	public int getGeneLength() {
		return length;
	}
}
