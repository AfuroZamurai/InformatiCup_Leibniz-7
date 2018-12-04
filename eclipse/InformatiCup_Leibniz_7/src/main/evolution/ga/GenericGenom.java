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
public class GenericGenom<T extends AbstractGene<?>> implements Comparable<GenericGenom<T>>{
	
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
	
	/**
	 * Takes two Genoms and creates two childs. The childs won't be mutated.
	 * @param parent1 first parent
	 * @param parent2 second parent
	 * @return two newly created genoms or null if the genoms have genes with a different size
	 */
	/*
	public List<GenericGenom<T>> reproduce(GenericGenom<T> parent1, GenericGenom<T> parent2) {
		if(parent1.getGenes().size() != parent2.getGenes().size()) {
			System.out.println("Trying to reproduce with genoms of different gene length!");
			return null;
		}
		
		List<GenericGenom<T>> offspring = new ArrayList<>(2);
		GenericGenom<T> child1 = new GenericGenom<>();
		GenericGenom<T> child2 = new GenericGenom<>();
		int p1GeneSize = parent1.getGenes().size();
		int p2GeneSize = parent2.getGenes().size();
		
		int geneSizeLower = p1GeneSize < p2GeneSize ? p1GeneSize : p2GeneSize;
		int geneSizeUpper = p1GeneSize > p2GeneSize ? p1GeneSize : p2GeneSize;
		int geneCrossoverSize = Evolutionhelper.randomInt(0, geneSizeLower);
		int geneValueCrossoverSize = Evolutionhelper.randomInt(0, parent1.getGenes().get(0).getGeneLength() - 1);
		
		int i, j;
		
		for(i = 0; i < geneCrossoverSize; i++) {
			child1.getGenes().add(parent1.getGenes().get(i));
			child2.getGenes().add(parent2.getGenes().get(i));
		}
		
		List<T> crossover1 = new ArrayList<>(parent1.getGenes().get(0).getGeneLength());
		List<T> crossover2 = new ArrayList<>(parent1.getGenes().get(0).getGeneLength());
		for(j = 0; j < parent1.getGenes().get(0).getGeneLength(); j++) {
			if(i < geneValueCrossoverSize) {
				crossover1.add((T) parent1.getGenes().get(i).getValues().get(j));
				crossover2.add((T) parent2.getGenes().get(i).getValues().get(j));
			} else {
				crossover1.add((T) parent2.getGenes().get(i).getValues().get(j));
				crossover2.add((T) parent1.getGenes().get(i).getValues().get(j));
			}
		}
		
		T crossoverGene1 = new T(crossover1);
		T crossoverGene2 = new T(crossover2);
		
		child1.getGenes().add(crossoverGene1);
		child2.getGenes().add(crossoverGene2);
		
		for(i++; i < geneSizeUpper; i++) {
			if(i < parent2.getGenes().size()) {
				child1.getGenes().add(parent2.getGenes().get(i));
			} 
			
			if(i < parent1.getGenes().size()){
				child2.getGenes().add(parent1.getGenes().get(i));
			}
		}
		
		offspring.add(child1);
		offspring.add(child2);
		
		return offspring;
	}
	*/

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
	
	/**
	 * Compares another genom to this genom instance.
	 * @param o the genom to which this genom gets compared to
	 * @return 1 if this genoms fitness is higher, -1 if it is lower and 0 if the fitness is equal
	 */
	public int compareTo(GenericGenom<T> o) {
		return this.fitness.getFitnessScore() - o.fitness.getFitnessScore() > 0 ? 1 : 
			this.fitness.getFitnessScore() - o.fitness.getFitnessScore() < 0 ? -1 : 0;
	}
}