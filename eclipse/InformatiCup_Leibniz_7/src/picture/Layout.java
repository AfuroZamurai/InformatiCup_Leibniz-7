package picture;

import java.awt.Graphics2D;
import java.util.ArrayList;

import picture.features.Feature;

/**
 * The layout specifies which features are shown on the sign and how they are
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

	/** Type of the layout */
	private LayoutType type;

	/** List of features on this sign */
	private ArrayList<Feature> features = new ArrayList<>();

	/**
	 * Creates a new layout.
	 * 
	 * @param type     The layout type
	 * @param features The list of features on the sign
	 */
	public Layout(LayoutType type, ArrayList<Feature> features) {
		this.type = type;
		this.features = features;
	}

	/**
	 * Draws the features on the sign using the Graphics2D object.
	 * 
	 * @param g The Graphics2D object used to draw on the sign
	 */
	public void drawFeatures(Graphics2D g) {
		for (Feature feature : features) {
			feature.draw(g);
		}
	}
}
