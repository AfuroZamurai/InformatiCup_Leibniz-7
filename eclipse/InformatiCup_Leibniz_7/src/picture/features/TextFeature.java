package picture.features;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class TextFeature implements Feature {

	private String text;
	private int x, y;
	private int size;

	public TextFeature(String text, int x, int y, int size) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.size = size;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		Font font = new Font("Arial", Font.BOLD, size);
		g.setFont(font);
		g.drawString(text, x, y);
	}

}
