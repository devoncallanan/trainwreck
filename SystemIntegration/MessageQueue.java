/*

Messenger class for inter module comunication

project trainwreck

*/

class MessageQueue {
	java.util.Stack<Message>[] messages;
	int length;
	
	public MessageQueue {
		messages = new java.util.Stack<Message>()[5];
	}
	
	public void send(Message m, int destination) {
		messages[destination].push(m);
	}
	
	public java.util.Stack<Message> recieve(int adress) {
		return messages[adress];
	}
	
	public void addTrain() {
		java.util.Stack<Message>[] temp = new java.util.Stack<Message>()[length + 2];
		for (int i = 0; i < length; i++) {
			temp[i] = messages[i];
		}
		length += 2;
		messages = temp;
	}
}