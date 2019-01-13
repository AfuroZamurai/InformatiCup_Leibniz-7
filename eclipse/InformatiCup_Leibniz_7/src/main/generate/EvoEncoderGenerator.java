package main.generate;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.encodings.IImageEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evolution.ga.encoding.EncodingSearch;
import main.io.ImageSaver;
import main.utils.ImageUtil;

/**
 * Generator for a fooling image based on a genetic algorithm.
 * The genetic algorithm optimizes encodings.
 * 
 * @author Felix
 *
 */
public class EvoEncoderGenerator implements IGenerator {
	
	/**
	 * The amount of fooling images optimized in the genetic algorithm
	 */
	private final int POPULATION_SIZE;
	
	/**
	 * Maximum of iterations the genetic algorithm will run
	 */
	private final int GENERATION_CAP;
	
	/**
	 * Amount of the best fooling images which will survive in each generation
	 */
	private final int ELITISM;
	
	/**
	 * Fitness threshold after which the genetic algorithm will stop with a success
	 */
	private final float TARGET_FITNESS = 0.9f;
	
	/**
	 * The instance of the genetic algorithm used to optimize encodings
	 */
	private EncodingSearch searcher;
	
	/**
	 * The chosen encoding method
	 */
	private IImageEncoding encoding;
	
	/**
	 * The target class which the neural net should be recognizing
	 */
	private IClassification targetClass;
	
	/**
	 * The currently best image
	 */
	private BufferedImage current;
	/**
	 * Single use value to determine the very first step
	 */
	private boolean started = false;
	
	/**
	 * Parameter to let the user set the population size
	 */
	private Parameter popSize = new Parameter("Populationsgröße", "Anzahl an Genomen in einer Generation", 60);
	
	/**
	 * Parameter to let the user set the generation cap
	 */
	private Parameter genCap = new Parameter("Maximale Generationszahl", "Wie viele Durchläufe der genetische Algorithmus "
			+ "ausgeführt wird. Aktuell äquivalent zur Anzahl an Iterationen", 100);
	
	/**
	 * Parameter to let the user decide if elitism should be used
	 */
	private Parameter hasElitism = new Parameter("Elitismus", "Ja: eine Anzahl der besten Genome wird in die "
			+ "Folgegeneration übernommen", true);
	
	/**
	 * Parameter to let the user set the amount of surviving genoms per generation
	 */
	private Parameter survivors = new Parameter("Elitismus", "Anzahl an Genomen, die in die Folgegeneration übernommen werden", 2);
	
	/**
	 * Creates a generator for the given type of encoding
	 * @param encoding
	 */
	public EvoEncoderGenerator(IImageEncoding encoding) {
		this.encoding = encoding;
		this.POPULATION_SIZE = popSize.getIntValue();
		this.GENERATION_CAP = genCap.getIntValue();
		this.ELITISM = survivors.getIntValue();
	}

	@Override
	public BufferedImage generateNextImage() {
		System.out.println("Generating image for gen " + searcher.currentGeneration() + "\n");
		
		//set the starting image before running the first generation
		if(started) {
			searcher.run(1);
			current = getResult();
			
			return current;
		} else {
			started = true;
			return current;
		}
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		float confidence = result.getConfidenceForClass(targetClass);
		
		//there is no best genom before the first generation
		BufferedImage encoded;
		try {
			encoded = searcher.getEncodingImage(searcher.getBestGenom());
		} catch (Exception e) {
			System.out.println("Cannot created encoded image must use old one!");
			encoded = current;
		}
		
		float coverage = ImageUtil.getTransparentPercent(encoded);
		float fitness = searcher.getHighestFitness();
		float fitnessGenom;
		int geneLength;
		float average;
		try {
			fitnessGenom = searcher.getBestGenom().getFitness();
			geneLength = searcher.getBestGenom().getGenes().size();
			average = searcher.getAverageFitness();
		} catch (Exception e) {
			fitnessGenom = -1.0f;
			geneLength = 0;
			average = 0.0f;
		}
		
		System.out.println("After generation " + searcher.currentGeneration() + ":\nfitness: " + fitness + 
				"\nfitness from genom: " + fitnessGenom + "\nconfidence: " + confidence + "\ncoverage: " + coverage +
				"\ngene length: " + geneLength + "\nAverage Fitness: " + average);
	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {
		//we need to set the current image to the given starting image
		current = img;
		targetClass = imageClass;
		searcher = new EncodingSearch(POPULATION_SIZE, TARGET_FITNESS, GENERATION_CAP, ELITISM, encoding, 
				imageClass, img);
	}

	@Override
	public List<Parameter> getParameterList() {
		List<Parameter> params = new ArrayList<>();
		//params.add(popSize);
		//params.add(genCap);
		//params.add(survivors);
		return params;
	}

	@Override
	public boolean isFinished() {
		return searcher.isFinished();
	}

	@Override
	public String getModuleDescription() {
		return "Dieses Verfahren verwendet einen genetischen Algorithmus, um Irrbilder"
				+ " auf Basis eines Gridencodings zu optimieren. Dabei wird über das Originalbild ein Gitter gelegt"
				+ ", dessen Zellen unterschiedliche Farben zugeordnet haben. Die Genome repräsentieren eine "
				+ "Konfiguration dieses Gitters.";
	}

	@Override
	public BufferedImage getResult() {
		return searcher.getImageFromGenom(searcher.getBestGenom());
	}
}
