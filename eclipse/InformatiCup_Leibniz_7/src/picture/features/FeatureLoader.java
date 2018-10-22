package picture.features;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import main.io.ImageLoader;

/**
 * This class is responsible for loading feature images. The ImageLoader class
 * can't be used for the features since ImageLoader can't preserve transparency.
 * 
 * @author Fredo
 *
 */
public class FeatureLoader implements ImageObserver {

	/** Indicates whether the init picture is drawn */
	private boolean imgDrawn = false;

	/** Graphics object used to draw the init picture */
	private Graphics g;

	/**
	 * Creates a new FeatureLoader and loads the init picture used for loading a
	 * feature.
	 */
	public FeatureLoader() {
		try {
			BufferedImage img = ImageLoader.loadImage("data/images/00000.png");
			g = img.getGraphics();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("failed to load init picture!");
		}
	}

	/**
	 * Loads a feature while preserving its transparency.
	 * 
	 * @param path The file path to the image that is loaded
	 * @return The Image that was loaded
	 */
	public Image loadFeature(String path) {
		Image img = null;

		try {
			img = Toolkit.getDefaultToolkit().getImage(path);
			g.drawImage(img, 0, 0, this);

			while (!imgDrawn) {
				Thread.sleep(200);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not load feature!");
		}

		return img;
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		if (infoflags == 32) {
			imgDrawn = true;
			return false;
		}
		return true;
	}

}
