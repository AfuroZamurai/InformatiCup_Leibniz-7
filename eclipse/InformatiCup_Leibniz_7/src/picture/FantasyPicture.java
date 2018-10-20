package picture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import main.io.ImageSaver;
import picture.actions.PAction;
import picture.features.Feature;
import picture.features.PedestrianFeature;

public class FantasyPicture {

	public enum Shape {
		SQUARE, CIRCLE, TRIANGLE, OCTAGON, REV_TRIANGLE
	}

	private final int WIDTH = 64;
	private final int HEIGHT = 64;
	private final int PADDING = 1;

	private Shape shape; // shape of the sign
	private Color bgColor; // background color of the sign
	private Layout layout; // layout of the sign containing the features
	private Border border; // border of the sign

	public FantasyPicture(Shape shape, Color bgColor, Layout layout, Border border) {
		this.shape = shape;
		this.bgColor = bgColor;
		this.layout = layout;
		this.border = border;
	}

	public Shape getShape() {
		return shape;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public Layout getLayout() {
		return layout;
	}

	public Border getBorder() {
		return border;
	}

	public List<PAction> getAvailableActions() {
		return null;
	}

	// renders the fantasy picture into an actual image
	public BufferedImage createImage() {
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		//drawing the border
		switch (shape) {
		case CIRCLE:
			g.setColor(border.getColor());
			g.fillOval(PADDING, PADDING, WIDTH - 2 * PADDING, HEIGHT - 2 * PADDING);
			g.setColor(bgColor);
			g.fillOval(PADDING + border.getWidth(), PADDING + border.getWidth(),
					WIDTH - 2 * (PADDING + border.getWidth()), HEIGHT - 2 * (PADDING + border.getWidth()));
			g.setColor(Color.BLACK);
			g.drawOval(PADDING, PADDING, WIDTH - 2 * PADDING, HEIGHT - 2 * PADDING);
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

		//drawing the features
		layout.drawFeatures(g);
		
		return image;
	}

	// testing purpose
	public static void main(String[] args) {
		ArrayList<Feature> features = new ArrayList<>();
		features.add(new PedestrianFeature());
		Layout layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
		Border border = new Border(Color.RED, 6);
		FantasyPicture pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
		BufferedImage img = pic.createImage();
		String path = Paths.get(".").toAbsolutePath().normalize().toString();
		try {
			ImageSaver.saveImage(img, path);
		} catch (IOException e) {
			System.out.println("could not save image");
		}
	}

}
