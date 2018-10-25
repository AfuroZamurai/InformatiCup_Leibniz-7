package main.encodings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sun.javafx.iio.ImageStorage.ImageType;

public class CircleEncoding implements IImageEncoding {

	@Override
	public BufferedImage createImage(int width, int height, float[] parameters) {
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();  
		
		int amount = (width*height/10);
		int i = 0;
		for(int n = 0; n < amount; n++) {
				
				int xPos = (int)(parameters[i]*width*1.4f - 0.2f*width);
				int yPos = (int)(parameters[i+1]*height*1.4f - 0.2f*height);
				int radius = (int)(parameters[i+2]*width/4f);
				
				g.setColor(new Color(parameters[i+3],parameters[i+4],parameters[i+5]));
				g.fillOval(xPos, yPos, radius, radius);
				
				i += 6;
		}
		
		return img;
	}

	@Override
	public int getParameterSize(int width, int height) {
		
		return (width*height/10)*6;
	}

}
