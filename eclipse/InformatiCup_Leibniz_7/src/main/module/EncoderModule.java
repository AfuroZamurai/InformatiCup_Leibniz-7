package main.module;

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

		int parameterAmount = this.encoding.getParameterBatchSize() * 6;

		this.parameters = new float[parameterAmount];

		rand = new Random();
	}

	@Override
	public BufferedImage generateNextImage() {

		float coverage = getTransparentPercent(current);

		float newCoverage = 0;

		while (newCoverage <= coverage && coverage < 1.0f) {
			for (int i = 0; i < parameters.length; i++) {
				parameters[i] = rand.nextFloat();
			}

			BufferedImage newImg = drawOnTop(current,
					this.encoding.createImage(original.getWidth(), original.getHeight(), parameters));

			newCoverage = getTransparentPercent(newImg);
		}

		current = drawOnTop(current, this.encoding.createImage(original.getWidth(), original.getHeight(), parameters));

		return drawOnTop(original, current);
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {

		float coverage = getTransparentPercent(current);

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
		return drawOnTop(original, last);
	}

	private BufferedImage drawOnTop(BufferedImage background, BufferedImage image) {
		BufferedImage result = new BufferedImage(background.getWidth(), background.getHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = (Graphics2D) result.getGraphics();

		graphics.drawImage(background, 0, 0, null);
		graphics.drawImage(image, 0, 0, null);

		return result;
	}

	/**
	 * Method calculates the percentage of transparent pixels in the image
	 * @param img the image which percentage should be calculated
	 * @return an float value between 1 and 0 showing the percentage of transparent pixels
	 */
	private float getTransparentPercent(BufferedImage img) {

		int transparentPixel = findTransparentPixels(img).length / 2;

		int allPixel = img.getWidth() * img.getHeight();

		return (float) transparentPixel / (float) allPixel;
	}

	/**
	 * Method finds the coordinates of Transparent Pixels
	 * 
	 * @param img
	 *            The image which pixels will be checked
	 * @return An array of integers, with length 2*amount of transparent pixels.
	 *         Each pixel is represented through an x and y cooridinate
	 */
	private int[] findTransparentPixels(BufferedImage img) {

		List<Integer> freePixelList = new ArrayList<Integer>();

		for (int x = 0; x < current.getWidth(); x++) {
			for (int y = 0; y < current.getHeight(); y++) {

				int pixel = img.getRGB(x, y);

				int alpha = (pixel >> 24) & 0xff;

				if (alpha == 255) {
					freePixelList.add(x);
					freePixelList.add(y);
				}
			}
		}

		int[] freePixel = new int[freePixelList.size()];

		for (int i = 0; i < freePixelList.size(); i++) {
			freePixel[i] = freePixelList.get(i);
		}

		return freePixel;
	}
}
