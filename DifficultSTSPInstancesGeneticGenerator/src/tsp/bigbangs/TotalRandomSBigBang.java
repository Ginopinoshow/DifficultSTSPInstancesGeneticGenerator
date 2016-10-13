package tsp.bigbangs;

import java.util.Random;

/**
 * Generate a population of Symmetric TSP with random value of the cost matrix
 * 
 * @author Andrea Galassi
 *
 */
public class TotalRandomSBigBang extends BaseBigBang {

	private int minvalue;
	private int maxvalue;
	private Random random;

	/**
	 * Create a random generator of the Symmetric TSP, which creates cost
	 * matrices with values between two limits
	 * 
	 * @param minvalue
	 *            The lower bound of cost of the arcs (inclusive)
	 * @param maxvalue
	 *            The upper bound of cost of the arcs (exclusive)
	 */
	public TotalRandomSBigBang(int minvalue, int maxvalue) {
		this(new Random(), minvalue, maxvalue);
	}

	/**
	 * Create a random generator of the Symmetric TSP, which creates cost
	 * matrices with values between two limits
	 * 
	 * @param r
	 *            The tool used to generate random instances
	 * @param minvalue
	 *            The lower bound of cost of the arcs (inclusive)
	 * @param maxvalue
	 *            The upper bound of cost of the arcs (exclusive)
	 */
	public TotalRandomSBigBang(Random r, int minvalue, int maxvalue) {
		super();
		this.minvalue = minvalue;
		this.maxvalue = maxvalue;
		random = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tsp.bigbangs.BaseBigBang#generateMatrix(int)
	 */
	@Override
	int[][] generateMatrix(int n) {
		int[][] matrix = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					matrix[i][j] = 0;
				} else {
					int value = minvalue + random.nextInt(maxvalue - minvalue);
					matrix[i][j] = value;
					matrix[j][i] = value;
				}
			}
		}
		return matrix;
	}

	@Override
	public String toString() {
		return "TotalRandomSBigBang " + minvalue + "-" + maxvalue;
	}

}
