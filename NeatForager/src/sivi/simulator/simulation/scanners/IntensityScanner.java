package sivi.simulator.simulation.scanners;

import java.io.ObjectInputStream.GetField;

import sivi.simulator.simulation.IncrementCalculator;
import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;
/** A new scanner class that works on just returning a value form each scanner, as opposed to a fraction of the total light. 
 * 
 * 
 * @author Cybelechild
 *
 */
public class IntensityScanner extends MasterScanner {
	

	public IntensityScanner(Entity master, DIR scanDirection, int lightValue) {
		super(master, scanDirection, lightValue);
	}



	/** Returns the light amount in the given quadrant. Unlike the getAbsoluteLight, this one prioritizes lights 
	 *  that are closer to the individual. 
	 * 
	 * @param world
	 * @param lightToScanFor
	 * @see distanceToLight
	 * @return
	 */
	public double scan(World world, ENTITYCOLOR lightToScanFor) {
		Entity[][] worldMap = world.getMap();
		
		float result = 0.f;
		
		int[] entityLocation = master.getLocation();
		int x = entityLocation[0];
		
		while (x < worldMap.length && x >= 0){
		    int y = entityLocation[1];
		    
		    while (y < worldMap[0].length && y >=0){
				Entity e = worldMap[x][y];
				if (e != null && !e.equals(master)) {
					if (e.lightOn() && e.getColor() == lightToScanFor) {
						// Currently we take simply the proportion between distance and the value we give to each light
						// If the fraction becomes too small for larger worlds we should consider an alternative formula
						result += ValueOfLight/distanceToLight(entityLocation[0], x, entityLocation[1], y);
					}
				}
				y = y + incrementY;
			}
			x = x + incrementX;
		}

		return result;

	}
		
	
	/**
	 * Made public to be able to test
	 * @param x0
	 * @param x1
	 * @param y0
	 * @param y1
	 * @return
	 */
	public double distanceToLight(int x0, int x1, int y0, int y1)
	{	// The Manhattan distance from the creature to the light
		double distanceToLight = Math.abs(x0 - x1) + Math.abs(y0 - y1);  
		
		//Alternatively the euclidian distance. We can give it a try
		//double distanceToLight = Math.sqrt((x0 - x1))^2 + (y0 - y1)^2);  
		
		return distanceToLight;
		
	}
}
