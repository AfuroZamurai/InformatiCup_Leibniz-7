package main.evolution.ga;

import main.utils.Evolutionhelper;

/**
 * Representation of a single gene. A gene consists of a number of integer values.
 * The values are randomly initialized from 0 to MAX_GENE_VALUE.
 * 
 * @author Felix
 *
 */
public class GenericGene {
	private int[] values;
	
	/**
	 * Creates a new gene with a given number of values. The values will be initialized during creation.
	 * 
	 * @param length the number of values this gene will have
	 */
	public GenericGene(int length) {
		values = new int[length];
		initializeGene();
	}
	
	public GenericGene(int[] values) {
		this.values = values;
	}
	
	private void initializeGene( ) {
		for(int i = 0; i < values.length; i++) {
			values[i] = Evolutionhelper.randomGeneValue();
		}
	}
	
	/**
	 * Replaces one value of this gene.
	 * @param index position of the value which will be replaced
	 * @param newValue the new value
	 */
	public void replaceValue(int index, int newValue) {
		values[index] = newValue;
	}

	public int[] getValues() {
		return values;
	}
}
