package main.evolution.ga.encoding;

import java.awt.image.BufferedImage;
import java.io.IOException;
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
import main.io.ImageSaver;
import main.utils.Evolutionhelper;
import main.utils.ImageUtil;

/**
 * Implementation of a genetic algorithm for the optimization of an encoding.
 *
 * @author Felix
 *
 */
public class EncodingSearch extends GeneticAlgorithm<EncodingGenom> {
	
	/**
	 * The type of encoding that is used.
	 */
	private final IImageEncoding encoding;
	/**
	 * The target class this algorithm seeks to optimize for.
	 */
	private final IClassification targetClass;
	
	/**
	 * The starting image which is given.
	 */
	private final BufferedImage original;
	
	/**
	 * Number of genes the genoms of the initial population will have.
	 */
	private int geneAmount = 100; 
	/**
	 * The probability that a gene will be added to a genom during mutation.
	 */
	public static final float GENE_ADD_PROBABILITY = 0.2f;

	/**
	 * Creates a new instance of this genetic algorithm.
	 * @param populationSize The number of genoms in the population
	 * @param targetFitness The fitness threshold
	 * @param generationCap The maximum number of generations
	 * @param elitism The amount of surviving genoms per generation
	 * @param encoding The encoding used for the genetic algorithm
	 * @param targetClass The class the target image should have 
	 * @param original The starting image
	 */
	public EncodingSearch(int populationSize, float targetFitness, int generationCap, int elitism, 
			IImageEncoding encoding, IClassification targetClass, BufferedImage original) {
		super(populationSize, targetFitness, generationCap, elitism);
		this.encoding = encoding;
		this.targetClass = targetClass;
		this.original = original;
	}
	
	/**
	 * Creates an image for a given genom. It will calculate the size and position of all encodings from the genes.
	 * The encodings will be added on top of the original image and overwrite these parts.
	 * @param genom The genom for which its phenotype, e.g. the image will be created
	 * @return An image where the encodings of the genom are drawn on top of the original image.
	 */
	public BufferedImage getImageFromGenom(EncodingGenom genom) {
		return ImageUtil.drawOnTop(original, getEncodingImage(genom));
	}
	
	/**
	 * Constructs an image where only pixels overlaid by an encoding are not transparent.
	 * @param genom The genom for which its encodings will be created
	 * @return An image containing all the encodings
	 */
	public BufferedImage getEncodingImage(EncodingGenom genom) {
		return this.encoding.createImage(64, 64, genom.getAllParameters());
	}

