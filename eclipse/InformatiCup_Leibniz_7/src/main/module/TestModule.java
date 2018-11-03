package main.module;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;

public class TestModule implements IModuleIterate {

	BufferedImage img;
	
	@Override
	public BufferedImage generateNextImage() {
		
		return img;
	}

	@Override
	public void setEvalResult(EvaluationResult result) {
		
		
	}

	@Override
	public void setInitImage(BufferedImage img, Sign sign) {
		
		this.img = img;
	}

	@Override
	public List<Parameter> getParameterList() {
		
		return new ArrayList<Parameter>();
	}

}
