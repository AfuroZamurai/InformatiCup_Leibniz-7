package main.utils;

import java.lang.Math;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import main.evolution.ga.GeneticAlgorithm;

public class Evolutionhelper {
	
	public static double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}
	
	public static double sigmoidPrime(double x) {
		return sigmoid(x) * (1 - sigmoid(x));
	}
	
	public static int randomGeneValue() {
		Random rnd = new Random();
		return rnd.nextInt(GeneticAlgorithm.MAX_GENE_VALUE);
	}
	
	public static int randomInt(int lowerBound, int upperBound) {
		return ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
	}
	
	public static double boundedIdentity(double lower, double upper, double value) {
		return Math.max(lower, Math.min(value, upper));
	}
	
	public static double gaussian(double value) {
		return Math.exp(-1 * (Math.pow(value, 2)) / 0.5);
	}
	
	public static int GeneToIntegerRange(int geneValue, double range) {
		return (int) Math.floor(geneValue / GeneticAlgorithm.MAX_GENE_VALUE * range);
	}
	
	public static double GeneToDoubleRange(int geneValue, double range) {
		return ((geneValue - GeneticAlgorithm.MAX_GENE_VALUE / 2.0) * 2.0 / GeneticAlgorithm.MAX_GENE_VALUE) * range;
	}
}
