package main.evolution.network;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.Math;
import java.util.List;

import main.evolution.ga.Gene;
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
	private final int NUM_INPUT = 4;
	
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
	
	public Gene createRandomGene() {
		return new Gene(55);
	}
	
	/**
	 * Creates an image using the CPPN. 
	 * @param gene
	 * @return the created image
	 */
	public BufferedImage createImage(Genom genom) {
		int width = config.getWidth();
		int height = config.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int centerX = config.getWidth() / 2;
		int centerY = config.getHeight() / 2;
		double maxCenterDist = Math.sqrt((Math.pow(width - centerX, 2) + Math.pow(height -centerY, 2)));
		
		List<Gene> genes = genom.getGenes();
		
		//calculate network size from genom
		int networkSize = NUM_INPUT + genes.size();
		
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
				
				// activate network
				for(int neuron = 0; neuron < genes.size(); neuron++) {
					double value = 0.0;
					for(int in = 0; in < neuron + 4; in++) {
						double even = Evolutionhelper.GeneToDoubleRange(genes.get(neuron).getValues()[1 + (2 * in)], 1);
						if(even > 0) {
							double odd = Evolutionhelper.GeneToDoubleRange(genes.get(neuron).getValues()[1 + (2 * in) + 1], 1);
							value += network[in] + odd;
						}
					}
					network[neuron + 4] = applyFunction(value, genes.get(neuron).getValues()[0]);
				}
				
				int r = (int) Math.floor(Evolutionhelper.boundedIdentity(0.0, 255.0, network[networkSize - 3] * 255));
				int g = (int) Math.floor(Evolutionhelper.boundedIdentity(0.0, 255.0, network[networkSize - 2] * 255));
				int b = (int) Math.floor(Evolutionhelper.boundedIdentity(0.0, 255.0, network[networkSize - 1] * 255));
				int rgb = new Color(r, g, b).getRGB();
				image.setRGB(x, y, rgb);
			}
		}
		
		return image;
	}
	
	/**
	 * Compute the mapping of a gene value to a function and applies the function to the given argument.
	 * Right now there are the following functions:
	 * cosinus, sinus, tangens hyperbolicus, identity in the range of [0, 1], gaussian and sigmoid   
	 * 
	 * @param argument the argument the function will be applied to
	 * @param geneValue the gene value which will be used to get the function
	 * @return the value of the function applied on the argument
	 */
	private double applyFunction(double argument, int geneValue) {
		switch (Evolutionhelper.GeneToIntegerRange(geneValue, 6)) {
		case 0:
			return Math.cos(argument);
		case 1:
			return Math.sin(argument);
		case 2:
			return Math.tanh(argument);
		case 3:
			return Evolutionhelper.boundedIdentity(0.0, 1.0, argument);
		case 4:
			return Evolutionhelper.gaussian(argument);
		case 5:
		case 6:
			return Evolutionhelper.sigmoid(argument);
		default:
			// should never happen
			return argument;
		}
	}
	
	/**
	 * Save this network.
	 */
	private void saveNetwork() {
		//TODO: implement
	}
}
