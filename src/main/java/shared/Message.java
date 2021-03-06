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
	private boolean[] dataBA;
	private Boolean dataB;
	private String dataS;
	private int type;
	public int trainID;
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

	public Message(int from, String dataS, int dataI, int type) {
		this.from = from;
		this.dataS = dataS;
		this.dataI = dataI;
		this.type = type;
	}

	public Message(int from, boolean[] dataBA, int type) {
		this.from = from;
		this.dataBA = dataBA;
		this.type = type;
	}

	public Message(int from, Boolean dataB, int type) {
		this.from = from;
		this.dataB = dataB;
		this.type = type;
	}

	public int from() {
		return from;
	}

	public int dataI() {
		return dataI;
	}

	public boolean[] dataBA() {
		return dataBA;
	}

	public Boolean dataB() {
		return dataB;
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
