/*

MType class for message types


access them by saying MType.AUTH;

and so on...
*/
package shared;

public class MType {
	public static final int AUTH = 0;					//double
	public static final int OCC = 1;					//?
	public static final int SPEED = 2;					//double
	public static final int DOORS = 3;					//int
	public static final int TEMP = 4;					//int
	public static final int BRAKES = 5;					//int
	public static final int LIGHTS = 6;					//int
	public static final int FEEDBACK = 7;				//double
	public static final int POWER = 8;					//double
	public static final int BLOCK = 9;					//?
	public static final int NEWTRAIN = 10;				//?
	public static final int BEACON = 11;				//String
	public static final int SPEEDLIMIT = 12;			//double
	public static final int TRACK = 13;					//boolean[]
	public static final int SWITCH = 14;				//Boolean
	public static final int ZEROSPEED = 15;				//int
	public static final int PASSENGERS = 16;			//int
	public static final int FAILURE = 17;				//int	ebrake 0, brake failure 1, signal failure 2, engine failure 3
	public static final int GRADE = 18;					//double
	public static final int UNDERGROUND = 19;			//int 	yes = 1, no = 0
	public static final int REALTRACK = 20;
	public static final int ADVERTISEMENT = 21;
	public static final int MAINTENANCE = 22;			//int
}
