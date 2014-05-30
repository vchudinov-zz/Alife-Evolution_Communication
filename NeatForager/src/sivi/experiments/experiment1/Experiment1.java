package sivi.experiments.experiment1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import sivi.experiments.MasterEvaluator;
import sivi.experiments.MasterExperiment;
import sivi.neat.jNeatCommon.IOseq;
import sivi.neat.jneat.Neat;
import sivi.neat.jneat.evolution.Organism;
import sivi.neat.jneat.evolution.Population;
import sivi.neat.jneat.evolution.Species;
import sivi.neat.jneat.neuralNetwork.Genome;

public class Experiment1 extends MasterExperiment{
	
	final String delimiter = "/";
	
	//Is set in the code
	String championFolder;
	String epochInfoFolder;
	
	public Experiment1(String propertiesFile) {
		super(propertiesFile);
	}
	
	@Override
	public void runExperiment(){
			
		for (int i = 0; i < numExpRuns; i++){
			System.out.println("Starting experiment run " + i);
			
			//Create ancestor genome
				Genome ancestor = createAncestor();
				
				//Create first population
				System.out.println("Spawning population from ancestor genome");
				Population pop = new Population(ancestor, Neat.p_pop_size);
				
				// Verify population
				System.out.println("Verifying spawned population");
				pop.verify();
				
				//Run experiments
				String mask6 = "000000";
				DecimalFormat fmt6 = new DecimalFormat(mask6);
				int gen = 0;
				int numberOfWinners = 0;
				long simStart = System.nanoTime();
				do {
					long startTime = System.nanoTime();
					// Create folder to save champion info and generation info
					championFolder = outputFolder + "/run" + i + "/Champions";
					testAndCreate(championFolder);

					epochInfoFolder = outputFolder + "/run" + i + "/Epochs";
					testAndCreate(epochInfoFolder);

					System.out.print("\n---------------- E P O C H  < " + gen
							+ " >--------------");
					String filenameEpochInfo = "g_" + fmt6.format(gen);
					try {
						numberOfWinners = goThroughEpoch(pop, gen, filenameEpochInfo, evaluator);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long endTime = System.nanoTime();
					System.out.println("Epoch took " + getElapsedTime(startTime, endTime));
					gen++;
				} while (gen <= maxGenerations && numberOfWinners == 0);
				
				long simEnd = System.nanoTime();
				System.out.println("Simulation run " + i + " took " + getElapsedTime(simStart, simEnd));
				
				//Clean up
				ancestor = null;
				pop = null;
		}
	}
	
	private String getElapsedTime(long start, long end){
		long nanos = end - start;
		
		long days = TimeUnit.NANOSECONDS.toDays(nanos);
		nanos -= TimeUnit.DAYS.toNanos(days);
        long hours = TimeUnit.NANOSECONDS.toHours(nanos);
        nanos -= TimeUnit.HOURS.toNanos(hours);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanos);
        nanos -= TimeUnit.MINUTES.toNanos(minutes);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos);
        nanos -= TimeUnit.SECONDS.toNanos(seconds);
        long milli = TimeUnit.NANOSECONDS.toMillis(nanos);
        
