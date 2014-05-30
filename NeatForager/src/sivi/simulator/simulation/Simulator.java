package sivi.simulator.simulation;

import java.util.ArrayList;

import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Food;

public class Simulator {
	private World world;
	private Entity[][] worldMap;
	private boolean dieOnNoEnergy;
	// the number of food and poison on the map
	private int food, poison;

	/**
	 * Instantiates the simulator and adds a quadratic world of size worldSize x
	 * worldSize
	 * 
	 * @param worldSize
	 */
	public Simulator(int worldSize, boolean dieOnNoEnergy) {
		// Create world
		world = new World(worldSize, worldSize);
		worldMap = world.getMap();
		
		this.dieOnNoEnergy = dieOnNoEnergy;
	}

	/**
	 * Performs all calculation related to one tick in the simulation
	 */
	public void tick() {
		// Get current world map
		worldMap = world.getMap();

		// Reset counts of light
		world.resetLightCounts();

		// Move all entities
		DIR direction;
		for (Entity e : world.getEntitiesOnMap()) {
			direction = e.getMove(world);
			if (direction != null) {
				world.moveEntity(e, direction);
			}
		}

		if (dieOnNoEnergy) {
			// Test to see if any entities should be removed from the world
			ArrayList<Entity> toDie = new ArrayList<>();
			for (Entity e : world.getEntitiesOnMap()) {
				if (e.getEnergy() <= 0) {
					toDie.add(e);
				}
			}

			// Kill off entities from the world
			for (Entity e : toDie) {
				world.removeEntity(e);
				//System.out.println(e.getIdentity() + " died!");
			}
			
		}
	}

	/**
	 * Places the entity e int the world at the coordinate (x,y)
	 * 
	 * @param e
	 * @param x
	 * @param y
	 */
	public boolean addEntity(Entity e, int x, int y) {
		return world.addEntity(e, x, y);
	}

	public Entity[][] getMap() {
		return worldMap;
	}
	
	public World getWorld(){
		return world;
	}

}
