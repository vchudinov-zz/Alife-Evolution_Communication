package sivi.experiments.agents;

import java.util.Vector;

import sivi.experiments.MasterNEATAgent;
import sivi.neat.jneat.evolution.Organism;
import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;
import sivi.simulator.simulation.scanners.MasterScanner;
import sivi.simulator.simulation.scanners.IntensityScanner;
import sivi.simulator.simulation.scanners.AmountScanner;
import sivi.simulator.simulation.scanners.MasterScanner.SCANNER_TYPE;
/**
 * This agent uses relative light as opposed to the absolute agent that uses absolute light
 * @author Simon
 *
 */
public class NeatAgentRelative2stepmemory extends MasterNEATAgent {
	double previousMove,twoStepsBeforeMove;
	int action;
	boolean previousLamp, twoStepsBeforeLamp;
	
	public NeatAgentRelative2stepmemory(int id, ENTITYCOLOR color, int startEnergy,
			Organism org) {
		super(id, color, startEnergy, org, SCANNER_TYPE.INTENSITY);
		
		
		//Initialize previous move to zero.  
		previousMove = 0;
		twoStepsBeforeMove = 0;
		//Initialize if the creature emitted light previous turn.
		previousLamp = false;
		twoStepsBeforeLamp = false;
				
		//initial action
		action = 1;
	}
	
	private double[] getInputs(World world){
		// ScannerList as follows:
	    // 1 times for color red, one times for color green 
		// +2 for ate poison and ate food
		// + 1 for bias
		// + 4 for previous actions and previous values of creature lamp.
		//
		double[] inputs = new double[scannerList.length * 2 + 2 + 4 + 1]; //Last +1 is bias
		int count = 0;
		
		//Read scanner inputs
		for (MasterScanner s : scannerList){
			inputs[count++] = s.scan(world, ENTITYCOLOR.BLUE);
			inputs[count++] = s.scan(world, ENTITYCOLOR.GREEN);
		}
		
		// Action from t-1 and t-2.  
		inputs[count++] = previousMove;
		inputs[count++] = twoStepsBeforeMove;
		
		// Outputed light from t-1 and t-2 
		if(previousLamp) inputs[count++] = 1;
		else inputs[count++] = 0;
				
		if(twoStepsBeforeLamp) inputs[count++] = 1;
		else inputs[count++] = 0;
				
		//Test see if agent ate poison or food last turn
		if (atePoisonBefore) inputs[count++] = 1;
		if (ateFoodBefore) inputs[count++] = 1;
		inputs[count++] = 1; //Bias
				
		return inputs;
	}
	
	public DIR getMove(World world) {
		//Update memory for t-2
		twoStepsBeforeMove = previousMove;
		twoStepsBeforeLamp = previousLamp;

		//Update memory for t-1
		ateFoodBefore = ateFood;
		atePoisonBefore = atePoison;
		ateFood = false;
		atePoison = false;
				
		//action is normalized.
		previousMove = action/4;
		previousLamp = lightOn;
				
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
		action = 1;
		for (int i = 0; i < outputs.length - 1; i++){
			if (outputs[i]> max){
				max = outputs[i];
				action = i;
			}
		}
		
		//See if light should be turned on
		lightOn = outputs[outputs.length-1]>0.5;
		//Reduce energy by one after move. 
		//this.energy--;
		
		return DIR.values()[action];
	}
	
	@Override
	public void collisionWith(Entity e) {
		
	}

}
