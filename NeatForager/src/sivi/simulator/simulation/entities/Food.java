package sivi.simulator.simulation.entities;

import sivi.simulator.simulation.World;


public class Food extends Mushroom {
	float foodValue;
	Entity[][] worldMap;
	int numOfAgents;
	
	public Food(int id, ENTITYCOLOR color, int startEnergy, float foodValue2, World world) {
		super(id, TYPE.FOOD, color, startEnergy);
		this.foodValue = foodValue2;
		this.worldMap = world.getMap();
		this.numOfAgents = world.getAgentsOnMap().size();
		}

	@Override
	/**
	 * The entity that collides with the food gets 10 energy
	 */
	public void collisionWith(Entity e) {
		float reward = foodValue;
		if (numOfAgents > 8) reward = foodValue/8;
		else reward = foodValue/numOfAgents;
		
		int neighbours = countNeighbors();
		if (this.energy > 0) {
			//No matter how many agents the loss is the same
			this.changeEnergy(foodValue*(-1));
			
			if (e.getType() == TYPE.AGENT){
				MasterAgent a = (MasterAgent) e;
				//More agents = greater fraction of food.
				a.changeEnergy(reward*neighbours);
				a.changeFoodEaten(reward*neighbours);
				a.setAteFood(true);
				
				}
			
				
			}
	}
	
		
	public int countNeighbors(){
		int[]location = this.getLocation();
		int x = location[0];
		int y = location[1];
		int numberOfNeighbours = 0;
		
		for (int i = y-1; i<= y+2 ; i++){
			for (int j = x-1; j <= x+2;j++){
				Entity e = worldMap[i][j];
				if (e != null && !e.equals(this) && e.type.equals(type.AGENT)) numberOfNeighbours++;
			}
		}
		
		return numberOfNeighbours;
		
		}
		
}
	
	


