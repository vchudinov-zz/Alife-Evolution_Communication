package sivi.simulator.simulation.entities;

import sivi.simulator.simulation.entities.Entity.TYPE;

public class Poison extends Mushroom {
	float poisonValue;
	public Poison(int id, ENTITYCOLOR color, int startEnergy, float foodValue) {
		super(id, TYPE.POISON, color, startEnergy);
		this.poisonValue = foodValue;
	}

	@Override
	/**
	 * The entity that collides with the poison loses 50 energy
	 */
	public void collisionWith(Entity e) {
		//Only substract energy if entity has energy to give
		if (e.getEnergy() > 0){
			e.changeEnergy(poisonValue);						
			this.changeEnergy(poisonValue);
			
			if (e.getType() == TYPE.AGENT){
				MasterAgent a = (MasterAgent) e;
				a.changePoisonEaten(poisonValue);
				a.setAtePoison(true);
			}
			
		}
		
	}

}