        String time = days + ":" + hours + ":" + minutes + ":" + seconds + ":" + milli; 
		return time;
	}
	
	protected void testAndCreate(String folderPath) {
		File f = new File(folderPath);
		f.mkdirs();
	}
	
	private Genome createAncestor() {
		// Open the file with the genome data
		IOseq ancestorGenomeFile = new IOseq(ancestorFilename);
		boolean ret = ancestorGenomeFile.IOseqOpenR();
		
		Genome ancestor = null;
		if (ret) {
			// Create ancestor genome
			ancestor = createGenome(ancestorGenomeFile);
		} else {
			System.out.println("Error during opening of " + ancestorFilename);
		}
		
		return ancestor;
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
	
	private boolean initializeExperiment(String parameterFileName){
		Neat.initbase();

		// Import the parameters to be used by NEAT
		boolean status = importParameters(parameterFileName);
		if (!status) {
			return false;
		}

		// Save imported parameters to new file
		// Can be used when debugging
		writeParametersToFile(parameterFileName + "- read");
		
		return true;
	}
	
	protected boolean importParameters(String parameterFileName) {
		boolean status = Neat.readParam(parameterFileName);
		if (status) {
			System.out.println("Parameter read okay");
		} else {
			System.out.println("Error in parameter read");
		}

		return status;
	}

	protected void writeParametersToFile(String parameterFileName) {
		Neat.writeParam(parameterFileName);
	}
	

	protected boolean enoughWinnersInPopulation(Population pop,
			double percentageThreshold, int numberOfWinners) {
		int totalNumberOfOrganism = pop.organisms.size();

		double winnerPercentage = (double) numberOfWinners
				/ totalNumberOfOrganism;
		System.out.println();
		System.out.println("Winner percentage: " + winnerPercentage);

		if (winnerPercentage >= percentageThreshold) {
			return true;
		}

		return false;
	}

	/**
	 * Evolves a new generation for the population
	 * 
	 * @param pop
	 * @param generation
	 * @param filenameEpochInfo
	 * @return True if a winner has been found in the population. False
	 *         otherwise
	 * @throws IOException
	 */
	protected int goThroughEpoch(Population pop, int generation,
			String filenameEpochInfo, MasterEvaluator evaluator) throws IOException {
		boolean status = false;
		ArrayList<Organism> winners = new ArrayList<>();

		//Set seed to make sure all are evaluated on the same map
				evaluator.setRandomizerSeed(System.currentTimeMillis());
		
		// Evaluate each organism to see if it is a winner
		boolean win = false;

		Iterator<Organism> itr_organism;
		itr_organism = pop.organisms.iterator();

		while (itr_organism.hasNext()) {
			// point to organism
			Organism curOrganism = ((Organism) itr_organism.next());
			// evaluate
			status = evaluator.evaluate(curOrganism);
			// if is a winner , store a flag
			if (status) {
				win = true;
				winners.add(curOrganism);
			}
		} // Looping through all the organisms in the population

		// compute average and max fitness for each species
		Iterator<Species> itr_specie;
		itr_specie = pop.species.iterator();
		while (itr_specie.hasNext()) {
			Species curSpecie = ((Species) itr_specie.next());
			curSpecie.compute_average_fitness();
			curSpecie.compute_max_fitness();
		}

		// Print best organism to file
		if (winners.isEmpty()) {
			printBest(pop.organisms, generation);
		} else {
			printBest(winners, generation);
		}

		
		// Only print to file every print_every generations
		if (win || (generation % Neat.p_print_every) == 0) {
			pop.print_to_file_by_species(epochInfoFolder + delimiter
					+ filenameEpochInfo);
		}
		
		// if a winner exist, write to file
		if (win) {
			int cnt = 0;
			itr_organism = pop.getOrganisms().iterator();
			while (itr_organism.hasNext()) {
				Organism _organism = ((Organism) itr_organism.next());
				if (_organism.winner) {
					// System.out.print("\n   -WINNER IS #" +
					// _organism.genome.getGenome_id());
					_organism.getGenome().print_to_filename(
							championFolder + delimiter + experimentName
									+ "_win " + cnt);
					cnt++;
				}
			}
		}

		// wait an epoch and make a reproduction of the best species
		pop.epoch(generation);

		if (win) {
			System.out.print("\t\t** I HAVE FOUND A CHAMPION **");
		}

		return winners.size();
	}

	private Organism findBestOrganism(Iterable<Organism> list) {
		double maxFitness = Double.NEGATIVE_INFINITY;
		Organism best = null;
		for (Organism o : list) {
			double myFitness = o.getFitness();
			if (myFitness > maxFitness) {
				best = o;
				maxFitness = myFitness;
			}
		}
		return best;
	}

	private void printBest(Iterable<Organism> list, int generation)
			throws IOException {
		Organism best = findBestOrganism(list);
		double maxFitnessThisGeneration = best.getFitness();

		System.out.println();
		System.out.println("Generation " + generation + " highest fitness: "
				+ maxFitnessThisGeneration);
		String filename = championFolder + delimiter + experimentName + " gen "
				+ generation + " best";
		best.getGenome().print_to_filename(filename);
		saveFitness(generation, maxFitnessThisGeneration);
		// System.out.println("CSV created");
	}

	private void saveFitness(int generation, double fitness) {
		String xNameFile = epochInfoFolder + delimiter + "popFitness.csv";

		// Test if file exists
		File file = new File(xNameFile);

		// Does the file already exist
		if (!file.exists()) {
			try {
				// Try creating the file
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		// Write to file
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(xNameFile, true)));
			out.println(generation + ";" + fitness);
			out.close();
		} catch (IOException e) {
			// oh noes!
		}
	}

	@Override
	protected boolean loadSpecificExperimentProperties() {
		return true;
	}

	

}
