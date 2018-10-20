package picture.features;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import main.io.ImageLoader;

public class PedestrianFeature implements Feature, ImageObserver {
	
	private BufferedImage feature;
	private Image img;
	
	public PedestrianFeature() {
		try {
			//feature = ImageLoader.loadImage("data/features/Pedestrian.png");
			img = Toolkit.getDefaultToolkit().getImage("data/features/Pedestrian.png");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not create Feature, because image could not be loaded.");
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		if (!g.drawImage(img, 0, 0, null)) System.out.println("failed");
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		return false;
	}

}
