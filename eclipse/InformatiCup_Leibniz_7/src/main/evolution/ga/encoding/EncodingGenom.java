package main.evolution.ga.encoding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import main.evolution.ga.GenericGenom;
import main.evolution.ga.GeneticAlgorithm;
import main.evolution.ga.cppn.CPPNGene;
import main.utils.Evolutionhelper;

/**
 * Genom representation of an arbitrary encoding.
 * 
 * @author Felix
 *
 */
public class EncodingGenom extends GenericGenom<EncodingGene> {
	
	/**
	 * The number of floats to represent a single encoding instance.
	 */
	private final int ENCODING_SIZE;
	
	private EncodingFitness fitness;
	
	/**
	 * Create a new genom with a given list of genes.
	 * @param initialFitness the initial fitness of this genom
	 * @param genes the list of genes the new genom will hold
	 */
	public EncodingGenom(float initialFitness, List<EncodingGene> genes) {
		this.fitness = new EncodingFitness(initialFitness);
		this.genes = genes;
		this.ENCODING_SIZE = genes.get(0).getGeneLength();
	}
	
	/**
	 * Create a genom without genes but for a selected encoding.
	 * @param encodingSize The length of a single gene
	 */
	public EncodingGenom(int encodingSize) {
		fitness = new EncodingFitness(-1.0f);
		genes = new ArrayList<>();
		this.ENCODING_SIZE = encodingSize;
	}
	
	/**
	 * Creates two children from the given genoms. It will perform a crossover of the gene lists and of the gene values.
	 * @param parent1 First genom from which genes the children will be created
	 * @param parent2 Second genom from which genes the children will be created
	 * @return A list of two newly created genoms 
	 * @throws Exception if the genoms are for encodings of different size
	 */
	public static List<EncodingGenom> reproduce(EncodingGenom parent1, EncodingGenom parent2) throws Exception {
		if(parent1.getGenes().get(0).getGeneLength() != parent2.getGenes().get(0).getGeneLength()) {
			throw new Exception("Trying to reproduce with different encoding size!");
		}
		
		int encodingSize = parent1.ENCODING_SIZE;
		
		List<EncodingGenom> offspring = new ArrayList<>(2);
		EncodingGenom child1 = new EncodingGenom(encodingSize);
		EncodingGenom child2 = new EncodingGenom(encodingSize);
		int p1GeneSize = parent1.getGenes().size();
		int p2GeneSize = parent2.getGenes().size();
		
		int geneSizeLower = p1GeneSize < p2GeneSize ? p1GeneSize : p2GeneSize;
		int geneSizeUpper = p1GeneSize > p2GeneSize ? p1GeneSize : p2GeneSize;
		int geneCrossoverSize = Evolutionhelper.randomInt(0, geneSizeLower - 1);
		int geneValueCrossoverSize = Evolutionhelper.randomInt(0, parent1.getGenes().get(0).getGeneLength() - 1);
		
		int i, j;
		
		for(i = 0; i < geneCrossoverSize; i++) {
			child1.getGenes().add(parent1.getGenes().get(i));
			child2.getGenes().add(parent2.getGenes().get(i));
		}
		
		List<Float> crossover1 = new ArrayList<>(parent1.getGenes().get(0).getGeneLength());
		List<Float> crossover2 = new ArrayList<>(parent1.getGenes().get(0).getGeneLength());
		for(j = 0; j < parent1.getGenes().get(0).getGeneLength(); j++) {
			if(i < geneValueCrossoverSize) {
				crossover1.add(parent1.getGenes().get(i).getValues().get(j));
				crossover2.add(parent2.getGenes().get(i).getValues().get(j));
			} else {
				crossover1.add(parent2.getGenes().get(i).getValues().get(j));
				crossover2.add(parent1.getGenes().get(i).getValues().get(j));
			}
		}
		
		EncodingGene crossoverGene1 = new EncodingGene(crossover1);
		EncodingGene crossoverGene2 = new EncodingGene(crossover2);
		
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
	
	/**
	 * Retrieve the complete list of all gene values for this genom.
	 * @return An array containing all gene values in order.
	 */
	public float[] getAllParameters() {
		int size = parameterSize();
		float[] params = new float[size];
		
		int index = 0;
		for(EncodingGene gene : genes) {
			List<Float> parameters = gene.getValues();
			for (Iterator<Float> iterator = parameters.iterator(); iterator.hasNext();) {
				Float curParameter = iterator.next();
				params[index] = curParameter;
				index++;
			}
		}
		
		return params;
	}
	
	/**
	 * Mutate this genom. It will randomly mutate a gene value with a probability of the selected mutation rate.
	 * Also it will add a new gene with a probability of the selected gene add probability.
	 */
	public void mutate() {
		//try to replace a single gene value
		for(EncodingGene gene : genes) {
			for(int i = 0; i < gene.getGeneLength(); i++) {
				if (Evolutionhelper.randomFloat() < GeneticAlgorithm.MUTATION_RATE) {
					gene.replaceValue(i, Evolutionhelper.randomFloat());
				}
			}
		}
		
		//try to add a new randomly initialised gene
		if(Evolutionhelper.randomFloat() < EncodingSearch.GENE_ADD_PROBABILITY) {
			EncodingGene addedGene = new EncodingGene(ENCODING_SIZE);
			genes.add(addedGene);
		}
	}
	
	private int parameterSize() {
		int size = 0;
		for(EncodingGene gene : genes) {
			size += gene.getGeneLength();
		}
		
		return size;
	}

	@Override
	public float getFitness() {
		return fitness.getFitnessScore();
	}
	
	public float getConfidence() {
		return fitness.getConfidence();
	}
	
	public float getCoverage() {
		return fitness.getCoverage();
	}
	
	public void updateFitness(float newFitness, float confidence, float coverage) {
		fitness.setFitnessScore(newFitness);
		fitness.setConfidence(confidence);
		fitness.setCoverage(coverage);
	}
	
	public int compareTo(EncodingGenom o) {
		return this.fitness.getFitnessScore() - o.fitness.getFitnessScore() > 0 ? 1 : 
			this.fitness.getFitnessScore() - o.fitness.getFitnessScore() < 0 ? -1 : 0;
	}
}
