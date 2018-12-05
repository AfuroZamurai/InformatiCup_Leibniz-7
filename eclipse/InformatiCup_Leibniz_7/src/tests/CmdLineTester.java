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
		
		System.out.println("Test 3: Too many arguments c");
		args = new String[] {"-c 5 10"};
		CmdLineStart.main(args);
		
		System.out.println("Test 4: Wrong argument evaluator");
		args = new String[] {"-e saadsd"};
		CmdLineStart.main(args);
		
		System.out.println("Test 5: Custom Output path");
		args = new String[] {"-test -g nochange -c 8 -o ../../image"};
		CmdLineStart.main(args);
		
		System.out.println("Test 6: MaxIterations");
		args = new String[] {"-test -g recursivesquare -c 8 -o ../../image -i 100"};
		CmdLineStart.main(args);
		
		System.out.println("Test 7: RequestDelay");
		args = new String[] {"-test -g recursivesquare -c 8 -o ../../image -d 10"};
		CmdLineStart.main(args);
		
		
		//Test all algorithms with all classes
		String[] generators = new String[] {"nochange","checkersearch","circlesearch","evoencoding","recursivesquare"};
		int classes = 43;
		
		for(int a = 0; a < generators.length; a++) {
			
			for(int c = 0; c < classes;c++) {
				
				System.out.println("Testing: " + generators[a] + " with Class " + c);
				args = new String[] {"-test -c " + c + " -g " + generators[a] + " -d 10"};
				CmdLineStart.main(args);
			}
		}
	}
}
