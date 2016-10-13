package tsp.generators.potions;

import java.util.*;

import tsp.IndividuoTSP;

/**
 * Choose randomly two arcs and exchange their cost (for symmetric TSP)
 * 
 * @author Andrea Galassi
 *
 */
public class SwapperSPotion implements Potion {

	private Random random;

	/**
	 * Choose randomly two arcs and exchange their cost (for symmetric TSP)
	 * 
	 * @author Andrea Galassi
	 *
	 */
	public SwapperSPotion() {
		random=new Random();
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
		int x1 = 0, y1 = 0, x2 = 0, y2 = 0;

		while (x1 == y1) { // finché non vengono diverse
			x1 = random.nextInt(size);
			y1 = random.nextInt(size);
		}

		while (x2 == y2 || (x1 == x2 && y1 == y2) || (x1 == y2 && y1 == x2)) {
			// finché non vengono diverse
			x2 = random.nextInt(size);
			y2 = random.nextInt(size);
		}

		int val1 = matrice[x1][y1];
		int val2 = matrice[x2][y2];

		matrice[x1][y1] = val2;
		matrice[y1][x1] = val2;
		matrice[x2][y2] = val1;
		matrice[y2][x2] = val1;

		return nuovo;
	}

	@Override
	public String toString() {
		return "SwapperSPotion";
	}
	
	

}
