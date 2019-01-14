package main.evolution.ga.encoding;

import main.evolution.ga.Fitness;

/**
 * The fitness score for an encoding genom. Here fitness is calculated as a combination of confidence and coverage.
 * @author Felix
 *
 */
public class EncodingFitness extends Fitness {
	
	/**
	 * The confidence value of this fitness score
	 */
	private float confidence;
	/**
	 * The coverage value of this fitness score
	 */
	private float coverage;
	
	/**
	 * Create a new fitness score with no knowledge about its components
	 * @param fitness
	 */
	public EncodingFitness(float fitness) {
		super(fitness);
	}
	
	/**
	 * Create a new fitness score from a confidence and coverage value
	 * @param confidence A confidence value
	 * @param coverage A coverage value
	 */
	public EncodingFitness(float confidence, float coverage) {
		super(getCombinedFitness(confidence, coverage));
		this.confidence = confidence;
		this.coverage = coverage;
	}
	
	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	public float getCoverage() {
		return coverage;
	}

	public void setCoverage(float coverage) {
		this.coverage = coverage;
	}

	/**
	 * Calculate the fitness score from a given confidence and coverage value.
	 * @param confidence A confidence value
	 * @param coverage A coverage value
	 * @return A fitness score calculated from combining confidence and coverage
	 */
	public static float getCombinedFitness(float confidence, float coverage) {
		float fitness = 1.0f;
		fitness -= 1.0f - confidence;
		fitness -= 1.0f - coverage;
		
		return fitness;
	}

}
