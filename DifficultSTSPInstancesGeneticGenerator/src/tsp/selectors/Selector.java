package tsp.selectors;

import java.util.Set;

import tsp.IndividuoTSP;

/**
 * Given two population (the old one and the new one), select instances from
 * both to give birth to a new population
 * 
 * @author Andrea Galassi
 *
 */
public interface Selector {

	/**
	 * Select the instances for the new population from both the old and the new
	 * population
	 * 
	 * @param oldPopulation
	 *            The population from which the new population has been built
	 * @param newPopulation
	 *            The population formed from mutations, copies and crossovers of
	 *            the old one
	 * @return A new population with instances taken from both the population,
	 *         choosing with some criterion
	 */
	Set<IndividuoTSP> select(Set<IndividuoTSP> oldPopulation, Set<IndividuoTSP> newPopulation);
}
