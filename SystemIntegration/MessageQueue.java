/*

Messenger class for inter module comunication

project trainwreck

The idea here is that we have an arraylist of stacks.  Each stack belongs to a specific instance of a module i.e. train model 1 will have its own stack, 
different from trin model 2's stack.  To send a message, simply call send and pass in you message and destination (destinations are defined in the MDest class 
right now).  We can grow the list because its an arraylist and the current plan is to calculate the destination for runtime added trains by adding the train ID
to the value from MDest.TRAINMODEL or something like that.

to recieve you will ask for the entire stack of messages and gothrough them yourself, popping each message so its not there next time around

*/
import java.util.ArrayList;
import java.util.Stack;

public class MessageQueue {
	private ArrayList<Stack<Message>> messages;
	
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