package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;
import main.gui.PixelSearchCancellationProcess;

class PixelSearchTester {
	
	/* Disabled until Code is restructured
	@Test
	void testPixelSearch() throws IOException {
		
		//Takes very long, adjust Filter size(when possible)
		for(int i = 0; i < Sign.values().length; i++) {
			
			PixelSearchCancellationProcess pixelSearch = new PixelSearchCancellationProcess(Sign.values()[i]);

			BufferedImage img = EvaluationResult.getExampleImage(Sign.values()[i]);
			
			BufferedImage result = pixelSearch.generateImage(img);
			
		}
	}
	*/
}
