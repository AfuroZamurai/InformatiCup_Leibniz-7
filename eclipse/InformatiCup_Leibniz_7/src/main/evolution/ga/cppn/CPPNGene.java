package main.evolution.ga.cppn;

import java.util.List;

import main.evolution.ga.AbstractGene;
import main.utils.Evolutionhelper;

public class CPPNGene extends AbstractGene<Integer> {

	public CPPNGene(int length) {
		super(length);
	}
	
	public CPPNGene(List<Integer> values) {
		super(values);
	}

	@Override
	protected void initializeGene() {
		for(int i = 0; i < values.size(); i++) {
			values.add(Evolutionhelper.randomIntegerGeneValue());
		}
	}
}
