package main.encodings;

import java.awt.image.BufferedImage;

public interface IImageEncoding {

	/**
	 * This Methods creates an Image with the given dimensions using its encoding
	 * 
	 * @param width
	 *            The width of the Image to be created
	 * @param height
	 *            The height of the Image to be created
	 * @param parameters
	 *            An array of floats between 0 and 1, more parameters generally
	 *            create more complex images
	 * @return The created Image
	 * 
	 */
	public BufferedImage createImage(int width, int height, float[] parameters);

	/**
	 * This Methods creates an Image with the given parameters and adds it on top of
	 * the given image
	 * 
	 * @param original
	 *            An Image which will be the background for the new image
	 * @param parameters
	 *            An array of floats between 0 and 1, more parameters generally
	 *            create more complex images
	 * @return The result Image, which is the original with new Content on top
	 */
	public BufferedImage addToImage(BufferedImage original, float[] parameters);
	
	/**
	 * This Method returns the amount of parameters which form a unit
	 * The length of the parameters array should be a multiple of this Size
	 * for efficient use. 
	 * @return The size of an ParameterBatch
	 */
	public int getParameterBatchSize();
}
