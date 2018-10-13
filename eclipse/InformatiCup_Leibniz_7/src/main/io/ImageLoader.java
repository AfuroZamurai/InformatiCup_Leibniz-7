package main.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * This class provides functionality to load Images from the file system
 * Compatible image formats are: .bmp .jpeg .wbmp .gif .png .jpg .ppm
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

		//PPM files need special parsing method
		if (path.endsWith(".ppm")) {
			img = loadPPMImage(new File(path));
		} else {
			img = ImageIO.read(new File(path));
		}

		// ImageIO Returns null for unsupported Formats
		if (img == null) {
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

				BufferedImage img;
				
				//PPM files need special parsing method
				if (listOfFiles[i].getPath().endsWith(".ppm")) {
					img = loadPPMImage(listOfFiles[i]);
				} else {
					img = ImageIO.read(listOfFiles[i]);
				}

				// ImageIO Returns null for unsupported Formats
				if (img == null) {
					throw new IOException("Unsupported Image Format!");
				}

				list.add(img);
			}
		}

		return list;
	}

	/**
	 * Method reads in an ppm image and returns it as bufferedImage. For more Info
	 * about the ppm format, see http://netpbm.sourceforge.net/doc/ppm.html
	 * 
	 * @param inputs
	 *            Filehandle to the ppm file
	 * @return The image of the ppm file as bufferedImage
	 * @throws IOException
	 *             When file could not be read(wrong format, no permission, file not
	 *             found)
	 */
	public static BufferedImage loadPPMImage(File input) throws IOException {

		DataInputStream in = new DataInputStream(new FileInputStream(input));

		// First line contains ppm version specification
		String version = in.readLine();
		if(!(version.equals("P6"))) {
			in.close();
			throw new IOException("Wrong PPM version, only P6 are supported");
		}
		
		// Second line contains width and height of image
		String[] dimensions = in.readLine().trim().split(" ");
		int w = Integer.parseInt(dimensions[0]);
		int h = Integer.parseInt(dimensions[1]);
		
		// Third line contains the maxval for the rgb values(usually 255)
		int maxVal = Integer.parseInt(in.readLine());

		// Create empty image with correct dimensions, uses 1 Byte for R,G and B
		// respecively
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

		// Read each pixel individually
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int r = in.read();
				int g = in.read();
				int b = in.read();

				// Using the color Class to transform r,g and b values into single rgb integer
				Color rgb = new Color(r, g, b);
				output.setRGB(x, y, rgb.getRGB());
			}
		}
		in.close();

		return output;
	}

}
