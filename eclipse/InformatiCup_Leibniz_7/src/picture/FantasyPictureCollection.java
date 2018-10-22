package picture;

import java.awt.Color;
import java.util.ArrayList;

import main.evaluate.EvaluationResult.Sign;
import picture.FantasyPicture.Shape;
import picture.features.ConstructionSiteFeature;
import picture.features.Feature;
import picture.features.TextFeature;
import picture.features.UnevenGroundFeature;
import picture.features.WarningFeature;
import picture.features.PedestrianFeature;

/**
 * This class gives a collection of all available signs as FantasyPictures. It
 * is used to get a FantasyPicture representation of one of the signs to then
 * alter its appearance using the available actions.
 * 
 * @author Fredo
 *
 */
public class FantasyPictureCollection {

	/**
	 * Creates a FantasyPicture representation from a sing.
	 * 
	 * @param sign The sign whose representation should be returned
	 * @return The FantasyPicture of the sign
	 */
	public static FantasyPicture createFantasyPictureFromSign(Sign sign) {
		ArrayList<Feature> features = new ArrayList<>();
		Layout layout = null;
		Border border = null;
		FantasyPicture pic = null;

		switch (sign) {
		case BAUSTELLE:
			features.add(new ConstructionSiteFeature());
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 6);
			pic = new FantasyPicture(Shape.TRIANGLE, Color.WHITE, layout, border);
			break;
		case DOPPELKURVE_LINKS:
			break;
		case EINFAHRT_VERBOT:
			break;
		case ENDE_ALLER_VERBOTE:
			break;
		case ENDE_DER_ZULAESSIGEN_HOECHSTGESCHWINDIGKEIT_80:
			break;
		case ENDE_UEBERHOLVERBOT:
			break;
		case ENDE_UEBERHOLVERBOT_LKW:
			break;
		case FAHRTRICHTUNG_GERADE:
			break;
		case FAHRTRICHTUNG_GERADE_LINKS:
			break;
		case FAHRTRICHTUNG_GERADE_RECHTS:
			break;
		case FAHRTRICHTUNG_LINKS:
			break;
		case FAHRTRICHTUNG_LINKS_VORBEI:
			break;
		case FAHRTRICHTUNG_RECHTS:
			break;
		case FAHRTRICHTUNG_RECHTS_VORBEI:
			break;
		case FAHRVERBOT:
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 8);
			pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
			break;
		case FUSSGAENGER:
			features.add(new PedestrianFeature());
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 6);
			pic = new FantasyPicture(Shape.TRIANGLE, Color.WHITE, layout, border);
			break;
		case GEFAHRSTELLE:
			features.add(new WarningFeature());
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 6);
			pic = new FantasyPicture(Shape.TRIANGLE, Color.WHITE, layout, border);
			break;
		case KINDER:
			break;
		case KREISVERKEHR:
			break;
		case KURVE_LINKS:
			break;
		case KURVE_RECHTS:
			break;
		case LICHTZEICHENANLAGE:
			break;
		case LKW_VERBOT:
			break;
		case RADVERKEHR:
			break;
		case SCHLEUDERGEFAHR:
			break;
		case SCHNEEGLAETTE:
			break;
		case STOP:
			break;
		case UEBERHOLVERBOT_ALLE:
			break;
		case UEBERHOLVERBOT_LKW:
			break;
		case UNEBENE_FAHRBAHN:
			features.add(new UnevenGroundFeature());
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 6);
			pic = new FantasyPicture(Shape.TRIANGLE, Color.WHITE, layout, border);
			break;
		case VERENGUNG_RECHTS:
			break;
		case VORFAHRT:
			break;
		case VORFAHRTSSTRASSE:
			break;
		case VORFAHRT_GEWAEHREN:
			break;
		case WILDWECHSEL:
			break;
		case ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_100:
			features.add(new TextFeature("100", 11, 41, 25));
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 8);
			pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
			break;
		case ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_120:
			features.add(new TextFeature("120", 11, 41, 25));
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 8);
			pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
			break;
		case ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_20:
			features.add(new TextFeature("20", 16, 43, 30));
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 8);
			pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
			break;
		case ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_30:
			features.add(new TextFeature("30", 16, 43, 30));
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 8);
			pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
			break;
		case ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_50:
			features.add(new TextFeature("50", 16, 43, 30));
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 8);
			pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
			break;
		case ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_60:
			features.add(new TextFeature("60", 16, 43, 30));
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 8);
			pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
			break;
		case ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_70:
			features.add(new TextFeature("70", 16, 43, 30));
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 8);
			pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
			break;
		case ZULAESSIGE_HOECHSTGESCHWINDIGKEIT_80:
			features.add(new TextFeature("80", 16, 43, 30));
			layout = new Layout(Layout.LayoutType.SINGLEFEATURE, features);
			border = new Border(new Color(230, 0, 3), 8);
			pic = new FantasyPicture(Shape.CIRCLE, Color.WHITE, layout, border);
			break;
		default:
			break;
		}

		return pic;
	}
}
