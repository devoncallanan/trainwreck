/*

Message class

project trainwreck

*/


class Message {
	int from;
	int dataI;
	float dataF;
	int type;
	
	public Message(int from, int dataI, int type) {
		this.from = from;
		this.dataI = dataI;
		this.type = type;
	}
	
	public Message(int from, float dataF, int type) {
		this.from = from;
		this.dataF = dataF;
		this.type = type;
	}
	
	public int from() {
		return from;
	}
	
	public int dataI() {
		return dataI;
	}
	
	public float dataF() {
		return dataF();
	}
	
	public int type() {
		return type;
	}
}