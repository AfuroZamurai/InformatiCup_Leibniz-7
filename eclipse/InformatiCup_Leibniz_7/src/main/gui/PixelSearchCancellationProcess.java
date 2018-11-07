package main.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import main.IModule;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.TrasiWebEvaluator;
import main.module.IModuleIterate;
import main.module.Parameter;

/**
 * This class contains the implementation of an image manipulate algorithm. From
 * an input image, the confidence is measured. Then a group of pixels is set to
 * black and a new confidence is measured. If the new confidence is higher, the
 * pixels in the output image are set to black otherwise white.
 * 
 * @author Dorian
 *
 */

public class PixelSearchCancellationProcess implements IModuleIterate {

	final int IMAGEHEIGHT = 64;
	final int IMAGEWIDTH = 64;
	int filter;
	final double ERROR_TOLERANCE = 0.0000005;
	float newConfidenceValue = 0;
	double percent;

	IClassification imageClass;
	BufferedImage inputImage, initialImage;
	ImageIcon icon2;
	BufferedImage outputImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
	float confidence;
	Controller controller;
	boolean isFinished = false;
	boolean isFirstStep = true;

	private EvaluationResult<IClassification> er;
	private int i, j;
	Color[] colorArray;

	private Parameter filterParameter = new Parameter("Filtergröße", "Größe des Filters (Wert zwischen 1 und 64)",
			16);

	/**
	 * Constructor
	 * 
	 * @param imageClass
	 *            meaning of the input image
	 * @see Sign
	 */
	public PixelSearchCancellationProcess(Controller controller) {
		this.controller = controller;
		colorArray = new Color[filter * filter];
	}

	/**
	 * This method get an Image and generate an new one.
	 * 
	 * @param input
	 *            a image that get changed
	 * @return new image
	 * @see BufferedImage
	 */

	public BufferedImage generateImage(BufferedImage input) {

		inputImage = input;

		Color BLACK = new Color(0, 0, 0);
		Color WHITE = new Color(255, 255, 255);
		Color RED = new Color(255, 0, 0);
		Color GREEN = new Color(0, 255, 0);
		Color BLUE = new Color(0, 0, 255);

		TrasiWebEvaluator twb = new TrasiWebEvaluator();
		EvaluationResult<IClassification> er;
		try {

			er = twb.evaluateImage(inputImage);
			confidence = er.getConfidenceForClass(imageClass); // initialized confidence
		} catch (Exception e) {
			e.printStackTrace();
		}

		// set one pixel to black and get the confidence value, then set the pixel to
		// its original color
		for (int i = 0; i < IMAGEHEIGHT; i += filter) {
			for (int j = 0; j < IMAGEWIDTH; j += filter) {
				percent = (((((double) (i * IMAGEHEIGHT) / (double) filter) + (double) j))
						/ ((double) (IMAGEHEIGHT * IMAGEWIDTH) / (double) filter) * 100);
				System.out.print("\nEs dauert noch "
						+ ((int) ((double) (IMAGEHEIGHT * IMAGEWIDTH) / (double) (filter * filter)
								- (((double) (i * IMAGEHEIGHT) / (double) filter) + (double) j) / filter))
						+ " Sekunden(");
				System.out.println((int) percent + "%)\n");

				Color[] colorArray = new Color[filter * filter];

				for (int k = 0; k < filter * filter; k++) { // ändern der einfärbung und speichern der echten farbe
					int x = k % filter;
					int y = k / filter;

					if (j + x < IMAGEWIDTH && i + y < IMAGEHEIGHT) {
						colorArray[k] = new Color(inputImage.getRGB(j + x, i + y));
						inputImage.setRGB(j + x, i + y, BLACK.getRGB());
					}
				}

				try {

					er = twb.evaluateImage(inputImage);
					newConfidenceValue = er.getConfidenceForClass(imageClass); // get confidence from InputImage with changed
																		// Pixel

					Thread.sleep(950);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (int k = 0; k < filter * filter; k++) { // set the Pixel for the outputImage and set the Pixel in
															// inputImage back

					int x = k % filter;
					int y = k / filter;

					if (j + x < IMAGEWIDTH && i + y < IMAGEHEIGHT) {
						if (newConfidenceValue > confidence) {
							outputImage.setRGB(j + x, i + y, BLACK.getRGB());
						} else {
							outputImage.setRGB(j + x, i + y, WHITE.getRGB());
						}
						inputImage.setRGB(j + x, i + y, colorArray[k].getRGB());

					}

				}

			}
		}

		return outputImage;
	}

	@Override
	public BufferedImage generateNextImage() {
		if (!isFirstStep) {
			percent = (((((double) (i * IMAGEHEIGHT) / (double) filter) + (double) j))
					/ ((double) (IMAGEHEIGHT * IMAGEWIDTH) / (double) filter) * 100);
			System.out
					.print("\nEs dauert noch "
							+ ((int) ((double) (IMAGEHEIGHT * IMAGEWIDTH) / (double) (filter * filter)
									- (((double) (i * IMAGEHEIGHT) / (double) filter) + (double) j) / filter))
							+ " Sekunden(");
			System.out.println((int) percent + "%)\n");

			for (int k = 0; k < filter * filter; k++) { // ändern der einfärbung und speichern der echten farbe
				int x = k % filter;
				int y = k / filter;

				if (j + x < IMAGEWIDTH && i + y < IMAGEHEIGHT) {
					// colorArray[k] = new Color(inputImage.getRGB(j + x, i + y));
					inputImage.setRGB(j + x, i + y, Color.BLACK.getRGB());
				}
			}

			if (i >= IMAGEHEIGHT - filter && j >= IMAGEWIDTH - filter) {
				isFinished = true;
				return outputImage;
			}
		}

		return inputImage;
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		er = result;
		if (isFirstStep) {
			confidence = er.getConfidenceForClass(imageClass);
			isFirstStep = false;
		} else {
			newConfidenceValue = er.getConfidenceForClass(imageClass);

			for (int k = 0; k < filter * filter; k++) { // set the Pixel for the outputImage and set the Pixel in
				// inputImage back

				int x = k % filter;
				int y = k / filter;

				if (j + x < IMAGEWIDTH && i + y < IMAGEHEIGHT) {
					if (newConfidenceValue > confidence) {
						outputImage.setRGB(j + x, i + y, Color.BLACK.getRGB());
					} else {
						outputImage.setRGB(j + x, i + y, Color.WHITE.getRGB());
					}
					// inputImage.setRGB(j + x, i + y, colorArray[k].getRGB());
					inputImage = copyImage(initialImage);

				}

			}

			if (!isFinished) {
				if (j < IMAGEWIDTH - filter) {
					j += filter;
				} else {
					j = 0;
					i += filter;
				}
			}
		}
	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {
		i = 0;
		j = 0;
		initialImage = img;
		inputImage = copyImage(img);
		this.imageClass = imageClass;
		isFinished = false;
		isFirstStep = true;
		filter = filterParameter.getIntValue();
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

	@Override
	public List<Parameter> getParameterList() {
		ArrayList<Parameter> list = new ArrayList<>();
		list.add(filterParameter);
		return list;
	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}
}
