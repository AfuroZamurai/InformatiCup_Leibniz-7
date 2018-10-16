package main.utils;

import java.lang.Math;

public class Evolutionhelper {
	
	public static double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}
	
	public static double sigmoidPrime(double x) {
		return sigmoid(x) * (1 - sigmoid(x));
	}
	
	
}
