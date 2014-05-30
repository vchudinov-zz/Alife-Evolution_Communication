package test;

import org.junit.*;

import static org.junit.Assert.*;
import sivi.simulator.simulation.entities.MasterAgent;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Food;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;

public class EntityTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEqualsObject_OnlyDiffID() {
		Entity a = new MasterAgent(0, ENTITYCOLOR.BLUE, 0);
		Entity b = new MasterAgent(1, ENTITYCOLOR.BLUE, 0);
		assertFalse(a.equals(b));
	}

	@Test
	public void testEqualsObject_OnlyDifColor() {
		Entity a = new MasterAgent(0, ENTITYCOLOR.BLUE, 0);
		Entity b = new MasterAgent(0, ENTITYCOLOR.GREEN, 0);
		assertFalse(a.equals(b));
	}

	@Test
	public void testEqualsObject_OnlyDiffType() {
		Entity a = new MasterAgent(0, ENTITYCOLOR.BLUE, 0);
		Entity b = new Food(0, ENTITYCOLOR.BLUE, 0,1);
		assertFalse(a.equals(b));
	}

	@Test
	public void testEqualsObject_AreEqual_SameVar() {
		Entity a = new MasterAgent(0, ENTITYCOLOR.BLUE, 0);
		assertTrue(a.equals(a));
	}

	@Test
	public void testEqualsObject_AreEqual_DifferentVar() {
		Entity a = new MasterAgent(0, ENTITYCOLOR.BLUE, 0);
		Entity b = a;
		assertTrue(a.equals(b));
	}

	@Test
	public void testEqualsObject_AreEqual_DifferentInstance() {
		Entity a = new MasterAgent(0, ENTITYCOLOR.BLUE, 0);
		Entity b = new MasterAgent(0, ENTITYCOLOR.BLUE, 0);
		assertTrue(a.equals(b));
	}

}
