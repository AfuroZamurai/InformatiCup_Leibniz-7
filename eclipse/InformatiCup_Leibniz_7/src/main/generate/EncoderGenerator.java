package main.generate;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.encodings.IImageEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.io.ImageSaver;
import main.utils.ImageUtil;

public class EncoderGenerator implements IGenerator {

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

	public EncoderGenerator(IImageEncoding encoding) {
		this.encoding = encoding;

		int parameterAmount = this.encoding.getParameterBatchSize() * 6;

		this.parameters = new float[parameterAmount];

		rand = new Random();
	}

	@Override
	public BufferedImage generateNextImage() {

		float coverage = ImageUtil.getTransparentPercent(current);

		float newCoverage = 0;

		while (newCoverage <= coverage && coverage < 1.0f) {
			for (int i = 0; i < parameters.length; i++) {
				parameters[i] = rand.nextFloat();
			}

			BufferedImage newImg = ImageUtil.drawOnTop(current,
					this.encoding.createImage(original.getWidth(), original.getHeight(), parameters));

			newCoverage = ImageUtil.getTransparentPercent(newImg);
		}

		current = ImageUtil.drawOnTop(current, this.encoding.createImage(original.getWidth(), original.getHeight(), parameters));

		return ImageUtil.drawOnTop(original, current);
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {

		float coverage = ImageUtil.getTransparentPercent(current);

		System.out.println("Coverage: " + coverage);

		float score = result.getConfidenceForClass(targetClass);

		if (score > optimalScore) {
			optimalScore = score;
			optimal = current;
		} else {
			float drop = optimalScore - score;

			if (drop > 0.1) {
				current = last;
			} else {
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
		this.current = this.encoding.createImage(original.getWidth(), original.getHeight(), new float[] {});
		this.optimal = this.encoding.createImage(original.getWidth(), original.getHeight(), new float[] {});
		this.last = this.encoding.createImage(original.getWidth(), original.getHeight(), new float[] {});
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
		return ImageUtil.drawOnTop(original, last);
	}
}
