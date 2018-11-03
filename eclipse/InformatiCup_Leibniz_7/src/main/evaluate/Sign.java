package main.evaluate;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.io.ImageLoader;

/**
 * This enum contains all 42 classes of the Neural Network Using german names
 * since the request returns german names as well
 * 
 * @author Jannik
 *
 */
public enum Sign implements IClassification {
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
	ENDE_UEBERHOLVERBOT_LKW;
	
	/**
	 * Names of the classes returned by the webAPI
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
	

	@Override
	public BufferedImage getExampleImage() throws IOException {
		
		int number = this.ordinal();

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
	
	
	
}
