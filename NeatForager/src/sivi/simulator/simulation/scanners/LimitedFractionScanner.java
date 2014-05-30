package sivi.simulator.simulation.scanners;

import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;

public class LimitedFractionScanner extends MasterScanner {
	
	public LimitedFractionScanner(Entity master, DIR scanDirection,
			int lightValue, int range) {
		super(master, scanDirection, lightValue);
		
	}
	@Override
	public double scan(World world, ENTITYCOLOR lightToScanFor) {
		Entity[][] worldMap = world.getMap();
		double result = 0.f;
		
		int[] entityLocation = master.getLocation();
		
		int x = entityLocation[0];
		int rangecounterX = 0;
		
		while (x < worldMap.length && x >= 0){
		    
			int y = entityLocation[1];
		    int rangecounterY = 0;
		    
		    while (y < worldMap[0].length && y >=0){
				Entity e = worldMap[x][y];
				if (e != null && !e.equals(master)) {
					if (e.lightOn() && e.getColor() == lightToScanFor) {
						result ++;
					}
				}
				y = y + incrementY;
				rangecounterY++;
				if (rangecounterY > range) break;
				
			}
			x = x + incrementX;
			rangecounterX++;
			if (rangecounterX > range) break;
		}
	
				
		return result;
	}
	
	
	
}
