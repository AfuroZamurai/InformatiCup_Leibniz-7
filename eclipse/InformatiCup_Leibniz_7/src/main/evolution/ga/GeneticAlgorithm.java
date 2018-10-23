package main.evolution.ga;

import java.util.List;

/**
 * Generic implementation of a genetic algoithm. It is intended to be used as a superclass.
 * The image-specific implementation used here is CuckooSearch. 
 * 
 * @author Felix
 *
 */
public class GeneticAlgorithm {
	
	protected final int populationSize;
	protected final float targetFitness;
	protected final int generationCap;
	protected Population population;
	
	public static final int MAX_GENE_VALUE = Integer.MAX_VALUE;
	public static final float MUTATION_RATE = 0.03f;
	
	/**
	 * Constructor to initialize a genetic algorithm with some needed parameters.
	 * 
	 * @param populationSize how many individuals the population will consist of
	 * @param targetFitness threshold which will terminate the genetic algorithm if reached
	 * @param generationCap limit of the number of iterations the genetic algorithm will run
	 */
	public GeneticAlgorithm(int populationSize, float targetFitness, int generationCap) {
		this.populationSize = populationSize;
		this.targetFitness = targetFitness;
		this.generationCap = generationCap;
		this.population = new Population();
	}
	
	/**
	 * Starts the genetic algorithm. It will run until either the target fitness or the generation cap was reached.
	 */
	public void run() {
		createPopulation();
		
		int generation = 1;
		while(generation < generationCap && getHighestFitness() < targetFitness) {
			createOffspring();
			selectSurvivors();
			generation++;
		}
		
		if(getHighestFitness() >= targetFitness) {
			System.out.println("Successful run!");
		} else {
			System.out.println("Reached generation cap with a maximum fitness of " + getHighestFitness());
		}
	}
	
	public float getHighestFitness() {
		return population.getBest().getFitness();
	}
	
	public float getAverageFitness() {
		List<Genom> genoms = population.getGenoms();
		float average = 0.0f;
		for(Genom genom : genoms) {
			average += genom.getFitness();
		}
		return average / genoms.size();
	}
	
	public Genom getBestGenom() {
		return population.getBest();
	}
	
	/**
	 * Creates a new population and assigns them a fitness value.
	 */
	protected void createPopulation() {
		
	}
	
	
	protected void createOffspring() {
		
	}
	
	protected void selectSurvivors() {
		
	}
	
	protected void calculateFitness() {

	}
}
