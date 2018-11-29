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
					+ " -e <evaluator> [parameters] -a <algorithm> [parameters] -c <targetclass> [optional arguments]");
			System.out.println("");
			System.out.println("Evaluators:");
			System.out.println(
					"\ttrasiweb\t\t(default)\tSends Requests to the Web Interface and receives evaluations as respone.");

			System.out.println("");
			System.out.println("Algorithms:");
			System.out.println(
					"\tnochange\t\t(default)\tSimply sends the example start Image of the class to be evaluated.");
			System.out.println(
					"\tcheckersearch\t\tPuts black or white boxes on the image depending on evaluation.");
			System.out.println("\tcirclesearch\t\t\t\tPuts colored circles on the image depending on evaluation.");
			System.out.println("\tencodingsearch\t\t\t\tUses any image encoding to generate new images.");
			System.out.println("\tcuckoosearch\t\t\t\tUses cuckoosearch to generate new images.");
			System.out.println("\trecursivesquare\t\t\t\tUses squares recursively to generate images");

			System.out.println("");
			System.out.println("Target class:");
			System.out.println(
					"\t<Number >= 0>\t\t\t\tThe index of the target class that is being used to fool the classifier. Default=0");

			System.out.println("");
			System.out.println("Optional Arguments:");
			System.out.println("\t-v\t\t\t\t\tVerbose, prints information of current process to the command line.");
			System.out.println("\t-o <path/filename>\t\t\tSpecify where the output will be saved. Default is under data/results/resultImage");
			
			
			return;
		}
		
		//Default values
		IEvaluator evaluator;
		IGenerator algorithm;
		int targetClass;
		String output = "data/results/resultImage";
		
		if(params.containsKey("-a")) {
			if(params.get("-a").size() != 1) {
				System.out.println("Error: Exptected 1 argument for option -a, but got " + params.get("-a").size());
				return;
			}
			
			if(params.get("-a").get(0).equals("nochange")) {
				algorithm = new NoChange();
			}
			else if(params.get("-a").get(0).equals("checkersearch")) {
				algorithm = new CheckerGenerator();
			}
			else if(params.get("-a").get(0).equals("encodingsearch")) {
				
				algorithm = new EncoderGenerator(new CircleEncoding());
			}
			else if(params.get("-a").get(0).equals("cuckoosearch")) {
				algorithm = new CuckooSearchGenerator(64, 64);
			}
			else if(params.get("-a").get(0).equals("recursivesquare")) {
				algorithm = new RecursiveSquareGenerator();
			}
			else {
				System.out.println("Error: argument \""+ params.get("-a").get(0) +" \" for option -a is invalid");
				return;
			}
		}
		else {
			algorithm = new NoChange();
		}
		
		if(params.containsKey("-e")) {
			if(params.get("-e").size() != 1) {
				System.out.println("Error: Exptected 1 argument for option -e, but got " + params.get("-e").size());
				return;
			}
			
			if(params.get("-e").get(0).equals("trasiweb")) {
				evaluator = new TrasiWebEvaluator();
			}
			else if(params.get("-e").get(0).equals("test")) {
				evaluator = new TestEvaluator();
			}
			else {
				System.out.println("Error: argument \""+ params.get("-e").get(0) +" \" for option -e is invalid");
				return;
			}
		}
		else {
			evaluator = new TrasiWebEvaluator();
		}
		
		if(params.containsKey("-c")) {
			if(params.get("-c").size() != 1) {
				System.out.println("Error: Exptected 1 argument for option -c, but got " + params.get("-c").size());
				return;
			}
			
			try {
				targetClass = Integer.parseInt(params.get("-c").get(0));
				if(targetClass < 0) {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Error: argument \""+ params.get("-c").get(0) + "\" for option -c is invalid. Must be integer >= 0");
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
		
		IClassification sign = Sign.values()[targetClass];
		runAlgorithm(evaluator, algorithm, sign, 60, output);
	}
	
	/**
	 * Runs the programm with the given config
	 * @param evaluator The Evaluation implementation, which retrieves confidence scores for images
	 * @param algo The algorithm that generates Images
	 * @param targetClass The class of the classifier which is tried to be approximated
	 * @param maxIterations The maxium number of iterations before the algorithm stops
	 * @Param outputPath The path where the output will be saved
	 * @throws Exception
	 */
	public static void runAlgorithm(IEvaluator evaluator, IGenerator algo, IClassification targetClass, int maxIterations, String outputPath) throws Exception {
		
		algo.setInitImage(targetClass.getExampleImage(), targetClass);
		
		int iterations = 1;
		
		BufferedImage next = algo.generateNextImage();
		
		EvaluationResult<IClassification> evalResult;
		
		while (!algo.isFinished() && iterations < maxIterations) {
			evalResult = evaluator.evaluateImage(next);
			algo.setEvalResult(evalResult);
			next = algo.generateNextImage();
			iterations++;
		}
		
		BufferedImage result = algo.getResult();
		
		evalResult = evaluator.evaluateImage(result);
		float score = evalResult.getConfidenceForClass(targetClass);
		
		System.out.println("Score: "+ score);
		
		ImageSaver.saveImage(result, outputPath);
	}
}
