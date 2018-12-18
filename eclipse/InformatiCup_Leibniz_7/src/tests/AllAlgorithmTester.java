package tests;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

import main.encodings.BoxEncoding;
import main.encodings.CircleEncoding;
import main.encodings.DirectEncoding;
import main.encodings.GridEncoding;
import main.encodings.IImageEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.IEvaluator;
import main.evaluate.Sign;
import main.evaluate.TrasiWebEvaluator;
import main.generate.CheckerGenerator;
import main.generate.EncoderGenerator;
import main.generate.EvoEncoderGenerator;
import main.generate.IGenerator;
import main.generate.NoChange;
import main.generate.RecursiveSquareGenerator;
import main.generate.SimpleGenerator;
import main.io.ImageSaver;

/**
 * Intended to get the results for all algorithms for all classes.
 * Will be using different parameters for testing.
 * @author Felix
 *
 */
public class AllAlgorithmTester {
	
	private static int maxIterations = 60;
	private final static int DELAY = 0;
	private final static String PATH = "data/results/testingAll/";
	
	private final static String[] EXAMPLE_CONFIDENCES = {"0,998", "0,972", "0,998", "0,739", "0,970", "0,904", 
			"0,989", "0,738", "0,988", "1,000", "0,999", "1,000", "0,994", "0,997", "1,000", "0,999", "1,000", 
			"1,000", "0,999", "0,981", "0,019", "0,975", "1,000", "1,000", "0,000", "1,000", "0,000", "0,997", 
			"0,000", "0,076", "0,000", "0,930", "0,121", "1,000", "0,000", "1,000", "0,000", "0,000", "0,972", 
			"0,000", "0,986", "0,000", "0,995"};
	
	public static AlgorithmResult runAlgorithm(IEvaluator evaluator, IGenerator generator, IClassification target,
			int maxIterations, int requestDelay) throws Exception {
		
		generator.setInitImage(target.getExampleImage(), target);
		
		int iterations = 1;
		long lastRequest = 0;
		
		EvaluationResult<IClassification> evalResult;
		BufferedImage next;
		
		while (!generator.isFinished() && iterations < maxIterations) {
			next = generator.generateNextImage();
			
			long delay = System.currentTimeMillis() - lastRequest;
			
			if(delay < requestDelay) {
				Thread.sleep(requestDelay - delay);
			}
			
			lastRequest = System.currentTimeMillis();
			evalResult = evaluator.evaluateImage(next);
			
			
			generator.setEvalResult(evalResult);
			iterations++;
		}
		
		BufferedImage result = generator.getResult();
		
		evalResult = evaluator.evaluateImage(result);
		float score = evalResult.getConfidenceForClass(target);
		
		return new AlgorithmResult(iterations - 1, score, result);
	}
	
