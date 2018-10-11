package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * This class contains methods to save images to the file system
 * 
 * @author Jannik
 *
 */
public class ImageSaver {

	// Constants for the supported Image formats
	public static final String PNG = "png";
	public static final String GIF = "gif";
	public static final String jpg = "jpg";

	/**
	 * This method saves an given BufferedImage as an image file in the filesystem
	 * 
	 * @param img
	 *            The Image to be saved
	 * @param savePath
	 *            Path where it will be saved(including filename but no extension!)
	 * @param format
	 *            The format in which the file will be saved(use the constants of
	 *            this class)
	 * @throws IOException
	 *             When image could not be saved(path does not exist, no write
	 *             permissions)
	 */
	public static void saveImage(BufferedImage img, String savePath, String format) throws IOException {

		File outputfile = new File(savePath + "." + format);

		// ImageIO write returns false when image could not be saved
		boolean success = ImageIO.write(img, format, outputfile);

		if (!success) {
			throw new IOException("Could not save Image!");
		}
	}

	/**
	 * This method saves an given BufferedImage as an image file in the filesystem,
	 * default type is png
	 * 
	 * @param img
	 *            The Image to be saved
	 * @param savePath
	 *            Path where it will be saved(including filename but no extension!)
	 * @throws IOException
	 *             When image could not be saved(path does not exist, no write
	 *             permissions)
	 */
	public static void saveImage(BufferedImage img, String savePath) throws IOException {

		File outputFile = new File(savePath + "." + PNG);

		// ImageIO write returns false when image could not be saved
		boolean success = ImageIO.write(img, PNG, outputFile);

		if (!success) {
			throw new IOException("Could not save Image!");
		}
	}

	/**
	 * This method saves all Images from a List into a folder. The images will be
	 * numbered after their order in the list
	 * 
	 * @param list
	 *            The list of bufferedImages to save
	 * @param savePath
	 *            The path and filename for the files, using no extension!
	 * @param format
	 *            Format in which the images will be saved, use the constants of
	 *            this class.
	 * @throws IOException
	 *             When images could not be saved(path does not exist, no write
	 *             permissions)
	 */
	public static void saveImagesFromList(List<BufferedImage> list, String savePath, String format) throws IOException {

		for (int i = 0; i < list.size(); i++) {
			BufferedImage img = list.get(i);
			File outputFile = new File(savePath + i + "." + format);

			// ImageIO write returns false when image could not be saved
			boolean success = ImageIO.write(img, format, outputFile);

			if (!success) {
				throw new IOException("Could not save Image(" + i + ")!");
			}
		}
	}

}
