package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.CmdLineStart;

class CmdLineTester {

	@Test
	void testMain() throws Exception {
		
		System.out.println("Test 0: No arguments");
		String[] args = new String[] {};
		CmdLineStart.main(args);
		
		System.out.println("Test 1: Help 1");
		args = new String[] {"-h"};
		CmdLineStart.main(args);
		
		System.out.println("Test 2: Help 2");
		args = new String[] {"--help"};
		CmdLineStart.main(args);
		
		System.out.println("Test 3: e trasiweb");
		args = new String[] {"-e trasiweb"};
		CmdLineStart.main(args);
		
		System.out.println("Test 4: Too many arguments c");
		args = new String[] {"-c 5 10"};
		CmdLineStart.main(args);
		
		System.out.println("Test 5: Wrong argument evaluator");
		args = new String[] {"-e saadsd"};
		CmdLineStart.main(args);
		
		System.out.println("Test 6: Custom Output path");
		args = new String[] {"-e test -c 8 -o ../../image"};
		CmdLineStart.main(args);
		
		//Test all algorithms with all classes
		String[] algorithms = new String[] {"nochange","checkersearch","encodingsearch","cuckoosearch","recursivesquare"};
		int classes = 43;
		
		for(int a = 0; a < algorithms.length; a++) {
			
			for(int c = 0; c < classes;c++) {
				
				System.out.println("Testing: " + algorithms[a] + " with Class " + c);
				args = new String[] {"-e test -c " + c + " -a " + algorithms[a]};
				CmdLineStart.main(args);
			}
		}
	}
}