	public static void iterateClasses(IGenerator generator, String folderName, String csvName) throws Exception {
		IEvaluator evaluator = new TrasiWebEvaluator();
		List<String[]> csvEntries = new ArrayList<>();
		
		for(int i = 0; i < 2; i++) {
			IClassification sign = Sign.values()[i];
			AlgorithmResult result = runAlgorithm(evaluator, generator, sign, maxIterations, DELAY);
			
			String truncatedConfidence = String.format("%.3f", result.getConfidence());
			String path = PATH + folderName + "/class" + i + "_" + truncatedConfidence;
			
			ImageSaver.saveImage(result.getResult(), path);
			String[] entries = {Integer.toString(i + 1), EXAMPLE_CONFIDENCES[i], 
					Integer.toString(maxIterations), truncatedConfidence, Integer.toString(result.getIterations())};
			csvEntries.add(entries);
		}
		
		//write the results into a csv file
		BufferedWriter writer = Files.newBufferedWriter(Paths.get(".", PATH + csvName + ".csv"));
		String[] headers = {"Klasse", "Konfidenz Beispielbild", "Maximale Iterationen", "Endkonfidenz", "Iterationen"};
		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));
		
		for (Iterator<String[]> iterator = csvEntries.iterator(); iterator.hasNext();) {
			String[] entry = iterator.next();
			printer.printRecord(Arrays.asList(entry));
		}
		
		printer.flush();
		printer.close();
	}
	
	public void testNoChangeGenerator() throws Exception {
		iterateClasses(new NoChange(), "NoChangeGenerator", "examples");
	}

	public void testCheckerGenerator() throws Exception {
		iterateClasses(new CheckerGenerator(), "CheckerGenerator", "checker");
	}
	
	public void testRecursiveSquareGenerator() throws Exception {
		iterateClasses(new RecursiveSquareGenerator(), "RecursiveSquareGenerator", "recursive");
	}

	public void testSimpleGenerator() throws Exception {
		iterateClasses(new SimpleGenerator(), "SimpleGenerator", "simple");
	}

	public void testBoxEncodingGenerator() throws Exception {
		IImageEncoding encoding = new BoxEncoding();
		iterateClasses(new EncoderGenerator(encoding), "BoxEncodingGenerator", "box");
	}

	public void testCircleEncodingGenerator() throws Exception {
		IImageEncoding encoding = new CircleEncoding();
		iterateClasses(new EncoderGenerator(encoding), "CircleEncodingGenerator", "circle");
	}

	public void testDirectEncodingGenerator() throws Exception {
		IImageEncoding encoding = new DirectEncoding();
		iterateClasses(new EncoderGenerator(encoding), "DirectEncodingGenerator", "direct");
	}

	public void testGridEncodingGenerator() throws Exception {
		for(int i = 16; i > 15; i /= 2) {
			IImageEncoding encoding = new GridEncoding(i, i);
			iterateClasses(new EncoderGenerator(encoding), "GridEncodingGenerator", "grid_" + i);
		}
	}
	
	public void testBoxEvoGenerator() throws Exception {
		IImageEncoding encoding = new BoxEncoding();
		iterateClasses(new EvoEncoderGenerator(encoding), "BoxEvoGenerator", "evobox");
	}
	
	public void testCircleEvoGenerator() throws Exception {
		IImageEncoding encoding = new CircleEncoding();
		iterateClasses(new EvoEncoderGenerator(encoding), "CircleEvoGenerator", "evocircle");
	}
	
	public void testDirectEvoGenerator() throws Exception {
		IImageEncoding encoding = new DirectEncoding();
		iterateClasses(new EvoEncoderGenerator(encoding), "DirectEvoGenerator", "evodirect");
	}
	
	public void testGridEvoGenerator() throws Exception {
		for(int i = 16; i > 0; i /= 2) {
			IImageEncoding encoding = new GridEncoding(i, i);
			iterateClasses(new EncoderGenerator(encoding), "GridEvoGenerator", "evogrid_" + i);
		}
	}
	
	@Test
	public void testAllAlgorithms() throws Exception {
		//testNoChangeGenerator();
		testCheckerGenerator();
		testRecursiveSquareGenerator();
		testSimpleGenerator();
		//testBoxEncodingGenerator(); //image only black
		testCircleEncodingGenerator();
		//testDirectEncodingGenerator(); //not working
		//testGridEncodingGenerator(); // not working
		//testBoxEvoGenerator();
		//testCircleEvoGenerator();
		//testDirectEvoGenerator();
		//testGridEvoGenerator();
	}
}

final class AlgorithmResult {
	private int iterations;
	private float confidence;
	private BufferedImage result;
	
	public AlgorithmResult(int iterations, float confidence, BufferedImage result) {
		super();
		this.iterations = iterations;
		this.confidence = confidence;
		this.result = result;
	}

	public int getIterations() {
		return iterations;
	}

	public float getConfidence() {
		return confidence;
	}

	public BufferedImage getResult() {
		return result;
	}
	
	
}
