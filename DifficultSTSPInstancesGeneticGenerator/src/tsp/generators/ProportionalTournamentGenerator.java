/**
 * 
 */
package tsp.generators;

import java.util.*;
import tsp.IndividuoTSP;
import tsp.generators.bedrooms.Bedroom;
import tsp.generators.potions.Potion;

/**
 * Use tournaments to choose which instances will be subject to genetic
 * operations. Each instance is randomly putted in a tournament. Each tournament
 * has only one winner. The winner is choosed randomly and the possibilities are
 * proportional to the fitness of the instance. The winner will be destinated to
 * one of the genetic operations
 * 
 * @author Andrea Galassi
 *
 */
public class ProportionalTournamentGenerator extends ModularGenerator {

	private int tournaments;
	private Random r;

	/**
	 * Randomly put every instance in a tournament (balancing the number of
	 * partecipants). Each tournament lead to a genetic operation. The number of
	 * tournaments that lead to crossover and that lead to mutation is specified
	 * (the ramaining tournaments will lead to reproduction)
	 * 
	 * @param potion
	 *            The tool used for mutation
	 * @param bedroom
	 *            The tool used for crossovers
	 * @param r
	 *            The tool used for random generation
	 * @param tournaments
	 *            Number of tournaments (must be >=0). If it's odd, will be
	 *            increased by 1.
	 */
	public ProportionalTournamentGenerator(Potion potion, Bedroom bedroom, Random r, int tournaments) {
		super(potion, bedroom);
		if (tournaments <= 0) {
			throw new IllegalArgumentException("use valid number of tournaments");
		}
		this.tournaments = tournaments;
		this.r = r;
		if (tournaments % 2 == 0) {
			this.tournaments = tournaments;
		} else {
			this.tournaments = tournaments + 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tsp.generators.Generator#generate(java.util.Set, int, int, int)
	 */
	@Override
	public Set<IndividuoTSP> generate(Set<IndividuoTSP> popolazione, int generation, int min, int max) {
		if (min > max)
			throw new IllegalArgumentException("min value cannot be higher than max value");

		HashSet<IndividuoTSP> nuovi = new HashSet<IndividuoTSP>();

		// calcolo il numero di individuo per ogni torneo
		// saranno partecipants o partecipants+1
		int partecipants = popolazione.size() / tournaments;
		int extra = popolazione.size() % tournaments;

		// creo i diversi tornei
		Map<Integer, HashSet<IndividuoTSP>> tornei = new HashMap<Integer, HashSet<IndividuoTSP>>();
		for (int i = 0; i < tournaments; i++) {
			HashSet<IndividuoTSP> torneo = new HashSet<IndividuoTSP>();
			tornei.put(i, torneo);
		}
		int allarm = 0;
		// per ogni individuo scelgo il suo torneo
		for (IndividuoTSP ind : popolazione) {
			int t = r.nextInt(tournaments);
			boolean inserito = false;
			do {
				HashSet<IndividuoTSP> torneo = tornei.get(t);
				// controllo se c'è spazio nel torneo
				if (torneo.size() < partecipants) {
					torneo.add(ind);
					inserito = true;
					allarm=0;
				}
				// se non c'è spazio ma posso sfruttare un posto extra
				else if (torneo.size() == partecipants && extra > 0) {
					torneo.add(ind);
					extra--;
					inserito = true;
					allarm=0;
				}
				// altrimenti cerco un altro torneo
				else {
					t++;
					if (t >= tournaments) {
						t = 0;
					}
					allarm++;
					if (allarm >= 10) {
						System.err.println("BLOCCATO NELL'INSERIMENTO DI UN INDIVIDUO IN UN TORNEO");
					}
				}
			} while (!inserito);
		}
		// eseguo i tornei
		for (int i = 0; i < tournaments; i++) {
			// primo torneo
			HashSet<IndividuoTSP> torneo = tornei.get(i);
			IndividuoTSP ind = null;
			allarm = 0;
			do {
				allarm++;
				if (allarm >= 10) {
					System.err.println("BLOCCATO IN UN TORNEO");
				}
				ind = makeTournament(torneo);
			} while (ind == null);

			// secondo torneo
			i++;
			HashSet<IndividuoTSP> torneo2 = tornei.get(i);
			IndividuoTSP ind2 = null;
			allarm = 0;
			do {
				allarm++;
				if (allarm >= 10) {
					System.err.println("BLOCCATO IN UN TORNEO");
				}
				ind2 = makeTournament(torneo2);
			} while (ind2 == null);

			// crossover
			Set<IndividuoTSP> children = bedroom.crossover(ind, ind2, generation, min, max);

			// mutation
			for (IndividuoTSP child : children) {
				IndividuoTSP bimbo = potion.mutate(child, generation, min, max);
				nuovi.add(bimbo);
			}
		}
		return nuovi;

	}

	private IndividuoTSP makeTournament(HashSet<IndividuoTSP> partecipanti) {
		double sum = 0;
		for (IndividuoTSP individuo : partecipanti) {
			sum = sum + individuo.getScores().get("fitness");
		}
		double score = r.nextDouble() * sum;
		sum = 0;
		for (IndividuoTSP individuo : partecipanti) {
			sum += individuo.getScores().get("fitness");
			if (sum > score) {
				return individuo;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "ProportionalTournamentGenerator " + tournaments;
	}

}
