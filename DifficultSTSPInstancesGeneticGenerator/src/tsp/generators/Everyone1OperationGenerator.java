/**
 * 
 */
package tsp.generators;

import java.util.*;

import tsp.IndividuoTSP;
import tsp.generators.bedrooms.Bedroom;
import tsp.generators.potions.Potion;

/**
 * Every instance has a uniform probability to be choosen for copy, crossover or
 * evolution
 * 
 * @author Andrea Galassi
 *
 */
public class Everyone1OperationGenerator extends ModularGenerator {

	private Random random;
	private boolean copy;

	/**
	 * Every instance has a uniform probability to be choosen for copy,
	 * crossover or mutation
	 * 
	 * @param r
	 *            The tool used for the possibilities
	 * @param potion
	 *            The tool used for the mutation
	 * @param bedroom
	 *            The tool used for the crossover
	 * @param copy
	 *            If the operation of copy must be used
	 */
	public Everyone1OperationGenerator(Random r, Potion potion, Bedroom bedroom, boolean copy) {
		super(potion, bedroom);
		random = r;
		this.copy = copy;
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
		Set<IndividuoTSP> scelti = new HashSet<IndividuoTSP>();
		scelti.addAll(popolazione);

		// DEBUG
		// System.out.println("Numero di scelti:" + scelti.size());

		HashSet<IndividuoTSP> nuovi = new HashSet<IndividuoTSP>();
		ArrayList<IndividuoTSP> toBed = new ArrayList<IndividuoTSP>();
		for (IndividuoTSP individuo : scelti) {
			int number;
			if (copy) {
				number = random.nextInt(3);
			} else {
				number = random.nextInt(2);
			}
			// destinati al crossover
			if (number == 0) {
				toBed.add(individuo);
			}
			// mutazione
			else if (number == 1) {
				IndividuoTSP nuovo = potion.mutate(individuo, generation, min, max);
				nuovi.add(nuovo);
			}
			// copia
			else if (number == 2) {
				IndividuoTSP nuovo = new IndividuoTSP(individuo, generation, false);
				nuovi.add(nuovo);
			}
		}
		// se il numero di oggetti destinati al crossover è dispari
		if (toBed.size() % 2 != 0) {
			IndividuoTSP individuo = toBed.remove(0);
			int number;
			if (copy) {
				number = random.nextInt(2);
			} else {
				number = random.nextInt(1);
			}
			// mutazione
			if (number == 0) {
				IndividuoTSP nuovo = potion.mutate(individuo, generation, min, max);
				nuovi.add(nuovo);
			}
			// copia
			else if (number == 1) {
				IndividuoTSP nuovo = new IndividuoTSP(individuo, generation, false);
				nuovi.add(nuovo);
			}
		}

		// crossover
		while (toBed.size() > 0) {
			IndividuoTSP mate1 = toBed.remove(0);
			IndividuoTSP mate2 = toBed.remove(0);

			Set<IndividuoTSP> children = bedroom.crossover(mate1, mate2, generation, min, max);

			for (IndividuoTSP i : children) {
				nuovi.add(i);
			}
		}
		return nuovi;
	}

	@Override
	public String toString() {
		return "Everyone1OperationGenerator-"+copy;
	}

}
