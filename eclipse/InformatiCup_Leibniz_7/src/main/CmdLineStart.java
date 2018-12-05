package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.encodings.CircleEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.IEvaluator;
import main.evaluate.Sign;
import main.evaluate.TestEvaluator;
import main.evaluate.TrasiWebEvaluator;
import main.generate.CheckerGenerator;
import main.generate.CuckooSearchGenerator;
import main.generate.EncoderGenerator;
import main.generate.EvoEncoderGenerator;
import main.generate.IGenerator;
import main.generate.NoChange;
import main.generate.RecursiveSquareGenerator;
import main.generate.SimpleGenerator;
import main.io.ImageSaver;

public class CmdLineStart {

	private static final String COMMAND_NAME = "icfooler";

	/**
	 * Main entry Point for Command Line usage
	 * @param args Arguments called when running the program, refer to the -help message for a list of commands
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Parses out all Args in a convenient HashMap Structure
		final Map<String, List<String>> params = new HashMap<String, List<String>>();

		List<String> options = null;
		for (int i = 0; i < args.length; i++) {
			final String s = args[i];
			String[] splitted = s.split(" ");
			
			for (int n = 0; n < splitted.length; n++){
				final String a = splitted[n];
	
				if (a.charAt(0) == '-') {
					if (a.length() < 2) {
						System.err.println("Error at argument " + a);
						return;
					}
	
					options = new ArrayList<>();
					params.put(a, options);
				} else if (options != null) {
					options.add(a);
				} else {
					System.err.println("Illegal parameter usage");
					return;
				}
			}
		}

		// Use params.get("flag").get(NumOfParameter) to get desired Info

		if (params.containsKey("-h") || params.containsKey("--help") || params.size() == 0) {

			System.out.println("General:");
			System.out.println("This program generates \"fooling images\" for Classifiers using different approaches.");
			System.out.println("Fooling images are images which are recognized as belonging to a class with a high confidence\n"
							+ "even when they clearly don't belong there(for a human) ");

			System.out.println("");
			System.out.println("Usage:");

			System.out.println("\t" + COMMAND_NAME
					+ " -g [generator]  -c [targetclass] [optional arguments]");
			System.out.println("");
			System.out.println("Generators:");
			System.out.println(
					"\tnochange\t\t\tSimply sends the example start Image of the class to be evaluated.");
			System.out.println(
					"\tcheckersearch\t\tPuts black or white boxes on the image depending on evaluation.");
			System.out.println("\tcirclesearch\t\t\t\tPuts colored circles on the image depending on evaluation.");
			System.out.println("\tencodingsearch\t\t\t\tUses any image encoding to generate new images.");
			System.out.println("\tevoencoding\t\t\t\tUses evolutionary algorithm to generate new images.");
			System.out.println("\trecursivesquare\t\t\t\tUses squares recursively to generate images");
			System.out.println("");
			System.out.println("Target class:");
			System.out.println(
					"\t<0 - 42>\t\t\t\tThe index of the target class that is being used to fool the classifier. Default=0");

			System.out.println("");
			System.out.println("Optional Arguments:");
			System.out.println("\t-o <path/filename>\t\t\tSpecify where the output will be saved. Default is \"result.png\"");
			
			
			return;
		}
		
		//Default values
		IEvaluator evaluator = new TrasiWebEvaluator();
		IGenerator generator;
		int targetClass;
		String output = "result";
		int requestDelay = 0;
		int maxIterations = 600;
		
		if(params.containsKey("-g")) {
			if(params.get("-g").size() != 1) {
				System.out.println("Error: Exptected 1 argument for option -g, but got " + params.get("-g").size());
				return;
			}
			
			if(params.get("-g").get(0).equals("nochange")) {
				generator = new NoChange();
			}
			else if(params.get("-g").get(0).equals("checkersearch")) {
				generator = new CheckerGenerator();
			}
			else if(params.get("-g").get(0).equals("circlesearch")) {
				
				generator = new SimpleGenerator();
			}
			else if(params.get("-g").get(0).equals("evoencoding")) {
				generator = new EvoEncoderGenerator(new CircleEncoding());
			}
			else if(params.get("-g").get(0).equals("recursivesquare")) {
				generator = new RecursiveSquareGenerator();
			}
			else {
				System.out.println("Error: argument \""+ params.get("-g").get(0) +" \" for option -g is invalid");
				return;
			}
		}
		else {
			System.out.println("Error: Argument for Generator -g must be set.");
			return;
		}
		
		if(params.containsKey("-test")) {
			evaluator = new TestEvaluator();
		}
		
		if(params.containsKey("-c")) {
			if(params.get("-c").size() != 1) {
				System.out.println("Error: Exptected 1 argument for option -c, but got " + params.get("-c").size());
				return;
			}
			
			try {
				targetClass = Integer.parseInt(params.get("-c").get(0));
				if(targetClass < 0 || targetClass > 42) {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Error: argument \""+ params.get("-c").get(0) + "\" for option -c is invalid. Must be integer in range 0-42");
				return;
			}
		}
		else {
			targetClass = 0;
		}
		
		if(params.containsKey("-o")) {
			if(params.get("-o").size() != 1) {
				System.out.println("Error: Exptected 1 argument for option -o, but got " + params.get("-o").size());
				return;
			}
			
			output = params.get("-o").get(0);
		}
		
		if(params.containsKey("-i")) {
			if(params.get("-i").size() != 1) {
				System.out.println("Error: Exptected 1 argument for option -i, but got " + params.get("-i").size());
				return;
			}
			try {
				String value = params.get("-i").get(0);
				maxIterations = Integer.parseInt(value);
				
				if(maxIterations <= 0) {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Error: Argument -i expects an integer > 0");
				return;
			}
		}
		
		if(params.containsKey("-d")) {
			if(params.get("-d").size() != 1) {
				System.out.println("Error: Exptected 1 argument for option -d, but got " + params.get("-d").size());
				return;
			}
			try {
				String value = params.get("-d").get(0);
				requestDelay = Integer.parseInt(value);
				
				if(requestDelay < -1) {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Error: Argument -d expects an integer >= 0");
				return;
			}
		}
		
		IClassification sign = Sign.values()[targetClass];
		runAlgorithm(evaluator, generator, sign, maxIterations, requestDelay, output);
	}
	
	/**
	 * Runs the programm with the given config
	 * @param evaluator The Evaluation implementation, which retrieves confidence scores for images
	 * @param generator The generator that generates Images
	 * @param targetClass The class of the classifier which is tried to be approximated
	 * @param maxIterations The maxium number of iterations before the algorithm stops
	 * @param requestDelay The number of ms the algorithm waits between requests
	 * @Param outputPath The path where the output will be saved
	 * @throws Exception
	 */
	public static void runAlgorithm(IEvaluator evaluator, IGenerator generator, IClassification targetClass, int maxIterations,int requestDelay, String outputPath) throws Exception {
		
		generator.setInitImage(targetClass.getExampleImage(), targetClass);
		
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
		float score = evalResult.getConfidenceForClass(targetClass);
		
		System.out.println("Score: "+ score);
		
		ImageSaver.saveImage(result, outputPath);
	}
}
