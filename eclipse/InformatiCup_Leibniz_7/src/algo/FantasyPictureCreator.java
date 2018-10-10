package algo;

import java.util.LinkedList;
import java.util.List;

import picture.FantasyPicture;
import picture.actions.PAction;

public class FantasyPictureCreator {

	//creates fantasy pictures given a starting picture
	public static List<FantasyPicture> createPictures(FantasyPicture startingPicture) {
		boolean creationDone = false;
		LinkedList<FantasyPicture> pictureList = new LinkedList<>();
		
		while(!creationDone) {
			for(int i = 0; i < pictureList.size(); i++) {
				FantasyPicture picture = pictureList.get(i);
				
				//applies all possible actions to the current picture and
				//adds it to the list while removing the old picture
				for(PAction action : picture.getAvailableActions()) {
					FantasyPicture newPicture =  action.applyToPicture(picture);
					
					//here: test newPicture for 75% confidence
					//if so, the add to list
					
					pictureList.add(newPicture);
				}
				
				pictureList.remove(i);
			}
		}
		
		return pictureList;
	}
}
