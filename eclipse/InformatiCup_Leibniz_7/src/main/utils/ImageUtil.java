package main.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class which provides methods to retrieve information about images and
 * manipulate images.
 * 
 * @author Felix
 *
 */
public class ImageUtil {

	/**
	 * Method calculates the percentage of transparent pixels in the image
	 * 
	 * @param img
	 *            the image which percentage should be calculated
	 * @return an float value between 1 and 0 showing the percentage of transparent
	 *         pixels
	 */
	public static float getTransparentPercent(BufferedImage img) {

		int transparentPixel = findTransparentPixels(img).length / 2;

		int allPixel = img.getWidth() * img.getHeight();

		return (float) transparentPixel / (float) allPixel;
	}

	/**
	 * Method finds the coordinates of Transparent Pixels
	 * 
	 * @param img
	 *            The image which pixels will be checked
	 * @return An array of integers, with length 2*amount of transparent pixels.
	 *         Each pixel is represented through an x and y cooridinate
	 */
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

	/**
	 * This method draws one image ontop of another. The top image should have
	 * transparent pixels or else it will simply cover the background image entirely
	 * Background and image should have the same dimensions.
	 * 
	 * @param background
	 *            Image that the other will be drawn over
	 * @param image
	 *            The image that is drawn over the background
	 * @return An image which is the image drawn ontop the background
	 */
	public static BufferedImage drawOnTop(BufferedImage background, BufferedImage image) {
		BufferedImage result = new BufferedImage(background.getWidth(), background.getHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = (Graphics2D) result.getGraphics();

		graphics.drawImage(background, 0, 0, null);
		graphics.drawImage(image, 0, 0, null);

		return result;
	}
}
