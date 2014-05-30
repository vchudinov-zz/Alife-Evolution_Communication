package sivi.experiments;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ExperimentController {
	
	private final String KEY_EXPERIMENT_CLASS_NAME = "experiment_class_name";
	private MasterExperiment experiment;
	private Properties expProp;
	private boolean propertiesSet = false; 
	
	public static void main(String[] args) {
		//Loads all the needed classes
		 String propertiesFile = "D:/Projects/neatforager/NeatForager/Experiments/parameters_flormemory";
		//String propertiesFile = args[0];
		
		ExperimentController con = new ExperimentController(propertiesFile);
		
		if (con.propertiesSet()){
			//Run the experiment
			con.runExperiment();
		} else {
			System.out.println("There was an error in the loading of the properties. Experiment will not run");
		}

	}
	
	public ExperimentController(String propertiesFile){ 
		this.propertiesSet = loadExperiment(propertiesFile);
		propertiesSet = experiment.loadProperties();
	}
	
	private boolean loadExperiment(String propertiesFile) {
		expProp = new Properties();
		boolean status = false;
				
		try{
			InputStream in = new FileInputStream(propertiesFile);
			//Load properties
			expProp.load(in);
			in.close();
			
			//Setup Experiment
			String experimentClassName = expProp.getProperty(KEY_EXPERIMENT_CLASS_NAME);
			Class<?> expClass = Class.forName(experimentClassName);
			Constructor<?> expCons = expClass.getConstructor(String.class);
			experiment = (MasterExperiment) expCons.newInstance(propertiesFile);
			status = true;
		} 
		catch(IOException e){	
			e.printStackTrace();
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
	
	public boolean propertiesSet(){
		return propertiesSet;
	}
	
	public void runExperiment(){
		experiment.runExperiment();
	}


}
