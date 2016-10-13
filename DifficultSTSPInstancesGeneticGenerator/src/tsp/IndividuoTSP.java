package tsp;

import java.util.*;

/**
 * An instance of the TSP problem
 * 
 * @author Andrea Galassi
 *
 */
public class IndividuoTSP {

	private static int count = 0;

	private int id;

	private int[][] costMatrix;

	private int size;

	private int generation;

	private Map<String, Double> scores;

	// private int optcost;
	//
	// private ArrayList<Integer> optsolution;

	/**
	 * Creates a new instance of the TSP problem with the given parameters
	 * 
	 * @param size
	 *            The size of the problem (number of nodes)
	 * @param costMatrix
	 *            The matrix with the cost of each arc
	 * @param generation
	 *            The generation which this instance belongs
	 */
	public IndividuoTSP(int size, int[][] costMatrix, int generation) {
		super();
		id = count;
		count++;
		scores = new HashMap<String, Double>();
		this.costMatrix = costMatrix;
		this.size = size;
		this.generation = generation;
	}

	/**
	 * Creates a new instance of the TSP problem using the parameters of the
	 * given instance
	 * 
	 * @param altroindividuo
	 *            The instance from which copy the cost matrix, the size and (if
	 *            specified) the scores
	 * @param generation
	 *            The generation which this instance belongs
	 * @param copyScores
	 *            True if the new instance must have the same scores of the old
	 *            one, false if the new one must do not have scores
	 */
	public IndividuoTSP(IndividuoTSP altroindividuo, int generation, boolean copyScores) {
		super();
		id = count;
		count++;
		if (copyScores)
			scores = new HashMap<String, Double>(altroindividuo.getScores());
		else
			scores = new HashMap<String, Double>();
		size = altroindividuo.getSize();
		this.generation = generation;
		costMatrix = new int[size][size];
		int[][] otherMatrix = altroindividuo.getCostMatrix();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				costMatrix[i][j] = otherMatrix[i][j];
			}
		}
	}

	/**
	 * Creates a new instance of the TSP problem with the given parameters, with
	 * the specified id. BE CAREFUL! There is no control on duplicate IDs
	 * 
	 * @param id
	 *            The unique id of the problem. Be careful! There is no control
	 *            on duplicate IDs
	 * @param size
	 *            The size of the problem (number of nodes)
	 * @param costMatrix
	 *            The matrix with the cost of each arc
	 * @param generation
	 *            The generation which this instance belongs
	 */
	public IndividuoTSP(int id, int size, int[][] costMatrix, int generation) {
		super();
		if (count < id) {
			count = id;
			count++;
		}
		this.id = id;
		scores = new HashMap<String, Double>();
		this.costMatrix = costMatrix;
		this.size = size;
		this.generation = generation;
	}

	/**
	 * The id of the problem
	 * 
	 * @return And integer >=0 that identify uniquely the instance
	 */
	public int getId() {
		return id;
	}

	/**
	 * The cost matrix of the problem
	 * 
	 * @return A nxn matrix, with 0 on the main diagonal, and the cost of the
	 *         arc from i to j in every other position
	 */
	public int[][] getCostMatrix() {
		return costMatrix;
	}

	/**
	 * The size of the problem
	 * 
	 * @return An integer >0 that indicate the number of nodes
	 */
	public int getSize() {
		return size;
	}

	/**
	 * The generation which the instance belongs to
	 * 
	 * @return An integer >0
	 */
	public int getGeneration() {
		return generation;
	}

	/**
	 * The scores of this problem
	 * 
	 * @return A map of <type of judgement , score>
	 */
	public Map<String, Double> getScores() {
		return scores;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "TSP " + id;
		// for (String key : scores.keySet()) {
		// s += "; " + key + ": " + scores.get(key);
		// }
		s += "; " + "fitness" + ": " + scores.get("fitness");
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(costMatrix);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndividuoTSP other = (IndividuoTSP) obj;
		if (!Arrays.deepEquals(costMatrix, other.costMatrix))
			return false;
		return true;
	}

	/**
	 * Prints the cost matrix
	 */
	public String getMatrixAsString() {
		String s = "";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				s += costMatrix[i][j] + "\t";
			}
			s += "\n";
		}
		return s;
	}

	/**
	 * Try the number that will be used as id for the next instance
	 * 
	 * @param id
	 *            The number that will be used as next id
	 * @return true if id is greater than the present number. otherwise return
	 *         FALSE and will not be used the id provided
	 */
	public static boolean setNextId(int id) {
		if (count < id) {
			count = id;
			return true;
		}
		return false;
	}

}
