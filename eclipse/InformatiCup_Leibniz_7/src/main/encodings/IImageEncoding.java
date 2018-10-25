package main.encodings;

import java.awt.image.BufferedImage;

public interface IImageEncoding {

	/**
	 * This Image creates an Image with the given dimensions using its encoding
	 * 
	 * @param width
	 *            The width of the Image to be created
	 * @param height
	 *            The height of the Image to be created
	 * @param parameters
	 *            An array of floats between 0 and 1 determining the content of the
	 *            image it should contain as many floats as getParameterSize
	 *            indicates
	 * @return The created Image
	 * 
	 * @see getParameterSize
	 */
	public BufferedImage createImage(int width, int height, float[] parameters);

	/**
	 * This method returns the required size of the parameter array for the
	 * createImage method for a given size of an image
	 * 
	 * @param width
	 *            The width the image should have
	 * @param height
	 *            The height the image should have This method indicates the
	 *            required size of the parameter Array
	 * @return The required size of the parameter array
	 */
	public int getParameterSize(int width, int height);
}
