package sivi.simulator.simulation.scanners;

import static org.junit.Assert.*;

import org.junit.Test;

import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;
import sivi.simulator.simulation.entities.MasterAgent;

public class LimitedFractionScannerTest {

	@Test
	public void testScan() {
		World world = new World(10, 10);
	
		MasterAgent master = new MasterAgent(0, ENTITYCOLOR.BLUE, 10);
		LimitedFractionScanner testScanner = new LimitedFractionScanner(master, DIR.N, 1,2);
		
		master.setScannerList(1);
		master.addScanner(0, testScanner);
		world.addEntity(master, 5, 1);
		
		double result = master.getScanner(0).scan(world, ENTITYCOLOR.BLUE);
		
		assertEquals(0.0, result, 2);
		
		world.removeEntity(master);
		world.addEntity(master, 5, 5);
		assertEquals(0.0, result, 2);
		
		
	}

//	@Test
//	public void testLimitedFractionScanner() {
//		fail("Not yet implemented");
//	}

}
