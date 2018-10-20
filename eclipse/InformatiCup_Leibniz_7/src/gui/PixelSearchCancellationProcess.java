package gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import main.IModule;
import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;
import main.evaluate.TrasiWebEvaluator;

public class PixelSearchCancellationProcess implements IModule {

	final int IMAGEHEIGHT = 64;
	final int IMAGEWIDTH = 64;
	final int FILTER = 8;
	final double ERROR_TOLERANCE = 0.0000005;

	Sign sign;
	BufferedImage inputImage;
	ImageIcon icon2;
	BufferedImage outputImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
	float confidence;

	public PixelSearchCancellationProcess(Sign selectedItem) {
		sign = selectedItem;
	}

	@Override
	public BufferedImage generateImage(BufferedImage input) {

		inputImage = input;

		Color BLACK = new Color(0, 0, 0);
		Color WHITE = new Color(255, 255, 255);
		Color RED = new Color(255, 0, 0);
		Color GREEN = new Color(0, 255, 0);
		Color BLUE = new Color(0, 0, 255);

		TrasiWebEvaluator twb = new TrasiWebEvaluator();
		EvaluationResult er;
		try {

			er = twb.evaluateImage(inputImage);
			confidence = er.getConfidenceForSign(sign); // initialized confidence
		} catch (Exception e) {
			e.printStackTrace();
		}

		// set one pixel to black and get the confidence value, then set the pixel to
		// its original color
		for (int i = 0; i < IMAGEHEIGHT; i += FILTER) {
			for (int j = 0; j < IMAGEWIDTH; j += FILTER) {
				
				System.out.print("\nEs dauert noch "+ ((int)((double) (IMAGEHEIGHT * IMAGEWIDTH)/(double)(FILTER * FILTER) - (((double) (i * IMAGEHEIGHT) / (double) FILTER) + (double) j) / FILTER)) +" Sekunden(");
				System.out.println((int) (((((double) (i * IMAGEHEIGHT) / (double) FILTER) + (double) j)) / ((double) (IMAGEHEIGHT * IMAGEWIDTH) / (double) FILTER) * 100) + "%)\n");
				
				Color[] colorArray = new Color[FILTER * FILTER];

				for (int k = 0; k < FILTER * FILTER; k++) { // ändern der einfärbung und speichern der echten farbe
					int x = k % FILTER;
					int y = k / FILTER;

					if (j + x < IMAGEWIDTH && i + y < IMAGEHEIGHT) {
						colorArray[k] = new Color(inputImage.getRGB(j + x, i + y));
						inputImage.setRGB(j + x, i + y, BLACK.getRGB());
					}
				}

				float newConfidenceValue = 0;
				try {

					er = twb.evaluateImage(inputImage);
					newConfidenceValue = er.getConfidenceForSign(sign); // get confidence from InputImage with changed Pixel

					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (int k = 0; k < FILTER * FILTER; k++) { // set the Pixel for the outputImage and set the Pixel in inputImage back

					int x = k % FILTER;
					int y = k / FILTER;

					if (j + x < IMAGEWIDTH && i + y < IMAGEHEIGHT) {
						if (newConfidenceValue - ERROR_TOLERANCE > confidence) {
							outputImage.setRGB(j + x, i + y, BLACK.getRGB());
						} else if (newConfidenceValue + ERROR_TOLERANCE < confidence) {
							outputImage.setRGB(j + x, i + y, WHITE.getRGB());
						} else {
							outputImage.setRGB(j + x, i + y,
									new Color((int) (Math.random() * 255), (int) (Math.random() * 255),
											(int) (Math.random() * 255), (int) (Math.random() * 255)).getRGB());
						}

						inputImage.setRGB(j + x, i + y, colorArray[k].getRGB());
					}

				}
			}
		}

		return outputImage;
	}

}
