package picture.features;

import java.awt.Graphics2D;
import java.awt.Image;

public class PedestrianFeature implements Feature {

	private Image feature;

	public PedestrianFeature() {
		FeatureLoader loader = new FeatureLoader();
		feature = loader.loadFeature("data/features/Pedestrian_smaller.png");
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(feature, 0, 0, null);
	}

}
