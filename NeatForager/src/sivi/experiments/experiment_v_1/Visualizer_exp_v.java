package sivi.experiments.experiment_v_1;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JFrame;

import sivi.experiments.MasterEvaluator;
import sivi.experiments.MasterNEATAgent;
import sivi.experiments.agents.NeatAgentWith2StepMemory;
import sivi.neat.jNeatCommon.IOseq;
import sivi.neat.jneat.evolution.Organism;
import sivi.neat.jneat.neuralNetwork.Genome;
import sivi.simulator.controller.Controller;
import sivi.simulator.simulation.Simulator;
import sivi.simulator.simulation.entities.MasterAgent;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Food;
import sivi.simulator.simulation.entities.Mushroom;
import sivi.simulator.simulation.entities.Poison;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;
import sivi.simulator.simulation.entities.Entity.TYPE;
import sivi.simulator.simulation.scanners.MasterScanner.SCANNER_TYPE;
import sivi.simulator.view.MapDrawer;

public class Visualizer_exp_v extends MasterEvaluator {
	

		// Colors used on the map
		private final Color RED_ON = new Color(255, 0, 0);
		private final Color GREEN_ON = new Color(0, 255, 0);
		private final Color BLUE_ON = new Color(0, 0, 255);
		private final Color RED_OFF = new Color(255, 102, 102);
		private final Color GREEN_OFF = new Color(102, 255, 102);
		private final Color BLUE_OFF = new Color(102, 102, 255);
		private final Color BLACK = new Color(0, 0, 0);
		private final Color GREY = new Color(160, 160, 160);
		private final Color POISON_YELLOW = new Color(204, 204, 0);

		private MapDrawer frame;

		
		public static void main(String[] args){
			
			String propertiesFile = args[0];
			String genomeToVisualize = args[1];
			
			// String genomeToVisualize = "D:/Projects/neatforager/NeatForager/exp/268";
			// String propertiesFile = "D:/Projects/neatforager/NeatForager/Experiments/parameters_flormemory";
			//Simulate
			Visualizer_exp_v v = new Visualizer_exp_v(loadPropertiesFile(propertiesFile));
			v.loadProperties();
			
			//Create organism
			String fileName = genomeToVisualize;
			Organism o = v.makeBrain(fileName);
			
			v.simulate(o);
		}
		
		public Visualizer_exp_v(Properties prop) {
			super(prop);
		}		
		
		private static Properties loadPropertiesFile(String propertiesFile){
			Properties expProp = new Properties();
			try{
				InputStream in = new FileInputStream(propertiesFile);
				//Load properties
				expProp.load(in);
				
				in.close();
			} 
			catch(IOException e){	
				e.printStackTrace();
			}
			
			return expProp;
		}
		
		private void simulate(Organism organism) {
			//Set up simulation 
			Simulator sim = new Simulator(50, dieOnNoEnergy);
			
			//Place entities in the world
			placeAgents(organism, numberOfAgents, energyStartAgent, sim, ENTITYCOLOR.BLUE);
			placeMushroom(numFoodStart, energyStartFood, sim, false);
			placeMushroom(numPoisonStart, energyStartPoison, sim, true);
			
			// Setup graphics
			Color[][] colorMap = convertToColorMap(sim.getMap());
			setupGraphics(colorMap);
			
			
			int interval = 200; // milliseconds between ticks
			int tick = 0;
			boolean stop = false;

			while (!stop) {
				sim.tick();
				System.out.println("Tick " + tick + "/" + maxTicks);

				updateGraphics(sim.getMap(), sim);
				try {
					Thread.sleep(interval);
				} catch (Exception e) {

				}

				if (tick++ > maxTicks)
					stop = true;
			}
		}

		
		private void placeAgents(Organism organism, int numberOfAgents, int startEnergy, Simulator sim, Entity.ENTITYCOLOR color){
			boolean success = false;
			int tries = 0;
			for (int i = 0; i < numberOfAgents; i++){
				do {
					MasterNEATAgent a = (MasterNEATAgent) createAgent(new Object[] {i, color, startEnergy, cloneOrganism(organism, 0, 0), SCANNER_TYPE.INTENSITY}); //Clones the organism to make sure there is no interference between the brains 
					int[] location = getRandomLocation(worldSize / 2, worldSize / 2, worldSize / 2);
					success = sim.addEntity(a, location[0], location[1]);
					tries++;
				} while (!success && tries < 20);
				if (!success) System.out.println("No room for agent " + i);
			}
		}
		
		/**
		 * It's not pretty, but it works.
		 * Taken from the Species class
		 * count and generation are not important
		 * @param org
		 * @param count
		 * @param generation
		 * @return
		 */
		private Organism cloneOrganism(Organism org, int count, int generation){
			Organism mom = org;
			Genome new_genome = mom.genome.duplicate(count);
			//Clone mom without any mutations
			Organism baby = new Organism(0.0, new_genome, generation); 
			
			return baby;
		}
		
