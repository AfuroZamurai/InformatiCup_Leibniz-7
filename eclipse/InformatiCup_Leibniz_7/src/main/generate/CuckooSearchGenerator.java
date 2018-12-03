package main.generate;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.Sign;
import main.evolution.ga.cppn.CuckooSearch;
import main.evolution.network.CPPN;
import main.evolution.network.Config;

/**
 * This class will evolve a fooling image for a given input image.
 * Currently this algorithm is suspended.
 * 
 * @author Felix
 *
 */
public class CuckooSearchGenerator implements IGenerator {
	
	private final int POPULATION_SIZE = 60;
	private final int GENERATION_CAP = 25;
	private final int ELITISM = 2;
	private final float TARGET_FITNESS = 0.9f;
	
	private CPPN net;
	private CuckooSearch searcher;
	
	private Parameter exampleParam = new Parameter("Example Parameter", "Example Explanation", 1);
	
	/**
	 * Constructor which instantiates the encoding network for a given image size.
	 * 
	 * @param imageWidth width of the input image
	 * @param imageHeight height of the input image
	 */
	public CuckooSearchGenerator(int imageWidth, int imageHeight) {
		this.net = new CPPN(0, 0, new Config(imageWidth, imageHeight, 0, 50));
	}

	@Override
	public BufferedImage generateNextImage() {
		return searcher.searchForImage();
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		// evaluation is handled by the genetic algorithm so nothing needs to be done
	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {
		searcher = new CuckooSearch(this.net, POPULATION_SIZE, TARGET_FITNESS, 
				GENERATION_CAP, ELITISM, imageClass);
	}

	@Override
	public List<Parameter> getParameterList() {
		List<Parameter> list = new ArrayList<Parameter>();

		list.add(exampleParam);

		return list;
	}

	@Override
	public boolean isFinished() {
		return searcher.isFinished();
	}

	@Override
	public String getModuleDescription() {
		return "Performs a cuckoo search to find an image to fool the neural network";
	}

	@Override
	public BufferedImage getResult() {
		return net.createImage(searcher.getBestGenom());
	}
	
}
