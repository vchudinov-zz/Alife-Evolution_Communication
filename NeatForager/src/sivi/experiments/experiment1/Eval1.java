package sivi.experiments.experiment1;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.Random;

import sivi.experiments.MasterEvaluator;
import sivi.experiments.MasterNEATAgent;
import sivi.neat.jneat.evolution.Organism;
import sivi.neat.jneat.neuralNetwork.Genome;
import sivi.simulator.simulation.Simulator;
import sivi.simulator.simulation.entities.Entity;
import sivi.simulator.simulation.entities.Food;
import sivi.simulator.simulation.entities.MasterAgent;
import sivi.simulator.simulation.entities.Mushroom;
import sivi.simulator.simulation.entities.Poison;
import sivi.simulator.simulation.entities.Entity.ENTITYCOLOR;
import sivi.simulator.simulation.entities.Entity.TYPE;

public class Eval1 extends MasterEvaluator {
		
	
	public Eval1(Properties prop) {
		super(prop);
	}
	
	@Override
	protected boolean loadSpecificProperties() {
		return true;
	}

	@Override
	public boolean evaluate(Organism organism) {
		double totalFitness = 0;
		
		for (int i = 0 ; i < numberOfEvaluations; i++){
			//Set up simulation 
			Simulator sim = new Simulator(worldSize, dieOnNoEnergy);
			
			//Place entities in the world
			placeAgents(organism, numberOfAgents, energyStartAgent, sim, ENTITYCOLOR.BLUE);
			placeMushroom(numFoodStart, energyStartFood, sim, false);
			placeMushroom(numPoisonStart, energyStartPoison, sim, true);
					
			//Run simulation
			int tick = 0;
			boolean stop = false;
			while (!stop) {
				sim.tick();		
	
				if (tick++ > maxTicks){
					stop = true;
				}
			}
			
			//Calculate fitness for this round
			totalFitness+= calculateFitness(sim);	
			
			//Clean up
			sim = null;
		}
		
		double fitness = totalFitness / (double) numberOfEvaluations;
		organism.setFitness(fitness);
		if (fitness > success_threshold){
			organism.setWinner(true);
			return true;
		}
		return false;
	}
	
		
	/**
	 * Fitness is based on amount of food collected
	 * @param sim
	 * @return
	 */
	private double calculateFitness(Simulator sim){
		double totalFitness = 0; 
		for (Entity e : sim.getWorld().getEntitiesOnMap()){
			if (e.getType() == TYPE.AGENT){ 
				MasterAgent a = (MasterAgent) e;
				totalFitness+= a.getCollectedFood(); 
			}
		}
		
		if (totalFitness == 0) return 0;
		
		return totalFitness / ((double)numFoodStart * energyStartFood);
	}
	
	private void placeAgents(Organism organism, int numberOfAgents, int startEnergy, Simulator sim, Entity.ENTITYCOLOR color){
		boolean success = false;
		int tries = 0;
		for (int i = 0; i < numberOfAgents; i++){
			do {
				MasterNEATAgent a = (MasterNEATAgent) createAgent(new Object[] {i, color, startEnergy, cloneOrganism(organism, 0, 0)}); //Clones the organism to make sure there is no interference between the brains 
				int[] location = getRandomLocation();
				success = sim.addEntity(a, location[0], location[1]);
				tries++;
			} while (!success && tries < 100);
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
					m = new Food(i, ENTITYCOLOR.GREEN, startEnergy,1);
				} else {
					m = new Poison(i, ENTITYCOLOR.GREEN, startEnergy,-1);
				}
				int[] location = getRandomLocation(worldSize / 2, worldSize / 2, worldSize / 2);
					success = sim.addEntity(m, location[0], location[1]);
				tries++;
			} while (!success && tries < 20);
			if (!success) System.out.println("No room for mushroom " + i);
		}
	}
		
	private int[] getRandomLocation(int centerX, int centerY, int maxDistance){
		int min, max, newValue;
		int[] newLocation = new int[2];
		Random random = new Random();
		
		//Get x-coordinate
		min = centerX - maxDistance;
		max = centerX + maxDistance;
		newValue = random.nextInt(max)- min;
		newLocation[0] = moveWithinBounds(newValue, 0, worldSize);
		
		//Get y-coordinate
				min = centerY - maxDistance;
				max = centerY + maxDistance;
				newValue =  random.nextInt(max)- min;
				newLocation[1] = moveWithinBounds(newValue, 0, worldSize);		
		
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
	/**
	 * This is where new agents are created based on the agent class name
	 * As long as we use agents with the same constructor as MasterNEATAgent, this doesn't need to be changed
	 */
	protected MasterAgent createAgent(Object[] constructorArguments){
		MasterAgent a = null;
		try {
			Class<?> agentClass = Class.forName(agentClassName);
			Constructor<?> agentCons = agentClass.getConstructor(int.class, ENTITYCOLOR.class, int.class, Organism.class);
			a = (MasterAgent) agentCons.newInstance(constructorArguments);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			//I know, the catch block is not pretty... /STC
			e.printStackTrace();
		}				
		return a;
	}

}
