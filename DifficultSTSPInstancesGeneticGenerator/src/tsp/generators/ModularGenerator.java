package tsp.generators;


import tsp.generators.bedrooms.*;
import tsp.generators.potions.*;

/**
 * Generates new population from an existing population using the given modules
 * to choose the instances and do crossover and evolution
 * 
 * @author Andrea Galassi
 *
 */
public abstract class ModularGenerator implements Generator {

	Bedroom bedroom;
	Potion potion;
	

	public ModularGenerator(Potion potion, Bedroom bedroom) {
		super();
		this.bedroom = bedroom;
		this.potion = potion;
	}

	public Bedroom getBedroom() {
		return bedroom;
	}

	public void setBedroom(Bedroom bedroom) {
		this.bedroom = bedroom;
	}

	public Potion getPotion() {
		return potion;
	}

	public void setPotion(Potion potion) {
		this.potion = potion;
	}

	
}
