package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdLineStart {

	
	private static final String COMMAND_NAME = "cnnfooler";
	
	public static void main(String[] args) {
		
		//Parses out all Args in a convenient HashMap Structure
		final Map<String, List<String>> params = new HashMap<String, List<String>>();

		List<String> options = null;
		for (int i = 0; i < args.length; i++) {
		    final String a = args[i];

		    if (a.charAt(0) == '-') {
		        if (a.length() < 2) {
		            System.err.println("Error at argument " + a);
		            return;
		        }

		        options = new ArrayList<>();
		        params.put(a.substring(1), options);
		    }
		    else if (options != null) {
		        options.add(a);
		    }
		    else {
		        System.err.println("Illegal parameter usage");
		        return;
		    }
		}
		
		//Use params.get("flag").get(NumOfParameter) to get desired Info
		
		if(params.containsKey("h") || params.containsKey("-help") ) {
			
			System.out.println("General:");
			System.out.println("This program generates \"fooling images\" for Classifiers using different approaches.");
			System.out.println("Fooling images are images which are recognized as belonging to a class with a high confidence "
					+ "even when they clearly don't belong there(for a human) ");
			
			System.out.println("");
			System.out.println("Usage:");
			
			System.out.println("\t" + COMMAND_NAME + " -e <evaluator> [parameters] -a <algorithm> [parameters] -c <targetclass> [optional arguments]");
			System.out.println("");
			System.out.println("Evaluators:");
			System.out.println("\ttrasiweb\t\t(default)\tSends Requests to the Web Interface and receives evaluations as respone.");
			
			System.out.println("");
			System.out.println("Algorithms:");
			System.out.println("\tnochange\t\t(default)\tSimply sends the example start Image of the class to be evaluated.");
			System.out.println("\tpixelsearch\t<filterSize=0-64>\t\tPuts black or white boxes on the image depending on evaluation.");
			System.out.println("\tcirclesearch\t\t\tPuts colored circles on the image depending on evaluation.");
			
			System.out.println("");
			System.out.println("Target class:");
			System.out.println("\t<Number >= 0>\t\t\tThe index of the target class that is being used to fool the classifier.");
			
			System.out.println("");
			System.out.println("Optional Arguments:");
			System.out.println("\t-v\t\t\tVerbose, prints information of current process to the command line.");
		}
	}
}
