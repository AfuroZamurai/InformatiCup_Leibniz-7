package main.module;

import java.awt.image.BufferedImage;

import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;

/**
 * This interface describes a module that generates new images given an initial
 * image to start with. It will iteratively generate these images trying to
 * maximize the evaluation result.
 * 
 * @author Fredo
 *
 */
public interface IModuleIterate {

	public BufferedImage generateNextImage();

	public void setEvalResult(EvaluationResult result);

	public void setInitImage(BufferedImage img, Sign sign);
}
