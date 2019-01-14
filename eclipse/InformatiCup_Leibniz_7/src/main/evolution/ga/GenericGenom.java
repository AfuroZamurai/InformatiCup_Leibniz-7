package main.evolution.ga;

import java.util.ArrayList;
import java.util.List;

import main.utils.Evolutionhelper;

/**
 * Generic implementation of a genom. A genom is part of a population, which gets optimized.
 * Genoms consist of a list of genes and have a fitness score assigned to them.
 * 
 * @author Felix
 *
 * @param <T> The implementation of a gene
 */
public class GenericGenom<T extends AbstractGene<?>> {
	
	protected Fitness fitness;
	protected List<T> genes;
	
	/**
	 * Create a genom with an initial fitness and a given list of genes.
	 * @param fitness the fitness of this new genom
	 * @param genes a list of genes
	 */
	public GenericGenom(float fitness, List<T> genes) {
		this.fitness = new Fitness(fitness);
		this.genes = genes;
	}
	
	/**
	 * Create a new genom without any genes. It will have an initial fitness of -1.
	 */
	public GenericGenom() {
		fitness = new Fitness(-1.0f);
		genes = new ArrayList<>();
	}

	public float getFitness() {
		return fitness.getFitnessScore();
	}

	public void setFitness(float fitness) {
		this.fitness.setFitnessScore(fitness);;
	}

	public List<T> getGenes() {
		return genes;
	}

	public void setGenes(List<T> genes) {
		this.genes = genes;
	}
}