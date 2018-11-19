package main.evolution.ga;

import java.util.List;

import org.apache.commons.logging.impl.Log4JLogger;

/**
 * Generic implementation of a genetic algorithm. It is intended to be used as a superclass.
 * The image-specific implementation used here is CuckooSearch. 
 * 
 * @author Felix
 *
 */
public class GeneticAlgorithm<T extends GenericGenom> {
	
	protected final int populationSize;
	protected final float targetFitness;
	protected final int generationCap;
	protected Population<T> population;
	
	protected boolean finished = false;
	protected int generation = 1;
	
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
		this.population = new Population<>();
	}
	
	/**
	 * Starts the genetic algorithm. It will run until either the target fitness or the generation cap was reached.
	 */
	public void run(int pauseCap) {
		System.out.println("Creating the initial population");
		createPopulation();
		
		while(generation < generationCap && getHighestFitness() < targetFitness) {
			System.out.println("Running generation " + generation + ":");
			createOffspring();
			System.out.println("Created new nests!");
			selectSurvivors();
			System.out.println("Selected the survivors!");
			generation++;
			System.out.println("Highest fitness fo far: " + getHighestFitness());
			
			if(generation >= pauseCap) {
				return;
			}
		}
		
		finished = true;
		
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
		List<T> genoms = population.getGenoms();
		float average = 0.0f;
		for(T genom : genoms) {
			average += genom.getFitness();
		}
		return average / genoms.size();
	}
	
	public T getBestGenom() {
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
	
	public boolean isFinished() {
		return finished;
	}
	
	public int currentGeneration() {
		return generation - 1;
	}
}
