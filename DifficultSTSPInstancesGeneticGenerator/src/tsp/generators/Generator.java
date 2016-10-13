package tsp.generators;

import java.util.Set;

import tsp.IndividuoTSP;

/**
 * Class that generates new population from existing ones
 * 
 * @author Andrea Galassi
 *
 */
public interface Generator {

	/**
	 * Generates a new population from the existing one
	 * 
	 * @param popolazione
	 *            The previous population
	 * @param generation
	 *            The number of the new generation
	 * @param min
	 *            The lower bound of the arc costs
	 * @param max
	 *            The upper bound of the arc costs
	 * @return A new population
	 * 
	 */
	Set<IndividuoTSP> generate(Set<IndividuoTSP> popolazione, int generation, int min, int max);
}
