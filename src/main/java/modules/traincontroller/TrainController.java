package modules.traincontroller;

import shared.*;
import java.util.Stack;

public class TrainController {
	
	
	double setpointSpeed = 0;
    double speedMax = 43.5;
    double speedLimit = 40;
    //double vF = 0;
    double powerCmd = 0;
    double powerMax = 120;
    double previousPowerCmd = 0;
    double vE = 0;
    double previousVE = 0;
    double previousUk = 0;
    double uk = 0;
    double KI = 0.5;
    double KP = 1;
    double authority = 0;
    int T = 1;
    boolean emergency = false;
    boolean service = false;
	
	//---------------------------------------------
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
				case 0:
					authority = currentM.dataI();
					break;
				case 1:
					break;
				case 2:
					velocityFeedback = currentM.dataD();
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				default:
					break;
			}
			System.out.println("vF = " + velocityFeedback);
		}
		//Send lights on
		/*
		messages.send(new Message(MDest.TrCtl, 1, MType.LIGHTS), MDest.TrMd);
		messages.send(new Message(MDest.TrCtl, 0, MType.LIGHTS), MDest.TrMd);
		messages.send(new Message(MDest.TrCtl, 1, MType.LIGHTS), MDest.TrMd);
		*/
		
	}
	
}