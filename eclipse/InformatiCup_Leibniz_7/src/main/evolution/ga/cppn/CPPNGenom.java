package main.evolution.ga.cppn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.evolution.ga.AbstractGene;
import main.evolution.ga.GenericGenom;
import main.evolution.ga.GeneticAlgorithm;
import main.evolution.network.CPPN;
import main.utils.Evolutionhelper;

public class CPPNGenom extends GenericGenom<CPPNGene> {
	
	private CPPN net;

	public CPPNGenom(float initialFitness, CPPNGene initialGene, CPPN net) {
		fitness = initialFitness;
		genes = new ArrayList<>();
		genes.add(initialGene);
		this.net = net;
	}
	
	public CPPNGenom() {
		fitness = -1.0f;
		genes = new ArrayList<>();
	}
	
	/**
	 * Mutate this genom. The method will iterate over all values of all genes.
	 * It will replace a value with a random new one with the probability given by the mutation rate.
	 */
	private void mutate() {
		for(CPPNGene gene : genes) {
			for(int i = 0; i < gene.getGeneLength(); i++) {
				Random rnd = new Random();
				if (rnd.nextFloat() < GeneticAlgorithm.MUTATION_RATE) {
					gene.replaceValue(i, Evolutionhelper.randomIntegerGeneValue());
				}
			}
		}
	}

	public CPPN getNet() {
		return net;
	}

	public void setNet(CPPN net) {
		this.net = net;
	}
}
