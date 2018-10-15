package picture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import picture.actions.PAction;

public class FantasyPicture {

	public enum Shape {
		SQUARE, CIRCLE, TRIANGLE, OCTAGON, REV_TRIANGLE
	}

	private final int WIDTH = 64;
	private final int HEIGHT = 64;
	private final int PADDING = 1;

	private Shape shape; // shape of the sign
	private Color bg_color; // background color of the sign
	private Layout layout; // layout of the sign containing the features
	private Border border; // border of the sign

	public List<PAction> getAvailableActions() {
		return null;
	}

	// renders the fantasy picture into an actual image
	public BufferedImage createImage() {
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		switch (shape) {
		case CIRCLE:
			g.setColor(border.getColor());
			g.fillOval(PADDING, PADDING, WIDTH - 2 * PADDING, HEIGHT - 2 * PADDING);
			g.setColor(bg_color);
			g.fillOval(PADDING + border.getWidth(), PADDING + border.getWidth(),
					WIDTH - 2 * (PADDING + border.getWidth()), HEIGHT - 2 * (PADDING + border.getWidth()));
			break;
		case OCTAGON:
			break;
		case REV_TRIANGLE:
			break;
		case SQUARE:
			break;
		case TRIANGLE:
			break;
		default:
			break;
		}

		return null;
	}

}
