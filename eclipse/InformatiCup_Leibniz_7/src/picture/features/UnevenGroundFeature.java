package picture.features;

import java.awt.Graphics2D;
import java.awt.Image;

public class UnevenGroundFeature implements Feature {

	private Image feature;
	
	public UnevenGroundFeature() {
		FeatureLoader loader = new FeatureLoader();
		feature = loader.loadFeature("data/features/Unebene_Fahrbahn.png");
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(feature, 0, -5, null);
	}

}
