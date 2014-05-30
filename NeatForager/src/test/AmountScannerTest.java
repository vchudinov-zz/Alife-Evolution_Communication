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

public class AmountScannerTest {
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
				
				//Add light sources
				
				addLight();		
				
				//Scan NW
				
				AmountScanner s = new AmountScanner(a, DIR.NW,1);
				assertTrue("NW, Blue",  + s.scan(w, ENTITYCOLOR.BLUE) == 2);
				assertTrue("NW, Red", s.scan(w, ENTITYCOLOR.RED) == 1);
					
				//Scan NE
				
				s = new AmountScanner(a, DIR.NE,1);
				assertTrue("NE, Blue", s.scan(w, ENTITYCOLOR.BLUE) == 0);
				assertTrue("NE, Red", s.scan(w, ENTITYCOLOR.RED) == 2);
				
				//Scan SW
				
				s = new AmountScanner(a, DIR.SW,1);
				assertTrue("SW, Blue", s.scan(w, ENTITYCOLOR.BLUE) == 1);
				assertTrue("SW, Red", s.scan(w, ENTITYCOLOR.RED) == 1);
										
				//Scan SE
				
				s = new AmountScanner(a, DIR.SE,1);
				assertTrue("SE, Blue", s.scan(w, ENTITYCOLOR.BLUE) == 1);
				assertTrue("SE, Red", s.scan(w, ENTITYCOLOR.RED) == 0);
				
				
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
