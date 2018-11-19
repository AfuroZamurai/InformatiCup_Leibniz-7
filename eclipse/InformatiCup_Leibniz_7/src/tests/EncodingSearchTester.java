package tests;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import main.encodings.CircleEncoding;
import main.evolution.ga.encoding.EncodingSearch;
import main.io.ImageSaver;

public class EncodingSearchTester {
	
	@Test
	public void testEncodingSearch() throws IOException {
		CircleEncoding encoding = new CircleEncoding();
		EncodingSearch es = new EncodingSearch(60, 0.9f, 30, encoding);

		while(!es.isFinished()) {
			es.run(1);
			System.out.println("Current best genom has a fitness of " + es.getHighestFitness());
			BufferedImage bestImage = es.getImageFromGenom(es.getBestGenom(), 64, 64);
			ImageSaver.saveImage(bestImage, "data/results/encodingsearch/gen" + es.currentGeneration());
		}
	}
}
