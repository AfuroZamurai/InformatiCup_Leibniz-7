package picture;

import java.awt.Color;
import java.awt.Image;
import java.util.List;

import picture.actions.PAction;

public class FantasyPicture {

	public enum Shape {
		SQUARE, CIRCLE, TRIANGLE, OCTAGON, REV_TRIANGLE
	}
	
	private Shape shape; //shape of the sign
	private Color bg_color; //background color of the sign
	private Layout layout; //layout of the sign containing the features
	private Border border; //border of the sign
	
	public List<PAction> getAvailableActions() {
		return null;
	}
	
	//renders the fantasy picture into an actual image
	public Image createImage() {
		return null;
	}
	
}
