package main.evolution.ga.encoding;

import java.util.List;

import main.evolution.ga.AbstractGene;

public class EncodingGene extends AbstractGene<Float> {
	
	public EncodingGene(int length) {
		super(length);
	}
	
	public EncodingGene(List<Float> values) {
		super(values);
	}

	@Override
	protected void initializeGene() {
		// TODO Auto-generated method stub
		
	}
}
