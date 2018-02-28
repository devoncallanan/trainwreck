/*

Messenger class for inter module comunication

project trainwreck

*/
import java.util.ArrayList;
import java.util.Stack;

public class MessageQueue {
	ArrayList<Stack<Message>> messages;
	int length;
	
	public MessageQueue() {
		messages = new ArrayList<Stack<Message>>(5);
	}
	
	public void send(Message m, int destination) {
		messages.get(destination).push(m);
	}
	
	public Stack<Message> recieve(int adress) {
		return messages.get(adress);
	}
	
	public void addTrain() {
		messages.add(new Stack<Message>());
		messages.add(new Stack<Message>());
	}
}