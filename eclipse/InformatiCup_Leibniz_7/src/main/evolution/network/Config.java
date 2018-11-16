package main.evolution.network;

/**
 * A config for a CPPN. This will hold the data about the image size and the network size.
 * 
 * @author Felix
 *
 */
public class Config {
	private final int width;
	private final int height;
	private final int minHlSize;
	private final int maxHlSize;
	
	/**
	 * Set the config parameters.
	 * 
	 * @param width width of the encoded image
	 * @param height height of the encoded image
	 * @param minHlSize lower bound for the hidden layer of the CPPN
	 * @param maxHlSize upper bound for the hidden layer of the CPPN
	 */
	public Config(int width, int height, int minHlSize, int maxHlSize) {
		super();
		this.width = width;
		this.height = height;
		this.minHlSize = minHlSize;
		this.maxHlSize = maxHlSize;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getMinHlSize() {
		return minHlSize;
	}

	public int getMaxHlSize() {
		return maxHlSize;
	}
	
}
