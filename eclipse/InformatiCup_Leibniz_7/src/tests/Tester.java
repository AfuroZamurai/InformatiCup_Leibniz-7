package tests;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import main.ImageLoader;

/**
 * This class contains JUnit Tests
 * @author Jannik
 *
 */
public class Tester {
	
	
	@Test
	public void testImageLoading() 
	{
		// Try every image format
		try {
			ImageLoader.loadImage("data/images/00000.png");
		} catch (IOException e) {
			Assert.assertTrue(false);
		}
		
		try {
			ImageLoader.loadImage("data/images/00000.jpeg");
		} catch (IOException e) {
			Assert.assertTrue(false);
		}
		
		try {
			ImageLoader.loadImage("data/images/00000.png");
		} catch (IOException e) {
			Assert.assertTrue(false);
		}
		
		try {
			ImageLoader.loadImage("data/images/00000.wbmp");
		} catch (IOException e) {
			Assert.assertTrue(false);
		}
		
		try {
			ImageLoader.loadImage("data/images/00000.jpg");
		} catch (IOException e) {
			Assert.assertTrue(false);
		}
		
		
	}
	
	@Test
	public void testImageSaving() 
	{
		//TODO Implement Tests
		
		
	}
}
