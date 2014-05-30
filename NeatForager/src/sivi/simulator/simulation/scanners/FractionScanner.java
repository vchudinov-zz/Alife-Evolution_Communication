package sivi.simulator.simulation.scanners;

import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;

public class FractionScanner extends MasterScanner{
	
	public FractionScanner(Entity master, DIR scanDirection, int lightValue) {
		super(master, scanDirection, lightValue);
	}

	/**
	 * Returns the fraction of a specific light type in the given direction
	 * Fraction = light in section / total light in map
	 * 
	 * @param worldMap
	 * @param lightToScanFor
	 * @param absolute If true the unweighted amount of light in a quadrant is used. 
	 * 		  If false the amount of light in a quadrant is based on the scanner's distance form the light source
	 * @return
	 */
	public double scan(World world, ENTITYCOLOR lightToScanFor) {
		int amountOfLightInMap = world.getTotalAmountOfLight(lightToScanFor);

		if (amountOfLightInMap == 0)
			return 0;
		
		if (lightToScanFor == master.getColor()){
			//The light of the agent should not be included in the scanning
			amountOfLightInMap--;
		}
		
		double	amountOfLightInThisSection = scanAbsoluteLightInSection(world, lightToScanFor);		
		
		double result = (double) amountOfLightInThisSection
				/ (double) amountOfLightInMap;

		return result;
	}

	/**
	 * Scans the number of active lightSources of the given color in the scanners
	 * direction
	 * 
	 * @param world
	 * @param lightToScanFor
	 * @return
	 */
	private double scanAbsoluteLightInSection(World world, ENTITYCOLOR lightToScanFor) {
		Entity[][] worldMap = world.getMap();
		double result = 0.f;
		
		int[] entityLocation = master.getLocation();
		int x = entityLocation[0];
		
		while (x < worldMap.length && x >= 0){
		    int y = entityLocation[1];
		    
		    while (y < worldMap[0].length && y >=0){
				Entity e = worldMap[x][y];
				if (e != null && !e.equals(master)) {
					if (e.lightOn() && e.getColor() == lightToScanFor) {
						result ++;
					}
				}
				y = y + incrementY;
			}
			x = x + incrementX;
		}

		return result;
	}	
}
