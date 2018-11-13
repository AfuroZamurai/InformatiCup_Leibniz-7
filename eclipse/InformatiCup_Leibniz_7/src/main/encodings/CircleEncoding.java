package main.encodings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sun.javafx.iio.ImageStorage.ImageType;

public class CircleEncoding implements IImageEncoding {
	
	int batchSize = 6;
	
	@Override
	public BufferedImage createImage(int width, int height, float[] parameters) {

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		g.setColor(new Color(0,0,0,0));
		g.fillRect(0,0,width,height);
		
		int amount = parameters.length / batchSize;
		for (int n = 0; n < amount; n++) {
			int i = n * batchSize;

			int xPos = (int) (parameters[i] * width * 1.4f - 0.2f * width);
			int yPos = (int) (parameters[i + 1] * height * 1.4f - 0.2f * height);
			int radius = (int) (parameters[i + 2] * width / 4f);

			g.setColor(new Color(parameters[i + 3], parameters[i + 4], parameters[i + 5]));
			g.fillOval(xPos, yPos, radius, radius);
		}

		return img;
	}

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
			int radius = (int) (parameters[i + 2] * width / 2f);

			g.setColor(new Color(parameters[i + 3], parameters[i + 4], parameters[i + 5]));
			g.fillOval(xPos, yPos, radius, radius);

		}

		g.dispose();

		return image;
	}

	@Override
	public int getParameterBatchSize() {
		return batchSize;
	}

}
