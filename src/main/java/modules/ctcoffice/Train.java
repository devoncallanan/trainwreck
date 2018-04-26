/*train thing that tells me where it is and i move it and stuff*/

package modules.ctcoffice;
import shared.*;
import java.util.regex.Pattern;


public class Train{

public static double KMH_TO_MS = 1000.0/3600.0; 
public static double MS_TO_KMH = 3600.0/10000.0; 

public double speed;					//Current speed m/s
public double authority;
public int id;							//train ID
public int track;
public String station;
public int passengers;
	
	public Train(int id, int track, String station) {
		this.id = id;
		this.track = track;
		this.station = station;
		this.passengers = 0;
	}
	
}