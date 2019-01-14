package main.evaluate;

import java.awt.image.BufferedImage;

/**
 * Evaluator Implementation that is only for software testing, not to be used for actual evaluation
 * @author Jannik
 *
 */
public class TestEvaluator implements IEvaluator {
	
	float eval = 0;
	
	@Override
	public float evaluate(BufferedImage image) throws Exception {
		
		eval += (1 - eval)*0.1f;
		
		return eval;
	}

	@Override
	public EvaluationResult<IClassification> evaluateImage(BufferedImage image) throws Exception {
		
		eval += (1 - eval)*0.1f;
		
		float[] scores = new float[43];
		for(int i = 0; i < scores.length; i++) {
			scores[i] = eval;
		}
		
		EvaluationResult<IClassification> result = new EvaluationResult<IClassification>(scores);
		
		return result;
	}

}
