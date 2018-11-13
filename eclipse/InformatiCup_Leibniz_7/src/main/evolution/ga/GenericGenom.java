package main.evolution.ga;

import java.util.ArrayList;
import java.util.List;

import main.utils.Evolutionhelper;

public class GenericGenom {
	
	protected float fitness;
	protected List<Gene> genes;
	
	public GenericGenom(float fitness, List<Gene> genes) {
		this.fitness = fitness;
		this.genes = genes;
	}
	
	public GenericGenom() {
		fitness = -1.0f;
		genes = new ArrayList<>();
	}
	
	/**
	 * Takes two Genoms and creates two childs. The childs won't be mutated.
	 * @param parent1 first parent
	 * @param parent2 second parent
	 * @return two newly created genoms or null if the genoms have genes with a different size
	 */
	public static GenericGenom[] reproduce(GenericGenom parent1, GenericGenom parent2) {
		if(parent1.getGenes().size() != parent2.getGenes().size()) {
			System.out.println("Tryong to reproduce with genoms of different gene length!");
			return null;
		}
		
		GenericGenom[] offspring = new GenericGenom[2];
		GenericGenom child1 = new GenericGenom();
		GenericGenom child2 = new GenericGenom();
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
		
		int[] crossover1 = new int[parent1.getGenes().get(0).getValues().length];
		int[] crossover2 = new int[parent1.getGenes().get(0).getValues().length];
		for(j = 0; j < parent1.getGenes().get(0).getValues().length; j++) {
			if(i < geneValueCrossoverSize) {
				crossover1[j] = parent1.getGenes().get(i).getValues()[j];
				crossover2[j] = parent2.getGenes().get(i).getValues()[j];
			} else {
				crossover1[j] = parent2.getGenes().get(i).getValues()[j];
				crossover2[j] = parent1.getGenes().get(i).getValues()[j];
			}
		}
		
		Gene crossoverGene1 = new Gene(crossover1);
		Gene crossoverGene2 = new Gene(crossover2);
		
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
		
		offspring[0] = child1;
		offspring[1] = child2;
		
		return offspring;
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
	
	public int compareTo(GenericGenom o) {
		return this.fitness - o.fitness > 0 ? 1 : this.fitness - o.fitness < 0 ? -1 : 0;
	}
}