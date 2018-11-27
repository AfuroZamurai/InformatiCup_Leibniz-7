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

public class EvoEncoderGenerator implements IGenerator {
	
	private final int POPULATION_SIZE = 10;
	private final int GENERATION_CAP = 25;
	private final float TARGET_FITNESS = 0.9f;
	
	private EncodingSearch searcher;
	private IImageEncoding encoding;
	private IClassification targetClass;
	
	private BufferedImage current;
	private boolean started = false;
	
	public EvoEncoderGenerator(IImageEncoding encoding) {
		this.encoding = encoding;
	}

	@Override
	public BufferedImage generateNextImage() {
		System.out.println("Generating image for gen " + searcher.currentGeneration());
		if(started) {
			searcher.run(1);
			current = getResult();
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
		current = img;
		targetClass = imageClass;
		searcher = new EncodingSearch(POPULATION_SIZE, TARGET_FITNESS, GENERATION_CAP, encoding, imageClass, img);
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
		return "Genetic algorithm using encoding to create falsely detected images";
	}

	@Override
	public BufferedImage getResult() {
		return searcher.getImageFromGenom(searcher.getBestGenom());
	}
}
