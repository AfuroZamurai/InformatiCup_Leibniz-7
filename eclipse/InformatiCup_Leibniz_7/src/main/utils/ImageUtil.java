package main.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {
	
	public static float getTransparentPercent(BufferedImage img) {

		int transparentPixel = findTransparentPixels(img).length / 2;

		int allPixel = img.getWidth() * img.getHeight();

		return (float) transparentPixel / (float) allPixel;
	}
	
	public static int[] findTransparentPixels(BufferedImage img) {

		List<Integer> freePixelList = new ArrayList<Integer>();

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {

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
