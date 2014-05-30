package sivi.simulator.simulation.scanners;

import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;

public class AmountScanner extends MasterScanner{
	
	public AmountScanner(Entity master, DIR scanDirection, int lightValue) {
		super(master, scanDirection, lightValue);
	}

	/**
	 * Scans the number of active lightSources of the given color in the scanners
	 * direction
	 * 
	 * @param world
	 * @param lightToScanFor
	 * @return
	 */
	public double scan(World world, ENTITYCOLOR lightToScanFor) {
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
