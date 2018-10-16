package main.evolution.network;

/**
 * Implementation of a compositional pattern producing network (CPPN) which will indirectly encode an image.
 * 
 * @author Felix
 *
 */
public class CPPN {
	private final int inlen;
	private final int outlen;
	private final Config config;
	
	/**
	 * Instantiate the CPPN.
	 * 
	 * @param inlen number of input nodes
	 * @param outlen number of output nodes
	 * @param config config for this CPPN
	 */
	public CPPN(int inlen, int outlen, Config config) {
		this.inlen = inlen;
		this.outlen = outlen;
		this.config = config;
		
		assert config.getMaxHlSize() >= 0 && config.getMinHlSize() >= 0;
		assert config.getMaxHlSize() >= config.getMinHlSize();
	}
	
	
}
