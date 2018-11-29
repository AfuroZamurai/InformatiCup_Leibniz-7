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
 * Genom representation of an arbitrary encoding
 * 
 * @author Felix
 *
 */
public class EncodingGenom extends GenericGenom<EncodingGene> {
	
	public EncodingGenom(float initialFitness, List<EncodingGene> genes) {
		super(initialFitness, genes);
	}
	
	public EncodingGenom() {
		super();
	}
	
	public static List<EncodingGenom> reproduce(EncodingGenom parent1, EncodingGenom parent2) {
		if(parent1.getGenes().size() != parent2.getGenes().size()) {
			System.out.println("Trying to reproduce with genoms of different gene length!");
			return null;
		}
		
		List<EncodingGenom> offspring = new ArrayList<>(2);
		EncodingGenom child1 = new EncodingGenom();
		EncodingGenom child2 = new EncodingGenom();
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
	
	public void mutate() {
		for(EncodingGene gene : genes) {
			for(int i = 0; i < gene.getGeneLength(); i++) {
				if (Evolutionhelper.randomFloat() < GeneticAlgorithm.MUTATION_RATE) {
					gene.replaceValue(i, Evolutionhelper.randomFloat());
				}
			}
		}
	}
	
	private int parameterSize() {
		int size = 0;
		for(EncodingGene gene : genes) {
			size += gene.getGeneLength();
		}
		
		return size;
	}
}
