package sivi.simulator.simulation;

import sivi.simulator.simulation.World.DIR;

/**
 * Used to make sure that increments are calculated the same way in all places
 * where it is needed
 * 
 * @author Simon
 * 
 */
public class IncrementCalculator {

	public static int[] getIncrements(DIR directionToScan) {
		int[] increments = new int[2];
		switch (directionToScan) {
		case N:
			increments[0] = 0;
			increments[1] = -1;
			break;
		case S:
			increments[0] = 0;
			increments[1] = 1;
			break;
		case E:
			increments[0] = 1;
			increments[1] = 0;
			break;
		case W:
			increments[0] = -1;
			increments[1] = 0;
			break;

		case NW:
			increments[0] = -1;
			increments[1] = -1;
			break;
		case NE:
			increments[0] = 1;
			increments[1] = -1;
			break;
		case SW:
			increments[0] = -1;
			increments[1] = 1;
			break;
		case SE:
			increments[0] = 1;
			increments[1] = 1;
			break;
		case STAY:
			increments[0] = 0;
			increments[1] = 0;
			break;
		default:
			break;
		}

		return increments;
	}

}
