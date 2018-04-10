/*
	track controller to hold blocks that are sent to washy
*/
/*
this will hold the data that the track controller will need and facilitate conversion to a format readable to the module
currently has fixed size arrays so no controller can have more than 25 blocks
*/

package modules.trackmodel;

import shared.*;

public class Controller {
	int[][] occupancy;
	int[][] convert;
	int numControllers;
	int[] switchConvert;
	//int[] side2;
	
	Controller(int numControllers) {
		occupancy = new int[numControllers][25];
		convert = new int[numControllers][25];
		this.numControllers = numControllers;
		switchConvert = new int[numControllers];
	}
	
	public void init() {
		java.io.File input;
		java.util.Scanner scan, scanLine;
		int blockid;
		int contNumber = 0;
		int i;
		String switchLocs;
		try {
			input = new java.io.File("controller_config.csv");
			scan = new java.util.Scanner(input).useDelimiter(",|\\r\\n");
		}
		catch (Exception e) {
			System.err.println("The config file 'controller_config.csv' could not be found");
		}
		
		switchLocs = scan.nextLine();
		scanLine = new java.util.Scanner(switchLocs);
		while (scanLine.hasNext()){
			this.setSwitchConvert(scan.nextInt(), i);
			i++;
		}
		
		while (scan.hasNext()) {
			blockid = 0;
			String line = scan.nextLine();
			scanLine = new java.util.Scanner(line).useDelimiter(",");
			while (scanLine.hasNext()) {
				convert[contNumber][blockid++] = scanLine.nextInt();
			}
			contNumber++;
		}
		
		
	}
	
	public boolean[] getOccArray(int controllerid) {
		boolean[] arr = new boolean[25];
		for (int i = 0; i< 25; i++) {
			arr[i] = occupancy[controllerid][i] == 1;
		}
		return arr;
	}
	
	public void update(int realBlock, int occ) {
		for (int i = 0; i<numControllers; i++) {
			for (int j = 0; j<25; j++) {
				if (convert[i][j] == realBlock) {
					occupancy[i][j] = occ;
					break;
				}
			}
		}
	}
	
	public void setSwitchConvert(int realBlock, int switchNum) {
		switchConvert[switchNum] = realBlock;
	}
	
	public int getSwitchConvert(int switchNum) {
		return switchConvert[switchNum];
	}
}


