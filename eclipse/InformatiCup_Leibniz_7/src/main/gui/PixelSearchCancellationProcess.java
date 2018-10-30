package main.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import main.IModule;
import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;
import main.evaluate.TrasiWebEvaluator;

/**
 * This class contains the implementation of an image manipulate algorithm. 
 * From an input image, the confidence is measured. Then a group of pixels is set to
 * black and a new confidence is measured. If the new confidence is higher, the
 * pixels in the output image are set to black otherwise white.
 * 
 * @author Dorian
 *
 */

public class PixelSearchCancellationProcess implements IModule {

	final int IMAGEHEIGHT = 64;
	final int IMAGEWIDTH = 64;
	int filter = 16;
	final double ERROR_TOLERANCE = 0.0000005;
	float newConfidenceValue = 0;
	double percent;

	Sign sign;
	BufferedImage inputImage;
	ImageIcon icon2;
	BufferedImage outputImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
	float confidence;
	Controller controller;

	/**
	 * Constructor
	 * 
	 * @param sign
	 *            meaning of the input image
	 * @see Sign
	 */
	public PixelSearchCancellationProcess(Sign sign, Controller controller, int filter) {
		this.sign = sign;
		this.controller = controller;
		this.filter = filter;
	}

	/**
	 * This method get an Image and generate an new one.
	 * 
	 * @param input
	 *            a image that get changed
	 * @return new image
	 * @see BufferedImage
	 */
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
		for (int i = 0; i < IMAGEHEIGHT; i += filter) {
			for (int j = 0; j < IMAGEWIDTH; j += filter) {
				percent = (((((double) (i * IMAGEHEIGHT) / (double) filter) + (double) j)) / ((double) (IMAGEHEIGHT * IMAGEWIDTH) / (double) filter) * 100);
				System.out.print("\nEs dauert noch "+ ((int)((double) (IMAGEHEIGHT * IMAGEWIDTH)/(double)(filter * filter) - (((double) (i * IMAGEHEIGHT) / (double) filter) + (double) j) / filter)) +" Sekunden(");
				System.out.println((int)percent + "%)\n");
				
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
					newConfidenceValue = er.getConfidenceForSign(sign); // get confidence from InputImage with changed Pixel

					Thread.sleep(950);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (int k = 0; k < filter * filter; k++) { // set the Pixel for the outputImage and set the Pixel in inputImage back

					int x = k % filter;
					int y = k / filter;

					if (j + x < IMAGEWIDTH && i + y < IMAGEHEIGHT) {
						if (newConfidenceValue > confidence) {
							outputImage.setRGB(j + x, i + y, BLACK.getRGB());
						} else  {
							outputImage.setRGB(j + x, i + y, WHITE.getRGB());
						} 
						inputImage.setRGB(j + x, i + y, colorArray[k].getRGB());
						
					}

				}
				
				   Platform.runLater(new Runnable() {
                       public void run() {
                           // Update GUI in this function
//                    	   controller.setOutputImage(SwingFXUtils.toFXImage(inputImage, null));
//                    	   controller.setConfidence(newConfidenceValue);
//                    	   controller.progressIndicator.setProgress(percent/100);
           
                       }
                   });
				
			}
		}

		return outputImage;
	}

}
