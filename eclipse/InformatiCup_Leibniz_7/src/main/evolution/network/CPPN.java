package main.evolution.network;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.Math;

import main.evolution.ga.GeneticAlgorithm;
import main.evolution.ga.Genom;
import main.utils.Evolutionhelper;

/**
 * Implementation of a compositional pattern producing network (CPPN) which will indirectly encode an image.
 * 
 * @author Felix
 *
 */
public class CPPN {
	private final int inlen;
	private final int outlen;
	private final Config config;
	
	/**
	 * Instantiate the CPPN.
	 * 
	 * @param inlen number of input nodes
	 * @param outlen number of output nodes
	 * @param config config for this CPPN
	 */
	public CPPN(int inlen, int outlen, Config config) {
		this.inlen = inlen;
		this.outlen = outlen;
		this.config = config;
		
		assert config.getMaxHlSize() >= 0 && config.getMinHlSize() >= 0;
		assert config.getMaxHlSize() >= config.getMinHlSize();
	}
	
	/**
	 * Creates an image using the CPPN. 
	 * @param gene
	 * @return the created image
	 */
	public BufferedImage createImage(Genom gene) {
		int width = config.getWidth();
		int height = config.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int centerX = config.getWidth() / 2;
		int centerY = config.getHeight() / 2;
		double maxCenterDist = Math.sqrt((Math.pow(width - centerX, 2) + Math.pow(height -centerY, 2)));
		
		//TODO: calculate network size from gene
		int networkSize = 0;
		
		//set each pixel
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {	
				// euclidian distance from the current pixel to the center normalized by the maximal distance
				double centerDist = Math.sqrt((Math.pow(x - centerX, 2) + Math.pow(y -centerY, 2))) / maxCenterDist;
				
				double[] network = new double[networkSize];
				network[0] = 1.0; // bias
				network[1] = x / width; // normalized x position
				network[2] = y / height; // normalized y position
				network[3] = centerDist; // normalized distance to center
				
				//TODO: activate network and set the r, g and b values
				
				int r = 0;
				int g = 0;
				int b = 0;
				int rgb = new Color(r, g, b).getRGB();
				image.setRGB(x, y, rgb);
			}
		}
		
		return image;
	}
	
	private double applyFunction(double argument, int geneValue) {
		switch (geneValue) {
		case 0:
			return Math.cos(argument);
		case 1:
			return Math.sin(argument);
		case 2:
			return Math.tanh(argument);
		case 3:
			return boundedIdentity(0.0, 1.0, argument);
		case 4:
			return gaussian(argument);
		case 5:
		case 6:
			return Evolutionhelper.sigmoid(argument);
		default:
			// should never happen
			return argument;
		}
	}
	
	private double boundedIdentity(double lower, double upper, double value) {
		return Math.max(lower, Math.min(value, upper));
	}
	
	private double gaussian(double value) {
		return Math.exp(-1 * (Math.pow(value, 2)) / 0.5);
	}
	
	private int transformGene(int geneValue, double range) {
		return (int) Math.floor(geneValue / GeneticAlgorithm.MAX_GENE_VALUE * range);
	}
}
