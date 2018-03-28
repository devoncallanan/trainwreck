
import java.util.Stack;

public class TrainController {
	
	double suggestedSpeed;
	double velocityFeedback;
	MessageQueue messages;
	Stack<Message> mymail;
	Message currentM;
	
	
	public TrainController(MessageQueue messages) {
		this.messages = messages;
	}
	
	
	public void run() {
		
		mymail = messages.receive(4);
		
		while(!mymail.isEmpty()) {
			currentM = mymail.pop();
			
			switch(currentM.type()){
				case 2:
					velocityFeedback = currentM.dataD();
					break;
				default:
					break;
			}
			System.out.println("vF = " + velocityFeedback);
		}
		//Send lights on
		messages.send(new Message(MDest.TrCtl, 1, MType.LIGHTS), MDest.TrMd);
		messages.send(new Message(MDest.TrCtl, 0, MType.LIGHTS), MDest.TrMd);
		messages.send(new Message(MDest.TrCtl, 1, MType.LIGHTS), MDest.TrMd);
		
	}
	
}