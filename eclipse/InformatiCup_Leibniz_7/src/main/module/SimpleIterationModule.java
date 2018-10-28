package main.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;

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
	private BufferedImage oldMask;
	private BufferedImage newMask;
	private Sign targetSign;
	private final float DIVERSION_CHANCE = 0.2f;
	

	@Override
	public BufferedImage generateNextImage() {
		Graphics2D g = currentImage.createGraphics();
		Graphics2D gm = newMask.createGraphics();
		boolean isDone = false;
		int x = 0, y = 0, radius = 0, i = 0;
		boolean diversion_true = false;
		
		while (!isDone) {
			i++;
			x = ThreadLocalRandom.current().nextInt(65);
			y = ThreadLocalRandom.current().nextInt(65);
			radius = ThreadLocalRandom.current().nextInt(8, 30);
			
			gm.setColor(Color.BLACK);
			gm.fillOval(x, y, radius, radius);
			
			if (bufferedImagesEqual(oldMask, newMask)) {
				float diversionDraw = ThreadLocalRandom.current().nextFloat();
				if (diversionDraw < DIVERSION_CHANCE) {
					isDone = true;
					diversion_true = true;
				}
			} else {
				isDone = true;
			}
		}
		//debugging purpose
		//System.out.println("Number of draws: " + i + "; Diversion: " + diversion_true);
		
		oldMask = copyImage(newMask);
		
		int red = ThreadLocalRandom.current().nextInt(256);
		int green = ThreadLocalRandom.current().nextInt(256);
		int blue = ThreadLocalRandom.current().nextInt(256);
		g.setColor(new Color(red, green, blue));
		g.fillOval(x, y, radius, radius);
		return currentImage;
	}

	@Override
	public void setEvalResult(EvaluationResult result) {
		float res = result.getConfidenceForSign(targetSign);
		if (lastResult != null) {
			if (res < 0.9f || res < lastResult.getConfidenceForSign(targetSign) - 0.01f) {
				currentImage = copyImage(lastImage);
			} else {
				lastImage = copyImage(currentImage);
			}
		}
		this.lastResult = result;
	}

	@Override
	public void setInitImage(BufferedImage img, Sign sign) {
		this.currentImage = img;
		this.lastImage = copyImage(currentImage);
		this.targetSign = sign;
		
		//init oldMask and newMask with plain white image
		oldMask = copyImage(img);
		Graphics2D g = oldMask.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		newMask = copyImage(oldMask);
	}

	private BufferedImage copyImage(BufferedImage source) {
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}

	boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
		if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
			for (int x = 0; x < img1.getWidth(); x++) {
				for (int y = 0; y < img1.getHeight(); y++) {
					if (img1.getRGB(x, y) != img2.getRGB(x, y))
						return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
}
