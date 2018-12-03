package main.generate;

import java.awt.image.BufferedImage;
import java.io.IOException;
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
	private final int POPULATION_SIZE = 10;
	
	/**
	 * Maximum of iterations the genetic algorithm will run
	 */
	private final int GENERATION_CAP = 25;
	
	/**
	 * Amount of the best fooling images which will survive in each generation
	 */
	private final int ELITISM = 2;
	
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
	 * Creates a generator for the given type of encoding
	 * @param encoding
	 */
	public EvoEncoderGenerator(IImageEncoding encoding) {
		this.encoding = encoding;
	}

	@Override
	public BufferedImage generateNextImage() {
		System.out.println("Generating image for gen " + searcher.currentGeneration());
		
		//set the starting image before runing the first generation
		if(started) {
			searcher.run(1);
			current = getResult();
			
			//currently here for debugging purposes
			String path = "data/results/encodingsearch/gen" + searcher.currentGeneration();
			try {
				ImageSaver.saveImage(current, path);
			} catch (IOException e) {
				System.out.println("Too bad, no saved image this time");
			}
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
			encoded = current;
		}
		
		float coverage = ImageUtil.getTransparentPercent(encoded);
		System.out.println("After generation " + searcher.currentGeneration() + ":\nconfidence: " +
							confidence + "\ncoverage: " + coverage);
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
		return null;
	}

	@Override
	public boolean isFinished() {
		return searcher.isFinished();
	}

	@Override
	public String getModuleDescription() {
		return "Dieses Verfahren verwendet einen genetischen Algorithmus, um Irrbilder"
				+ " auf Basis eines Encodings zu optimieren.";
	}

	@Override
	public BufferedImage getResult() {
		return searcher.getImageFromGenom(searcher.getBestGenom());
	}
}
