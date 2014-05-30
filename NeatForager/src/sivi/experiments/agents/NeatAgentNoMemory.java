package sivi.experiments.agents;

import java.util.Vector;

import sivi.experiments.MasterNEATAgent;
import sivi.neat.jneat.evolution.Organism;
import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.scanners.MasterScanner;
import sivi.simulator.simulation.scanners.AmountScanner;
import sivi.simulator.simulation.scanners.MasterScanner.SCANNER_TYPE;
/**
 * The only difference between this class and NEATAgentRelative is the kind of scanner it uses
 * @author Simon
 *
 */
public abstract class NeatAgentNoMemory extends MasterNEATAgent{

	public NeatAgentNoMemory(int id, ENTITYCOLOR color, int startEnergy,
			Organism org, SCANNER_TYPE scannerType) {
		super(id, color, startEnergy, org, scannerType);
	}
	
	private double[] getInputs(World world){
		double[] inputs = new double[scannerList.length * 2 + 2 + 1]; //Last +1 is bias
		int count = 0;
		
		//Read scanner inputs
		for (MasterScanner s : scannerList){
			inputs[count++] = s.scan(world, ENTITYCOLOR.BLUE);
			inputs[count++] = s.scan(world, ENTITYCOLOR.GREEN);
		}
		
		//Test see if agent ate poison or food last turn
		if (atePoisonBefore) inputs[count++] = 1;
		if (ateFoodBefore) inputs[count++] = 1;
		inputs[count++] = 1; //Bias
		
		return inputs;
	}
	
	public DIR getMove(World world) {
		//Update memory
		ateFoodBefore = ateFood;
		atePoisonBefore = atePoison;
		ateFood = false;
		atePoison = false;
		
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
		int action = 1;
		for (int i = 0; i < outputs.length - 1; i++){
			if (outputs[i]> max){
				max = outputs[i];
				action = i;
			}
		}
		
		//See if light should be turned on
		lightOn = outputs[outputs.length-1]>0.5;		

		return DIR.values()[action];
	}
	
	@Override
	public void collisionWith(Entity e) {
		
	}

}
