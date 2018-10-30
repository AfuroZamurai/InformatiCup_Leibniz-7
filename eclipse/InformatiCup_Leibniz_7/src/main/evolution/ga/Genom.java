package main.evolution.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.evaluate.EvaluationResult;
import main.evolution.network.CPPN;
import main.utils.Evolutionhelper;

public class Genom implements Comparable<Genom> {
	
	private float fitness;
	private List<Gene> genes;
	private CPPN net;

	public Genom(float initialFitness, Gene initialGene, CPPN net) {
		fitness = initialFitness;
		genes = new ArrayList<>();
		genes.add(initialGene);
		this.net = net;
	}
	
	public Genom() {
		fitness = -1.0f;
		genes = new ArrayList<>();
	}
	
	/**
	 * Takes two Genoms and creates two childs. The childs will already be mutated.
	 * @param parent1 first parent
	 * @param parent2 second parent
	 * @return two newly created genoms
	 */
	public static Genom[] reproduce(Genom parent1, Genom parent2) {
		Genom[] offspring = new Genom[2];
		Genom child1 = new Genom();
		Genom child2 = new Genom();
		int p1GeneSize = parent1.getGenes().size();
		int p2GeneSize = parent2.getGenes().size();
		
		int geneSizeLower = p1GeneSize < p2GeneSize ? p1GeneSize : p2GeneSize;
		int geneSizeUpper = p1GeneSize > p2GeneSize ? p1GeneSize : p2GeneSize;
		int geneCrossoverSize = Evolutionhelper.randomInt(0, geneSizeLower);
		int geneValueCrossoverSize = Evolutionhelper.randomInt(0, parent1.getGenes().get(0).getValues().length - 1);
		
		int i, j;
		
		for(i = 0; i < geneCrossoverSize; i++) {
			child1.getGenes().add(parent1.getGenes().get(i));
			child2.getGenes().add(parent2.getGenes().get(i));
		}
		
		//TODO: implement crossover of the gene values
		
		
		return offspring;
	}
	
	/**
	 * Mutate this genom. The method will iterate over all values of all genes.
	 * It will replace a value with a random new one with the probability given by the mutation rate.
	 */
	private void mutate() {
		for(Gene gene : genes) {
			for(int i = 0; i < gene.getValues().length; i++) {
				Random rnd = new Random();
				if (rnd.nextFloat() < GeneticAlgorithm.MUTATION_RATE) {
					gene.replaceValue(i, Evolutionhelper.randomGeneValue());
				}
			}
		}
	}

	public float getFitness() {
		return fitness;
	}

	public void setFitness(float fitness) {
		this.fitness = fitness;
	}
	
	public List<Gene> getGenes() {
		return genes;
	}
	
	public void setGenes(List<Gene> genes) {
		this.genes = genes;
	}

	public CPPN getNet() {
		return net;
	}

	public void setNet(CPPN net) {
		this.net = net;
	}

	@Override
	public int compareTo(Genom o) {
		return this.fitness - o.fitness > 0 ? 1 : this.fitness - o.fitness < 0 ? -1 : 0;
	}
}
