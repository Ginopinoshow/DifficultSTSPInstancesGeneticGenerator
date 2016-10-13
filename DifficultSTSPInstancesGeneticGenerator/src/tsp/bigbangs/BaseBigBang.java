package tsp.bigbangs;

import java.util.*;

import tsp.IndividuoTSP;

/**
 * Generate a population of TSP problems using an abstract method to generate
 * the cost matrices
 * 
 * @author Andrea Galassi
 *
 */
public abstract class BaseBigBang implements BigBang {

	/**
	 * Generate a cost matrix
	 * @param size The size of the problem (number of the nodes)
	 * @return The cost matrix of the problem
	 */
	abstract int[][] generateMatrix(int size);

	/*
	 * (non-Javadoc)
	 * @see tsp.bigbangs.BigBang#generatePopulation(int, int)
	 */
	@Override
	public Set<IndividuoTSP> generatePopulation(int popsize, int problemsize) {

		HashSet<IndividuoTSP> pop0 = new HashSet<IndividuoTSP>();
		for (int i = 0; i < popsize; i++) {
			int[][] matrice = generateMatrix(problemsize);

			IndividuoTSP individuo = new IndividuoTSP(problemsize, matrice, 0);

			pop0.add(individuo);
		}
		return pop0;
	}

}
