package sivi.simulator.simulation.scanners;

import sivi.simulator.simulation.IncrementCalculator;
import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;

public abstract class MasterScanner {
	protected Entity master;
	protected DIR scanDirection;
	protected int incrementX;
	protected int incrementY;
	public int range;
	public enum SCANNER_TYPE {AMOUNT, FRACTION, INTENSITY, LIMITED};
	
		// Constant value for the light source. Used to make distance to a light source meaningful
		// Maybe it would be better to have this inside the entities themselves, so that different 
		//things can emmit different amount of light. We don't need this now, as it is beyond the scope of our experiment
	protected int ValueOfLight;
	
	public MasterScanner(Entity master, DIR scanDirection, int lightValue) {
		this.scanDirection = scanDirection;
		int[] increments = IncrementCalculator.getIncrements(scanDirection);
		this.incrementX = increments[0];
		this.incrementY = increments[1];
		this.master = master;
		
		this.ValueOfLight = lightValue;
		
	}
	public MasterScanner(Entity master, DIR scanDirection, int lightValue, int scanRange) {
		this.scanDirection = scanDirection;
		int[] increments = IncrementCalculator.getIncrements(scanDirection);
		this.incrementX = increments[0];
		this.incrementY = increments[1];
		this.master = master;
		this.range = scanRange;
		this.ValueOfLight = lightValue;
		
	}
	
	public abstract double scan(World world, ENTITYCOLOR lightToScanFor);
}
