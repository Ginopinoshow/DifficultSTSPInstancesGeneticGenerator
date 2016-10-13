package tsp.generators.potions;

import tsp.IndividuoTSP;

/**
 * Mutate an instance to make a new similar instance
 * 
 * @author Andrea Galassi
 *
 */
public interface Potion {

	/**
	 * Modify the given instance to make a new similar instance. The instance
	 * created will always respect the two given bounds
	 * 
	 * @param individuo
	 *            The old instance
	 * @param generazione
	 *            The generation number of the new instance
	 * @param min
	 *            The lower bound of the arc costs
	 * @param max
	 *            The upper bound of the arc costs
	 * @return A new instance, similar to the old one
	 */
	public IndividuoTSP mutate(IndividuoTSP individuo, int generazione, int min, int max);
}
