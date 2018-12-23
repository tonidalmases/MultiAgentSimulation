package utilities;

import org.apache.commons.math3.random.MersenneTwister;

public class MT19937 {

	private MersenneTwister mt;
	
	public MT19937(int seed) {
		mt = new MersenneTwister(seed);
	}
	
	public double nextDouble() {
		return mt.nextDouble();
	}
	
	public double nextGaussian() {
		return mt.nextGaussian();
	}
	
	public int nextInt(int n) {
		return mt.nextInt(n);
	}
	
}
