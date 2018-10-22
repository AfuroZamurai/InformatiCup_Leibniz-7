package picture;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;
import main.evaluate.IEvaluator;
import main.evaluate.TrasiWebEvaluator;
import main.io.ImageSaver;
import picture.actions.PAction;

/**
 * This class describes the picture of a traffic sign. Its appearance can be
 * modified to create all sorts of known traffic signs and to create completely
 * new ones, so called fantasy signs.
 * 
 * @author Fredo
 *
 */
public class FantasyPicture {

	/**
	 * This enum classifies the shape of the sign.
	 */
	public enum Shape {
		SQUARE, CIRCLE, TRIANGLE, OCTAGON, REV_TRIANGLE
	}

	/** fixed width of the sign */
	private final int WIDTH = 64;

	/** fixed height of the sign */
	private final int HEIGHT = 64;

	/** fixed padding between sign and picture edge */
	private final int PADDING = 1;

	/** shape of the sign */
	private Shape shape;

	/** background color of the sign */
	private Color bgColor;

	/** layout of the sign containing the features */
	private Layout layout;

	/** border of the sign */
	private Border border;

	/**
	 * Creates a new FantasyPicture.
	 * 
	 * @param shape   Shape of the new sign
	 * @param bgColor background color of the new sign
	 * @param layout  layout of the new sign
	 * @param border  border of the new sign
	 */
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

	/**
	 * Returns a list of actions that can be applied to this picture to alter its
	 * appearance.
	 * 
	 * @return A list of available actions
	 */
	public List<PAction> getAvailableActions() {
		return null;
	}

	/**
	 * Renders the sign into an actual picture.
	 * 
	 * @return A BufferedImage of this sign
	 */
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
			int[] xPoints = new int[] { 4, 60, 32 };
			int[] yPoints = new int[] { 52, 52, 6 };
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
			EvaluationResult res = evaluator.evaluateImage(img);
			System.out.println("Result: " + res.getMaxSign().toString() + " Value: " + res.getMaxValue());

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
