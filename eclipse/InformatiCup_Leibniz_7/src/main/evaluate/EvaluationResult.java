package main.evaluate;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.io.ImageLoader;

public class EvaluationResult {

	/**
	 * This enum contains all 42 classes of the Neural Network Using german names
	 * since the request returns german names as well
	 * 
	 * @author Jannik
	 *
	 */
	public static enum Sign {
		ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_20,
		ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_30,
		ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_50,
		ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_60,
		ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_70,
		ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_80,
		ENDE_DER_ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_80,
		ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_100,
		ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_120,
		UEBERHOLVERBOT_ALLE, 
		UEBERHOLVERBOT_LKW, 
		VORFAHRT, 
		VORFAHRTSSTRASSE, 
		VORFAHRT_GEWAEHREN, 
		STOP, 
		FAHRVERBOT,
		LKW_VERBOT, 
		EINFAHRT_VERBOT, 
		GEFAHRSTELLE, 
		KURVE_LINKS, 
		KURVE_RECHTS,
		DOPPELKURVE_LINKS, 
		UNEBENE_FAHRBAHN, 
		SCHLEUDERGEFAHR,
		VERENGUNG_RECHTS, 
		BAUSTELLE, 
		LICHTZEICHENANLAGE, 
		FUSSGAENGER, 
		KINDER, 
		RADVERKEHR, 
		SCHNEEGLAETTE, 
		WILDWECHSEL, 
		ENDE_ALLER_VERBOTE, 
		FAHRTRICHTUNG_RECHTS, 
		FAHRTRICHTUNG_LINKS, 
		FAHRTRICHTUNG_GERADE, 
		FAHRTRICHTUNG_GERADE_RECHTS, 
		FAHRTRICHTUNG_GERADE_LINKS, 
		FAHRTRICHTUNG_RECHTS_VORBEI, 
		FAHRTRICHTUNG_LINKS_VORBEI, 
		KREISVERKEHR, 
		ENDE_UEBERHOLVERBOT, 
		ENDE_UEBERHOLVERBOT_LKW
	}

	/**
	 * Return the name of a Sign
	 * 
	 * @param sign
	 *            the sign which name is required
	 * @return the name
	 */
	public static String getNameOfSign(Sign sign) {
		return sign.toString();
	}

	/**
	 * Returns the example image of the given sign(should give high Confidence with
	 * Evaluation)
	 * 
	 * @param sign
	 *            The Sign of which the example image is wanted
	 * @return The image of the Sign
	 * @throws IOException
	 *             When the image could not be loaded
	 */
	public static BufferedImage getExampleImage(Sign sign) throws IOException {

		int number = sign.ordinal();

		String numString = "" + number;

		if (number < 10) {
			numString = "0" + number;
		}

		BufferedImage example;

		try {
			example = ImageLoader.loadImage("data/images/000" + numString + ".ppm");
		} catch (IOException e) {

			e.printStackTrace();
			throw new IOException("The example image could not be loaded!");
		}

		return example;
	}

}