	@Override
	protected void createPopulation() {
		for(int i = 0; i < populationSize; i++) {
			List<EncodingGene> genes = new ArrayList<>();
			//create genoms with the given amount of genes using the correct amount of values for the genes
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
		//select parents and survivors before removing the old population
		List<Pair<EncodingGenom, EncodingGenom>> parents = selectParents();
		List<EncodingGenom> survivors = getElite(elitism);
		
		List<EncodingGenom> newPopulation = new ArrayList<>();
		for (Iterator<Pair<EncodingGenom, EncodingGenom>> iterator = parents.iterator(); iterator.hasNext();) {
			Pair<EncodingGenom, EncodingGenom> pair = iterator.next();
			List<EncodingGenom> offspring;
			
			//get the children but if it fails just make two new random genoms
			try {
				offspring = EncodingGenom.reproduce(pair.getFirst(), pair.getSecond());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				offspring = new ArrayList<>();
				for(int i = 0; i < 2; i++) {
					List<EncodingGene> genes = new ArrayList<>();
					for(int j = 0; j < geneAmount; j++) {
						genes.add(new EncodingGene(encoding.getParameterBatchSize()));
					}
					EncodingGenom genom = new EncodingGenom(-1.0f, genes);
					offspring.add(genom);
				}
			}
			newPopulation.addAll(offspring);
		}
		
		for(EncodingGenom genom : newPopulation) {
			genom.mutate();
		}
		
		//calculate fitness only for the children and remove as many of the worst ones as old genoms are surviving 
		calculateFitness(newPopulation);
		for(int i = 0; i < elitism; i++) {
			newPopulation.remove(0);
		}
		population.getGenoms().clear();
		population.getGenoms().addAll(newPopulation);
		population.getGenoms().addAll(survivors);
		
		//for debugging purposes
		try {
			ImageSaver.saveImage(getImageFromGenom(population.getBest()), "data/results/encodingsearch/bestbeforegen" + currentGeneration());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Perform a roulette wheel selection to select a list of parents which will create the next generation.
	 * @return A list containing enough parents (pairs of genoms) to create exactly enough new genoms
	 */
	private List<Pair<EncodingGenom, EncodingGenom>> selectParents() {
		List<Pair<EncodingGenom, EncodingGenom>> parents = new ArrayList<>();
		
		//calculate the cumulative fitness for every genom to create the selection slots
		float[] cumulativeFitnesses = new float[populationSize];
        cumulativeFitnesses[0] = population.getGenoms().get(0).getFitness();
        
        for (int i = 1; i < populationSize; i++)
        {
            float fitness = population.getGenoms().get(i).getFitness();
            cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + fitness;
        }
        
        //we need two genoms, so in every loop select a parent pair. 
        //Reproducing yields two children, so we need only parents in size of half the population 
        for(int i = 0; i < populationSize / 2; i++) {
        	//calculate two random values and select the index for the two genoms in which slot range the values belong 
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
            
            //the last slot gives us an index out of range, so we need to decrement it
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
	
	/**
	 * Selects a given number of the best genoms of the current population.
	 * @param survivors The number of genoms which will be selected
	 * @return A list containing the specified number of best genoms of this generation
	 */
	private List<EncodingGenom> getElite(int survivors) {
		List<EncodingGenom> elite = new ArrayList<>();
		List<EncodingGenom> genoms = population.getGenoms();
		Collections.sort(genoms);
		for(int i = 0; i < survivors; i++) {
			//sorting lets the best genoms be at the end
			elite.add(genoms.get(genoms.size() - 1 - i));
		}
		
		return elite;
	}

	@Override
	protected void selectSurvivors() {
		// maybe some sort of elitism?
	}
	
	/**
	 * Calculates the fitness for all genoms in the given list.
	 * @param genoms The list of genoms for which the fitness will be calculated
	 */
	private void calculateFitness(List<EncodingGenom> genoms) {
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
					//calculate fitness from confidence and coverage. Maybe one of them should be more important
					//TODO: create a method for fitness calculation
					float fitness = 1.0f - (1.0f - confidence) - (1.0f - coverage);
					genom.setFitness(fitness);
				} else {
					//shouldn't happen
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
		EncodingGenom best = (genoms.get(genoms.size() - 1));

		//make sure we really have a new best genom as we are no assigning a fitness score to the whole population
		if(best.getFitness() > population.getBest().getFitness()) {
			population.setBest(best);
		}
	}

	@Override
	protected void calculateFitness() {
		List<EncodingGenom> genoms = population.getGenoms();
		TrasiWebEvaluator evaluator = new TrasiWebEvaluator();
		
		//this method calculates fitness for the whole population
		for(EncodingGenom genom : genoms) {
			BufferedImage image = getImageFromGenom(genom);
			//to calculate fitness we need coverage from the transparent-based image
			BufferedImage encodingImage = getEncodingImage(genom);
			EvaluationResult<IClassification> result;
			try {
				result = evaluator.evaluateImage(image);
				if (result != null) {
					float confidence = result.getConfidenceForClass(targetClass);
					float coverage = ImageUtil.getTransparentPercent(encodingImage);
					//calculate fitness from confidence and coverage. Maybe one of them should be more important
					float fitness = 1.0f - (1.0f - confidence) - (1.0f - coverage);
					genom.setFitness(fitness);
				} else {
					//shouldn't happen
					genom.setFitness(0.0f);
					System.out.println("Evaluation currently impossible!");
				}
			} catch (Exception e) {
				//wrong image size, shouldn't happen
				genom.setFitness(0.0f);
			}	
		}
		
		//best genom found by sorting
		try {
			Collections.sort(genoms);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		population.setBest(genoms.get(genoms.size() - 1));
	}
	
	
}
