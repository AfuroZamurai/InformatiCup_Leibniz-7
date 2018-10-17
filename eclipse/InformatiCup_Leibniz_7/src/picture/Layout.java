package picture;

import java.awt.Graphics2D;
import java.util.ArrayList;

import picture.features.Feature;

/**
 * The Layout specifies which features are shown on the sign and how they are
 * laid out.
 * 
 * @author Fredo
 *
 */
public class Layout {

	/**
	 * The LayoutType specifies how the features are arranged on the sign.
	 */
	public enum LayoutType {
		SINGLEFEATURE, // one feature in the middle of the sign
		ONE_FEATURE_AND_TEXT // one feature and a text underneath
	}

	private LayoutType type;
	private ArrayList<Feature> features = new ArrayList<>();

	/**
	 * Draws the features on the sign using the Graphics2D object.
	 * 
	 * @param g
	 *            The Graphics2D object used to draw on the sign
	 */
	public void drawFeatures(Graphics2D g) {
		for (Feature feature : features) {
			feature.draw(g);
		}
	}
}
