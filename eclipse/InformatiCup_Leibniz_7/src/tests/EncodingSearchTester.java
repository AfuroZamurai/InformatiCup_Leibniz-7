package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import main.encodings.CircleEncoding;
import main.encodings.DirectEncoding;
import main.encodings.IImageEncoding;
import main.evaluate.IClassification;
import main.evaluate.Sign;
import main.evolution.ga.encoding.EncodingGene;
import main.evolution.ga.encoding.EncodingGenom;
import main.evolution.ga.encoding.EncodingSearch;
import main.io.ImageSaver;

public class EncodingSearchTester {
	
	@Test
	public void testImageGeneration() throws IOException {
		IImageEncoding encoding = new CircleEncoding();
		BufferedImage baustelle = Sign.BAUSTELLE.getExampleImage();
		EncodingSearch es = new EncodingSearch(60, 0.9f, 30, encoding, Sign.BAUSTELLE, baustelle);
		
		List<EncodingGene> genes = new ArrayList<>();
		for(int i = 0; i < 10; i++) {
			genes.add(new EncodingGene(encoding.getParameterBatchSize()));
		}
		EncodingGenom random = new EncodingGenom(0, genes);
		
		float[] params = random.getAllParameters();
		assertEquals(params.length, 10 * encoding.getParameterBatchSize());
		
		Random rand = new Random();
		
		float[] parameters = new float[encoding.getParameterBatchSize()*10];
		
		for(int i = 0; i < parameters.length; i++) {

			parameters[i] = rand.nextFloat();
		}
		
		BufferedImage generated = es.getImageFromGenom(random);
		ImageSaver.saveImage(baustelle, "data/test/encodingsearch/original");
		ImageSaver.saveImage(generated, "data/test/encodingsearch/randomEncoded");
		
		BufferedImage img = encoding.addToImage(baustelle, parameters);
		
		ImageSaver.saveImage(img, "data/test/encodingsearch/nonGenom");
	}
	
	//@Test
	public void testEncodingSearch() throws IOException {
		CircleEncoding encoding = new CircleEncoding();
		BufferedImage baustelle = Sign.BAUSTELLE.getExampleImage();
		EncodingSearch es = new EncodingSearch(60, 0.9f, 30, encoding, Sign.BAUSTELLE, baustelle);

		while(!es.isFinished()) {
			es.run(1);
			System.out.println("Current best genom has a fitness of " + es.getHighestFitness());
			BufferedImage bestImage = es.getImageFromGenom(es.getBestGenom());
			ImageSaver.saveImage(bestImage, "data/results/encodingsearch/gen" + es.currentGeneration());
		}
	}
}
