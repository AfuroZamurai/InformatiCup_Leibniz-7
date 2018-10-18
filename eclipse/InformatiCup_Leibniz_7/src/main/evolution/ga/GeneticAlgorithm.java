package main.evolution.ga;

import java.util.List;

public class GeneticAlgorithm {
	
	protected final int populationSize;
	protected final float targetFitness;
	protected final int generationCap;
	protected Population population;
	
	public static final int MAX_GENE_VALUE = Integer.MAX_VALUE;
	
	public GeneticAlgorithm(int populationSize, float targetFitness, int generationCap) {
		this.populationSize = populationSize;
		this.targetFitness = targetFitness;
		this.generationCap = generationCap;
	}
	
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
		List<Genom> genes = population.getIndividuals();
		float average = 0.0f;
		for(Genom gene : genes) {
			average += gene.getFitness();
		}
		return average / genes.size();
	}
	
	public Genom getBestGene() {
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
}
