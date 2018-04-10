/*

Message class

project trainwreck

This holds the message data.  There are two constructors, one for int data and one for float data.

you will be able to get any of the data members, but please only try to get the ones that are initialized.
maybe make an abstract class so we can have extensions with different data
*/
package shared;

public class Message {
	private int from;
	private int dataI;
	private double dataD;
	private String dataS;
	private int type;
	private int trainID;
	private boolean[] occupancy;
	
	
	public Message(int from, int dataI, int type) {
		this.from = from;
		this.dataI = dataI;
		this.type = type;
	}
	
	public Message(int from, double dataD, int type) {
		this.from = from;
		this.dataD = dataD;
		this.type = type;
	}
	
	public Message(int from, String dataS, int type) {
		this.from = from;
		this.dataS = dataS;
		this.type = type;
	}
	
	public int from() {
		return from;
	}
	
	public int dataI() {
		return dataI;
	}
	
	public double dataD() {
		return dataD;
	}
	
	public String dataS() {
		return dataS;
	}
	
	public boolean[] occupancy() {
		return occupancy;
	}
	
	public int trainId() {
		return trainID;
	}
	
	public int type() {
		return type;

	}

}

