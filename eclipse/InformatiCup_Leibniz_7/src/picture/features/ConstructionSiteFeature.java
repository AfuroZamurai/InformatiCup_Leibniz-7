package picture.features;

import java.awt.Graphics2D;
import java.awt.Image;

public class ConstructionSiteFeature implements Feature{
	
	private Image feature;
	
	public ConstructionSiteFeature() {
		FeatureLoader loader = new FeatureLoader();
		feature = loader.loadFeature("data/features/Baustelle.png");
	}
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(feature, 0, -2, null);
	}
}
