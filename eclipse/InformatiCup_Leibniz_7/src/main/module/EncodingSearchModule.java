package main.module;

import java.awt.image.BufferedImage;
import java.util.List;

import main.encodings.IImageEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evolution.ga.encoding.EncodingSearch;
import main.utils.ImageUtil;

public class EncodingSearchModule implements IModuleIterate {
	
	private final int POPULATION_SIZE = 60;
	private final int GENERATION_CAP = 25;
	private final float TARGET_FITNESS = 0.9f;
	
	private EncodingSearch searcher;
	private IImageEncoding encoding;
	private IClassification targetClass;
	
	private BufferedImage current;
	
	public EncodingSearchModule(IImageEncoding encoding) {
		this.encoding = encoding;
	}

	@Override
	public BufferedImage generateNextImage() {
		searcher.run(1);
		current = getResult();
		return current;
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		float confidence = result.getConfidenceForClass(targetClass);
		float coverage = ImageUtil.getTransparentPercent(current);
		System.out.println("After generation " + searcher.currentGeneration() + ":\nconfidence: " +
							confidence + "\ncoverage: " + coverage);
	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {
		targetClass = imageClass;
		searcher = new EncodingSearch(POPULATION_SIZE, TARGET_FITNESS, GENERATION_CAP, encoding, imageClass);
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
		return searcher.getImageFromGenom(searcher.getBestGenom(), 64, 64);
	}
}
