package sivi.experiments;

import java.util.Vector;

import sivi.neat.jneat.evolution.Organism;
import sivi.neat.jneat.neuralNetwork.Network;
import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.MasterAgent;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;
import sivi.simulator.simulation.scanners.FractionScanner;
import sivi.simulator.simulation.scanners.IntensityScanner;
import sivi.simulator.simulation.scanners.AmountScanner;
import sivi.simulator.simulation.scanners.LimitedFractionScanner;
import sivi.simulator.simulation.scanners.MasterScanner.SCANNER_TYPE;

public abstract class MasterNEATAgent extends MasterAgent {
	protected Network brain;
	
	public MasterNEATAgent(int id, ENTITYCOLOR color, int startEnergy, Organism org, SCANNER_TYPE scannerType) {
		super(id, color, startEnergy);
		this.brain = org.getNet();
		setupScanners(scannerType);
		//Default value used only for the scannners with limited range
		
	}	
	
	/**
	 * Adds scanners to the agent. Currently only 4 light scanners are used
	 */
	private void setupScanners(SCANNER_TYPE scannerType) {
		switch(scannerType){
		case INTENSITY:
			//Use relative scanners
			scannerList = new IntensityScanner[4];
			scannerList[0] = new IntensityScanner(this, DIR.NE,1);
			scannerList[1] = new IntensityScanner(this, DIR.NW,1);
			scannerList[2] = new IntensityScanner(this, DIR.SE,1);
			scannerList[3] = new IntensityScanner(this, DIR.SW,1);
			break;
		case AMOUNT:
			//Use absolute scanners
			scannerList = new AmountScanner[4];
			scannerList[0] = new AmountScanner(this, DIR.NE,1);
			scannerList[1] = new AmountScanner(this, DIR.NW,1);
			scannerList[2] = new AmountScanner(this, DIR.SE,1);
			scannerList[3] = new AmountScanner(this, DIR.SW,1);
			break;
		case FRACTION:
			//Use fraction scanners
			scannerList = new FractionScanner[4];
			scannerList[0] = new FractionScanner(this, DIR.NE,1);
			scannerList[1] = new FractionScanner(this, DIR.NW,1);
			scannerList[2] = new FractionScanner(this, DIR.SE,1);
			scannerList[3] = new FractionScanner(this, DIR.SW,1);
			break;
		case LIMITED:
			scannerList = new LimitedFractionScanner[4];
			scannerList[0] = new LimitedFractionScanner(this, DIR.NE,1, 2);
			scannerList[1] = new LimitedFractionScanner(this, DIR.NW,1, 2);
			scannerList[2] = new LimitedFractionScanner(this, DIR.SE,1, 2);
			scannerList[3] = new LimitedFractionScanner(this, DIR.SW,1, 2);
		}
	}
}
