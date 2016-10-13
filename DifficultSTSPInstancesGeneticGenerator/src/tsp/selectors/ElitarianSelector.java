/**
 * 
 */
package tsp.selectors;

import java.util.*;

import tsp.IndividuoTSP;

/**
 * Maintains the best k instances of the old population and the best n-k best
 * instances of the new population
 * 
 * @author Andrea Galassi
 *
 */
public class ElitarianSelector implements Selector {

	private Comparator<IndividuoTSP> comparatore;
	private int size;
	private int k;

	/**
	 * Use the given comparator to choose the best k instances of the old
	 * population and the remaining best instances of the new one
	 * 
	 * @param comparatore
	 *            The tool used to confront the instances
	 * @param size
	 *            The size of the new population. Must be different from 0. If
	 *            used negative value, use the whole new population
	 * @param k
	 *            The number of instances to keep from the old population. Must
	 *            be 0<=k<=size
	 */
	public ElitarianSelector(Comparator<IndividuoTSP> comparatore, int size, int k) {
		super();
		if (size == 0 || k < 0 || k > size)
			throw new IllegalArgumentException("size must be >0, k>=0 and k<=size");
		this.comparatore = comparatore;
		this.size = size;
		this.k = k;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tsp.selectors.Selector#select(java.util.Set, java.util.Set)
	 */
	@Override
	public Set<IndividuoTSP> select(Set<IndividuoTSP> oldPopulation, Set<IndividuoTSP> newPopulation) {
		HashSet<IndividuoTSP> result = new HashSet<IndividuoTSP>();

		// ordino i nuovi in ordine inverso
		TreeSet<IndividuoTSP> nuovi = new TreeSet<IndividuoTSP>(comparatore.reversed());
		nuovi.addAll(newPopulation);
		// ordino i vecchi in ordine inverso
		TreeSet<IndividuoTSP> olds = new TreeSet<IndividuoTSP>(comparatore.reversed());
		olds.addAll(oldPopulation);

		Iterator<IndividuoTSP> olditerator = olds.iterator();
		// aggiungo i primi k vecchi
		for (int i = 0; i < k; i++) {
			IndividuoTSP ind = olditerator.next();
			result.add(ind);
		}

		// aggiungo in ordine i nuovi
		Iterator<IndividuoTSP> nuoviterator = nuovi.iterator();
		// finché ci sono dei nuovi da aggiungere e non ho raggiunto la
		// popolazione desiderata (o essa è <0) aggiungo i nuovi
		while (nuoviterator.hasNext() && (result.size() < size || size < 0)) {
			IndividuoTSP ind = nuoviterator.next();
			result.add(ind);
		}

		// se ancora non ho raggiunto la popolazione desiderata aggiungo i
		// vecchi
		while (olditerator.hasNext() && result.size() < size) {
			IndividuoTSP ind = olditerator.next();
			// se è già presente tale individuo (ad esempio per una copia)
			if (!result.add(ind)) {
				// lo rimuovo
				result.remove(ind);
				// e lo riaggiungo
				result.add(ind);
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return "ElitarianSelector " + k;
	}

}
