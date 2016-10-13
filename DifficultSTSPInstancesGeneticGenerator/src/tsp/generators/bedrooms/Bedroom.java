package tsp.generators.bedrooms;

import java.util.Set;

import tsp.IndividuoTSP;

/**
 * Execute the crossover between two instances of a population
 * 
 * @author Andrea Galassi
 *
 */
public interface Bedroom {
	/**
	 * Mates two instances and generate new instances mixing parts of the two
	 * 
	 * @param i1
	 *            The first instance to mate
	 * @param i2
	 *            The second instance to mate. Must have the same size of the
	 *            first mate
	 * @param generation
	 *            The generation number of the new instances
	 * @param min
	 *            The lower bound of the arc costs
	 * @param max
	 *            The upper bound of the arc costs
	 * @return A set of new instances (typically two instances)
	 */
	Set<IndividuoTSP> crossover(IndividuoTSP i1, IndividuoTSP i2, int generation, int min, int max);
}
