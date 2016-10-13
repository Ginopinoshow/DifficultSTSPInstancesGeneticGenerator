/**
 * 
 */
package tsp.generators;

import java.util.*;

import tsp.IndividuoTSP;
import tsp.generators.bedrooms.Bedroom;
import tsp.generators.potions.Potion;

/**
 * Every instance will be used for crossover. The spawn will be used for
 * mutation
 * 
 * @author Andrea Galassi
 *
 */
public class EveryoneBothOperationGenerator extends ModularGenerator {

	/**
	 * Every instance will be used for crossover. The spawn will be used for
	 * mutation
	 * 
	 * @param potion
	 *            The tool used for mutation
	 * @param bedroom
	 *            The tool used for crossover
	 */
	public EveryoneBothOperationGenerator(Potion potion, Bedroom bedroom) {
		super(potion, bedroom);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tsp.generators.Generator#generate(java.util.Set, int, int, int)
	 */
	@Override
	public Set<IndividuoTSP> generate(Set<IndividuoTSP> popolazione, int generation, int min, int max) {
		if (min > max)
			throw new IllegalArgumentException("min value cannot be higher than max value");
		Set<IndividuoTSP> res = new HashSet<IndividuoTSP>();

		Set<IndividuoTSP> scelti = new HashSet<IndividuoTSP>();
		scelti.addAll(popolazione);

		Set<IndividuoTSP> damutare = new HashSet<IndividuoTSP>();
		Iterator<IndividuoTSP> it = popolazione.iterator();
		// eseguo il crossover
		while (it.hasNext()) {
			IndividuoTSP i1 = it.next();
			// posso fare il crossover
			if (it.hasNext()) {
				IndividuoTSP i2 = it.next();
				// eseguo crossover e aggiungo all'insieme da mutare
				damutare.addAll(bedroom.crossover(i1, i2, generation, min, max));
			} else {
				// è rimasto solo un individuo spaiato
				damutare.add(i1);
			}
		}
		
		///////////
		// DEBUG //
		System.out.println("crossover effettuati");
		// DEBUG //
		//////////
		
		// eseguo le mutazioni
		for (IndividuoTSP i : damutare) {
			res.add(potion.mutate(i, generation, min, max));
		}

		return res;
	}

	@Override
	public String toString() {
		return "EveryoneBothOperationGenerator";
	}

	
	
}
