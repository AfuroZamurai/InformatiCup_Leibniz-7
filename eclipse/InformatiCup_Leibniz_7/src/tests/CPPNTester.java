package tests;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.TrasiWebEvaluator;
import main.evolution.ga.cppn.CPPNGenom;
import main.evolution.network.CPPN;
import main.evolution.network.Config;
import main.io.ImageSaver;

public class CPPNTester {
	
	@Test
	public void testCPPNImageGenerator() {
		CPPN net = new CPPN(0, 0, new Config(64, 64, 0, 50));
		CPPNGenom genom = new CPPNGenom(0.0f, net.createRandomGene(), net);
		
		BufferedImage image = net.createImage(genom);
		
		TrasiWebEvaluator evaluator = new TrasiWebEvaluator();
		try {
			EvaluationResult<IClassification> result = evaluator.evaluateImage(image);
			System.out.println(result.getMaxValue() + ", confidence: " + result.getMaxValue());
		} catch (Exception e1) {
			Assert.fail("Couldn't evaluate the image");
		}
				
		try {
			ImageSaver.saveImage(image, "data/testimages/cppntest");
		} catch (IOException e) {
			Assert.fail("Couldn't save image");
		}
	}
}
