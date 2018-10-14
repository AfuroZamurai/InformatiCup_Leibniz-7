package tests;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import main.io.ImageLoader;
import main.io.ImageSaver;

/**
 * This class contains JUnit Tests
 * 
 * @author Jannik
 *
 */
public class IOTester {

	@Test
	public void testImageLoading() {

		BufferedImage img;

		// Test each of the supported image formats
		try {
			img = ImageLoader.loadImage("data/images/00000.png");

			Assert.assertNotNull(img);

		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			img = ImageLoader.loadImage("data/images/00000.jpeg");

			Assert.assertNotNull(img);

		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			img = ImageLoader.loadImage("data/images/00000.png");

			Assert.assertNotNull(img);

		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			img = ImageLoader.loadImage("data/images/00000.wbmp");

			Assert.assertNotNull(img);

		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			img = ImageLoader.loadImage("data/images/00000.jpg");

			Assert.assertNotNull(img);

		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		try {
			img = ImageLoader.loadImage("data/images/00000.ppm");

			Assert.assertNotNull(img);

		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	@Test
	public void testImageSaving() {
		// TODO Implement Tests

	}
}
