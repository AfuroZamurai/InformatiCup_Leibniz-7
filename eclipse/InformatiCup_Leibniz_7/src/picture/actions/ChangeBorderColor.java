package picture.actions;

import java.awt.Color;

import picture.Border;
import picture.FantasyPicture;

/**
 * This Action changes the color of the border.
 * 
 * @author Fredo
 *
 */
public class ChangeBorderColor implements PAction {

	private Color newBorderColor;

	/**
	 * Creates a new Action which changes the color of the border.
	 * 
	 * @param newBorderColor
	 *            The new color of the border
	 */
	public ChangeBorderColor(Color newBorderColor) {
		this.newBorderColor = newBorderColor;
	}

	@Override
	public FantasyPicture applyToPicture(FantasyPicture picture) {
		Border newBorder = new Border(newBorderColor, picture.getBorder().getWidth());
		return new FantasyPicture(picture.getShape(), picture.getBgColor(), picture.getLayout(), newBorder);
	}

}
