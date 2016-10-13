package tsp;

import java.util.*;

public class FitnessComparator implements Comparator<IndividuoTSP> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IndividuoTSP o1, IndividuoTSP o2) {
		if (o1.equals(o2))
			return 0;

		String key = "fitness";
		String temp = "";
		int i = 1;
		int result = 0;

		// finché ho parità di fitness
		while (result == 0) {

			// se uno o entrambi non hanno fitness
			if (!o1.getScores().containsKey(key + temp)) {
				if (!o2.getScores().containsKey(key + temp))
					// se entrambi non hanno fitness, il minore è quello con id maggiore
					return o2.getId() - o1.getId();
				else
					return -1;
			} else if (!o2.getScores().containsKey(key + temp))
				return 1;
			// entrambi hanno un punteggio di fitness
			else {
				result = o1.getScores().get(key + temp).compareTo(o2.getScores().get(key + temp));
			}
			// in caso di parità, scendo di un livello di fitness
			temp = "" + i;
			i++;
		}
		return result;
	}

}
