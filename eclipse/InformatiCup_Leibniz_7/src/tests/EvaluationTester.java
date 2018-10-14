package tests;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import main.evaluate.IEvaluator;
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

}
