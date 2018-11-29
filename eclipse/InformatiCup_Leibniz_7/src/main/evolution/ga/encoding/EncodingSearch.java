package main.evolution.ga.encoding;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import main.encodings.IImageEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.TrasiWebEvaluator;
import main.evolution.ga.GeneticAlgorithm;
import main.evolution.ga.cppn.CPPNGenom;
import main.utils.Evolutionhelper;
import main.utils.ImageUtil;

public class EncodingSearch extends GeneticAlgorithm<EncodingGenom> {
	
	private final IImageEncoding encoding;
	private final IClassification targetClass;
	
	private final BufferedImage original;
	
	private int geneAmount = 1; 

	public EncodingSearch(int populationSize, float targetFitness, int generationCap, IImageEncoding encoding, 
			IClassification targetClass, BufferedImage original) {
		super(populationSize, targetFitness, generationCap);
		this.encoding = encoding;
		this.targetClass = targetClass;
		this.original = original;
	}
	
	public BufferedImage getImageFromGenom(EncodingGenom genom) {
		return ImageUtil.drawOnTop(original, getEncodingImage(genom));
	}
	
	public BufferedImage getEncodingImage(EncodingGenom genom) {
		return this.encoding.createImage(64, 64, genom.getAllParameters());
	}

	@Override
	protected void createPopulation() {
		for(int i = 0; i < populationSize; i++) {
			List<EncodingGene> genes = new ArrayList<>();
			for(int j = 0; j < geneAmount; j++) {
				genes.add(new EncodingGene(encoding.getParameterBatchSize()));
			}
			EncodingGenom genom = new EncodingGenom(-1.0f, genes);
			population.addGenom(genom);
		}
		
		calculateFitness();
		System.out.println("Best confidence: " + population.getBest().getFitness());
	}

	@Override
	protected void createOffspring() {
		List<Pair<EncodingGenom, EncodingGenom>> parents = selectParents();
		List<EncodingGenom> newPopulation = new ArrayList<>();
		for (Iterator<Pair<EncodingGenom, EncodingGenom>> iterator = parents.iterator(); iterator.hasNext();) {
			Pair<EncodingGenom, EncodingGenom> pair = iterator.next();
			List<EncodingGenom> offspring = EncodingGenom.reproduce(pair.getFirst(), pair.getSecond());
			newPopulation.addAll(offspring);
		}
		
		for(EncodingGenom genom : newPopulation) {
			genom.mutate();
		}
		
		population.getGenoms().clear();
		population.getGenoms().addAll(newPopulation);
		calculateFitness();
	}
	
	private List<Pair<EncodingGenom, EncodingGenom>> selectParents() {
		List<Pair<EncodingGenom, EncodingGenom>> parents = new ArrayList<>();
		
		float[] cumulativeFitnesses = new float[populationSize];
        cumulativeFitnesses[0] = population.getGenoms().get(0).getFitness();
        
        for (int i = 1; i < populationSize; i++)
        {
            float fitness = population.getGenoms().get(i).getFitness();
            cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + fitness;
        }
        
        for(int i = 0; i < populationSize / 2; i++) {
        	float randomFitness1 = Evolutionhelper.randomFloat() * cumulativeFitnesses[cumulativeFitnesses.length - 1];
        	float randomFitness2 = Evolutionhelper.randomFloat() * cumulativeFitnesses[cumulativeFitnesses.length - 1];
            int index1 = Arrays.binarySearch(cumulativeFitnesses, randomFitness1);
            int index2 = Arrays.binarySearch(cumulativeFitnesses, randomFitness2);
            
            if (index1 < 0)
            {
                // Convert negative insertion point to array index.
                index1 = Math.abs(index1 + 1);
            }
            if (index2 < 0)
            {
                // Convert negative insertion point to array index.
                index2 = Math.abs(index2 + 1);
            }
            
            if(index1 == populationSize) {
            	index1--;
            }
            if(index2 == populationSize) {
            	index2--;
            }
            
            EncodingGenom selected1 = population.getGenoms().get(index1);
            EncodingGenom selected2 = population.getGenoms().get(index2);
            Pair<EncodingGenom, EncodingGenom> selectedParents = new Pair<EncodingGenom, EncodingGenom>(selected1, selected2);
            parents.add(selectedParents);
        }
		
		return parents;
	}

	@Override
	protected void selectSurvivors() {
		// maybe some sort of elitism?
	}

	@Override
	protected void calculateFitness() {
		List<EncodingGenom> genoms = population.getGenoms();
		TrasiWebEvaluator evaluator = new TrasiWebEvaluator();
		for(EncodingGenom genom : genoms) {
			BufferedImage image = getImageFromGenom(genom);
			BufferedImage encodingImage = getEncodingImage(genom);
			EvaluationResult<IClassification> result;
			try {
				result = evaluator.evaluateImage(image);
				if (result != null) {
					float confidence = result.getConfidenceForClass(targetClass);
					float coverage = ImageUtil.getTransparentPercent(encodingImage);
					float fitness = 1.0f - (1.0f - confidence) - (1.0f - coverage);
					genom.setFitness(fitness);
				} else {
					genom.setFitness(0.0f);
					System.out.println("Evaluation currently impossible!");
				}
			} catch (Exception e) {
				//wrong image size, shouldn't happen
				genom.setFitness(0.0f);
			}	
		}
		
		try {
			Collections.sort(genoms);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		population.setBest(genoms.get(genoms.size() - 1));
	}
	
	
}
