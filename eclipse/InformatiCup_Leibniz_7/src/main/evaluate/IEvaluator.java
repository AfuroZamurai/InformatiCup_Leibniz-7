package main.evaluate;

import java.awt.image.BufferedImage;

/**
 * Interface for a general Entity that can return confidence scores for images
 * Examples are: 
 * - A Web API that takes image requests and returns scores
 * - A classifier on a local mashine
 * - A user interface which lets users give confidence scores for images
 * 
 * @author Jannik
 *
 */
public interface IEvaluator {

	/**
	 * Returns a score for the given image between 0 and 1, 
	 * which is the highest confidence of this image in any Imageclass
	 * 
	 * Method is deprecated, use evaluateImage instead.
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
	 * Retrieves an Evaluation for a given Image.
	 * 
	 * @param image
	 *            The image to be scored
	 * @return An EvaltionResult object containing an array scores, which contains
	 *         the confidence scores for each classes
	 * @throws Exception
	 *             see implementing class for detailed exception causes
	 */
	public EvaluationResult<IClassification> evaluateImage(BufferedImage image) throws Exception;

}
