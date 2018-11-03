package picture;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import main.evaluate.Sign;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.IEvaluator;
import main.evaluate.TrasiWebEvaluator;
import main.io.ImageSaver;
import picture.actions.PAction;

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
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// drawing the border
		switch (shape) {
		case CIRCLE:
			g.setColor(border.getColor());
			g.fillOval(PADDING, PADDING, WIDTH - 2 * PADDING, HEIGHT - 2 * PADDING);
			g.setColor(bgColor);
			g.fillOval(PADDING + border.getWidth(), PADDING + border.getWidth(),
					WIDTH - 2 * (PADDING + border.getWidth()), HEIGHT - 2 * (PADDING + border.getWidth()));
			g.setColor(Color.WHITE);
			g.drawOval(PADDING, PADDING, WIDTH - 2 * PADDING, HEIGHT - 2 * PADDING);
			break;
		case OCTAGON:
			break;
		case REV_TRIANGLE:
			break;
		case SQUARE:
			break;
		case TRIANGLE:
			int[] xPoints = new int[] {4, 60, 32};
			int[] yPoints = new int[] {52, 52, 6};
			g.setColor(bgColor);
			g.fillPolygon(xPoints, yPoints, 3);
			g.setColor(Color.WHITE);
			g.setStroke(new BasicStroke(border.getWidth() + 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawPolygon(xPoints, yPoints, 3);
			g.setColor(border.getColor());
			g.setStroke(new BasicStroke(border.getWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawPolygon(xPoints, yPoints, 3);
			break;
		default:
			break;
		}

		// drawing the features
		layout.drawFeatures(g);

		return image;
	}

	// testing purpose
	public static void main(String[] args) {
		FantasyPicture pic = FantasyPictureCollection.createFantasyPictureFromSign(Sign.BAUSTELLE);
		BufferedImage img = pic.createImage();
		String path = Paths.get(".").toAbsolutePath().normalize().toString();
		try {
			ImageSaver.saveImage(img, path);
		} catch (IOException e) {
			System.out.println("could not save image");
		}
		IEvaluator evaluator = new TrasiWebEvaluator();
		try {
			EvaluationResult<IClassification> res = evaluator.evaluateImage(img);
			System.out.println("Result: " + Sign.values()[res.getMaxClassIndex()].toString() + " Value: " + res.getMaxValue());

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
