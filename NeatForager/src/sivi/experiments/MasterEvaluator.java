package sivi.experiments;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import sivi.neat.jneat.evolution.Organism;
import sivi.simulator.simulation.entities.MasterAgent;

public abstract class MasterEvaluator {
	
	protected Properties prop;
	
	/**Property file keys**/
	private final String KEY_WORLD_SIZE = "world_size";
	private final String KEY_MAX_TICKS = "max_ticks";
	private final String KEY_NUMBER_OF_EVALUATIONS = "num_eval";
	private final String KEY_DIE_ON_NO_ENERGY ="die_on_no_energy";
	private final String KEY_NUM_AGENTS = "num_agents";
	private final String KEY_NUM_FOOD = "num_food";
	private final String KEY_NUM_POISON = "num_poison";
	private final String KEY_ENERGY_START_AGENT = "energy_start_agent";
	private final String KEY_ENERGY_START_FOOD = "energy_start_food";
	private final String KEY_ENERGY_START_POSION = "energy_start_poison";
	private final String KEY_AGENT_CLASS_NAME = "agent_class_name";
	private final String KEY_SUCCESS_THRESHOLD = "success_threshold";
	
	/**Properties**/
	protected int worldSize;
	protected int maxTicks;
	protected int numberOfEvaluations;
	protected boolean dieOnNoEnergy;
	protected int numberOfAgents;
	protected int energyStartAgent;
	protected int numFoodStart;
	protected int energyStartFood;
	protected int numPoisonStart;
	protected int energyStartPoison;
	protected String agentClassName;
	protected double success_threshold;
	
	/** The randomizer used in placing objects*/
	protected Random random;
	protected long seed; 

	public MasterEvaluator(Properties prop){
		this.prop = prop;
		random = new Random();
		seed = System.currentTimeMillis();
	}
	
	public boolean loadProperties(){
		//Find all string properties
		ArrayList<String> arr = new ArrayList<>();
		
		String wsize = prop.getProperty(KEY_WORLD_SIZE);
		arr.add(wsize);
		
		String mticks = prop.getProperty(KEY_MAX_TICKS);
		arr.add(mticks);
		
		String noE = prop.getProperty(KEY_NUMBER_OF_EVALUATIONS);
		arr.add(noE);
		
		String die = prop.getProperty(KEY_DIE_ON_NO_ENERGY);
		arr.add(die);
		
		String noA = prop.getProperty(KEY_NUM_AGENTS);
		arr.add(noA);
		
		String noF = prop.getProperty(KEY_NUM_FOOD);
		arr.add(noF);
		
		String noP = prop.getProperty(KEY_NUM_POISON);
		arr.add(noP);
		
		String esA = prop.getProperty(KEY_ENERGY_START_AGENT);
		arr.add(esA);
		
		String esF = prop.getProperty(KEY_ENERGY_START_FOOD);
		arr.add(esF);
		
		String esP = prop.getProperty(KEY_ENERGY_START_POSION);
		arr.add(esP);
		
		String sucThreshold = prop.getProperty(KEY_SUCCESS_THRESHOLD);
		arr.add(sucThreshold);
		
		agentClassName = prop.getProperty(KEY_AGENT_CLASS_NAME);
		
		//Test for null values
		for (int i = 0; i < arr.size(); i++){
			if (arr.get(i)==null){
				System.out.println("String " + i + " is empty");
				return false;
			}
		}
		
		//Set values
		worldSize = Integer.parseInt(wsize);
		maxTicks = Integer.parseInt(mticks);
		numberOfEvaluations = Integer.parseInt(noE);
		dieOnNoEnergy = Boolean.parseBoolean(die);
		numberOfAgents = Integer.parseInt(noA);
		numFoodStart = Integer.parseInt(noF);
		numPoisonStart = Integer.parseInt(noP);
		energyStartAgent = Integer.parseInt(esA);
		energyStartFood = Integer.parseInt(esF);
		energyStartPoison = Integer.parseInt(esP);
		success_threshold = Double.parseDouble(sucThreshold);
		
		return loadSpecificProperties();
	}
	
	public void setRandomizerSeed(long seed){
		this.seed = seed;
	}
	
	protected abstract MasterAgent createAgent(Object[] constructorArguments);
	
	public abstract boolean evaluate(Organism organism);
	
	/**
	 * Loads properties specific to the evaluator
	 * Overwrite this methods if there is any specifc properties.
	 * @return true as default;
	 */
	protected abstract boolean loadSpecificProperties();
	
}
