package main.evolution.ga;

import java.awt.image.BufferedImage;

import main.evolution.network.CPPN;

/**
 * Implementation of the cuckoo search algorithm
 * 
 * @author Felix
 *
 */
public class CuckooSearch extends GeneticAlgorithm {
	
	private CPPN net;
	private final int populationSize;
	private final float targetFitness;
	private final int generationCap;
	
	/**
	 * Instantiates a cuckoo search.
	 * 
	 * @param net the CPPN which will be used for the indirect encoding of the images
	 * @param populationSize the amount of nests
	 * @param targetFitness minimum fitness which must be reached for the algorithm to terminate
	 * @param generationCap maximum number of generations the algorithm will search 
	 */
	public CuckooSearch(CPPN net, int populationSize, float targetFitness, int generationCap) {
		super();
		this.net = net;
		this.populationSize = populationSize;
		this.targetFitness = targetFitness;
		this.generationCap = generationCap;
	}

	/**
	 * Performs a cuckoo search to create a fooling image for a given class.
	 * 
	 * @param inputClass label of the target class, which the tricked neural net will predict
	 * @return a BufferedImage of the resulting fooling image
	 */
	public BufferedImage searchForImage(String inputClass) {
		
		return null;
	}
	
	private float getFitness() {
		return 0.0f;
	}
	
	private void createNewNests() {
		
	}
	
	@Override
	protected void selectSurvivors() {
		
	}
	
	private void performLevyFlight( ) {
		
	}
}
