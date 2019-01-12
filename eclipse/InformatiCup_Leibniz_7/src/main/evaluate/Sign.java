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
	public static final String[] classNames = new String[] { "Zul�ssige H�chstgeschwindigkeit (20)",
			"Zul�ssige H�chstgeschwindigkeit (30)", "Zul�ssige H�chstgeschwindigkeit (50)",
			"Zul�ssige H�chstgeschwindigkeit (60)", "Zul�ssige H�chstgeschwindigkeit (70)",
			"Zul�ssige H�chstgeschwindigkeit (80)", "Ende der Geschwindigkeitsbegrenzung (80)",
			"Zul�ssige H�chstgeschwindigkeit (100)", "Zul�ssige H�chstgeschwindigkeit (120)",
			"�berholverbot f�r Kraftfahrzeuge aller Art",
			"�berholverbot f�r Kraftfahrzeuge mit einer zul�ssigen Gesamtmasse �ber 3,5t", "Einmalige Vorfahrt",
			"Vorfahrt", "Vorfahrt gew�hren", "Stoppschild", "Verbot f�r Fahrzeuge aller Art",
			"Verbot f�r Kraftfahrzeuge mit einer zul�ssigen Gesamtmasse von 3,5t", "Verbot der Einfahrt",
			"Gefahrenstelle", "Kurve (links)", "Kurve (rechts)", "Doppelkurve (zun�chst links)", "Unebene Fahrbahn",
			"Schleudergefahr bei N�sse oder Schmutz", "", "Baustelle", "", "Fu�g�nger", "", "Fahrradfahrer", "",
			"Wildwechsel", "Ende aller Streckenverbote", "Ausschlie�lich rechts", "Ausschlie�lich links",
			"Ausschlie�lich geradeaus", "", "", "Rechts vorbei", "Links vorbei", "Kreisverkehr",
			"Ende des �berholverbotes f�r Kraftfahrzeuge aller Art",
			"Ende des �berholverbotes f�r Kraftfahrzeuge mit einer zul�ssigen Gesamtmasse �ber 3,5t" };

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
		readableName = readableName.replaceAll("AE", "�");
		readableName = readableName.replaceAll("UE", "�");
		readableName = readableName.replaceAll("OE", "�");

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
