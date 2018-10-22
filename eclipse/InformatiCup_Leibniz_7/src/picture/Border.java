package picture;

import java.awt.Color;

/**
 * This class represents the border of a traffic sign. It is defined by a color
 * and a width. For example: the stop sign has a medium thick white border.
 * 
 * @author Fredo
 *
 */
public class Border {

	/**
	 * Color of the border
	 */
	private Color color; // Color of the border
	
	/**
	 * Width of the border
	 */
	private int width; // border width

	/**
	 * Creates a new Border.
	 * 
	 * @param color The color of the border
	 * @param width The width of the border
	 */
	public Border(Color color, int width) {
		this.color = color;
		this.width = width;
	}

	/**
	 * Returns the color of this border.
	 * 
	 * @return The color of the border
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns the width of this border.
	 * 
	 * @return The width of the border
	 */
	public int getWidth() {
		return width;
	}

}
