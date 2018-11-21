package main.evolution.ga.encoding;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.encodings.IImageEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.TrasiWebEvaluator;
import main.evolution.ga.GeneticAlgorithm;
import main.evolution.ga.cppn.CPPNGenom;
import main.utils.ImageUtil;

public class EncodingSearch extends GeneticAlgorithm<EncodingGenom> {
	
	private final IImageEncoding encoding;
	private final IClassification targetClass;
	
	private final BufferedImage original;

	public EncodingSearch(int populationSize, float targetFitness, int generationCap, IImageEncoding encoding, 
			IClassification targetClass, BufferedImage original) {
		super(populationSize, targetFitness, generationCap);
		this.encoding = encoding;
		this.targetClass = targetClass;
		this.original = original;
	}
	
	public BufferedImage getImageFromGenom(EncodingGenom genom) {
		return encoding.addToImage(original, genom.getAllParameters());
	}

	@Override
	protected void createPopulation() {
		for(int i = 0; i < populationSize; i++) {
			List<EncodingGene> genes = new ArrayList<>();
			genes.add(new EncodingGene(encoding.getParameterBatchSize()));
			EncodingGenom genom = new EncodingGenom(-1.0f, genes);
			population.addGenom(genom);
		}
		
		calculateFitness();
		System.out.println("Best confidence: " + population.getBest().getFitness());
	}

	@Override
	protected void createOffspring() {
		//List<EncodingGenom> newPopulation = new ArrayList<>();
		population.getGenoms().clear();
		createPopulation();
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
			EvaluationResult<IClassification> result;
			try {
				result = evaluator.evaluateImage(image);
				if (result != null) {
					float confidence = result.getConfidenceForClass(targetClass);
					float coverage = ImageUtil.getTransparentPercent(image);
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
