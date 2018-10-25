package main.encodings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sun.javafx.iio.ImageStorage.ImageType;

public class GridEncoding implements IImageEncoding {

	
	int boxWidth;
	int boxHeight;
	
	public GridEncoding(int boxWidth, int boxHeight) {
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
	}
	
	@Override
	public BufferedImage createImage(int width, int height, float[] parameters) {
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		
		int i = 0;
		for(int x = 0; x < width/boxWidth; x++) {
			for(int y = 0; y < height/boxHeight; y++) {
				
				int xPos = x*boxWidth;
				int yPos = y*boxHeight;
				
				g.setColor(new Color(parameters[i],parameters[i+1],parameters[i+2]));
				g.fillRect(xPos, yPos, boxWidth, boxHeight);
				
				i += 3;
			}
		}
		
		return img;
	}

	@Override
	public int getParameterSize(int width, int height) {
		
		return (width/boxWidth)*(height/boxHeight)*6;
	}

}
