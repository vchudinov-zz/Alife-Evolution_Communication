package sivi.experiments.agents;

import sivi.experiments.MasterNEATAgent;
import sivi.neat.jneat.evolution.Organism;
import sivi.simulator.simulation.scanners.MasterScanner.SCANNER_TYPE;
/**
 * This agent uses relative light as opposed to the absolute agent that uses absolute light
 * @author Simon
 *
 */
public class NeatAgentFraction extends NeatAgentNoMemory {
	
	private static SCANNER_TYPE type = SCANNER_TYPE.FRACTION;
	
	public NeatAgentFraction(int id, ENTITYCOLOR color, int startEnergy,
			Organism org) {
		super(id, color, startEnergy, org, type);
	
	}
	
	

}
