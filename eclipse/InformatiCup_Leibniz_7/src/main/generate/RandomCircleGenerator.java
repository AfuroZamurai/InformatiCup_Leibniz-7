package main.generate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;

/**
 * A Module that alters an image by painting random circles with random colors
 * on the picture. It uses a greedy approach to not drop the confidence below 90
 * percent.
 * 
 * @author Fredo
 *
 */
public class RandomCircleGenerator implements IGenerator {

	/** The image that is generated in the current step */
	private BufferedImage currentImage;

	/** The result of the evaluation of the image generated in the last step */
	private EvaluationResult<IClassification> lastResult;

	/** The image that was generated in the last step */
	private BufferedImage lastImage;

	/** The mask of the image that was generated in the last step */
	private BufferedImage oldMask;

	/** The mask of the image that is generated in the current step */
	private BufferedImage newMask;

	/** The image class whose confidence should be maximized */
	private IClassification targetClass;

	/**
	 * The chance to draw a circle over other previously drawn circles without
	 * drawing over an area that has not been drawn over before
	 */
	private Parameter diversionChance = new Parameter("Diversions Chance",
			"Die Chance einen Kreis �ber einen anderen zu zeichnen (Wert zwischen 0 und 1)", 0.1f);

	@Override
	public BufferedImage generateNextImage() {
		Graphics2D g = currentImage.createGraphics();
		Graphics2D gm = newMask.createGraphics();
		boolean isDone = false;
		int x = 0, y = 0, radius = 0;

		while (!isDone) {
			x = ThreadLocalRandom.current().nextInt(0, 65);
			y = ThreadLocalRandom.current().nextInt(0, 65);
			radius = ThreadLocalRandom.current().nextInt(8, 30);

			gm.setColor(Color.BLACK);
			gm.fillOval(x - radius, y - radius, radius, radius);

			if (bufferedImagesEqual(oldMask, newMask)) {
				float diversionDraw = ThreadLocalRandom.current().nextFloat();
				if (diversionDraw < diversionChance.getFloatValue()) {
					isDone = true;
				}
			} else {
				isDone = true;
			}
		}

		oldMask = copyImage(newMask);

		int red = ThreadLocalRandom.current().nextInt(256);
		int green = ThreadLocalRandom.current().nextInt(256);
		int blue = ThreadLocalRandom.current().nextInt(256);
		g.setColor(new Color(red, green, blue));
		g.fillOval(x - radius, y - radius, radius, radius);
		return currentImage;
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		float res = result.getConfidenceForClass(targetClass);
		if (lastResult != null) {
			if (res < 0.9f || res < lastResult.getConfidenceForClass(targetClass) - 0.01f) {
				currentImage = copyImage(lastImage);
			} else {
				lastImage = copyImage(currentImage);
			}
		}
		this.lastResult = result;
	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {
		this.currentImage = img;
		this.lastImage = copyImage(currentImage);
		this.targetClass = imageClass;

		// init oldMask and newMask with plain white image
		oldMask = copyImage(img);
		Graphics2D g = oldMask.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		newMask = copyImage(oldMask);
	}

	/**
	 * Copies the content of a BufferedImage into a new BufferedImage object.
	 * 
	 * @param source
	 *            The BufferedImage whose content is copied
	 * @return The copy of the source image
	 */
	private BufferedImage copyImage(BufferedImage source) {
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}

	/**
	 * Compares two BufferedImages and evaluates whether their content is equal or
	 * not.
	 * 
	 * @param img1
	 *            The first image
	 * @param img2
	 *            The second image
	 * @return A boolean indicating whether the images are equal or not
	 */
	private boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
		if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
			for (int x = 0; x < img1.getWidth(); x++) {
				for (int y = 0; y < img1.getHeight(); y++) {
					if (img1.getRGB(x, y) != img2.getRGB(x, y))
						return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	@Override
	public List<Parameter> getParameterList() {
		ArrayList<Parameter> list = new ArrayList<>();

		list.add(diversionChance);

		return list;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public String getModuleDescription() {
		return "Zufallskreise: \n\n"
				+ "Dieser Generator malt zuf�llig Kreise �ber das Eingabebild. "
				+ "Wenn die Konfidenz dabei zu stark oder"
				+ " unter 90% sinkt, dann wird der Kreis entfernt und ein neuer "
				+ "gezeichnet. Der Generator benutzt dabei eine Heuristik, durch die "
				+ "er versucht Kreise �ber eine noch unbemalte Fl�che zu zeichnen, "
				+ "um das Bild schneller zu f�llen. Die Chance trotzdem einen Kreis "
				+ "zu zeichen kann �ber den Parameter \"Diversions Chance\" eingestellt "
				+ "werden."
				+ "";
	}

	@Override
	public BufferedImage getResult() {
		return currentImage;
	}
}
