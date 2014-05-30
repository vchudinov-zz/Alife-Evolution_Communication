package sivi.simulator.simulation.entities;

import java.util.Random;

import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;

public abstract class Mushroom extends Entity {

	public Mushroom(int id, TYPE type, ENTITYCOLOR color, int startEnergy) {
		super(id, type, color, startEnergy);
		lightOn = true; // Lights is always on for mushrooms
	}

	@Override
	/**
	 * Mushrooms doesn't move
	 */
	public DIR getMove(World world) {
		this.lightOn = true;
		return null;
	}

}