		private void placeMushroom(int number, int startEnergy, Simulator sim, boolean poison){
			boolean success = false;
			int tries = 0;
			
			
			for (int i = 0; i < number; i++){
				do {
					Mushroom m = null;
					if (!poison){
						m = new Food(i, ENTITYCOLOR.RED, startEnergy, 5, sim.getWorld());
					} else {
						m = new Poison(i, ENTITYCOLOR.RED, startEnergy, -5);
					}
					int location = random.nextInt(5);
					int[] locationFood = getLocationFromCorner(location);
					success = sim.addEntity(m, locationFood[0], locationFood[1]);
					tries++;
				} while (!success && tries < 20);
				if (!success) System.out.println("No room for mushroom " + i);
			}
		}
		
		private int[] getLocationFromCorner(int corner){
			int third = worldSize/3;
			int x = 0;
			int y = 0;
			switch(corner){
			case 0 : x = third; y = third; break; //NW
			case 1 : x = 2*third; y = 2*third; break; //NE
			case 2 : x = third; y = 2*third; break; //SW
			case 3 : x = 2*third; y = third; break; //SE
			case 4 : x = worldSize/2; y = worldSize/2; break; //Center. The cornerest corner of them all.
			}
			
			int[] result = {x,y};
			return result;
		}	
		
		private int[] getRandomLocation(int centerX, int centerY, int maxDistance){
			int min, max, newValue;
			int[] newLocation = new int[2];
			Random random = new Random();
			
			//Get x-coordinate
			min = centerX - maxDistance;
			max = centerX + maxDistance;
			newValue = random.nextInt(max)- min;
			newLocation[0] = moveWithinBounds(newValue, min, worldSize);
			
			//Get y-coordinate
					min = centerY - maxDistance;
					max = centerY + maxDistance;
					newValue =  random.nextInt(max)- min;
					newLocation[1] = moveWithinBounds(newValue, min, worldSize);
			
			
			return newLocation;
		}
		
		
		private int[] getRandomLocation(){
			int newValue;
			int[] newLocation = new int[2];
			Random random = new Random();
			
			//Get x-coordinate
			newValue = random.nextInt(worldSize);
			newLocation[0] = moveWithinBounds(newValue, 0, worldSize);
			
			//Get y-coordinate
					newValue =  random.nextInt(worldSize);
					newLocation[1] = moveWithinBounds(newValue, 0, worldSize);
			
			
			return newLocation;
		}
		
		private int moveWithinBounds(int i, int min, int max){
			if (i < min) return min;
			if (i > max) return max;
			return i;
		}

		private void setupGraphics(Color[][] colorMap) {
			frame = new MapDrawer(colorMap);
			frame.setTitle("Simple forager simulator");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);

		}

		private void updateGraphics(Entity[][] entityMap, Simulator sim) {
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
			
			if (e.getType() == TYPE.POISON){
				return POISON_YELLOW;
			}

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
		
		private Organism makeBrain(String genomeFileName) {
			Organism o = null;
			// Open the file with the genome data
			IOseq starterGenomeFile = new IOseq(genomeFileName);
			boolean ret = starterGenomeFile.IOseqOpenR();

			if (ret) {
				// Create starter genome
				Genome testGenome = createGenome(starterGenomeFile);

				// Create organism
				o = new Organism(0, testGenome, 1);

			} else {
				System.out.println("Error during opening of " + genomeFileName);
			}
			return o;
		}

		private Genome createGenome(IOseq starterGenomeFile) {
			String curWord;

			System.out.println("Read genome");

			// Read file
			String line = starterGenomeFile.IOseqRead();
			StringTokenizer st = new StringTokenizer(line);

			// Skip first word in file
			curWord = st.nextToken();

			// Read ID of the genome
			curWord = st.nextToken();
			int id = Integer.parseInt(curWord);

			// Create the genome
			System.out.println("Create genome id " + id);
			Genome startGenome = new Genome(id, starterGenomeFile);

			return startGenome;

		}

		@Override
		protected MasterAgent createAgent(Object[] constructorArguments){
			MasterAgent a = null;
			try {
				Class<?> agentClass = Class.forName(agentClassName);
				Constructor<?> agentCons = agentClass.getConstructor(int.class, ENTITYCOLOR.class, int.class, Organism.class, SCANNER_TYPE.class);
				a = (MasterAgent) agentCons.newInstance(constructorArguments);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				//I know, the catch block is not pretty... /STC
				e.printStackTrace();
			}				
			return a;
		}

		@Override
		public boolean evaluate(Organism organism) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean loadSpecificProperties() {
			// TODO Auto-generated method stub
			return false;
		}
}
