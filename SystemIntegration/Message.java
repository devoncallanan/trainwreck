/*

Message class

project trainwreck

This holds the message data.  There are two constructors, one for int data and one for float data.

you will be able to get any of the data members, but please only try to get the ones that are initialized.
maybe make an abstract class so we can have extensions with different data
*/


class Message {
	private int from;
	private int dataI;
	private double dataD;
	private int type;
	
	
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
	
	public int from() {
		return from;
	}
	
	public int dataI() {
		return dataI;
	}
	
	public float dataD() {
		return dataD;
	}
	
	public int type() {
		return type;
	}
}