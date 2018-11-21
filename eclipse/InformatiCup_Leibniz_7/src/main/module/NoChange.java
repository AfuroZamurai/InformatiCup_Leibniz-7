package main.module;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;

public class NoChange implements IModuleIterate {

	BufferedImage image;
	
	@Override
	public BufferedImage generateNextImage() {
		// TODO Auto-generated method stub
		return this.image;
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {
		this.image = img;

	}

	@Override
	public List<Parameter> getParameterList() {
		// TODO Auto-generated method stub
		return new ArrayList<Parameter>();
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getModuleDescription() {
		// TODO Auto-generated method stub
		return "Erklärungstext zu dem ausgewählten Algorithmus:\n\nDieser Algorithmus sendet das Eingabebild\nan die künstliche Inteligenz.\n"
				+ "Das Bild ist auch wieder das Ausgabebild,\nda keine Veränderung vorgenommen wurde\n"
				+ "und die dazugeöhrige Konfidenz wird ausgegeben.";
	}

	@Override
	public BufferedImage getResult() {
		// TODO Auto-generated method stub
		return this.image;
	}

}
