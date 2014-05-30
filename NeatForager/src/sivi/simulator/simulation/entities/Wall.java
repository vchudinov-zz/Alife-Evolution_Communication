package sivi.simulator.simulation.entities;

import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;

public class Wall extends Entity {

	public Wall(int id, ENTITYCOLOR color, int startEnergy) {
		super(id, TYPE.WALL, color, startEnergy);
	}

	@Override
	public void collisionWith(Entity e) {
		//System.out.println(e.getIdentity() + " hit a wall");

	}

	@Override
	public DIR getMove(World world) {
		return null;
	}

}
