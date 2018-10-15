package picture;

import java.awt.Graphics2D;
import java.util.ArrayList;

import picture.features.Feature;

public class Layout {

	public enum LayoutType {
		SINGLEFEATURE, //one feature in the middle of the sign
		ONE_FEATURE_AND_TEXT //one feature and a text underneath
	}
	
	private LayoutType type;
	private ArrayList<Feature> features;
	
	public void drawFeatures(Graphics2D g) {
		for (Feature feature : features) {
			feature.draw(g);
		}
	}
}
