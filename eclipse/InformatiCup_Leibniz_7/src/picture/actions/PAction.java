package picture.actions;

import picture.FantasyPicture;

public interface PAction {

	//creates a new picture by applying this action to a picture
	public FantasyPicture applyToPicture(FantasyPicture picture);
	
}
