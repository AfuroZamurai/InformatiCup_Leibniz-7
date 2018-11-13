package main.evolution.ga;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.special.Gamma;

import main.evaluate.EvaluationResult;
import main.evaluate.Sign;
import main.evaluate.TrasiWebEvaluator;
import main.evolution.network.CPPN;
import main.utils.Evolutionhelper;

/**
 * Implementation of the cuckoo search algorithm
 * 
 * @author Felix
 *
 */
public class CuckooSearch extends GeneticAlgorithm {
	
	private CPPN net;
	private Sign target;
	
	public static final float ABANDONED_NESTS = 0.1f;
	private static final double LEVY_ALPHA = 0.01 * MAX_GENE_VALUE;
	private static final double LEVY_BETA = 3.0 / 2.0;
	private static final double LEVY_SIGMA = Math.pow((Gamma.gamma(1.0 + LEVY_BETA) * 
			Math.sin(Math.PI * LEVY_BETA / 2.0) / (Gamma.gamma((1.0 + LEVY_BETA) / 2.0) *
			LEVY_BETA * Math.pow(2, (LEVY_BETA - 1.0) / 2.0))), (1.0 / LEVY_BETA));
	
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
	 * @return a BufferedImage of the resulting fooling image
	 */
	public BufferedImage searchForImage() {
		System.out.println("Trying to create an image for the sign " + target.name());
		run();
		Genom best = getBestGenom();
		return net.createImage(best);
	}
	
	@Override
	protected void createPopulation() {
		for(int i = 0; i < populationSize; i++) {
			Genom genom = new Genom(-1.0f, net.createRandomGene(), net);
			population.addGenom(genom);
		}
		
		calculateFitness();
		System.out.println("Best confidence: " + population.getBest().getFitness());
	}
	
	private void createNewNests() {
		//create new eggs from the old population using levy flight
		List<Genom> newEggs = new ArrayList<>();
		for (Iterator<Genom> iterator = population.getGenoms().iterator(); iterator.hasNext();) {
			Genom old = iterator.next();
			Genom cuckoo = new Genom();
			cuckoo.setFitness(old.getFitness());
			cuckoo.setGenes(old.getGenes());
			cuckoo.setNet(old.getNet());
			newEggs.add(performLevyFlight(cuckoo));
		}
		
		//replace eggs with the cuckoos
		for (Iterator<Genom> iterator = newEggs.iterator(); iterator.hasNext();) {
			Genom egg = iterator.next();
			int randomNest = Evolutionhelper.randomInt(0, populationSize - 1);
			//only replace when new genom is better
			if(egg.getFitness() > population.getGenoms().get(randomNest).getFitness()) {
				population.getGenoms().set(randomNest, egg);
			}
		}
	}
	
	/**
	 * Replaces the old population with a new one. For cuckoo search this means, a levy flight will be performed
	 * on the old population. The changed genoms can now randomly replace an older and worse solution.
	 */
	@Override
	protected void createOffspring() {
		createNewNests();
	}
	
	/**
	 * Replaces a part of the worst members of the population by new genoms.
	 */
	@Override
	protected void selectSurvivors() {
		int abandoned = (int) (populationSize * ABANDONED_NESTS);
		List<Genom> curPopulation = population.getGenoms();
		Collections.sort(curPopulation);
		
		List<Genom> newNests = new ArrayList<>(abandoned);
		for(int i = 0; i < abandoned; i++) {
			curPopulation.remove(0);
			Genom replace = new Genom(-1.0f, net.createRandomGene(), net);
			newNests.add(replace);
		}
		
		calculateFitness(newNests);
		curPopulation.addAll(newNests);
	}
	
	private void calculateFitness(List<Genom> genoms) {
		TrasiWebEvaluator evaluator = new TrasiWebEvaluator();
		for(Genom genom : genoms) {
			BufferedImage image = net.createImage(genom);
			EvaluationResult result;
			try {
				result = evaluator.evaluateImage(image);
				if (result != null) {
					float fitness = result.getConfidenceForClass(target);
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
		Genom best = (genoms.get(genoms.size() - 1));
		if(best.getFitness() > population.getBest().getFitness()) {
			population.setBest(best);
		}
	}
	
	/**
	 * Performs a Levy flight on the given genom. A Levy flight is a special type of random walk.
	 * The step-lengths have a probability distribution that is heavy-tailed.
	 * 
	 * @param cuckoo the genom on which the Levy flight will be performed 
	 * @return the changed genom
	 */
	private Genom performLevyFlight(Genom cuckoo) {
		Genom egg = new Genom();
		egg.setGenes(cuckoo.getGenes());
		egg.setNet(cuckoo.getNet());
		List<Gene> eggGenes = egg.getGenes();
		
		for(int i = 0; i < eggGenes.size(); i++) {
			for(int j = 0; j < eggGenes.get(i).getValues().length; j++) {
				double u = Evolutionhelper.getNormalDistributedDouble() * LEVY_SIGMA;
				double v = Evolutionhelper.getNormalDistributedDouble();
				double stepSize = LEVY_ALPHA * u / Math.pow(Math.abs(v), 1.0 / LEVY_BETA);
				int newValue = (int) Math.abs((double) eggGenes.get(i).getValues()[j] + 
						stepSize * Evolutionhelper.getNormalDistributedDouble());
				
				if(newValue < 0) {
					newValue = 0;
				} else if (newValue > MAX_GENE_VALUE) {
					newValue = MAX_GENE_VALUE;
				}
				
				eggGenes.get(i).replaceValue(j, newValue);
			}
		}
		
		calculateFitness(egg);
		return egg;
	}
	
	private void calculateFitness(Genom genom) {
		BufferedImage image = net.createImage(genom);
		TrasiWebEvaluator evaluator = new TrasiWebEvaluator();
		EvaluationResult result;
		try {
			result = evaluator.evaluateImage(image);
			if (result != null) {
				float fitness = result.getConfidenceForClass(target);
				genom.setFitness(fitness);
			} else {
				genom.setFitness(0.0f);
				System.out.println("Evaluation currently impossible!");
			}
		} catch (Exception e) {
			//wrong image size, shouldn't happen
			genom.setFitness(0.0f);
		}	
		
		if(genom.getFitness() > population.getBest().getFitness()) {
			population.setBest(genom);
		}
	}
	
	@Override
	protected void calculateFitness() {
		List<Genom> genoms = population.getGenoms();
		TrasiWebEvaluator evaluator = new TrasiWebEvaluator();
		for(Genom genom : genoms) {
			BufferedImage image = net.createImage(genom);
			EvaluationResult result;
			try {
				result = evaluator.evaluateImage(image);
				if (result != null) {
					float fitness = result.getConfidenceForClass(target);
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
