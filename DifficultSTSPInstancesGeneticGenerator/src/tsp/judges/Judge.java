package tsp.judges;

import java.util.*;

import tsp.IndividuoTSP;

public interface Judge {

	/**
	 * Judges the entire population, adding its valuation to the scores, and
	 * adding the fitness value. If a fitness value is already setted, it adds
	 * another fitness value calling it fitness1 or fitness2 and so long...
	 * 
	 * @param popolazione
	 *            The population of instances of the problem
	 */
	public void judge(Set<IndividuoTSP> popolazione);
}
