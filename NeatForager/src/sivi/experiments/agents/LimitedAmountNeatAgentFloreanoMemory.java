package sivi.experiments.agents;

import java.util.Vector;

import sivi.experiments.MasterNEATAgent;
import sivi.neat.jneat.evolution.Organism;
import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;
import sivi.simulator.simulation.scanners.MasterScanner;
import sivi.simulator.simulation.scanners.MasterScanner.SCANNER_TYPE;

public class LimitedAmountNeatAgentFloreanoMemory extends MasterNEATAgent{

	private double foodMemory, poisonMemory;
	private boolean ateFood, atePoison, previousLight;
	
	public LimitedAmountNeatAgentFloreanoMemory(int id, ENTITYCOLOR color,
			int startEnergy, Organism org, SCANNER_TYPE scannerType) {
		super(id, color, startEnergy, org, scannerType);
		
		this.foodMemory = 0.0;
		this.poisonMemory = 0.0;
		this.ateFood = false;
		this.atePoison = false;
		this.previousLight = false;
				
		// TODO Auto-generated constructor stub
	}
	
	private double[] getInputs(World world){
		//Diminish memory of food and poison
		foodMemory = foodMemory * 0.95;
		poisonMemory = poisonMemory * 0.95;
		
		double[] inputs = new double[scannerList.length * 2 + 2 + 1 + 1]; //Last +1 is bias
		int count = 0;
		
		//Read light scanner inputs
		for (MasterScanner s : scannerList){
			inputs[count++] = s.scan(world, ENTITYCOLOR.RED);//Food
			
			int range = s.range;
			s.range = world.getMap().length;
			inputs[count++] = s.scan(world, ENTITYCOLOR.BLUE); //Agents
			s.range = range;
			
			}
		
		//Test to see if agent is on poison or food 
		if (ateFood) {foodMemory = 1;}
		if (atePoison) {poisonMemory = 1;}
		inputs[count++] = foodMemory;
		inputs[count++] = poisonMemory;
		if(previousLight) inputs[count++] = 1;
		
		else inputs[count++] = 0;
		
		inputs[count++] = 1; //Bias
		
		return inputs;
	}
	
	public DIR getMove(World world) {
		previousLight = lightOn;
		
		//Collect inputs
		double[] inputs = getInputs(world);
		//Give inputs to brain
		brain.load_sensors(inputs);
		
		//Get outputs		
		brain.activate();
		Vector<sivi.neat.jneat.neuralNetwork.NNode> outputNodes = brain.getOutputs();
		
		double[] outputs = new double[outputNodes.size()];
		for (int i = 0; i < outputNodes.size(); i++) {
			outputs[i] = outputNodes.get(i).getActivation();
		}
		
		brain.flush();
		
		//Get move action
		double max = Double.NEGATIVE_INFINITY;
		int action = -1;
		for (int i = 0; i < outputs.length - 1; i++){
			if (outputs[i]> max){
				max = outputs[i];
				action = i;
			}
		}
		
		//See if light should be turned on
		
		lightOn = outputs[outputs.length-1]>0.5;		
		
		// set food and poison to false for the next iteration
		
		ateFood = false;
		atePoison = false;
		return DIR.values()[action];
	}
	
	public void setAteFood(boolean ate){
		this.ateFood = ate;
	}
	public void setAtepoison(boolean ate){
		this.ateFood = ate;
	}

}
