package tests;

import java.awt.image.BufferedImage;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import main.evaluate.Sign;
import main.evaluate.TestEvaluator;
import main.generate.RecursiveSquareGenerator;
import main.io.ImageLoader;

public class RecursiveSquareTester {
	@Test
	public void testGenerator() {
		BufferedImage img;
		TestEvaluator evaluator = new TestEvaluator();
		try {
			img = ImageLoader.loadImage("data/images/00000.png");
			RecursiveSquareGenerator generator = new RecursiveSquareGenerator();
			generator.setInitImage(img, Sign.HOECHSTGESCHWINDIGKEIT_100);
			BufferedImage img2 = generator.generateNextImage();
			Assert.assertTrue(bufferedImagesEqual(img, img2));
			generator.setEvalResult(evaluator.evaluateImage(img2));
			BufferedImage img3 = generator.generateNextImage();
			Assert.assertTrue(!bufferedImagesEqual(img2, img3));
		} catch (Exception e) {
			Assert.assertTrue(false);
			e.printStackTrace();
		}
		
	}
	
	private boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
		if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
			for (int x = 0; x < img1.getWidth(); x++) {
				for (int y = 0; y < img1.getHeight(); y++) {
					if (img1.getRGB(x, y) != img2.getRGB(x, y))
						return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
}
