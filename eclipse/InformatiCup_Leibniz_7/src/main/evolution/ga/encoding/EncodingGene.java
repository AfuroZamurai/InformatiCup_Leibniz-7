package main.evolution.ga.encoding;

import java.util.List;

import main.evolution.ga.AbstractGene;
import main.utils.Evolutionhelper;

public class EncodingGene extends AbstractGene<Float> {
	
	public EncodingGene(int length) {
		super(length);
	}
	
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
