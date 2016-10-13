package tsp.selectors;

import java.util.*;

import tsp.IndividuoTSP;

/**
 * Selects the best instances from both the populations. In case of draw, the
 * new instance survives
 * 
 * @author Andrea Galassi
 *
 */
public class SteadyStateSelector implements Selector {

	private Comparator<IndividuoTSP> comparatore;
	private int size;

	/**
	 * Use the given comparator to choose the best instances in both the
	 * population
	 * 
	 * @param comparatore
	 *            The tool used to confront the instances
	 * @param size
	 *            The size of the new population. Must be >0
	 */
	public SteadyStateSelector(Comparator<IndividuoTSP> comparatore, int size) {
		super();
		if (size <= 0)
			throw new IllegalArgumentException("size must be >0");
		this.comparatore = comparatore;
		this.size = size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tsp.selectors.Selector#select(java.util.Set, java.util.Set)
	 */
	@Override
	public Set<IndividuoTSP> select(Set<IndividuoTSP> oldPopulation, Set<IndividuoTSP> newPopulation) {
		TreeSet<IndividuoTSP> tree = new TreeSet<IndividuoTSP>(comparatore.reversed());
		HashSet<IndividuoTSP> res = new HashSet<IndividuoTSP>();

		// aggiungo tutti i nuovi
		tree.addAll(oldPopulation);
		tree.addAll(newPopulation);

		while(res.size()<size) {
			res.add(tree.pollFirst());
		}

		// DEBUG
		if (res.size() != size) {
			System.err.println("WTF!!! Size: " + size);
		}

		return res;
	}

	@Override
	public String toString() {
		return "SteadyStateSelector";
	}

	
}
