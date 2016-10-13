package tsp.bigbangs;

import java.util.Set;
import tsp.*;

/**
 * Class that generates a random population
 * 
 * @author Andrea Galassi
 *
 */
public interface BigBang {
	
	/**
	 * Generate a new population
	 * @param popsize Number of instances in the population
	 * @param problemsize Size of the instances of the population
	 * @return The new population
	 */
	Set<IndividuoTSP> generatePopulation(int popsize, int problemsize);
}
