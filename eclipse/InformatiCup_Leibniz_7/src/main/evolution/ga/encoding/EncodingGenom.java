package main.evolution.ga.encoding;

import java.util.Iterator;
import java.util.List;

import main.evolution.ga.GenericGenom;

/**
 * Genom representation of an arbitrary encoding
 * 
 * @author Felix
 *
 */
public class EncodingGenom extends GenericGenom<EncodingGene> {
	
	public EncodingGenom(float initialFitness, List<EncodingGene> genes) {
		super(initialFitness, genes);
	}
	
	public EncodingGenom( ) {
		super();
	}
	
	public float[] getAllParameters() {
		int size = parameterSize();
		float[] params = new float[size];
		
		int index = 0;
		for(EncodingGene gene : genes) {
			List<Float> parameters = gene.getValues();
			for (Iterator<Float> iterator = parameters.iterator(); iterator.hasNext();) {
				Float curParameter = iterator.next();
				params[index] = curParameter;
				index++;
			}
		}
		
		return params;
	}
	
	private int parameterSize() {
		int size = 0;
		for(EncodingGene gene : genes) {
			size += gene.getGeneLength();
		}
		
		return size;
	}
}
