package tests;

import java.awt.image.BufferedImage;
import org.junit.Assert;
import org.junit.Test;

import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.IEvaluator;
import main.evaluate.Sign;
import main.evaluate.TrasiWebEvaluator;
import main.io.ImageLoader;

public class EvaluationTester {

	@Test
	public void testTrasiWeb() {

		BufferedImage img;

		try {
			img = ImageLoader.loadImage("data/images/00000.png");

			IEvaluator evaluator = new TrasiWebEvaluator();

			evaluator.evaluate(img);
		} catch (Exception e) {

			e.printStackTrace();
			Assert.assertTrue(false);
		}

	}

	@Test
	public void testEvaluationResult() throws Exception {

		TrasiWebEvaluator evaluator = new TrasiWebEvaluator();

		for (int i = 0; i < Sign.values().length; i++) {
			System.out.println("Testing Class: " + Sign.values()[i]);
			
			BufferedImage img = Sign.values()[i].getExampleImage();
			
			EvaluationResult<IClassification> result = evaluator.evaluateImage(img);
			
			for (float f : result.scores) {

				System.out.print((int) (f * 100) + ",");

			}
			System.out.print("\n");
			
			System.out.println("Confidence for this class: " + result.getConfidenceForClass(Sign.values()[i]));
			
			System.out.print("\n");
			System.out.print("\n");
		}

	}

}
