package main.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * This class provides functionality to load Images from the file system
 * Compatible image formats are: .bmp .jpeg .wbmp .gif .png .jpg
 * 
 * @author Jannik
 *
 */
public class ImageLoader {

	/**
	 * This methods loads an image from the file system and returns a buffered Image
	 * object
	 * 
	 * @param path
	 *            The path to the file(including file name and extension)
	 * @return An BufferedImage Object containing the image data
	 * @throws IOException
	 *             If Image could not be loaded(wrong path, no read permission)
	 */
	public static BufferedImage loadImage(String path) throws IOException {

		// Check for null parameters
		if (path == null) {
			throw new NullPointerException("Given path string is null!");
		}

		BufferedImage img;
		img = ImageIO.read(new File(path));

		//ImageIO Returns null for unsupported Formats
		if(img == null) {
			throw new IOException("Unsupported Image Format!");
		}
		
		return img;
	}

	/**
	 * This method loads all images in a folder. It will ignore directories in the
	 * folder, but will attempt to load every single file.
	 * 
	 * @param folderPath
	 *            The folder with the image files
	 * @return an ArrayList filled with BufferedImages from the folder(no guaranteed
	 *         order, but likely alphabetical)
	 * @throws IOException
	 *             When the folderpath is not valid or folder contains an not as
	 *             image loadable file
	 */
	public static List<BufferedImage> loadImagesFromFolder(String folderPath) throws IOException {

		List<BufferedImage> list = new ArrayList<BufferedImage>();

		// Add handle for the folder and read out filelist
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		// Check for invalid folderpath
		if (listOfFiles == null) {
			throw new IOException("Path does not lead to a valid folder");
		}

		// Attempt to load every file in folder
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				BufferedImage img = ImageIO.read(listOfFiles[i]);
				
				//ImageIO Returns null for unsupported Formats
				if(img == null) {
					throw new IOException("Unsupported Image Format!");
				}
				
				list.add(img);
			}
		}

		return list;
	}

}
