/*
	track controller to hold blocks that are sent to washy
*/

package modules.trackmodel;

import shared.*;

public class Controllers {
	int[][] occupancy;
	int[][] convert;
	int numControllers;
	//int[] side2;
	
	Controllers(int numControllers) {
		occupancy = new int[numControllers][];
		convert = new int[numControllers][];
		this.numControllers = numControllers;
	}
	
	public void init() {
		java.io.File input;
		java.util.Scanner scan;
		int blocks;
		try {
			input = new java.io.File("controller_config.csv");
			scan = new java.util.Scanner(input).useDelimiter(",|\\r\\n");
		}
		catch (Exception e) {
			System.err.println("The config file 'controller_config.csv' could not be found");
		}
		
		blocks = scan.nextInt();
		
	}
}


