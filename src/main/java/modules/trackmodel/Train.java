/*train thing that tells me where it is and i move it and stuff*/

package modules.trackmodel;

import shared.*;
import java.util.regex.Pattern;


public class Train{
	public int location;					//Block number
	public int backNode;					//Node number that the train is traveling from
	public double speed;					//Current speed m/s
	public double distanceIn;				//Distance into the block km
	public int id;							//train ID
	private static double DELTA_T = .1;		//quantum for calculations
	
	public Train(int id, int location, int node) {
		this.location = location;
		this.id = id;
		this.distanceIn = 0;
		this.backNode = node;
	}
	
	public double move() {
		distanceIn += (DELTA_T*speed);
		return distanceIn;
	}
	
}