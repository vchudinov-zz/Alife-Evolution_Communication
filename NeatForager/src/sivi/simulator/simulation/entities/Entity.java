package sivi.simulator.simulation.entities;

import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;

public abstract class Entity {
	protected int entityX, entityY;
	protected int id;
	protected String identity;
	protected int energy;
	
	
    
	public enum TYPE {
		AGENT, FOOD, POISON, WALL
	};

	public enum ENTITYCOLOR {
		RED, GREEN, BLUE, BLACK
	};

	protected TYPE type;
	protected ENTITYCOLOR color;
	protected boolean lightOn; // Entity only emits light if this is true

	public Entity(int id, TYPE type, ENTITYCOLOR color, int startEnergy) {
		this.id = id;
		this.type = type;
		this.color = color;
		this.energy = startEnergy;

		identity = "" + color + " " + type + "-" + id;
	}

	public void setLocation(int[] location) {
		this.entityX = location[0];
		this.entityY = location[1];
	}

	public int[] getLocation() {
		int[] location = new int[2];
		location[0] = entityX;
		location[1] = entityY;

		return location;
	}

	/**
	 * Called when another entity collides with this
	 * 
	 * @param e
	 *            - the other entity (the aggressor)
	 */
	public abstract void collisionWith(Entity e);

	public TYPE getType() {
		return type;
	}

	public int getID() {
		return id;
	}

	public ENTITYCOLOR getColor() {
		return color;
	}

	/**
	 * The identity is a string consisting of the agent type, color and id
	 * 
	 * @return
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Returns the move direction of the agent. Return null if no move.
	 * Implement move logic here
	 * 
	 * @param map
	 * @return
	 */
	public abstract DIR getMove(World world);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (!identity.equalsIgnoreCase(other.getIdentity()))
			return false;
		return true;
	}

	public void changeEnergy(float f) {
		this.energy += f;
		if (this.energy<0) this.energy = 0;
	}

	
	
	public int getEnergy() {
		return this.energy;
	}

	public boolean lightOn() {
		return this.lightOn;
	}
	
	
	
	
}
