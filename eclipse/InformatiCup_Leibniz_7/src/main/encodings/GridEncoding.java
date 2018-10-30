package main.encodings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sun.javafx.iio.ImageStorage.ImageType;

public class GridEncoding implements IImageEncoding {

	int boxWidth;
	int boxHeight;
	int batchSize;
	
	public GridEncoding(int boxWidth, int boxHeight) {
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
		this.batchSize = 3;
	}

	@Override
	public BufferedImage createImage(int width, int height, float[] parameters) {

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();

		int i = 0;
		for (int x = 0; x < width / boxWidth; x++) {
			for (int y = 0; y < height / boxHeight; y++) {

				int xPos = x * boxWidth;
				int yPos = y * boxHeight;

				g.setColor(new Color(parameters[i], parameters[i + 1], parameters[i + 2]));
				g.fillRect(xPos, yPos, boxWidth, boxHeight);

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

		int width = original.getWidth();
		int height = original.getHeight();
		
		int rows = width / boxWidth;
		int cols = height / boxHeight;

		int amount = parameters.length / batchSize;

		for (int n = 0; n < amount; n++) {
			int i = n * batchSize;
			int xPos = (n%cols) * boxWidth;
			int yPos = (n/cols) * boxHeight;
			
			g.setColor(new Color(parameters[i], parameters[i + 1], parameters[i + 2]));
			g.fillRect(xPos, yPos, boxWidth, boxHeight);
		}

		g.dispose();

		return image;
	}

	@Override
	public int getParameterBatchSize() {
		
		return batchSize;
	}

}
