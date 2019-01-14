package main.evolution.ga.encoding;

import java.util.List;

import main.evolution.ga.AbstractGene;
import main.utils.Evolutionhelper;

/**
 * Implementation of an encoding gene. Encodings are represented by floats, the type of the genes values.
 * @author Felix
 *
 */
public class EncodingGene extends AbstractGene<Float> {
	
	/**
	 * Creates a new gene with a given amount of values. The values will be initialized.
	 * @param length
	 */
	public EncodingGene(int length) {
		super(length);
	}
	
	/**
	 * Creates a new gene from a given list of values.
	 * @param values
	 */
	public EncodingGene(List<Float> values) {
		super(values);
	}

	@Override
	protected void initializeGene() {
		for(int i = 0; i < length; i++) {
			values.add(Evolutionhelper.randomFloat());
		}
	}
}
