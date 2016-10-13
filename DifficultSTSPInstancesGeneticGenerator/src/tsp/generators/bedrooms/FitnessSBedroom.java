package tsp.generators.bedrooms;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import tsp.IndividuoTSP;

/**
 * Generate two sons, the probability that one son (the favourite) inherit an
 * arc from the parent with higher fitness is
 * fitnessofbestparent/sumoffitnessofparents (for symmetric TSP)
 * 
 * @author Andrea Galassi
 *
 */
public class FitnessSBedroom implements Bedroom {

	private Random random;

	/**
	 * Generate two sons, the probability that one son (the favourite) inherit
	 * an arc from the parent with higher fitness is
	 * fitnessofbestparent/sumoffitnessofparents (for symmetric TSP)
	 */
	public FitnessSBedroom() {
		this(new Random());
	}

	/**
	 * Generate two sons, the probability that one son (the favourite) inherit
	 * an arc from the parent with higher fitness is
	 * fitnessofbestparent/sumoffitnessofparents (for symmetric TSP)
	 * 
	 * @param r
	 *            The tool used to generate the possibilities
	 */
	public FitnessSBedroom(Random r) {
		super();
		random = r;
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

		double fitness1 = i1.getScores().get("fitness");
		double fitness2 = i2.getScores().get("fitness");
		double sum = fitness1 + fitness2;
		int percentage = (int) (fitness1 / sum);

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

		IndividuoTSP n1 = new IndividuoTSP(size, newMatrix1, generation);
		IndividuoTSP n2 = new IndividuoTSP(size, newMatrix2, generation);
		Set<IndividuoTSP> result = new HashSet<IndividuoTSP>();
		result.add(n1);
		result.add(n2);
		return result;
	}

}
