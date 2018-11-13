package main.evolution;

import java.awt.image.BufferedImage;
import main.IModule;
import main.evaluate.Sign;
import main.evolution.ga.CuckooSearch;
import main.evolution.network.CPPN;
import main.evolution.network.Config;

/**
 * This class will evolve a fooling image for a given input image.
 * 
 * @author Felix
 *
 */
public class ImageEvolver implements IModule {
	
	private final int POPULATION_SIZE = 60;
	private final int GENERATION_CAP = 25;
	private final float TARGET_FITNESS = 0.9f;
	
	private CPPN net;
	
	/**
	 * Constructor which instantiates the encoding network for a given image size.
	 * 
	 * @param imageWidth width of the input image
	 * @param imageHeight height of the input image
	 */
	public ImageEvolver(int imageWidth, int imageHeight) {
		this.net = new CPPN(0, 0, new Config(imageWidth, imageHeight, 0, 50));
	}

	@Override
	public BufferedImage generateImage(BufferedImage input) {
		CuckooSearch searcher = new CuckooSearch(this.net, POPULATION_SIZE, TARGET_FITNESS, 
				GENERATION_CAP, Sign.VORFAHRT);
		return searcher.searchForImage();
	}
	
}
