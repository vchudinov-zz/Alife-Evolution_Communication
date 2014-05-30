package sivi.simulator.controller;

import java.awt.Color;

import javax.swing.JFrame;

import sivi.simulator.simulation.Simulator;
import sivi.simulator.simulation.entities.MasterAgent;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Food;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;
import sivi.simulator.view.MapDrawer;

public class Controller {
	// Colors used on the map
	private final Color RED_ON = new Color(255, 0, 0);
	private final Color GREEN_ON = new Color(0, 255, 0);
	private final Color BLUE_ON = new Color(0, 0, 255);
	private final Color RED_OFF = new Color(255, 102, 102);
	private final Color GREEN_OFF = new Color(102, 255, 102);
	private final Color BLUE_OFF = new Color(102, 102, 255);
	private final Color BLACK = new Color(0, 0, 0);
	private final Color GREY = new Color(160, 160, 160);

	private MapDrawer frame;

	// Info about world
	private Simulator sim;
	private static final int WORLD_SIZE = 50;
	private boolean visual;

	public static void main(String[] args) {
		boolean visual = true;
		boolean dieOnNoEnergy = true;
		Controller c = new Controller(WORLD_SIZE, visual, dieOnNoEnergy);
		c.simulate(5000);

	}

	public Controller(int worldSize, boolean showSimulation, boolean dieOnNoEnergy) {
		// Create world
		sim = new Simulator(worldSize, dieOnNoEnergy);

		visual = showSimulation;
		
		// Setup world
		setupWorld(sim);

		// Setup graphics
		if (visual) {
			Color[][] colorMap = convertToColorMap(sim.getMap());
			setupGraphics(colorMap);
		}
	}

	public void simulate(int maxTicks) {
		int interval = 200; // milliseconds between ticks
		int tick = 0;
		boolean stop = false;

		while (!stop) {
			simulateTick();

			if (visual) {
				updateGraphics(sim.getMap());
				try {
					Thread.sleep(interval);
				} catch (Exception e) {

				}
			}

			if (tick++ > maxTicks)
				stop = true;
		}
	}

	private void simulateTick() {
		sim.tick();

	}

	private void setupWorld(Simulator sim) {
		// Place food
		Food f = new Food(0, ENTITYCOLOR.GREEN, 100, 1);
		sim.addEntity(f, 2, 10);

		// Place agent
		MasterAgent a = new MasterAgent(0, ENTITYCOLOR.BLUE, 100);
		sim.addEntity(a, 3, 11);

		// Place another agent
		MasterAgent b = new MasterAgent(1, ENTITYCOLOR.RED, 50);
		sim.addEntity(b, 3, 5);
	}

	private void setupGraphics(Color[][] colorMap) {
		frame = new MapDrawer(colorMap);
		frame.setTitle("Simple forager simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	private void updateGraphics(Entity[][] entityMap) {
		Color[][] colorMap = convertToColorMap(sim.getMap());
		frame.updateColorGrid(colorMap);
		frame.revalidate();
		frame.repaint();

	}

	/**
	 * Converts the map of entities to a color map
	 * 
	 * @param entityMap
	 * @return
	 */
	private Color[][] convertToColorMap(Entity[][] entityMap) {
		int numberOfRows = entityMap[0].length;
		int numberOfCols = entityMap.length;
		Color[][] tmpMap = new Color[numberOfCols][numberOfRows];

		for (int row = 0; row < entityMap[0].length; row++) {
			for (int col = 0; col < entityMap.length; col++) {
				
				// Color map has row in first dimension and columns in second.
				// In the entity map it is the other way around
				Entity e = entityMap[col][row];
				tmpMap[row][col] = getColor(e);
			}
		}
		return tmpMap;
	}

	private Color getColor(Entity e) {
		if (e == null)
			return GREY;

		if (e.lightOn()) {
			switch (e.getColor()) {
			case BLACK:
				return BLACK;
			case BLUE:
				return BLUE_ON;
			case GREEN:
				return GREEN_ON;
			case RED:
				return RED_ON;
			}
		} else {
			// Light is off
			switch (e.getColor()) {
			case BLACK:
				return BLACK;
			case BLUE:
				return BLUE_OFF;
			case GREEN:
				return GREEN_OFF;
			case RED:
				return RED_OFF;
			}
		}
		return null;
	}

}
