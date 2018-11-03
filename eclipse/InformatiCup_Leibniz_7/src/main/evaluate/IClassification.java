package main.evaluate;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface IClassification {

	/**
	 * This method returns an Image which represents a good example of this class
	 * 
	 * @return An BufferedImage showing a good example of this class
	 * @throws IOException
	 *             When the image could not be loaded
	 */
	public BufferedImage getExampleImage() throws IOException;

	/**
	 * Returns a String which is the name of this class
	 * 
	 * @return String representing this class
	 */
	public String getNameOfClass();

	/**
	 * Returns an integer >= 0 which is the index of this class in the array
	 * 
	 * @return The index number of this class
	 */
	public int getOrdinal();

	/**
	 * Returns an array holding all classes of this classification in Order
	 * 
	 * @return Array with all classes
	 */
	public IClassification[] getValues();

}
