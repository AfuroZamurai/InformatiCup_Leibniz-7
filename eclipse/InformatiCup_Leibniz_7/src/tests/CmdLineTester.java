package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.CmdLineStart;

class CmdLineTester {

	//@Test
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
		
		System.out.println("Test 4: a pixelsearc");
		args = new String[] {"-a pixelsearch"};
		CmdLineStart.main(args);
		
		System.out.println("Test 5: a circlesearch");
		args = new String[] {"-a circlesearch"};
		CmdLineStart.main(args);
		
		System.out.println("Test 6: a nochange");
		args = new String[] {"-a nochange"};
		CmdLineStart.main(args);
		
		System.out.println("Test 7: c 0");
		args = new String[] {"-c 0"};
		CmdLineStart.main(args);
		
		System.out.println("Test 8: Too many arguments c");
		args = new String[] {"-c 5 10"};
		CmdLineStart.main(args);
		
		System.out.println("Test 9: Wrong argument evaluator");
		args = new String[] {"-e saadsd"};
		CmdLineStart.main(args);
	}
	
	@Test
	void testEncoder() throws Exception {
		String[] args = new String[] {"-e trasiweb -a encodingsearch -c 4"};
		CmdLineStart.main(args);
	}

}
