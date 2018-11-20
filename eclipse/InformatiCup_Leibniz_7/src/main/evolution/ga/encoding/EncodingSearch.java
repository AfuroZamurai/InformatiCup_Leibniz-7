package main.evolution.ga.encoding;

import java.awt.image.BufferedImage;

import main.encodings.IImageEncoding;
import main.evolution.ga.GeneticAlgorithm;

public class EncodingSearch extends GeneticAlgorithm<EncodingGenom> {
	
	private final IImageEncoding encoding;

	public EncodingSearch(int populationSize, float targetFitness, int generationCap, IImageEncoding encoding) {
		super(populationSize, targetFitness, generationCap);
		this.encoding = encoding;
	}
	
	public BufferedImage getImageFromGenom(EncodingGenom genom, int width, int height) {
		return encoding.createImage(width, height, genom.getAllParameters());
	}
	
	
}
