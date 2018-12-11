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
public class GeneticAlgorithm<T extends GenericGenom<? extends AbstractGene<?>>> {
	
	protected final int populationSize;
	protected final float targetFitness;
	protected final int generationCap;
	protected final int elitism;
	protected Population<T> population;
	
	protected boolean finished = false;
	protected int generation = 1;
	
	/**
	 * The probability that a mutation happens
	 */
	public static final float MUTATION_RATE = 0.03f;
	
	
	/**
	 * Constructor to initialize a genetic algorithm with some needed parameters.
	 * 
	 * @param populationSize how many individuals the population will consist of
	 * @param targetFitness threshold which will terminate the genetic algorithm if reached
	 * @param generationCap limit of the number of iterations the genetic algorithm will run
	 * @param elitism amount of best genoms that will live on in the new generation 
	 */
	public GeneticAlgorithm(int populationSize, float targetFitness, int generationCap, int elitism) {
		this.populationSize = populationSize;
		this.targetFitness = targetFitness;
		this.generationCap = generationCap;
		this.elitism = elitism;
		this.population = new Population<>();
	}
	
	/**
	 * Starts the genetic algorithm. It will run until either the target fitness or the generation cap was reached.
	 * The current run can be paused and the current generation is saved, so the next call will continue from there.
	 * @param pauseCap A generation number after which the algorithm will pause.
	 */
	public void run(int pauseCap) {
		if(generation == 1) {
			System.out.println("Creating the initial population");
			createPopulation();
		}
		
		while(generation < generationCap && getHighestFitness() < targetFitness) {
			System.out.println("Running generation " + generation + ":");
			createOffspring();
			System.out.println("Created new population!");
			selectSurvivors();
			//System.out.println("Selected the survivors!");
			generation++;
			System.out.println("Highest fitness so far: " + getHighestFitness());
			
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
	
	/**
	 * Retrieve the currently best fitness.
	 * @return the best fitness or 0 if no generation was run
	 */
	public float getHighestFitness() {
		try {
			return population.getBest().getFitness();
		} catch (NullPointerException e) {
			return 0.0f;
		}
	}
	
	/**
	 * Retrieves the average fitness of the current population.
	 * @return The average fitness of all genoms in the population
	 */
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
	
	/**
	 * Creates a new population from the current population
	 */
	protected void createOffspring() {
		
	}
	
	/**
	 * Currently only used by CuckooSearch. Might be changed in the future so elitism is executed here.
	 */
	protected void selectSurvivors() {
		
	}
	
	/**
	 * (Re)calculate the fitness for all genoms in the population.
	 */
	protected void calculateFitness() {

	}
	
	/**
	 * Calculate the fitness for a single genom
	 * @param genomIndex index of the genom
	 */
	protected void calculateFitness(int genomIndex) {
		
	}
	
	/**
	 * Retrieves the current status of the run.
	 * @return if the run is finished
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * Retrieves the current generation number of this run.
	 * @return The current generation number
	 */
	public int currentGeneration() {
		return generation - 1;
	}
}
