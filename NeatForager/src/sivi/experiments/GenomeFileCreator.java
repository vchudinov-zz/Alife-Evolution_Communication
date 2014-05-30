package sivi.experiments;

import java.io.File;

import sivi.neat.jNeatCommon.IOseq;

/**
 * This class creates a genome file based on number of inputs and outputs
 * @author Simon
 *
 */
public class GenomeFileCreator {
	

	
	public static void main(String[] args) {
		
		/****
		 * File name is here
		 */
		String fileName = new File("").getAbsolutePath() + "\\OldSchoolFLoreanoMemoryStarter.txt";
		
		/**
		 * Number of inputs and outputs
		 */
		int inputNodes = 11;
		int outputNodes = 10;
		
		/**
		 * Fully connected or not
		 */
		boolean fullyConnected = true;

		GenomeFileCreator gfc = new GenomeFileCreator();
		gfc.createGenomeFile(fileName, inputNodes, outputNodes, fullyConnected);

	}
	
	
	/**
	 * Creates the genome file based on the number of inputs and outputs. It will add the bias node automatically
	 * @param inputNodes
	 * @param outputNodes
	 * @param fullyConnected
	 */
	public void createGenomeFile(String fileName, int inputNodes,
			int outputNodes, boolean fullyConnected) {
		String s = "";
		String delimiter = "\n";

		// Write start of file
		s = "genomestart 1 " + delimiter;
		s = s + "trait 1 0.1 0 0 0 0 0 0 0 " + delimiter;
		s = s + "trait 2 0.1 0 0 0 0 0 0 0 " + delimiter;

		int nodeNumber = 1;

		for (int i = 1; i <= inputNodes; i++) {
			s += "node " + nodeNumber + " 0 1 1 " + delimiter;
			nodeNumber++;
		}

		// Add bias node
		int biasNumber = nodeNumber;
		s += "node " + biasNumber + " 0 1 3 " + delimiter;
		nodeNumber++;

		int outStart = nodeNumber;
		for (int i = 1; i <= outputNodes; i++) {
			s += "node " + nodeNumber + " 0 0 2 " + delimiter;
			nodeNumber++;
		}

		// Add genes 
		int innovationNumber = 1;
		if (fullyConnected){
			for (int i = 1; i <= biasNumber; i++){
				for (int out = outStart; out < nodeNumber; out++) {
					s += "gene 1 " + i + " " + out + " 0 0 " + innovationNumber + " 0 1 " + delimiter;
					innovationNumber++;
				}
			}
		} else {
			for (int out = outStart; out < nodeNumber; out++) {
				s += "gene 1 " + biasNumber + " " + out + " 0 0 " + innovationNumber + " 0 1 " + delimiter;
				innovationNumber++;
			}
		}

		// Add close line
		s += "genomeend 1";

		IOseq writer = new IOseq(fileName);
		writer.IOseqOpenW(false);
		for (String line : s.split(delimiter)) {
			writer.IOseqWrite(line);
		}
		writer.IOseqCloseW();
	}
}
