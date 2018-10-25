package tests;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import main.encodings.CircleEncoding;
import main.encodings.DirectEncoding;
import main.encodings.GridEncoding;
import main.encodings.IImageEncoding;
import main.io.ImageSaver;

public class EncodingTester {
	
	Random rand;
	
	@Test
	public void testDirectEncoding() throws IOException {
		
		rand = new Random();
		
		IImageEncoding encoding = new DirectEncoding();
		
		float[] parameters = new float[encoding.getParameterSize(64, 64)];
		
		for(int i = 0; i < parameters.length; i++) {

			parameters[i] = rand.nextFloat();
		}
		
		BufferedImage img = encoding.createImage(64, 64, parameters);
		
		ImageSaver.saveImage(img, "data/direct");
	}
	
	@Test
	public void testCircleEncoding() throws IOException {
		
		rand = new Random();
		
		IImageEncoding encoding = new CircleEncoding();
		
		float[] parameters = new float[encoding.getParameterSize(64, 64)];
		
		for(int i = 0; i < parameters.length; i++) {

			parameters[i] = rand.nextFloat();
		}
		
		BufferedImage img = encoding.createImage(64, 64, parameters);
		
		ImageSaver.saveImage(img, "data/circles");
	}
	
	@Test
	public void testGridEncoding() throws IOException {
		
		rand = new Random();
		
		IImageEncoding encoding = new GridEncoding(8,8);
		
		float[] parameters = new float[encoding.getParameterSize(64, 64)];
		
		for(int i = 0; i < parameters.length; i++) {

			parameters[i] = rand.nextFloat();
		}
		
		BufferedImage img = encoding.createImage(64, 64, parameters);
		
		ImageSaver.saveImage(img, "data/grid");
	}
	
}
