package main.evaluate;

import java.awt.image.BufferedImage;

public interface IEvaluator {

	// TODO: Change so classes are considered when getting score, currently only
	// highest score is returning

	/**
	 * Returns a score for the given image between 0 and 1, which rates how high the
	 * confidence for this image is
	 * 
	 * @param image
	 *            The image to be scored
	 * @return The score in the interval from 0 to 1, where 1 is highest confidence
	 *         is 0 is lowest confidence, or -1 for an error without exception
	 * @throws Exception
	 *             see implementing class for detailed exception causes
	 */
	public float evaluate(BufferedImage image) throws Exception;

}
