package main.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	public enum FileExtension {
		PNG,
		GIF,
		JPG,
	}
	

	/**
	 *
	 * This method saves an given BufferedImage as an image file in the filesystem
	 * 
	 * @param img
	 *            The Image to be saved
	 * @param savePath
	 *            Path where it will be saved(including filename but no extension!)
	 * @param ext
	 *            The FileExtension used for the image
	 * @return File handle for the newly created file
	 * @throws IOException
	 *             When image could not be saved(path does not exist, no write
	 *             permissions)
	 *
	 */
	public static File saveImage(BufferedImage img, String savePath, FileExtension ext) throws IOException {

		File outputfile = new File(savePath + "." + ext.toString().toLowerCase());

		// ImageIO write returns false when image could not be saved
		boolean success = ImageIO.write(img, ext.toString().toLowerCase(), outputfile);

		if (!success) {
			throw new IOException("Could not save Image!");
		}
		
		return outputfile;
	}

	/**
	 * This method saves an given BufferedImage as an image file in the filesystem,
	 * default type is png
	 * 
	 * @param img
	 *            The Image to be saved
	 * @param savePath
	 *            Path where it will be saved(including filename but no extension!)
	 * @return File handle for the newly created file
	 * @throws IOException
	 *             When image could not be saved(path does not exist, no write
	 *             permissions)
	 */
	public static File saveImage(BufferedImage img, String savePath) throws IOException {

		File outputFile = new File(savePath + "." + FileExtension.PNG.toString().toLowerCase());

		// ImageIO write returns false when image could not be saved
		boolean success = ImageIO.write(img, FileExtension.PNG.toString().toLowerCase(), outputFile);

		if (!success) {
			throw new IOException("Could not save Image!");
		}
		
		return outputFile;
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
	 * @return List of file handles for the newly created Files
	 * @throws IOException
	 *             When images could not be saved(path does not exist, no write
	 *             permissions)
	 */
	public static List<File> saveImagesFromList(List<BufferedImage> list, String savePath, String format) throws IOException {
		
		List<File> fileList = new ArrayList<File>();
		
		for (int i = 0; i < list.size(); i++) {
			BufferedImage img = list.get(i);
			File outputFile = new File(savePath + i + "." + format);
			
			// ImageIO write returns false when image could not be saved
			boolean success = ImageIO.write(img, format, outputFile);

			if (!success) {
				throw new IOException("Could not save Image(" + i + ")!");
			}
			
			fileList.add(outputFile);
		}
		
		return fileList;
	}

}
