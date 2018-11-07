package main.module;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.encodings.IImageEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;

public class EncoderModule implements IModuleIterate {

	private Parameter exampleParam = new Parameter("Example Parameter", "Example Explanation", 1);

	BufferedImage original;

	IClassification targetClass;

	IImageEncoding encoding;

	public EncoderModule(IImageEncoding encoding) {
		this.encoding = encoding;
	}

	@Override
	public BufferedImage generateNextImage() {

		return original;
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {

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
		this.targetClass = imageClass;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
