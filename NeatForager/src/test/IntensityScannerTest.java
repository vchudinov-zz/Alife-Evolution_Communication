package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Food;
import sivi.simulator.simulation.entities.MasterAgent;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;
import sivi.simulator.simulation.scanners.AmountScanner;
import sivi.simulator.simulation.scanners.IntensityScanner;

public class IntensityScannerTest {

	World w;
	
	@Before
	public void setUp() throws Exception {
		//Create world
		w = new World(50, 50);
	}

	@Test
	public void testScan() {
		//Create agent in the middle of the world
				MasterAgent a = new MasterAgent(0, ENTITYCOLOR.BLUE, 1);
				w.addEntity(a, 25, 25);
				int[] entityLocation = a.getLocation();
						
				//Add light sources
				addLight();		
				
				//Create scanner
				int lightValue = 1;
				IntensityScanner s = new IntensityScanner(a, DIR.NW, lightValue);
				//Scan NW
				
				// Locations of the test entities in NW
				
				int[] blue1NW = w.getEntitiesOnMap().get(1).getLocation();
				double bluedist = s.distanceToLight(entityLocation[0], blue1NW[0], entityLocation[1], blue1NW[1]);
					
				int[] blue2NW = w.getEntitiesOnMap().get(2).getLocation();
				double blue2Distance = s.distanceToLight(entityLocation[0], blue2NW[0], entityLocation[1], blue2NW[1]);
				
				int[] red = w.getEntitiesOnMap().get(3).getLocation();
				double redDistance = s.distanceToLight(entityLocation[0], red[0], entityLocation[1], red[1]);
				
				double expectedBlue = lightValue/bluedist + lightValue/blue2Distance;
				double expectedRed = lightValue/redDistance;
				
				assertEquals("NW, Blue ", s.scan(w, ENTITYCOLOR.BLUE),expectedBlue, 0.001);
				assertEquals("NW, Red ", s.scan(w, ENTITYCOLOR.RED),expectedRed, 0.001);
				
				//Scan NE
				s = new IntensityScanner(a, DIR.NE, lightValue);
				
				int[] red1NE = w.getEntitiesOnMap().get(4).getLocation();
				int[] red2NE = w.getEntitiesOnMap().get(5).getLocation();
				
				double expected = lightValue/s.distanceToLight(entityLocation[0], red1NE[0], entityLocation[1], red1NE[1]) +
						lightValue/s.distanceToLight(entityLocation[0], red2NE[0], entityLocation[1], red2NE[1]);
				
				assertEquals("NE, Blue ", s.scan(w, ENTITYCOLOR.BLUE),0, 0.0001);
				assertEquals("NE, Red ", s.scan(w, ENTITYCOLOR.RED),expected, 0.001);
				
				//Scan SW
				
				s = new IntensityScanner(a, DIR.SW, lightValue);
				int[] blueSW = w.getEntitiesOnMap().get(6).getLocation();
				int[] redSW = w.getEntitiesOnMap().get(7).getLocation();
				
				expectedBlue = lightValue/s.distanceToLight(entityLocation[0], blueSW[0], entityLocation[1], blueSW[1]);
				expectedRed = lightValue/s.distanceToLight(entityLocation[0], redSW[0], entityLocation[1], redSW[1]);
				
				assertEquals("SW, Blue", s.scan(w, ENTITYCOLOR.BLUE), expectedBlue, 0.001);
				assertEquals("SW, Red", s.scan(w, ENTITYCOLOR.RED), expectedRed, 0.001);
						
				//Scan SE
				
				s = new IntensityScanner(a, DIR.SE, lightValue);
				int[] blueSE = w.getEntitiesOnMap().get(8).getLocation();
				expected = lightValue/s.distanceToLight(entityLocation[0], blueSE[0], entityLocation[1],blueSE[1]);
				assertEquals("SE, Blue", s.scan(w, ENTITYCOLOR.BLUE), expected, 0.001);
				assertEquals("SE, Red", s.scan(w, ENTITYCOLOR.RED), 0, 0.0001);
	}

	@Test
	public void testDistanceToLight() {
		//Create agent in the middle of the world
				MasterAgent a = new MasterAgent(0, ENTITYCOLOR.BLUE, 1);
				w.addEntity(a, 25, 25);
								
				//Add light sources
				addLight();		
						
				//Test Distance function
				IntensityScanner s = new IntensityScanner(a, DIR.NW, 1);
				int[] agentLocation = a.getLocation();
						
				//NW Blue
				int[] entity = w.getEntitiesOnMap().get(1).getLocation();
				double distance_blue = s.distanceToLight(agentLocation[0], entity[0] , agentLocation[1], entity[1]);
				assertTrue("NW, Blue - " + distance_blue, distance_blue == 37.0);
						
				entity = w.getEntitiesOnMap().get(2).getLocation();
				double distance_blue2 = s.distanceToLight(agentLocation[0],entity[0] , agentLocation[1], entity[1]);
						
				assertTrue("NW, Blue2 - " + distance_blue2, distance_blue2 == 45.0);
						
				//NW Red
				entity = w.getEntitiesOnMap().get(3).getLocation();
				double distance_red = s.distanceToLight(agentLocation[0],entity[0] , agentLocation[1], entity[1]);
						
				assertTrue("NW, Red - " + distance_red, distance_red == 44.0);
	}
	
	private void addLight(){
		//Add light sources in each quadrant
				int  i = 0;
				//NW
				w.addEntity(new Food(i++, ENTITYCOLOR.BLUE, 1,1), 1, 12); 
				w.addEntity(new Food(i++, ENTITYCOLOR.BLUE, 1,1), 3, 2); 
				w.addEntity(new Food(i++, ENTITYCOLOR.RED, 1,1), 1, 5);
				
				//NE
				w.addEntity(new Food(i++, ENTITYCOLOR.RED, 1,1), 26, 2);
				w.addEntity(new Food(i++, ENTITYCOLOR.RED, 1,1), 40, 1);
				
				//SW
				w.addEntity(new Food(i++, ENTITYCOLOR.BLUE, 1,1), 1, 47);
				w.addEntity(new Food(i++, ENTITYCOLOR.RED, 1,1), 3, 29);
				
				//SE
				w.addEntity(new Food(i++, ENTITYCOLOR.BLUE, 1,1), 33, 33);		
	}

}
