package picture.actions;

import java.awt.Color;

import picture.FantasyPicture;

/**
 * This action changes the background color of the sign.
 * 
 * @author Bruder
 *
 */
public class ChangeBGColor implements PAction {

	private Color newBGColor;

	/**
	 * Creates a new action which changes the background color of the sign.
	 * 
	 * @param newBGColor
	 *            The new background color of the sign.
	 */
	public ChangeBGColor(Color newBGColor) {
		this.newBGColor = newBGColor;
	}

	@Override
	public FantasyPicture applyToPicture(FantasyPicture picture) {
		return new FantasyPicture(picture.getShape(), newBGColor, picture.getLayout(), picture.getBorder());
	}

}
