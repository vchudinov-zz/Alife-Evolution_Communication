package sivi.simulator.simulation.entities;

import java.util.Random;

import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.scanners.MasterScanner;
import sivi.simulator.simulation.scanners.IntensityScanner;
import sivi.simulator.simulation.scanners.AmountScanner;

public class MasterAgent extends Entity {
	protected MasterScanner[] scannerList;
	protected boolean ateFood, atePoison; //Set to true if agent bumps into food / poison
	protected boolean atePoisonBefore, ateFoodBefore; //Used as memory
	// Separate from energy so that we could return only what the agent has consumed.
		protected int foodCollected;
		protected int poisonCollected;

	public boolean isAtePoison() {
		return atePoison;
	}
	public void setScannerList(int len){
		this.scannerList = new MasterScanner[len];
	}
	 public MasterScanner getScanner(int index){
		 return scannerList[index];
	}
	public void addScanner(int index, MasterScanner scanner){
		scannerList[index] = scanner;
	}

	public void setAtePoison(boolean atePoison) {
		this.atePoison = atePoison;
	}

	public boolean isAteFood() {
		return ateFood;
	}

	public void setAteFood(boolean ateFood) {
		this.ateFood = ateFood;
	}

	public MasterAgent(int id, ENTITYCOLOR color, int startEnergy) {
		super(id, TYPE.AGENT, color, startEnergy);
		this.foodCollected = 0;
		this.poisonCollected = 0;
	}

	@Override
	public void collisionWith(Entity e) {
		//System.out.println(identity + ": The " + e.getIdentity()
		//		+ " bumped into me!");
	}

	@Override
	public DIR getMove(World world) {
		int pick = new Random().nextInt(DIR.values().length);

		Random r = new Random();
		this.lightOn = r.nextBoolean();

		return DIR.values()[pick];
	}
	
	public void changeFoodEaten(float foodValue){
		this.foodCollected += foodValue;
	}
	
	public void changePoisonEaten(float poisonValue){
		this.foodCollected += poisonValue;
	}
	
	public int getCollectedFood(){
		return this.foodCollected;
	}

	public int getCollectedPoison(){
		return this.poisonCollected;
	}
	
	
	
	

}
