package main.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import main.evaluate.EvaluationResult;

/**
 * A Module that alters an image by painting random circles with random colors
 * on the picture. It uses a greedy approach to not drop the confidence below 90
 * percent.
 * 
 * @author Fredo
 *
 */
public class SimpleIterationModule implements IModuleIterate {

	private BufferedImage currentImage;
	private EvaluationResult lastResult;
	private BufferedImage lastImage;

	@Override
	public BufferedImage generateNextImage() {
		Graphics g = currentImage.getGraphics();
		int red = ThreadLocalRandom.current().nextInt(256);
		int green = ThreadLocalRandom.current().nextInt(256);
		int blue = ThreadLocalRandom.current().nextInt(256);
		g.setColor(new Color(red, green, blue));
		int x = ThreadLocalRandom.current().nextInt(65);
		int y = ThreadLocalRandom.current().nextInt(65);
		int radius = ThreadLocalRandom.current().nextInt(5, 20);
		g.fillOval(x, y, radius, radius);
		return currentImage;
	}

	@Override
	public void setEvalResult(EvaluationResult result) {
		float res = result.getMaxValue();
		if (lastResult != null) {
			if (res < 0.9f || res < lastResult.getMaxValue() - 0.01f) {
				currentImage = copyImage(lastImage);
			} else {
				lastImage = copyImage(currentImage);
			}
		}
		this.lastResult = result;
	}

	@Override
	public void setInitImage(BufferedImage img) {
		this.currentImage = img;
		this.lastImage = copyImage(currentImage);
	}

	private BufferedImage copyImage(BufferedImage source) {
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}
}
