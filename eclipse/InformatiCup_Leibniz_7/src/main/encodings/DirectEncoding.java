package main.encodings;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.sun.javafx.iio.ImageStorage.ImageType;

public class DirectEncoding implements IImageEncoding {

	@Override
	public BufferedImage createImage(int width, int height, float[] parameters) {
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		int i = 0;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < width; y++) {
				
				Color c = new Color(parameters[i],parameters[i+1],parameters[i+2]);
				
				img.setRGB(x, y, c.getRGB());
				
				i += 3;
			}
		}
		
		return img;
	}

	@Override
	public int getParameterSize(int width, int height) {
		
		return width*height*3;
	}

}
