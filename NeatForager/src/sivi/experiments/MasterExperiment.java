package sivi.experiments;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;

import sivi.neat.jneat.Neat;
import sivi.simulator.simulation.entities.MasterAgent;

public abstract class MasterExperiment {
	
	/** Property keys used in the properties files **/
	//Property keys may not start with p_ as that is used by the NEAT property reader
	private final String KEY_OUTPUT_FOLDER = "output_folder";
	private final String KEY_ANCESTOR_GENOME_FILENAME = "ancestor_filename";
	private final String KEY_EXPERIMENT_NAME = "experiment_name";
	private final String KEY_MAX_GENERATIONS = "max_generations";
	private final String KEY_EVALUATOR_CLASS_NAME = "evaluator_class_name";
	private final String KEY_NUMBER_OF_EXPERIMENT_RUNS = "num_experiment_runs";
	
	private String propertiesFile;
	
	/** Properties **/
	protected Properties expProp;
	protected String outputFolder;
	protected String ancestorFilename;
	protected String experimentName;
	protected int maxGenerations;
	protected int numExpRuns;
	protected MasterEvaluator evaluator;
	
	
	public MasterExperiment(String propertiesFile){
		this.propertiesFile = propertiesFile;
	}
	
	public boolean loadProperties(){
		
		//Load NEAT parameters
		if (!importNEATParameters(propertiesFile)){
			System.out.println("Error in loading NEAT");
			return false;
		}
		
		//Load properties file
		if (!loadPropertiesFile(propertiesFile)){
			System.out.println("Error in loading MasterExperiment properties");
			return false;
		}
		
		//Load evaluator
		if (!loadEvaluator()){
			System.out.println("Error in loading evaluator");
			return false;
		}
		
		//Load experiment properties
		if (!loadGeneralExperimentProperties()){
			System.out.println("Error in loading experiment properties");
			return false;
		}
		
		//Load the specific experiment properies
		if (! loadSpecificExperimentProperties()){
			System.out.println("Error in loading specific experiment properties");
			return false;
		}
		
		return true;		
	}
	
	private boolean loadGeneralExperimentProperties(){
		
		//Find all string properties
		ArrayList<String> arr = new ArrayList<>();
		outputFolder = expProp.getProperty(KEY_OUTPUT_FOLDER);
		arr.add(outputFolder);
		ancestorFilename =  expProp.getProperty(KEY_ANCESTOR_GENOME_FILENAME);
		arr.add(ancestorFilename);
		experimentName = expProp.getProperty(KEY_EXPERIMENT_NAME);
		arr.add(experimentName);
		String maxGenString = expProp.getProperty(KEY_MAX_GENERATIONS);
		arr.add(maxGenString);
		String numberOfRuns = expProp.getProperty(KEY_NUMBER_OF_EXPERIMENT_RUNS);
		arr.add(numberOfRuns);
		
		//Check for null values
		for (int i = 0; i < arr.size(); i++){
			if (arr.get(i) == null) return false;
		}
		
		//Convert to other values if necessary
		maxGenerations = Integer.parseInt(maxGenString);
		numExpRuns = Integer.parseInt(numberOfRuns);
		
		return true;
	}
	
	private boolean importNEATParameters(String parameterFileName) {
		boolean status = Neat.readParam(parameterFileName);
		if (status) {
			System.out.println("Parameter read okay");
		} else {
			System.out.println("Error in parameter read");
		}

		return status;
	}
	
	private boolean loadEvaluator(){
		boolean status = false;
		String evalClassName = expProp.getProperty(KEY_EVALUATOR_CLASS_NAME);
		try {
			Class<?> evalClass = Class.forName(evalClassName);
			Constructor<?> evalCons = evalClass.getConstructor(Properties.class);
			evaluator = (MasterEvaluator) evalCons.newInstance(expProp);
			status  = evaluator.loadProperties();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	private boolean loadPropertiesFile(String propertiesFile){
		boolean status = false;
		expProp = new Properties();
		try{
			InputStream in = new FileInputStream(propertiesFile);
			//Load properties
			expProp.load(in);
			in.close();
			status = true;
		} 
		catch(IOException e){	
			e.printStackTrace();
		}
		
		return status;
	}
	
	/**
	 * Override this in subclasses if there are experiments specific properties that has to be loaded
	 * @return
	 */
	protected abstract boolean loadSpecificExperimentProperties();
	
	public abstract void runExperiment();
	
	
}
