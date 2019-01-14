package main.encodings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Implementation of ImageEncoding. Generates Images with boxes.
 * Batchsize is 6, the numbers are interpreted this way:
 * Xpos, YPos, Size, Red, Blue, Green
 * @author Jannik
 *
 */
public class BoxEncoding implements IImageEncoding {

	int batchSize = 6;

	/**
	 * Creates BoxImage with the given Parameters in the given height and width
	 * Batchsize is 6, the numbers are interpreted this way:
	 * Xpos, YPos, Size, Red, Blue, Green
	 */
	@Override
	public BufferedImage createImage(int width, int height, float[] parameters) {

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();

		int amount = parameters.length / batchSize;
		for (int n = 0; n < amount; n++) {
			int i = n * batchSize;

			int xPos = (int) (parameters[i] * width * 1.4f - 0.2f * width);
			int yPos = (int) (parameters[i + 1] * height * 1.4f - 0.2f * height);
			int size = (int) (parameters[i + 2] * width / 4f);

			g.setColor(new Color(parameters[i + 3], parameters[i + 4], parameters[i + 5]));
			g.fillRect(xPos, yPos, size, size);
		}

		return img;
	}
	
	/**
	 * Generates Boxes and draws them ontop the given Image
	 * Batchsize is 6, the numbers are interpreted this way:
	 * Xpos, YPos, Size, Red, Blue, Green
	 */
	@Override
	public BufferedImage addToImage(BufferedImage original, float[] parameters) {

		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);

		int width = original.getWidth();
		int height = original.getHeight();

		int amount = parameters.length / batchSize;

		for (int n = 0; n < amount; n++) {
			int i = n * batchSize;

			int xPos = (int) (parameters[i] * width * 1.4f - 0.2f * width);
			int yPos = (int) (parameters[i + 1] * height * 1.4f - 0.2f * height);
			int size = (int) (parameters[i + 2] * width / 2f);

			g.setColor(new Color(parameters[i + 3], parameters[i + 4], parameters[i + 5]));
			g.fillRect(xPos, yPos, size, size);

		}

		g.dispose();

		return image;
	}

	@Override
	public int getParameterBatchSize() {
		return batchSize;
	}

}
