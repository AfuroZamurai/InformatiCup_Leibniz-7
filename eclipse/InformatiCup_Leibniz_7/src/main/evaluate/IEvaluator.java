package main.evaluate;

import java.awt.image.BufferedImage;

public interface IEvaluator {

	// TODO: Change so classes are considered when getting score, currently only
	// highest score is returning

	/**
	 * Returns a score for the given image between 0 and 1, which rates how high the
	 * confidence for this image is
	 * 
	 * Method is deprecated, use evaluateImage intead
	 * 
	 * @param image
	 *            The image to be scored
	 * @return The score in the interval from 0 to 1, where 1 is highest confidence
	 *         is 0 is lowest confidence, or -1 for an error without exception
	 * @throws Exception
	 *             see implementing class for detailed exception causes
	 */
	@Deprecated
	public float evaluate(BufferedImage image) throws Exception;

	/**
	 * Returns a score for the given image between 0 and 1, which rates how high the
	 * confidence for this image is
	 * 
	 * 
	 * @param image
	 *            The image to be scored
	 * @return An EvaltionResult object, containing an array scores, which contains
	 *         all confidence scores of all classes
	 * @throws Exception
	 *             see implementing class for detailed exception causes
	 */
	public EvaluationResult evaluateImage(BufferedImage image) throws Exception;

}
