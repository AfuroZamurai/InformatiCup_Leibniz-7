package tests;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import main.encodings.BoxEncoding;
import main.encodings.CircleEncoding;
import main.encodings.DirectEncoding;
import main.encodings.GridEncoding;
import main.encodings.IImageEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.Sign;
import main.io.ImageSaver;
import main.utils.ImageUtil;

public class EncodingTester {
	
	Random rand;
	
	@Test
	public void testDirectEncoding() throws IOException {
		
		rand = new Random();
		
		IImageEncoding encoding = new DirectEncoding();
		
		float[] parameters = new float[encoding.getParameterBatchSize()*10];
		
		for(int i = 0; i < parameters.length; i++) {

			parameters[i] = rand.nextFloat();
		}
		
		BufferedImage baustelle = Sign.BAUSTELLE.getExampleImage();
		
		BufferedImage img = encoding.addToImage(baustelle, parameters);
		
		ImageSaver.saveImage(img, "data/test/direct");
	}
	
	@Test
	public void testCircleEncoding() throws IOException {
		
		rand = new Random();
		
		IImageEncoding encoding = new CircleEncoding();
		
		float[] parameters = new float[encoding.getParameterBatchSize()*10];
		
		for(int i = 0; i < parameters.length; i++) {

			parameters[i] = rand.nextFloat();
		}
		
		BufferedImage baustelle = Sign.BAUSTELLE.getExampleImage();
		
		BufferedImage img = encoding.addToImage(baustelle, parameters);
		
		ImageSaver.saveImage(img, "data/test/circles");
	}
	
	@Test
	public void testGridEncoding() throws IOException {
		
		rand = new Random();
		
		IImageEncoding encoding = new GridEncoding(8,8);
		
		float[] parameters = new float[encoding.getParameterBatchSize()*10];
		
		for(int i = 0; i < parameters.length; i++) {

			parameters[i] = rand.nextFloat();
		}
		
		BufferedImage baustelle = Sign.BAUSTELLE.getExampleImage();
		
		BufferedImage img = encoding.addToImage(baustelle, parameters);
		
		System.out.println(ImageUtil.getTransparentPercent(img));
		
		ImageSaver.saveImage(img, "data/test/grid");
	}
	
	@Test
	public void testBoxEncoding() throws IOException {
		
		rand = new Random();
		
		IImageEncoding encoding = new BoxEncoding();
		
		float[] parameters = new float[encoding.getParameterBatchSize()*10];
		
		for(int i = 0; i < parameters.length; i++) {

			parameters[i] = rand.nextFloat();
		}
		
		BufferedImage baustelle = Sign.BAUSTELLE.getExampleImage();
		
		BufferedImage img = encoding.addToImage(baustelle, parameters);
		
		ImageSaver.saveImage(img, "data/test/box");
	}
	
}
