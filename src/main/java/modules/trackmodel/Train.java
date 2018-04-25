/*train thing that tells me where it is and i move it and stuff*/

package modules.trackmodel;

import shared.*;
import java.util.regex.Pattern;


public class Train{

public static double KMH_TO_MS = 1000.0/3600.0; 
public static double MS_TO_KMH = 3600.0/10000.0; 
	public int location;					//Block number
	public int backNode;					//Node number that the train is traveling from
	public double speed;					//Current speed m/s
	public double distanceIn;				//Distance into the block m
	public double distanceInTail;
	public double tailOverflow;
	public java.util.ArrayList<Block> blocks;
	public int id;							//train ID
	public double init;
	private static double DELTA_T = .01;		//quantum for calculations
	
	public Train(int id, int location, int node) {
		this.location = location;
		this.id = id;
		this.distanceIn = 0;
		this.distanceInTail = 0;
		this.init = 0;
		this.backNode = node;
		this.blocks = new java.util.ArrayList<Block>();
	}
	
	public double move() {
		distanceIn += (DELTA_T*speed)*KMH_TO_MS;
		if (init < 64.4) {
			init += (DELTA_T*speed)*KMH_TO_MS;
		}
		else {
			distanceInTail += (DELTA_T*speed)*KMH_TO_MS;
			tailOverflow = distanceInTail - blocks.get(0).length;
			//System.out.println(blocks);
		}
		return distanceIn;
	}
	
}