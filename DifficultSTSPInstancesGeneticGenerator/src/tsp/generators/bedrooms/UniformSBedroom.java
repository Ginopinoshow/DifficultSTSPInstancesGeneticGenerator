package tsp.generators.bedrooms;

import java.util.*;

import tsp.IndividuoTSP;

/**
 * Generates two sons which have for each arc a fixed probability to inherit it
 * from a parent (for symmetric TSP)
 * 
 * @author Andrea Galassi
 *
 */
public class UniformSBedroom implements Bedroom {

	private Random random;
	private int percentage;

	/**
	 * Generates two sons. The probability for each element of the new matrices
	 * to be inherited from one parent or the other is fixed (for symmetric TSP)
	 * 
	 * @param percentage
	 *            Percentage for a son to inherit an element from the first
	 *            parent. Obviously the percentage to inherit it from the other
	 *            parent is 1-p. Must be between 0 and 100
	 */
	public UniformSBedroom(int percentage) {
		this(new Random(), percentage);
	}

	/**
	 * Generates two sons. The probability for each element of the new matrices
	 * to be inherited from one parent or the other is fixed (for symmetric TSP)
	 * 
	 * @param r
	 *            The tool used to generate the possibilities
	 * @param percentage
	 *            Percentage for a son to inherit an element from the first
	 *            parent. Obviously the percentage to inherit it from the other
	 *            parent is 1-p. Must be between 0 and 100
	 */
	public UniformSBedroom(Random r, int percentage) {
		super();
		if (percentage < 0 || percentage > 100)
			throw new IllegalArgumentException("The percentage must be between 0 and 100");
		random = r;
		this.percentage = percentage;
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
		int num;

		for (int z = 0; z < size; z++) {
			newMatrix1[z][z] = 0;
			newMatrix2[z][z] = 0;

			for (int k = z + 1; k < size; k++) {
				num = random.nextInt(100);
				if (num < percentage) {
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

		// Array.Copy(_matrix, newMatrix, size);

		IndividuoTSP n1 = new IndividuoTSP(size, newMatrix1, generation);
		IndividuoTSP n2 = new IndividuoTSP(size, newMatrix2, generation);
		Set<IndividuoTSP> result = new HashSet<IndividuoTSP>();
		result.add(n1);
		result.add(n2);
		return result;
	}

	@Override
	public String toString() {
		return "UniformSBedroom " + percentage;
	}

}
