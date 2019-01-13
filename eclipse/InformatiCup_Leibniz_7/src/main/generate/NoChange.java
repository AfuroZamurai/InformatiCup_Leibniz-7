package main.generate;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;

/**
 * A simple Implementation of IGenerator, 
 * simply always returns the Original Image.
 * Contains mostly stubs.
 * @author Jannik
 *
 */
public class NoChange implements IGenerator {

	BufferedImage image;
	
	@Override
	public BufferedImage generateNextImage() {
		return this.image;
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {
		this.image = img;

	}

	@Override
	public List<Parameter> getParameterList() {
		return new ArrayList<Parameter>();
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public String getModuleDescription() {
		
		return "Unverändert: \n\n" + "Dieser Generator verändert das Bild nicht, sondern evaluiert es nur einmal.";
	}

	@Override
	public BufferedImage getResult() {
		return this.image;
	}

}
