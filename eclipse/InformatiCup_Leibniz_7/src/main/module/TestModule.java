package main.module;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;

public class TestModule implements IModuleIterate {

	BufferedImage img;
	
	@Override
	public BufferedImage generateNextImage() {
		
		return img;
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		
		
	}

	@Override
	public List<Parameter> getParameterList() {
		
		return new ArrayList<Parameter>();
	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {
		this.img = img;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public String getModuleDescription() {
		return "TestModule";
	}

	@Override
	public BufferedImage getResult() {
		return this.img;
	}

}
