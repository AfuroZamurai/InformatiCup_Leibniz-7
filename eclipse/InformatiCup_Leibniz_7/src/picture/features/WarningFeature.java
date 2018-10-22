package picture.features;

import java.awt.Graphics2D;
import java.awt.Image;

public class WarningFeature implements Feature {

	private Image feature;

	public WarningFeature() {
		FeatureLoader loader = new FeatureLoader();
		feature = loader.loadFeature("data/features/Warning.png");
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(feature, 1, 1, null);
	}

}
