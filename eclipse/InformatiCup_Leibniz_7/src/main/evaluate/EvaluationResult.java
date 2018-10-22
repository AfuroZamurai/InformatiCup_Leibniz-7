package main.evaluate;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;

import main.io.ImageLoader;
import javax.json.*;

/**
 * This class handles the Result returned by the Evaluation of the TrasiWebEvaluator
 * @author Jannik
 *
 */
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
		ENDE_DER_ZULAESSIGEN_HOECHSTGESCHWINDIGKEIT_80,
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
	 * Die Namen der Klassen die der WebServer zur�ckgibt
	 * TODO: Input missing names(could not be retrieved because no images reach high enough confidence)
	 */
	public static final String[] classNames = new String[] {
			"Zulässige Höchstgeschwindigkeit (20)",
			"Zulässige Höchstgeschwindigkeit (30)",
			"Zulässige Höchstgeschwindigkeit (50)",
			"Zulässige Höchstgeschwindigkeit (60)",
			"Zulässige Höchstgeschwindigkeit (70)",
			"Zulässige Höchstgeschwindigkeit (80)",
			"Ende der Geschwindigkeitsbegrenzung (80)",
			"Zulässige Höchstgeschwindigkeit (100)",
			"Zulässige Höchstgeschwindigkeit (120)",
			"Überholverbot für Kraftfahrzeuge aller Art",
			"Überholverbot für Kraftfahrzeuge mit einer zulässigen Gesamtmasse über 3,5t",
			"Einmalige Vorfahrt",
			"Vorfahrt",
			"Vorfahrt gewähren",
			"Stoppschild",
			"Verbot für Fahrzeuge aller Art",
			"Verbot für Kraftfahrzeuge mit einer zulässigen Gesamtmasse von 3,5t",
			"Verbot der Einfahrt",
			"Gefahrenstelle",
			"Kurve (links)",
			"Kurve (rechts)",
			"Doppelkurve (zunächst links)",
			"Unebene Fahrbahn",
			"Schleudergefahr bei Nässe oder Schmutz",
			"",
			"Baustelle",
			"",
			"Fußgänger",
			"",
			"Fahrradfahrer",
			"",
			"Wildwechsel",
			"Ende aller Streckenverbote",
			"Ausschließlich rechts",
			"Ausschließlich links",
			"Ausschließlich geradeaus",
			"",
			"",
			"Rechts vorbei",
			"Links vorbei",
			"Kreisverkehr",
			"Ende des Überholverbotes für Kraftfahrzeuge aller Art",
			"Ende des Überholverbotes für Kraftfahrzeuge mit einer zulässigen Gesamtmasse über 3,5t"
	};
	
	/**
	 * Contains the confidence scores for each class
	 */
	public float[] scores;
	
	/**
	 * Creates an EvaluationResult from the json String of the Response
	 * 
	 * @param result 
	 * 		The JsonString returned by the response
	 * @throws Exception When Json could not be parsed
	 */
	public EvaluationResult(String result) throws Exception	{
         
		scores = new float[43];
		
		JsonReader reader = Json.createReader(new StringReader(result));
		
		JsonArray classes = reader.readArray();
		
		for(int i = 0; i < classes.size(); i++) {
			JsonObject object = classes.getJsonObject(i);
			
			String className = object.getString("class");
			double confidence = object.getJsonNumber("confidence").doubleValue();
			
			for(int n = 0; n < classNames.length; n++) {
				if(classNames[n].equals(className)) {
					scores[n] = (float)confidence;
					break;
				}
				if(n == classNames.length -1) {
					throw new Exception("Could not find Class: " + className);
				}
			}
			
		}
	}
	
	/**
	 * 
	 * Returns the confidence of the given sign class
	 * 
	 * @param s 
	 * 		The Sign class
	 * @return The confidence between 0 and 1
	 */
	public float getConfidenceForSign(Sign s) {
		
		return scores[s.ordinal()];
	}
	
	/**
	 * 
	 * This method returns the sign-class with the highest confidence
	 * 
	 * @return The Sign which the result has the highest confidence in
	 *  		for multiple maximum signs it will result the first in 
	 *  		the order of the signs
	 */
	public Sign getMaxSign() {
		
		int maxIndex = 0;
		
		for(int i = 1; i < scores.length; i++) {
			if(scores[maxIndex] < scores[i]) {
				maxIndex = i;
			}
		}
		
		return Sign.values()[maxIndex];
	}
	
	/**
	 * 
	 * This method returns the maximum confidence of all classes
	 * 
	 * @return the maximum confidence score as an float between 1 and 0
	 */
	public float getMaxValue() {
		
		int maxIndex = 0;
		
		for(int i = 1; i < scores.length; i++) {
			if(scores[maxIndex] < scores[i]) {
				maxIndex = i;
			}
		}
		
		return scores[maxIndex];
	}
	
	/**
	 * 
	 * For a given class name(from Response) returns the corresponding Sign
	 * 
	 * @param name Name of Class
	 * @return The Sign it belongs to
	 * @throws Exception name is not found in list
	 */
	public static Sign mapClassNameToSign(String name) throws Exception {
		
		for(int n = 0; n < classNames.length; n++) {
			if(classNames[n].equals(name)) {
				return Sign.values()[n];
			}
		}
		
		throw new Exception("Could not find Class: " + name);
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
