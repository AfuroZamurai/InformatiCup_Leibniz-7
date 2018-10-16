package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import main.evolution.ImageEvolver;
import main.io.ImageLoader;
import main.io.ImageSaver;

public class Main {
	
	private final static int WIDTH = 64;
	private final static int HEIGHT = 64;

	public static void main(String[] args) {

		System.out.print("Hello InformatiCup!");
		System.out.println("Testing the evolutionary algorithm...");
		System.out.println("1/3: Loading the image...");
		File imageFile = new File("data/images/00000.ppm");
		BufferedImage input;
		try {
			input = ImageLoader.loadPPMImage(imageFile);
			
			System.out.println("2/3: Setting up the image evolver...");;
			ImageEvolver evolver = new ImageEvolver(WIDTH, HEIGHT);
			
			System.out.println("3/3: Evolving the output image...");
			BufferedImage output = evolver.generateImage(input);
			
			System.out.println("Done! Now saving the evolved image...");
			ImageSaver.saveImage(output, "data/images/output", ImageSaver.PNG);
			
			System.out.println("Thanks for believing in Leibniz-7!");
		} catch (IOException e) {
			System.out.println("Couldn't load the image!");
		}
		
		
	}

}
