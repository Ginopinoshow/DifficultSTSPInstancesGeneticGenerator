/**
 * 
 */
package tsp.generators.bedrooms;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import tsp.IndividuoTSP;

/**
 * Randomly choose a point (n,n) of the matrix of the parents. Generates two
 * children: the first one inherit the elements (x,y) with x,y<=n from the first
 * parent and the other from the second one. The second child the opposite
 * 
 * @author Andrea Galassi
 *
 */
public class SinglePointSBedroom implements Bedroom {

	private Random r;

	/**
	 * Randomly choose a point (n,n) of the matrix of the parents. Generates two
	 * children: the first one inherit the elements (x,y) with x,y<=n from the
	 * first parent and the other from the second one. The second child the
	 * opposite
	 * 
	 * @param r
	 *            The tool used to generate the random point
	 */
	public SinglePointSBedroom(Random r) {
		super();
		this.r = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tsp.generators.bedrooms.Bedroom#crossover(tsp.IndividuoTSP,
	 * tsp.IndividuoTSP, int, int, int)
	 */
	@Override
	public Set<IndividuoTSP> crossover(IndividuoTSP i1, IndividuoTSP i2, int generation, int min, int max) {
		int size = i1.getSize();
		if (i2.getSize() != i1.getSize())
			throw new IllegalArgumentException("The two mates must have the same size");

		int[][] newMatrix1 = new int[size][size];

		int[][] newMatrix2 = new int[size][size];
		int num = r.nextInt(size);

		for (int z = 0; z < size; z++) {
			newMatrix1[z][z] = 0;
			newMatrix2[z][z] = 0;

			for (int k = z + 1; k < size; k++) {
				if (k <= num) {
					newMatrix1[z][k] = i1.getCostMatrix()[z][k];
					newMatrix1[k][z] = i1.getCostMatrix()[z][k];

					newMatrix2[z][k] = i2.getCostMatrix()[z][k];
					newMatrix2[k][z] = i2.getCostMatrix()[z][k];
				} else {
					newMatrix1[z][k] = i2.getCostMatrix()[z][k];
					newMatrix1[k][z] = i2.getCostMatrix()[z][k];

					newMatrix2[z][k] = i1.getCostMatrix()[z][k];
					newMatrix2[k][z] = i1.getCostMatrix()[z][k];
				}
			}
		}

		IndividuoTSP n1 = new IndividuoTSP(size, newMatrix1, generation);
		IndividuoTSP n2 = new IndividuoTSP(size, newMatrix2, generation);
		Set<IndividuoTSP> result = new HashSet<IndividuoTSP>();
		result.add(n1);
		result.add(n2);
		return result;
	}

	@Override
	public String toString() {
		return "SinglePointSBedroom";
	}
	
	

}
