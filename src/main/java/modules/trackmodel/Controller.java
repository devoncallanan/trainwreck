/*
	track controller to hold blocks that are sent to washy
*/
/*
this will hold the data that the track controller will need and facilitate conversion to a format readable to the module
currently has fixed size arrays so no controller can have more than 25 blocks
*/

package modules.trackmodel;

import shared.*;

public class Controller extends javax.swing.AbstractListModel<String>{
	int[][] occupancy;
	int[][] convert;
	int numControllers;
	int[] switchConvert;
	int[] positions;
	
	Controller(int numControllers) {
		this.occupancy = new int[numControllers][100];
		this.convert = new int[numControllers][100];
		this.numControllers = numControllers;
		this.switchConvert = new int[numControllers];
		this.positions = new int[numControllers];
	}

	public void init() {
		java.io.File input;
		java.util.Scanner scan = null;
		java.util.Scanner scanLine = null;
		int blockid;
		int contNumber = 0;
		int i = 0;
		String switchLocs;
		try {
			input = new java.io.File("./src/main/java/modules/trackmodel/controller_config.csv");
			//input = new java.io.File("./modules/trackmodel/controller_config.csv");
			scan = new java.util.Scanner(input);
		}
		catch (Exception e) {
			System.err.println("The config file 'controller_config.csv' could not be found " + System.getProperty("user.dir"));
		}

		switchLocs = scan.nextLine();
		System.out.println(switchLocs);
		scanLine = new java.util.Scanner(switchLocs).useDelimiter(",|\\r\\n");
		while (scanLine.hasNext()){
			this.setSwitchConvert(scanLine.nextInt(), i);
			this.setSwitch(i, 1);
			if(i < 9) {
				i++;
			} else {
				break;
			}
		}
		while (scan.hasNext()) {
			blockid = 0;
			String line = scan.nextLine();
			scanLine = new java.util.Scanner(line).useDelimiter(",|\\r\\n");
			System.out.println(line);
			while (scanLine.hasNext()) {
				convert[contNumber][blockid++] = scanLine.nextInt();
			}
			contNumber++;
		}


	}

	public boolean[] getOccArray(int controllerid) {
		boolean[] arr = new boolean[27];
		for (int i = 0; i< 27; i++) {
			arr[i] = occupancy[controllerid][i] == 1;
		}
		return arr;
	}

	public void update(int realBlock, int occ) {
		for (int i = 0; i<numControllers; i++) {
			for (int j = 0; j<27; j++) {
				if (convert[i][j] == realBlock) {
					occupancy[i][j] = occ;
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
	
	public void setSwitch(int i, int branch) {
		positions[i] = branch;
		fireContentsChanged(this, i, i);
	}
	
	public int getSize() {
		return positions.length;
	}
	
	public String getElementAt(int i) {
		/*
		StringBuilder info = new StringBuilder();
		boolean[] arr = getOccArray(i);
		for (int j = 0; j < arr.length; j++) {
			info.append(arr[j] + " ");
		}
		return info.toString();
		*/
		
		return "" + positions[i];
	}
}
