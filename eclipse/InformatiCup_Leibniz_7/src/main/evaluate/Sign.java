package main.evaluate;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import main.io.ImageLoader;

/**
 * 
 * Implementation of IClassification
 * Represents Image classes of 43 german traffic signs
 * 
 * This enum contains all 43 classes of the Neural Network Using german names
 * since the request returns german names as well
 * 
 * @author Jannik
 *
 */
public enum Sign implements IClassification {
	HOECHSTGESCHWINDIGKEIT_20, 
	HOECHSTGESCHWINDIGKEIT_30, 
	HOECHSTGESCHWINDIGKEIT_50, 
	HOECHSTGESCHWINDIGKEIT_60, 
	HOECHSTGESCHWINDIGKEIT_70, 
	HOECHSTGESCHWINDIGKEIT_80, 
	ENDE_HOECHSTGESCHWINDIGKEIT_80, 
	HOECHSTGESCHWINDIGKEIT_100, 
	HOECHSTGESCHWINDIGKEIT_120, 
	UEBERHOLVERBOT_ALLE, 
	UEBERHOLVERBOT_LKW, 
	VORFAHRT, 
	VORFAHRTSSTRASSE, 
	VORFAHRT_GEWAEHREN, 
	STOP, 
	FAHRVERBOT, 
	LKW_VERBOT, 
	EINFAHRT_VERBOTEN, 
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
	ENDE_UEBERHOLVERBOT_LKW;

	/**
	 * Names of the classes returned by the webAPI TODO: Input missing names(could
	 * not be retrieved because no images reach high enough confidence)
	 */
	public static final String[] classNames = new String[] { "Zulässige Höchstgeschwindigkeit (20)",
			"Zulässige Höchstgeschwindigkeit (30)", "Zulässige Höchstgeschwindigkeit (50)",
			"Zulässige Höchstgeschwindigkeit (60)", "Zulässige Höchstgeschwindigkeit (70)",
			"Zulässige Höchstgeschwindigkeit (80)", "Ende der Geschwindigkeitsbegrenzung (80)",
			"Zulässige Höchstgeschwindigkeit (100)", "Zulässige Höchstgeschwindigkeit (120)",
			"Überholverbot für Kraftfahrzeuge aller Art",
			"Überholverbot für Kraftfahrzeuge mit einer zulässigen Gesamtmasse über 3,5t", "Einmalige Vorfahrt",
			"Vorfahrt", "Vorfahrt gewähren", "Stoppschild", "Verbot für Fahrzeuge aller Art",
			"Verbot für Kraftfahrzeuge mit einer zulässigen Gesamtmasse von 3,5t", "Verbot der Einfahrt",
			"Gefahrenstelle", "Kurve (links)", "Kurve (rechts)", "Doppelkurve (zunächst links)", "Unebene Fahrbahn",
			"Schleudergefahr bei Nässe oder Schmutz", "", "Baustelle", "", "Fußgänger", "", "Fahrradfahrer", "",
			"Wildwechsel", "Ende aller Streckenverbote", "Ausschließlich rechts", "Ausschließlich links",
			"Ausschließlich geradeaus", "", "", "Rechts vorbei", "Links vorbei", "Kreisverkehr",
			"Ende des Überholverbotes für Kraftfahrzeuge aller Art",
			"Ende des Überholverbotes für Kraftfahrzeuge mit einer zulässigen Gesamtmasse über 3,5t" };

	@Override
	public BufferedImage getExampleImage() throws IOException {

		int number = this.ordinal();

		String numString = "" + number;

		if (number < 10) {
			numString = "0" + number;
		}

		BufferedImage example;

		try {
			example = ImageLoader.loadInternalImage("/images/000" + numString + ".ppm");
		} catch (IOException e) {

			e.printStackTrace();
			throw new IOException("The example image could not be loaded!");
		}

		return example;
	}

	@Override
	public String getNameOfClass() {

		return this.toString();
	}

	@Override
	public int getOrdinal() {
		return this.ordinal();
	}

	@Override
	public IClassification[] getValues() {
		return Sign.values();
	}

	@Override
	public String toString() {

		String readableName = this.name();

		//Convert Umlaute
		readableName = readableName.replaceAll("AE", "Ä");
		readableName = readableName.replaceAll("UE", "Ü");
		readableName = readableName.replaceAll("OE", "Ö");

		//Make first letter Uppercase
		readableName = readableName.substring(0, 1).toUpperCase() + readableName.substring(1).toLowerCase();
		
		//Replace all _ with spaces and the letter after it to upper case
		int spaceIndex = readableName.indexOf("_");
		while (spaceIndex >= 0) {
			readableName = readableName.substring(0, spaceIndex) + " "
					+ readableName.substring(spaceIndex + 1, spaceIndex + 2).toUpperCase()
					+ readableName.substring(spaceIndex + 2);
			spaceIndex = readableName.indexOf("_");
		}

		return readableName;
	}

}
