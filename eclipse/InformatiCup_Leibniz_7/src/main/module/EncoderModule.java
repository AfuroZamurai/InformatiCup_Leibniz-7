package main.module;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.encodings.IImageEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;

public class EncoderModule implements IModuleIterate {

	private Parameter exampleParam = new Parameter("Example Parameter", "Example Explanation", 1);

	BufferedImage original;
	
	BufferedImage last;
	
	BufferedImage current;
	
	BufferedImage optimal;
	
	float optimalScore;

	IClassification targetClass;

	IImageEncoding encoding;
	
	Random rand;
	
	float[] parameters;

	public EncoderModule(IImageEncoding encoding) {
		this.encoding = encoding;
		
		int parameterAmount = this.encoding.getParameterBatchSize()*6;
		
		this.parameters = new float[parameterAmount];
		
		rand = new Random();
	}

	@Override
	public BufferedImage generateNextImage() {
			
		for(int i = 0; i < parameters.length; i++) {
			parameters[i] = rand.nextFloat();
		}
		
		current = this.encoding.addToImage(current, parameters);
		
		return current;
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		
		float score = result.getConfidenceForClass(targetClass);
		
		if(score > optimalScore) {
			optimalScore = score;
			optimal = current;
		}
		else {
			float drop = optimalScore - score;
			
			if(drop > 0.1) {
				current = last;
			}
			else {
				last = current;
			}
		}
	}

	@Override
	public List<Parameter> getParameterList() {

		List<Parameter> list = new ArrayList<Parameter>();

		list.add(exampleParam);

		return list;
	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {

		this.original = img;
		this.current = img;
		this.optimal = img;
		this.last = img;
		this.targetClass = imageClass;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public String getModuleDescription() {
		return "EncoderModule";
	}

	@Override
	public BufferedImage getResult() {
		return last;
	}

}
