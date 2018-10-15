package picture;

import java.awt.Color;

public class Border {
	
	private Color color; //Color of the border
	private int width; //border width
	
	public Border(Color color, int width) {
		this.color = color;
		this.width = width;
	}

	public Color getColor() {
		return color;
	}

	public int getWidth() {
		return width;
	}
	
}
