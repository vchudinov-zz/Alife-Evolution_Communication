package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sivi.simulator.simulation.IncrementCalculator;
import sivi.simulator.simulation.World;
import sivi.simulator.simulation.World.DIR;
import sivi.simulator.simulation.entities.MasterAgent;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Food;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;

public class WorldTest {
	World w;
	
	@Before
	public void setUp() throws Exception {
		w = new World(50, 50);
	}

	@Test
	public void testAddEntity() {
		//Make sure world is empty
		assertTrue(w.getEntitiesOnMap().isEmpty());
		
		//Add agent
		MasterAgent a  = new MasterAgent(0, ENTITYCOLOR.BLACK, 5);
		int x = 5;
		int y = 5;
		int[] location = {x,y};
		w.addEntity(a, x, y);
		
		//Test that agent is at location
		assertTrue(w.entityAtLocation(location).equals(a));
		
		//Test that agent is first element in entities list
		assertTrue(a.equals(w.getEntitiesOnMap().get(0)));
		
	}

	@Test
	public void testMoveEntity_FreeMovement() {
		
		for (int i = 0; i < World.DIR.values().length; i++){
			//Create and place agent
			MasterAgent a  = new MasterAgent(0, ENTITYCOLOR.BLACK, 5);
			int x = 5;
			int y = 5;
			w.addEntity(a, x, y);
			
			//Move agent in i'th direction
			DIR dir = World.DIR.values()[i];
			w.moveEntity(a, dir);
			
			//Test that it is in the expected tile
			int[] increments = IncrementCalculator.getIncrements(dir);
			int[] expectedLocation ={x + increments[0], y + increments[1]};
			int[] curLocation = a.getLocation();
			
			assertTrue(expectedLocation[0] == curLocation [0] && expectedLocation[1] == curLocation[1]);
			
			//Kill agent to make room for new
			w.removeEntity(a);
		}
		
	}
	
	@Test
	public void testMoveEntity_Collision() {
		//Create and place first agent
		MasterAgent a  = new MasterAgent(0, ENTITYCOLOR.BLACK, 5);
		int x = 5;
		int y = 5;
		w.addEntity(a, x, y);
		
		//Create and place second agent
		int[] increments = IncrementCalculator.getIncrements(DIR.N);		
		MasterAgent b  = new MasterAgent(1, ENTITYCOLOR.BLACK, 5);
		int bx = x + increments[0];
		int by = y + increments[1];
		w.addEntity(b, bx, by);
		
		//Move agent a and expect collision
		assertFalse(w.moveEntity(a, DIR.N));
	}

	@Test
	public void testIsEmpty() {
		int x = 5;
		int y = 5;
		int[] location = {x,y};
		
		//Test empty space
		assertTrue(w.isEmpty(location));
		
		//Test non empty space
		MasterAgent a  = new MasterAgent(0, ENTITYCOLOR.BLACK, 5);
		w.addEntity(a, x, y);
		assertFalse(w.isEmpty(location));
	}

	@Test
	public void testEntityAtLocation() {
		MasterAgent a  = new MasterAgent(0, ENTITYCOLOR.BLACK, 5);
		int x = 5;
		int y = 5;
		
		w.addEntity(a, x, y);
		
		//Test that it is at (x,y) and nowhere else
		Entity[][] map = w.getMap();
		int[] location = new int[2];
		for (int i = 0; i < map.length; i++){
			for (int j = 0; j < map[0].length; j++){
				location[0] = i;
				location[1] = j;
				Entity e = w.entityAtLocation(location);
				if ( i == x && j == y){
					assertTrue(a.equals(e));
				} else {
					assertTrue(e == null);
				}
				
			}
		}
	}

	@Test
	public void testGetTotalAmountOfLight() {
		int number_green = 5;
		int number_red = 7;
		
		//Place green light sources
		for (int i = 0; i < number_green; i++){
			Food f = new Food(i,ENTITYCOLOR.GREEN,1,1);
			w.addEntity(f, i,i);
		}
		
		//Place red light sources
		for (int i = number_green; i < number_green + number_red; i++){
			Food f = new Food(i,ENTITYCOLOR.RED,1,1);
			w.addEntity(f, i,i);
		}
		
		int amountOfGreen = w.getTotalAmountOfLight(ENTITYCOLOR.GREEN);
		assertTrue("Counted green light wrong. I counted " + amountOfGreen , amountOfGreen == number_green);
		
		int amountOfRed = w.getTotalAmountOfLight(ENTITYCOLOR.RED);
		assertTrue("Counted red light wrong. I counted " + amountOfRed,amountOfRed== number_red);
		
		int amountOfBlack = w.getTotalAmountOfLight(ENTITYCOLOR.BLACK);
		assertTrue("Counted black light wrong. I counted "+ amountOfBlack,amountOfBlack == 0);
		
	}

	@Test
	public void testAreNeighbors() {
				
		
		//Create and place first agent
				MasterAgent a  = new MasterAgent(0, ENTITYCOLOR.BLACK, 5);
				int x = 5;
				int y = 5;
				w.addEntity(a, x, y);
				
				//Create and place second agent
				MasterAgent b  = new MasterAgent(1, ENTITYCOLOR.BLACK, 5);
				int bx = x;
				int by = y + 1;
				w.addEntity(b, bx, by);
				
				assertTrue("Failed seeing a neigbor", w.areNeighbors(a, b));
				
				//Create and place non neighbour agent
				MasterAgent c  = new MasterAgent(2, ENTITYCOLOR.BLACK, 5);
				int cx = x + 2;
				int cy = y + 2;
				w.addEntity(c, cx, cy);
				
				assertFalse("Thinks agent c is a neighbor", w.areNeighbors(a, c));
	}

}
