package main.evolution.ga.cppn;

import java.util.ArrayList;
import java.util.Random;

import main.evolution.ga.Fitness;
import main.evolution.ga.GenericGenom;
import main.evolution.ga.GeneticAlgorithm;
import main.evolution.network.CPPN;
import main.utils.Evolutionhelper;

public class CPPNGenom extends GenericGenom<CPPNGene> implements Comparable<CPPNGenom> {
	
	private CPPN net;

	public CPPNGenom(float initialFitness, CPPNGene initialGene, CPPN net) {
		fitness = new Fitness(initialFitness);
		genes = new ArrayList<>();
		genes.add(initialGene);
		this.net = net;
	}
	
	public CPPNGenom() {
		super();
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
	
	/**
	 * Compares another genom to this genom instance.
	 * @param o the genom to which this genom gets compared to
	 * @return 1 if this genoms fitness is higher, -1 if it is lower and 0 if the fitness is equal
	 */
	public int compareTo(CPPNGenom o) {
		return this.fitness.getFitnessScore() - o.fitness.getFitnessScore() > 0 ? 1 : 
			this.fitness.getFitnessScore() - o.fitness.getFitnessScore() < 0 ? -1 : 0;
	}
}
