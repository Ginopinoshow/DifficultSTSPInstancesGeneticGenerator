package tsp.judges;

import java.util.*;

import tsp.IndividuoTSP;

/**
 * Judge that give to each instance of the population a judgement in term of
 * solution and time using the Held-Karp algorithm
 * 
 * @author Andrea Galassi
 *
 */
public class HeldKarpSJudge implements Judge {

	private static HKTSPSolver solver = new HKTSPSolver();

	public HeldKarpSJudge() {

		// risoluzione di prova
		int[][] matrix = new int[4][4];
		for (int i = 0; i < 4; i++) {
			matrix[i][i] = 0;
		}
		matrix[0][1] = 3;
		matrix[0][2] = 1;
		matrix[0][3] = 5;
		matrix[1][2] = 2;
		matrix[1][3] = 4;
		matrix[2][3] = 6;

		matrix[1][0] = 3;
		matrix[2][0] = 1;
		matrix[3][0] = 5;
		matrix[2][1] = 2;
		matrix[3][1] = 4;
		matrix[3][2] = 6;
		solver.setParams(4, matrix);
		solver.solve();
	}

	@Override
	public void judge(Set<IndividuoTSP> popolazione) {

		for (IndividuoTSP individuo : popolazione) {
			///////////
			// DEBUG //
			Date t = new Date();
			System.out.println(t + ": Giudico " + individuo.getId());
			// DEBUG //
			///////////

			int n = individuo.getSize();
			int[][] matrice = individuo.getCostMatrix();
			Map<String, Double> scores = individuo.getScores();
			solver.setParams(n, matrice);
			double sol;

			double time;

			try {
				Date d1 = new Date();
				sol = solver.solve();
				Date d2 = new Date();

				time = d2.getTime() - d1.getTime();

				if (time >= 0) {
					scores.put("timeHKS", time);
					scores.put("solutionHKS", sol);
				}
			} catch (Exception e) {
				System.out.println("Eccezione scatenata da " + e.getMessage());
				time = -1;
			} catch (Error er) {
				System.out.println("Errore scatenato da " + er.getMessage());
				time = -1;
			}
			String temp = "";
			int i = 1;
			while (scores.containsKey("fitness" + temp)) {
				temp = "" + i;
				i++;
			}
			scores.put("fitness" + temp, time);

			///////////
			// DEBUG //
			System.out.println("Individuo " + individuo.getId() + " giudicato: " + time);
			// DEBUG //
			///////////

		}

	}

	@Override
	public String toString() {
		return "HeldKarpSJudge";
	}

}
