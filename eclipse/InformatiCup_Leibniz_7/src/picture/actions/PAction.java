package picture.actions;

import picture.FantasyPicture;

/**
 * This interface describes an action that can be applied to a picture. These
 * actions will change the appearance of the sign in any way. Chaining multiple
 * different actions together and applying them to a sign picture will result in
 * a sort of "fantasy" sign which will still look like a sign, but not like an
 * actual real one.
 * 
 * @author Fredo
 *
 */
public interface PAction {

	/**
	 * Applies this action to a picture which will result in a new picture with
	 * changes appearance.
	 * 
	 * @param picture
	 *            The picture this action is applied to
	 * @return The changed picture
	 */
	public FantasyPicture applyToPicture(FantasyPicture picture);

}
