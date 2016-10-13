package tsp.judges;

import java.util.*;

/**
 * simple exact TSP solver based on branch-and-bound/Held--Karp partially based
 * on http://stackoverflow.com/questions/7159259/optimized-tsp-algorithms
 * 
 * @author Andrea Galassi
 *
 */
public class HKTSPSolver {
	// number of cities
	private int n;
	// cost matrix
	private int[][] cost;
	// matrix of adjusted costs
	private double[][] costWithPi;
	// node with best solution
	private Node bestNode;

	public HKTSPSolver() {
	}

	/**
	 * Initialize
	 * 
	 * @param size
	 * @param costMatrix
	 */
	public HKTSPSolver(int size, int[][] costMatrix) {
		cost = costMatrix;
		n = size;
	}

	public void setParams(int size, int[][] costMatrix) {
		cost = costMatrix;
		n = size;
	}

	public int solve() {
		bestNode = new Node();
		bestNode.lowerBound = Integer.MAX_VALUE;
		Node currentNode = new Node();
		currentNode.excluded = new boolean[n][n];
		costWithPi = new double[n][n];
		computeHeldKarp(currentNode);
		PriorityQueue<Node> pq = new PriorityQueue<Node>(11, new NodeComparator());

		// ERROR
		// boolean best = false;

		do {
			do {
				// boolean isTour = true;
				int i = -1;
				for (int j = 0; j < n; j++) {
					if (currentNode.degree[j] > 2 && (i < 0 || currentNode.degree[j] < currentNode.degree[i]))
						i = j;
				}
				if (i < 0) {
					if (currentNode.lowerBound < bestNode.lowerBound) {
						bestNode = currentNode;
						// ERROR
						// best = true;
						// System.err.printf("%.0f", bestNode.lowerBound);
					} else {
						// ERROR
						// best = false;
					}
					break;
				}
				// System.err.printf(".");
				PriorityQueue<Node> children = new PriorityQueue<Node>(11, new NodeComparator());
				children.add(exclude(currentNode, i, currentNode.parent[i]));
				for (int j = 0; j < n; j++) {
					if (currentNode.parent[j] == i)
						children.add(exclude(currentNode, i, j));
				}

				// ERROR
				// svuoto il current node (nel caso non sia il bestnode)
				// if (!best) {
				// bestNode.clear();
				// }

				currentNode = children.poll();
				pq.addAll(children);
			} while (currentNode.lowerBound < bestNode.lowerBound);
			// System.err.printf("%n");

			currentNode = pq.poll();

		} while (currentNode != null && currentNode.lowerBound < bestNode.lowerBound);
		// output suitable for gnuplot
		// set style data vector
		// System.out.printf("# %.0f%n", bestNode.lowerBound);
		int j = 0;
		int length = 0;
		do {
			int i = bestNode.parent[j];
			// System.out.println(i+"\t"+cost[i][j]);
			length += cost[i][j];
			j = i;
		} while (j != 0);
		
		return length;
	}

	private Node exclude(Node node, int i, int j) {
		Node child = new Node();
		child.excluded = node.excluded.clone();
		child.excluded[i] = node.excluded[i].clone();
		child.excluded[j] = node.excluded[j].clone();
		child.excluded[i][j] = true;
		child.excluded[j][i] = true;
		computeHeldKarp(child);
		return child;
	}

	private void computeHeldKarp(Node Node) {
		Node.pi = new double[n];
		Node.lowerBound = Integer.MIN_VALUE;
		Node.degree = new int[n];
		Node.parent = new int[n];
		double lambda = 0.1;
		while (lambda > 1e-06) {
			double previousLowerBound = Node.lowerBound;
			computeOneTree(Node);
			if (!(Node.lowerBound < bestNode.lowerBound))
				return;
			if (!(Node.lowerBound < previousLowerBound))
				lambda *= 0.9;
			int denom = 0;
			for (int i = 1; i < n; i++) {
				int d = Node.degree[i] - 2;
				denom += d * d;
			}
			if (denom == 0)
				return;
			double t = lambda * Node.lowerBound / denom;
			for (int i = 1; i < n; i++)
				Node.pi[i] += t * (Node.degree[i] - 2);
		}
	}

	private void computeOneTree(Node Node) {
		// compute adjusted costs
		Node.lowerBound = 0.0;
		Arrays.fill(Node.degree, 0);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				costWithPi[i][j] = Node.excluded[i][j] ? Integer.MAX_VALUE : cost[i][j] + Node.pi[i] + Node.pi[j];
		}
		int firstNeighbor;
		int secondNeighbor;
		// find the two cheapest edges from 0
		if (costWithPi[0][2] < costWithPi[0][1]) {
			firstNeighbor = 2;
			secondNeighbor = 1;
		} else {
			firstNeighbor = 1;
			secondNeighbor = 2;
		}
		for (int j = 3; j < n; j++) {
			if (costWithPi[0][j] < costWithPi[0][secondNeighbor]) {
				if (costWithPi[0][j] < costWithPi[0][firstNeighbor]) {
					secondNeighbor = firstNeighbor;
					firstNeighbor = j;
				} else {
					secondNeighbor = j;
				}
			}
		}
		addEdge(Node, 0, firstNeighbor);
		Arrays.fill(Node.parent, firstNeighbor);
		Node.parent[firstNeighbor] = 0;
		// compute the minimum spanning tree on Nodes 1..n-1
		double[] minCost = costWithPi[firstNeighbor].clone();
		for (int k = 2; k < n; k++) {
			int i;
			for (i = 1; i < n; i++) {
				if (Node.degree[i] == 0)
					break;
			}
			for (int j = i + 1; j < n; j++) {
				if (Node.degree[j] == 0 && minCost[j] < minCost[i])
					i = j;
			}
			addEdge(Node, Node.parent[i], i);
			for (int j = 1; j < n; j++) {
				if (Node.degree[j] == 0 && costWithPi[i][j] < minCost[j]) {
					minCost[j] = costWithPi[i][j];
					Node.parent[j] = i;
				}
			}
		}
		addEdge(Node, 0, secondNeighbor);
		Node.parent[0] = secondNeighbor;
		Node.lowerBound = Math.rint(Node.lowerBound);
	}

	private void addEdge(Node Node, int i, int j) {
		Node.lowerBound += costWithPi[i][j];
		Node.degree[i]++;
		Node.degree[j]++;
	}
}

class Node {
	public boolean[][] excluded;
	// Held--Karp solution
	public double[] pi;
	public double lowerBound;
	public int[] degree;
	public int[] parent;

	public void clear() {
		excluded = null;
		pi = null;
		degree = null;
		parent = null;
	}

}

class NodeComparator implements Comparator<Node> {
	public int compare(Node a, Node b) {
		return Double.compare(a.lowerBound, b.lowerBound);
	}
}