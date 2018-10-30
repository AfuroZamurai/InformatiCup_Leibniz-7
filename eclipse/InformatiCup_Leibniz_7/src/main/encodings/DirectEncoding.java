package main.encodings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class DirectEncoding implements IImageEncoding {

	int batchSize = 4;

	@Override
	public BufferedImage createImage(int width, int height, float[] parameters) {

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		int i = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < width; y++) {

				Color c = new Color(parameters[i], parameters[i + 1], parameters[i + 2], parameters[i + 3]);

				img.setRGB(x, y, c.getRGB());

				i += batchSize;
			}
		}

		return img;
	}

	@Override
	public BufferedImage addToImage(BufferedImage original, float[] parameters) {

		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);

		int i = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {

				if (i + 3 >= parameters.length) {
					break;
				}

				Color c = new Color(parameters[i], parameters[i + 1], parameters[i + 2], parameters[i + 3]);

				g.setColor(c);
				//Width must be 0 for 1x1 Pixel
				g.drawRect(x, y, 0, 1);

				i += batchSize;

			}
		}

		g.dispose();

		return image;
	}

	@Override
	public int getParameterBatchSize() {
		return batchSize;
	}

}
