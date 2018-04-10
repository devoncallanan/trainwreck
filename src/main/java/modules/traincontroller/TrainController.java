package modules.traincontroller;

import shared.*;
import java.util.Stack;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 *
 * @author Jen Dudek
 */
public class TrainController {
    private final Power power1;
	private final Power power2;
	private final Power power3;
	private double p1;
	private double p2;
	private double p3;
	private double p;		//Final power command sent
	public Velocity velocity;
	private boolean mode; //Manual = false, Automatic = true
	private boolean emergency;
	private boolean service;
	private boolean rightDoors;
	private boolean leftDoors;
	private boolean lights;
	public boolean stopping;
    public boolean starting;
	private int trainID;
	private double authority;
	private double brakingDistance; //in meters
	private double metersRemaining; //in meters
	private int temp;
    private int currentBlock;
	
	private final double SERVICE_DECELERATION = 1.2; //meters/second^2
	private final double KMH_TO_MS = (double)1000/(double)3600;
	private final double MS_TO_KMH = (double)3600/(double)1000;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    
	
	//Mailbox variables----------------------------
	MessageQueue messages;
	Stack<Message> mymail;
	Message currentM;
	//---------------------------------------------
	
	
	public TrainController(MessageQueue messages) {
		this.messages = messages;
		power1 = new Power();
		power2 = new Power();
		power3 = new Power();
		velocity = new Velocity();
        mode = true;
		
		//Initialize my GUI
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		java.awt.EventQueue.invokeLater(() -> {
            new TestingUI(this).setVisible(true);
        });
            
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
	
	
	public void run() {
		
		mymail = messages.receive(4);
		
		while(!mymail.isEmpty()) {
			currentM = mymail.pop();

			switch(currentM.type()){
				case 0:
					authority = currentM.dataD();
					metersRemaining = authority;
					this.pcs.firePropertyChange("metersRemaining", -1 , metersRemaining);
					break;
				case 2:
					velocity.setSuggestedSpeed(currentM.dataD());
					this.pcs.firePropertyChange("suggestedSpeed", -1 , currentM.dataD());
					break;
				case 7:
					velocity.setFeedback(currentM.dataD(), mode);
					this.pcs.firePropertyChange("currentSpeed", -1 , velocity.feedback());
					break;
				case 9:
					currentBlock = currentM.dataI();
					break;
				case 11:
					this.pcs.firePropertyChange("station", -1, currentM.dataS());
					break;
				case 12:
					velocity.setSpeedLimit(currentM.dataD(), mode);
					break;
				default:
					break;
			}
                    System.out.println("vF = " + velocity.feedback());
		}//End while loop for message checking
		
		
		//CHECK IF TRAIN NEEDS TO START SLOWING FOR STOP
		metersRemaining = (metersRemaining - (velocity.feedback()*(double)100/(double)3600));
		authority = (authority - (velocity.feedback()*(double)100/(double)3600));
		this.pcs.firePropertyChange("metersRemaining", -1 , metersRemaining);
        //System.out.println("meters: " + metersRemaining);
                
		brakingDistance = (Math.pow(velocity.feedback()*(double)100/(double)3600, 2))/((2*SERVICE_DECELERATION)/(double)100);
        this.pcs.firePropertyChange("brakingDist", -1 , brakingDistance);
        //System.out.println("braking distance: " + brakingDistance);
                
		if (metersRemaining - 20 <= brakingDistance) {
			if(!service) setService(true);
			stopping = true;
		} else {
			stopping = false;
		}
		
		
		//POWER COMMAND
		if (!stopping) {
			if (velocity.error() < 0) {
				if (!service) setService(true);
				p = 0;
			} else {
				if (service) setService(false);
				//GENERATE POWER COMMAND
				p1 = power1.generatePower(velocity.error(), velocity.previousError());
				p2 = power2.generatePower(velocity.error(), velocity.previousError());
				p3 = power3.generatePower(velocity.error(), velocity.previousError());

				p = p1/1000;

			}	
		}
		

		if (service || emergency){
			//BRAKING, POWER = 0
			p = 0;
		}
		
		//SEND POWER COMMAND
		//send(new Message(From who, Data being sent, Type of data), message destination);
        this.pcs.firePropertyChange("powerUpdate", -1 , p);
		messages.send(new Message(MDest.TrCtl, p, MType.POWER), MDest.TrMd);
   

   
	} // End run method
	
	public void setTrainConstants(double Kp, double Ki) {
		//set KI and KP
		power1.setKp(Kp);
		power1.setKi(Ki);
		power2.setKp(Kp);
		power2.setKi(Ki);
		power3.setKp(Kp);
		power3.setKi(Ki);
	}
	
	public void setMode(boolean mode) {
        this.mode = mode;
	}
	
	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
		if (emergency) {
			messages.send(new Message(MDest.TrCtl, 3, MType.BRAKES), MDest.TrMd);
            this.pcs.firePropertyChange("brake", -1 , "Emergency");
		} else {
			messages.send(new Message(MDest.TrCtl, 2, MType.BRAKES), MDest.TrMd);
            this.pcs.firePropertyChange("brake", -1 , " ");
		}
	}
	
	public void setService(boolean service) {
		this.service = service;
		if (service) {
			messages.send(new Message(MDest.TrCtl, 1, MType.BRAKES), MDest.TrMd);
			this.pcs.firePropertyChange("brake", -1 , "Service");
		} else {
			messages.send(new Message(MDest.TrCtl, 0, MType.BRAKES), MDest.TrMd);
			this.pcs.firePropertyChange("brake", -1 , " ");
		}
	}
	
	public void setVelocityInfo(double setpointSpeed, double suggestedSpeed) {
		if(setpointSpeed != 0) {
            velocity.setSetpointSpeed(setpointSpeed);
		}
		if(suggestedSpeed != 0) {
            velocity.setSuggestedSpeed(suggestedSpeed);
		}
	}
	
	public void setAuthority(int authority) {
		this.authority = authority;
		stopping = false;
	}
	
	public void operateDoors(int opDoors) {
		switch(opDoors) {
			case 0:	//close left doors
				leftDoors = false; 	
				break;
			case 1:	//open left doors
				leftDoors = true; 	
				break;
			case 2:	//close right doors
				rightDoors = false; 
				break;
			case 3:	//open right doors
				rightDoors = true; 	
				break;
			default:
				break;
		} //End switch
	}
	
	public void setTemp(int temp) {
        this.temp = temp;
	}
	
	public void setLights(boolean lights) {
        this.lights = lights;
	}
	
	
	public void setMetersRemaining(int metersRemaining) {
        this.metersRemaining = metersRemaining;
	}
        
    public void setStart(boolean start) {
        this.starting = start;
	}
	

}
