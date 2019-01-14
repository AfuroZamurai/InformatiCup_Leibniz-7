package main.utils;

import java.lang.Math;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.math3.distribution.*;

import main.evolution.ga.GeneticAlgorithm;
import main.evolution.ga.cppn.CuckooSearch;

/**
 * A collection of utility math methods, mainly intended for the evolution package.
 * @author Felix
 *
 */
public class Evolutionhelper {
	
	/**
	 * Calculates the value of the sigmoid function.
	 * @param x a number for which the sigmoid function value will be calculated
	 * @return the value of the sigmoid function for x
	 */
	public static double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}
	
	/**
	 * Calculates the value of the sigmoid prime function. 
	 * This function is calling sigmoid. 
	 * @param x a number for which the sigmoid function value will be calculated
	 * @return the value of the sigmoid prime function for x
	 */
	public static double sigmoidPrime(double x) {
		return sigmoid(x) * (1 - sigmoid(x));
	}
	
	/**
	 * Generates a randomly chosen gene value. 
	 * 
	 * @return An integer between 0 and the MAX_GENE_VALUE of the genetic algorithm.
	 */
	public static int randomIntegerGeneValue() {
		Random rnd = new Random();
		return rnd.nextInt(CuckooSearch.MAX_GENE_VALUE);
	}
	
	/**
	 * Generates a random integer in a specified range.
	 * @param lowerBound the inclusive lower bound for the random integer
	 * @param upperBound the inclusive upper bound for the random integer
	 * @return a random integer in the range [lower bound, upper bound]
	 */
	public static int randomInt(int lowerBound, int upperBound) {
		return ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
	}
	
	/**
	 * Generates a random float.
	 * @return a random float in the range of [0,1)
	 */
	public static float randomFloat() {
		Random rnd = new Random();
		return rnd.nextFloat();
	}
	
	/**
	 * Generates a normal distributed double
	 * @return a random double from the gaussian normal distribution
	 */
	public static double getNormalDistributedDouble() {
		NormalDistribution nd = new NormalDistribution();
		return nd.sample();
	}
	
	/**
	 * Calculates the identity for a double but only if inside a given range.
	 * @param lower lower bound of the range
	 * @param upper upper bound of the range
	 * @param value the value for which the bounded identity will be computed
	 * @return the value if it is inside [lower, upper], otherwise the nearest bound
	 */
	public static double boundedIdentity(double lower, double upper, double value) {
		return Math.max(lower, Math.min(value, upper));
	}
	
	/**
	 * Calculates the value of the gaussian function
	 * @param value the input for the gaussian function
	 * @return the value of the gaussian function for x
	 */
	public static double gaussian(double value) {
		return Math.exp(-1 * (Math.pow(value, 2)) / 0.5);
	}
	
	/**
	 * Converts a gene to a value inside a range
	 * @param geneValue the gene value
	 * @param range upper bound of the range
	 * @return an integer in the range of [0, range]
	 */
	public static int GeneToIntegerRange(int geneValue, double range) {
		return (int) Math.floor(geneValue / CuckooSearch.MAX_GENE_VALUE * range);
	}
	
	/**
	 * Converts a gene to a value inside a range
	 * @param geneValue the gene value
	 * @param range distance of the range from 0
	 * @return an double in the range of [-range, range]
	 */
	public static double GeneToDoubleRange(int geneValue, double range) {
		return ((geneValue - CuckooSearch.MAX_GENE_VALUE / 2.0) * 2.0 / CuckooSearch.MAX_GENE_VALUE) * range;
	}
}
