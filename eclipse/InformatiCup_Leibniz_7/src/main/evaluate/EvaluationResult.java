package main.evaluate;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;

import main.io.ImageLoader;
import javax.json.*;

/**
 * This class handles the Result returned by an Evaluation
 * 
 * @author Jannik
 *
 */
public class EvaluationResult<T extends IClassification> {

	/**
	 * Contains the confidence scores for each class
	 */
	public float[] scores;

	/**
	 * Creates an EvaluationResult from the json String of the Response
	 * 
	 * @param scores
	 *            An array of confidence values, one for each Imageclass
	 * @throws Exception
	 *             When Json could not be parsed
	 */
	public EvaluationResult(float[] scores) throws Exception {

		this.scores = scores;

	}

	/**
	 * 
	 * Returns the confidence of the given sign class
	 * 
	 * @param imageClass
	 *            The Imageclass from that the confidence is requested 
	 * @return The confidence between 0 and 1
	 */
	public float getConfidenceForClass(T imageClass) {

		return scores[imageClass.getOrdinal()];
	}

	/**
	 * 
	 * This method returns the maximum confidence of all classes
	 * 
	 * @return the maximum confidence score as an float between 1 and 0
	 */
	public float getMaxValue() {

		int maxIndex = this.getMaxClassIndex();

		return scores[maxIndex];
	}

	/**
	 * This Method returns index of the imageClass with the highest confidence out of this
	 * Evaluation
	 * 
	 * @return The index of the class with the highest confidence
	 */
	public int getMaxClassIndex() {
		
		int maxIndex = 0;

		for (int i = 1; i < scores.length; i++) {
			if (scores[maxIndex] < scores[i]) {
				maxIndex = i;
			}
		}

		return maxIndex;
		
	}
}
