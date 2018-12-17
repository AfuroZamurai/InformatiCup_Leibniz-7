package tests;

import java.awt.image.BufferedImage;
import org.apache.commons.math3.util.Pair;
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
	private final static String CSV_PATH = "data/results/testingAll";
	
	public static Pair<BufferedImage, Float> runAlgorithm(IEvaluator evaluator, IGenerator generator, IClassification target,
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
		
		return new Pair<BufferedImage, Float>(result, score);
	}
	
	public static void iterateClasses(IGenerator generator, String folderName) throws Exception {
		IEvaluator evaluator = new TrasiWebEvaluator();
		for(int i = 0; i < 1; i++) {
			IClassification sign = Sign.values()[i];
			Pair<BufferedImage, Float> result = runAlgorithm(evaluator, generator, sign, maxIterations, DELAY);
			String truncatedConfidence = String.format("%.3f", result.getSecond());
			String path = "data/results/testingAll/" + folderName + "/class" + i + "_" + truncatedConfidence;
			ImageSaver.saveImage(result.getFirst(), path);
		}
	}
	
	public void testNoChangeGenerator() throws Exception {
		iterateClasses(new NoChange(), "NoChangeGenerator");
	}

	public void testCheckerGenerator() throws Exception {
		iterateClasses(new CheckerGenerator(), "CheckerGenerator");
	}
	
	public void testRecursiveSquareGenerator() throws Exception {
		iterateClasses(new RecursiveSquareGenerator(), "RecursiveSquareGenerator");
	}

	public void testSimpleGenerator() throws Exception {
		iterateClasses(new SimpleGenerator(), "SimpleGenerator");
	}

	public void testBoxEncodingGenerator() throws Exception {
		IImageEncoding encoding = new BoxEncoding();
		iterateClasses(new EncoderGenerator(encoding), "BoxEncodingGenerator");
	}

	public void testCircleEncodingGenerator() throws Exception {
		IImageEncoding encoding = new CircleEncoding();
		iterateClasses(new EncoderGenerator(encoding), "CircleEncodingGenerator");
	}

	public void testDirectEncodingGenerator() throws Exception {
		IImageEncoding encoding = new DirectEncoding();
		iterateClasses(new EncoderGenerator(encoding), "DirectEncodingGenerator");
	}

	public void testGridEncodingGenerator() throws Exception {
		for(int i = 16; i > 15; i /= 2) {
			IImageEncoding encoding = new GridEncoding(i, i);
			iterateClasses(new EncoderGenerator(encoding), "GridEncodingGenerator");
		}
	}
	
	public void testBoxEvoGenerator() throws Exception {
		IImageEncoding encoding = new BoxEncoding();
		iterateClasses(new EvoEncoderGenerator(encoding), "BoxEvoGenerator");
	}
	
	public void testCircleEvoGenerator() throws Exception {
		IImageEncoding encoding = new CircleEncoding();
		iterateClasses(new EvoEncoderGenerator(encoding), "CircleEvoGenerator");
	}
	
	public void testDirectEvoGenerator() throws Exception {
		IImageEncoding encoding = new DirectEncoding();
		iterateClasses(new EvoEncoderGenerator(encoding), "DirectEvoGenerator");
	}
	
	public void testGridEvoGenerator() throws Exception {
		for(int i = 16; i > 0; i /= 2) {
			IImageEncoding encoding = new GridEncoding(i, i);
			iterateClasses(new EncoderGenerator(encoding), "GridEvoGenerator");
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
