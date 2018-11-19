package tests;

import org.junit.Test;

import main.CmdLineStart;
import main.evaluate.Sign;
import main.evolution.ga.cppn.CuckooSearch;
import main.evolution.network.CPPN;
import main.evolution.network.Config;

public class CuckooSearchTester {
	
	//@Test
	public void testCuckooSearch() {
		CuckooSearch searcher = new CuckooSearch(new CPPN(0, 0, new Config(64, 64, 0, 50)), 30, 0.9f, 20, Sign.VORFAHRT);
		searcher.searchForImage();
	}
	
	@Test
	public void testCmdLineCuckooSearch() throws Exception {
		String[] args = new String[] {};
		args = new String[] {"-a cuckoosearch"};
		CmdLineStart.main(args);
	}
}
