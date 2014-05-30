package sivi.simulator.simulation;

import java.util.ArrayList;
import java.util.EnumMap;

import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Wall;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;

public class World {
	private Entity[][] worldmap;
	private ArrayList<Entity> entitiesOnMap;
	private ArrayList<Entity> foodOnMap;
	private ArrayList<Entity> poisonOnMap;
	private ArrayList<Entity> agentsOnMap;
	private EnumMap<ENTITYCOLOR, Integer> colorCount;
	
	public enum DIR {
		N, S, E, W, NW, NE, SW, SE, STAY
	};

	public World(int x, int y) {
		worldmap = new Entity[x][y];
		entitiesOnMap = new ArrayList<>();
		foodOnMap = new ArrayList<>();
		poisonOnMap = new ArrayList<Entity>();
		agentsOnMap = new ArrayList<Entity>();
		
		colorCount = new EnumMap<>(ENTITYCOLOR.class);
		for (int i = 0; i < Entity.ENTITYCOLOR.values().length; i++){
			colorCount.put(Entity.ENTITYCOLOR.values()[i],-1);
		}
		resetLightCounts();
	}

	/**
	 * Adds the entity e to the world if a) it's not already there and b) no
	 * other entity is at the coordinates
	 * 
	 * @param e
	 * @param x
	 * @param y
	 * @return true if entity was places successfully. False otherwise
	 */
	public boolean addEntity(Entity e, int x, int y) {
		// Test if entity already is in the world
		if (entitiesOnMap.contains(e)) {
			//System.out.println(e.getIdentity() + " already present in the map");
			return false;
		}

		// Add entity
		int[] location = new int[2];
		location[0] = x;
		location[1] = y;
		if (isEmpty(location)) {
			worldmap[x][y] = e;
			e.setLocation(location);
			entitiesOnMap.add(e);
			
			// If the entity is food add it to the list of food 
			if (e.getType() == Entity.TYPE.FOOD) foodOnMap.add(e);
			// Same with poison
			if (e.getType() == Entity.TYPE.POISON) poisonOnMap.add(e);
			// And agents?
			if (e.getType() == Entity.TYPE.AGENT) agentsOnMap.add(e);
			return true;
		} else {
			
			/*
			System.out.println("It is not possible to add " + e.getIdentity()
					+ " at location (" + x + "," + y
					+ "). There is already an entity here.");
			*/
			return false;
		}
	}

	/**
	 * Moves the entity e in the given direction if the new location is free. If
	 * not, a collision with the entity at the new location happens.
	 * 
	 * @param e
	 * @param moveDirection
	 * @return
	 */
	public boolean moveEntity(Entity e, DIR moveDirection) {
		int[] curLocation = e.getLocation();
		int[] newLocation = getNewLocation(curLocation, moveDirection);
		Entity occupant = entityAtLocation(newLocation);
		if (occupant == null) {
			e.setLocation(newLocation);
			worldmap[getX(curLocation)][getY(curLocation)] = null;
			worldmap[getX(newLocation)][getY(newLocation)] = e;
			return true;
		} else {
			occupant.collisionWith(e);
		}
		return false;
	}

	private int getX(int[] location) {
		return location[0];
	}

	private int getY(int[] location) {
		return location[1];
	}

	/**
	 * Test to see if a given location is empty
	 * 
	 * @param location
	 * @return
	 */
	public boolean isEmpty(int[] location) {
		int x = location[0];
		int y = location[1];
		if (worldmap[x][y] == null)
			return true;
		return false;
	}

	/**
	 * Returns the coordinates for the new location. Does not test for out of
	 * bounds
	 * 
	 * @param curLocation
	 * @param dir
	 * @return
	 */
	private int[] getNewLocation(int[] curLocation, DIR dir) {
		int curX = curLocation[0];
		int curY = curLocation[1];
		int[] increments = IncrementCalculator.getIncrements(dir);

		int newLoc[] = new int[2];
		newLoc[0] = curX + increments[0];
		newLoc[1] = curY + increments[1];
		return newLoc;
	}

	/**
	 * Returns the entity at the given location. If the location is outside the
	 * map, a wall is returned, else the entity at the location is returned
	 * 
	 * @param location
	 * @return
	 */
	public Entity entityAtLocation(int[] location) {
		int x = location[0];
		int y = location[1];
		if (withinBounds(x, y)) {
			return worldmap[x][y];
		} else {
			return new Wall(0, ENTITYCOLOR.BLACK, 0);
		}

	}

	/**
	 * Test to see if given coordinates is within the bounds of the map
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean withinBounds(int x, int y) {
		if (x >= 0 && x < worldmap.length && y >= 0 && y < worldmap[0].length) {
			return true;
		}
		return false;
	}

	/**
	 * Returns all the entities currently on the map
	 * 
	 * @return
	 */
	public ArrayList<Entity> getEntitiesOnMap() {
		return entitiesOnMap;
	}
	
	public ArrayList<Entity> getAgentsOnMap() {
		return agentsOnMap;
	}

	public ArrayList<Entity> getFoodOnMap() {
		return foodOnMap;
	}
	
	public ArrayList<Entity> getPoisonOnMap() {
		return poisonOnMap;
	}
	
	
	/**
	 * Removes the entity from the map be removing it from the list of entities
	 * and setting its current position = null
	 * 
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entitiesOnMap.remove(e);
		
		//Remove from poison and food lists
		if (e.getType() == Entity.TYPE.FOOD) foodOnMap.remove(e);
		if (e.getType() == Entity.TYPE.POISON) poisonOnMap.remove(e);
		
		int[] location = e.getLocation();
		int x = location[0];
		int y = location[1];
		worldmap[x][y] = null;
	}

	public Entity[][] getMap() {
		return worldmap;
	}

	public int getTotalAmountOfLight(ENTITYCOLOR lightToScanFor) {
		
		int lightAmount = colorCount.get(lightToScanFor);
		if (lightAmount == -1) {
			// Search hasn't been performed
			lightAmount = 0;
			for (Entity e : entitiesOnMap) {
				if (e != null) {
					if (e.getColor() == lightToScanFor) {
						lightAmount++;
					}
				}
			}
		}
		return lightAmount;
	}

	public void resetLightCounts() {
		for (ENTITYCOLOR c : colorCount.keySet()) {
			colorCount.put(c, -1); // Set to -1 to show that search hasn't been
									// performed
		}
	}
	
	public boolean areNeighbors(Entity a, Entity b){
		int[]location = a.getLocation();
		int x = location[0];
		int y = location[1];
		
		for (int i = -1; i<= 1 ; i++){
			for (int j = -1; j <= 1;j++){
				Entity q = worldmap[x + i][y + j];
				if (b.equals(q)) return true;
			}
		}
		return false;
	}
	/**
	 * Returns the number of entities of the given type directly adjacent to the entity
	 * @param a
	 * @return
	 */
	public int numberOfNeighbours(Entity a, Entity.TYPE type){
		int[]location = a.getLocation();
		int x = location[0];
		int y = location[1];
		int neighbours = 0;
		
		for (int i = -1; i<= 1 ; i++){
			for (int j = -1; j <= 1;j++){
				Entity q = worldmap[x + i][y + j];
				if (q != null){
					if (q.getType() == type){
						neighbours++;
					}
				}
			}
		}
		return neighbours;
	}
}
