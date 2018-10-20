package main.evolution.ga;

import java.awt.image.BufferedImage;
import java.util.List;

import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;
import main.evaluate.TrasiWebEvaluator;
import main.evolution.network.CPPN;

/**
 * Implementation of the cuckoo search algorithm
 * 
 * @author Felix
 *
 */
public class CuckooSearch extends GeneticAlgorithm {
	
	private CPPN net;
	private Sign target;
	
	/**
	 * Instantiates a cuckoo search.
	 * 
	 * @param net the CPPN which will be used for the indirect encoding of the images
	 * @param populationSize the amount of nests
	 * @param targetFitness minimum fitness which must be reached for the algorithm to terminate
	 * @param generationCap maximum number of generations the algorithm will search 
	 */
	public CuckooSearch(CPPN net, int populationSize, float targetFitness, int generationCap, Sign target) {
		super(populationSize, targetFitness, generationCap);
		this.net = net;
		this.target = target;
	}

	/**
	 * Performs a cuckoo search to create a fooling image for a given class.
	 * 
	 * @param inputClass label of the target class, which the tricked neural net will predict
	 * @return a BufferedImage of the resulting fooling image
	 */
	public BufferedImage searchForImage(String inputClass) {
		run();
		Genom best = getBestGenom();
		return net.createImage(best);
	}
	
	private float getFitness() {
		return 0.0f;
	}
	
	@Override
	protected void createPopulation() {
		for(int i = 0; i < populationSize; i++) {
			Genom genom = new Genom(-1.0f, net.createRandomGene(), net);
			population.addGenom(genom);
		}
	}
	
	private void createNewNests() {
		
	}
	
	@Override
	protected void createOffspring() {
		createNewNests();
	}
	
	@Override
	protected void selectSurvivors() {
		
	}
	
	private void performLevyFlight( ) {
		
	}
	
	@Override
	protected void calculateFitness( ) {
		List<Genom> genoms = population.getGenoms();
		TrasiWebEvaluator evaluator = new TrasiWebEvaluator();
		for(Genom genom : genoms) {
			BufferedImage image = net.createImage(genom);
			EvaluationResult result;
			try {
				result = evaluator.evaluateImage(image);
				if (result != null) {
					float fitness = result.getConfidenceForSign(target);
					genom.setFitness(fitness);
				} else {
					Thread.sleep(950);
					result = evaluator.evaluateImage(image);
					if (result != null) {
						float fitness = result.getConfidenceForSign(target);
						genom.setFitness(fitness);
					} else {
						// TODO: find a better solution
						// prevent endless loop if service is unavailable
						genom.setFitness(0.0f);
						System.out.println("Evaluation currently impossible!");
					}
				}
			} catch (Exception e) {
				//wrong image size, shouldn't happen
				genom.setFitness(0.0f);
			}	
		}
	}
}
