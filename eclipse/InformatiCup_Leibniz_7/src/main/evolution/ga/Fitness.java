package main.evolution.ga;

/**
 * Basic representation of a fitness score. Basic means the fitness is a value which only consists of this one value.
 * Any derived class can represent this score as a combination of other scores.
 * 
 * @author Felix
 *
 */
public class Fitness {
	
	/**
	 * The fitness score
	 */
	protected float fitnessScore;
	
	public Fitness(float fitness) {
		setFitnessScore(fitness);
	}

	public float getFitnessScore() {
		return fitnessScore;
	}

	public void setFitnessScore(float fitnessScore) {
		this.fitnessScore = fitnessScore;
	}
	
}
