package tsp.generators.potions;

import java.util.*;

import tsp.IndividuoTSP;

/**
 * Chooses randomly a fixed number of arcs and randomly modify their value
 * between two given percentages (for symmetric TSP)
 * 
 * @author Andrea Galassi
 *
 */
public class RandomArcsPercentagesSPotion implements Potion {

	private Random random;
	private int minp;
	private int maxp;
	private int m;

	/**
	 * 
	 * Chooses randomly a fixed number of arcs and randomly modify their value
	 * between two given percentages (for symmetric TSP)
	 * 
	 * @param arcs
	 *            The number of arcs to change, must be >0
	 * @param minpercentage
	 *            The minimum percentage of changing (inclusive), must be >=0
	 * @param maxpercentage
	 *            The maximum percentage of changing (inclusive), must be >=
	 *            minpercentage
	 */
	public RandomArcsPercentagesSPotion(int arcs, int minpercentage, int maxpercentage) {
		this(new Random(), arcs, minpercentage, maxpercentage);
	}

	/**
	 * 
	 * Chooses randomly a fixed number of arcs and randomly modify their value
	 * between two given percentages (for symmetric TSP)
	 * 
	 * @param r
	 *            The tool used to generate the random coordinates
	 * @param arcs
	 *            The number of arcs to change, must be >0
	 * @param minpercentage
	 *            The minimum percentage of changing (inclusive), must be >=0
	 * @param maxpercentage
	 *            The maximum percentage of changing (inclusive), must be >=
	 *            minpercentage
	 */
	public RandomArcsPercentagesSPotion(Random r, int arcs, int minpercentage, int maxpercentage) {
		super();
		if (minpercentage > maxpercentage || minpercentage < 0)
			throw new IllegalArgumentException(
					"minpercentage must be greater than zero and lesser or equal to maxpercentage!");
		if (arcs <= 0)
			throw new IllegalArgumentException("arcs must be greater than zero!");

		random = r;
		minp = minpercentage;
		maxp = maxpercentage;
		m = arcs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tsp.generators.potions.Potion#evolve(tsp.IndividuoTSP, int, int,
	 * int)
	 */
	@Override
	public IndividuoTSP mutate(IndividuoTSP individuo, int generazione, int min, int max) {
		IndividuoTSP nuovo = new IndividuoTSP(individuo, generazione, false);
		int[][] matrice = nuovo.getCostMatrix();
		int size = nuovo.getSize();
		int x, y;
		// uso due insiemi: uno mi serve per verificare di non generare archi
		// simmetrici, l'altro è quello che scorrerò dopo
		HashSet<Integer> coordinate = new HashSet<Integer>();
		HashSet<Integer> scorrimi = new HashSet<Integer>();

		// genero le coordinate degli archi da cambiare
		for (int i = 0; i < m; i++) {

			// genero le coordinate random finché non sono diverse da quelle già
			// generate
			// memorizzo le coordinate generate nella forma X*size+Y e Y*size+X
			do {
				x = 0;
				y = 0;
				while (x == y) { // finché non vengono diverse
					x = random.nextInt(size);
					y = random.nextInt(size);
				}
			} while (!coordinate.add(x * size + y));
			coordinate.add(y * size + x);
			scorrimi.add(x * size + y);

			// DEBUG
			// System.out.println("coordinate: " + x + ", " + y);
		}

		// cambio gli archi
		for (Integer num : scorrimi) {
			y = num % size;
			x = num / size;
			int valore = matrice[x][y];

			// genero la percentuale di modifica
			int p = minp + random.nextInt(maxp + 1 - minp);
			double variation = p;
			variation = variation / 100;

			// modifico il valore, controllando di non superare i bound
			int nuovovalore = (int) (variation * valore);
			if (nuovovalore > max)
				nuovovalore = max;
			else if (nuovovalore < min)
				nuovovalore = min;

			// DEBUG
			// System.out.println(
			// "valore: " + valore + ", p: " + p + ", variation: " + variation +
			// ", nuovo valore: " + nuovovalore);

			matrice[x][y] = nuovovalore;
			matrice[y][x] = nuovovalore;
		}

		return nuovo;
	}

	@Override
	public String toString() {
		return "RandomArcsPercentagesSPotion " + m + " " + minp + "-" + maxp;
	}

}
