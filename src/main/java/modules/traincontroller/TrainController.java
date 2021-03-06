package modules.traincontroller;

import shared.*;
import java.util.Stack;
import java.util.function.BooleanSupplier;
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
	private Voter vital;
	private boolean mode; //Manual = false, Automatic = true
	private boolean emergency;
	private boolean service;
	private boolean rightDoors;
	private boolean leftDoors;
	private boolean lights;
	public boolean stopping;
    public boolean starting;
    public boolean pause;
	private int trainID;
	private int failure;
	private double authority;
	private double brakingDistance; //in meters
	private double metersRemaining; //in meters
	private int temp;
    private int currentBlock;
	private String station;
	private int doorSide = 1;
	private int ad = 0;
	private String ad1 = "University of Pittsburgh. Engineering better minds.";
	private String ad2 = "Aerotech. Dedicated to the science of motion.";
	private String ad3 = "Do something extraordinary.";
	
	private final double SERVICE_DECELERATION = 1.2; //meters/second^2
	private final double KMH_TO_MS = (double)1000/(double)3600;
	private final double MS_TO_KMH = (double)3600/(double)1000;
	private final double KMH_TO_MPH = (double)1/(double)1.609344;
	private final double M_TO_F = 3.280840;
	private int ID;
	
	private final PropertyChangeSupport pcs;

    
	
	//Mailbox variables----------------------------
	MessageQueue messages;
	Stack<Message> mymail;
	Message currentM;
	//---------------------------------------------
	
	
	public TrainController(MessageQueue messages , int trainID) {
		this.ID = 2*trainID;
		this.messages = messages;
		power1 = new Power();
		power2 = new Power();
		power3 = new Power();
		velocity = new Velocity();
		vital = new Voter();
        mode = true;
		pause = true;
		station = " ";
		failure = 4;
		this.trainID = trainID;
		this.pcs = new PropertyChangeSupport(this);
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
            java.util.logging.Logger.getLogger(TrainControllerUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			
        }
//		java.awt.EventQueue.invokeLater(() -> {
//           new TestingUI(this).setVisible(true);
//        });
		java.awt.EventQueue.invokeLater(() -> {
            new TrainControllerUI(this).setVisible(true);
        });

            
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
	
	
	public void run() {
		
		mymail = messages.receive(MDest.TrCtl+ID);
		
		while(!mymail.isEmpty()) {
			currentM = mymail.pop();

			switch(currentM.type()){
				case 0:		//AUTH
					authority = currentM.dataD();
					metersRemaining = authority;
					this.pcs.firePropertyChange("metersRemaining", -1 , metersRemaining*M_TO_F);
					break;
				case 2:		//SPEED
					velocity.setSuggestedSpeed(currentM.dataD());
					break;
				case 7:		//FEEDBACK
					velocity.setFeedback(currentM.dataD(), mode, emergency);
					this.pcs.firePropertyChange("currentSpeed", -1 , velocity.feedback()*KMH_TO_MPH);
					break;
				case 9:		//BLOCK
					currentBlock = currentM.dataI();
					break;
				case 11:	//BEACON
					if (station.equals(currentM.dataS())) {
						this.pcs.firePropertyChange("station", -1, " ");
						station = " ";
					} else {
						station = currentM.dataS();
						this.pcs.firePropertyChange("station", -1, currentM.dataS());
					}
					doorSide = currentM.dataI();
					break;
				case 12:	//SPEEDLIMIT
					velocity.setSpeedLimit(currentM.dataD(), mode);
					break;
				case 15:	//ZEROSPEED
					//System.out.println("ZEROSPEED");
					setEmergency(true);
					break;
				case 17:	//FAILURE
					setFailure(currentM.dataI());
					break;
				case 19:	//UNDERGROUND
					if(currentM.dataI() == 1) {
						setLights(true);
					} else {
						setLights(false);
					}
					break;
				default:
					break;
			}
            
		}//End while loop for message checking
		
		
		//Sends different advertisements to the train model
		if (ad == 0) {
			messages.send(new Message(MDest.TrCtl+ID, ad1, 0, MType.ADVERTISEMENT), MDest.TrMd+ID);
			this.pcs.firePropertyChange("ad", -1 , ad1);
		}
		if (ad == 3600) {
			messages.send(new Message(MDest.TrCtl+ID, ad2, 0, MType.ADVERTISEMENT), MDest.TrMd+ID);
			this.pcs.firePropertyChange("ad", -1 , ad2);
		}
		if (ad == 7200) {
			messages.send(new Message(MDest.TrCtl+ID, ad3, 0, MType.ADVERTISEMENT), MDest.TrMd+ID);
			this.pcs.firePropertyChange("ad", -1 , ad3);
			ad = 0;
		}

		ad++;
		
		//Update UI with speed displays
		this.pcs.firePropertyChange("speedLimit", -1 , velocity.speedLimit*KMH_TO_MPH);
		this.pcs.firePropertyChange("suggestedSpeed", -1 , velocity.suggestedSpeed*KMH_TO_MPH);
		this.pcs.firePropertyChange("setpointSpeed", -1 , velocity.setpointSpeed*KMH_TO_MPH);
		
		
		//CHECK IF TRAIN NEEDS TO START SLOWING FOR STOP
		metersRemaining = (metersRemaining - (velocity.feedback()*KMH_TO_MS*.01));
		authority = (authority - (velocity.feedback()*KMH_TO_MS)*.01);
		this.pcs.firePropertyChange("metersRemaining", -1 , metersRemaining*M_TO_F);

        //Braking Distance used in Testing UI        
		brakingDistance = Math.pow(velocity.feedback()*KMH_TO_MS,2)/((2*SERVICE_DECELERATION));
        this.pcs.firePropertyChange("brakingDist", -1 , brakingDistance*M_TO_F);
        

		//Check to see if the train needs to start stopping
		if (metersRemaining - .5 <= brakingDistance) {
			if(!service) setService(true);
			stopping = true;
			power1.resetPower();
			power2.resetPower();
			power3.resetPower();
		} else {
			stopping = false;
		}
		
		
		
		//POWER COMMAND
		if (!stopping) {
			//if (velocity.error() < 0) {
				//if (!service) setService(true);
				//p = 0;
			//} else {
				if (mode) {
                    if (service) setService(false);
                }
				//GENERATE POWER COMMAND
				p1 = power1.generatePower(velocity.error(), velocity.previousError());
				p2 = power2.generatePower(velocity.error(), velocity.previousError());
				p3 = power3.generatePower(velocity.error(), velocity.previousError());

				p = vital.vote(p1, p2, p3);

			//}	
		}

		//Any brakes applied, the power command is set to 0
		if (service || emergency || pause || failure == 1){
			//BRAKING, POWER = 0
			p = 0;
		}
		
		//Stopped, checking to open or close doors
		if (!station.equals(" ") && velocity.feedback() == 0) {
			if (doorSide == -1) {
				operateDoors(3);	//Open doors on the right side
			} else if (doorSide == 1) {
				operateDoors(1); //Open doors on the left side
			}
			
		}
		
		
		//SEND POWER COMMAND
		//send(new Message(From who, Data being sent, Type of data), message destination);
        this.pcs.firePropertyChange("powerUpdate", -1 , p);
		messages.send(new Message(MDest.TrCtl+ID, p, MType.POWER), MDest.TrMd+ID);
		

   
	} // End run method
	
	public void setTrainConstants(double Kp, double Ki) {
		//set KI and KP
		pause = false;
		power1.setKp(Kp);
		power1.setKi(Ki);
		power2.setKp(Kp);
		power2.setKi(Ki);
		power3.setKp(Kp);
		power3.setKi(Ki);
		this.pcs.firePropertyChange("ki", -1 , power1.getKi());
        this.pcs.firePropertyChange("kp", -1 , power1.getKp());
	}
	
	//Set the mode
	public void setMode(boolean mode) {
        this.mode = mode;
	}
	
	//Set the emergency brake
	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
		if (emergency) {
			messages.send(new Message(MDest.TrCtl+ID, 3, MType.BRAKES), MDest.TrMd+ID);
            this.pcs.firePropertyChange("brake", -1 , "Emergency");
		} else {
			operateDoors(0);
			operateDoors(2);
			messages.send(new Message(MDest.TrCtl+ID, 2, MType.BRAKES), MDest.TrMd+ID);
            this.pcs.firePropertyChange("brake", -1 , " ");
		}
	}
	
	//Set the service brake
	public void setService(boolean service) {
		this.service = service;
		if (service) {
			messages.send(new Message(MDest.TrCtl+ID, 1, MType.BRAKES), MDest.TrMd+ID);
			this.pcs.firePropertyChange("brake", -1 , "Service");
		} else {
			operateDoors(0);
			operateDoors(2);
			messages.send(new Message(MDest.TrCtl+ID, 0, MType.BRAKES), MDest.TrMd+ID);
			this.pcs.firePropertyChange("brake", -1 , " ");
		}
	}
	
	//Send setpoint and suggested to the velocity class
	public void setVelocityInfo(double setpointSpeed, double suggestedSpeed) {
		if(setpointSpeed != 0) {
            velocity.setSetpointSpeed(setpointSpeed);
		}
		if(suggestedSpeed != 0) {
            velocity.setSuggestedSpeed(suggestedSpeed);
		}
	}
	
	//Set train authority
	public void setAuthority(int authority) {
		this.authority = authority;
		this.metersRemaining = authority;
		stopping = false;
		setService(false);
	}
	
	//Operate doors
	public void operateDoors(int opDoors) {
		if(velocity.feedback() == 0) {
			switch(opDoors) {
				case 0:	//close left doors
					leftDoors = false; 
					messages.send(new Message(MDest.TrCtl+ID, 0, MType.DOORS), MDest.TrMd+ID);
					this.pcs.firePropertyChange("doors", -1 , 0);
					break;
				case 1:	//open left doors
					leftDoors = true; 
					messages.send(new Message(MDest.TrCtl+ID, 1, MType.DOORS), MDest.TrMd+ID);
					this.pcs.firePropertyChange("doors", -1 , 1);
					break;
				case 2:	//close right doors
					rightDoors = false;
					messages.send(new Message(MDest.TrCtl+ID, 2, MType.DOORS), MDest.TrMd+ID);
					this.pcs.firePropertyChange("doors", -1 , 2);
					break;
				case 3:	//open right doors
					rightDoors = true;
					messages.send(new Message(MDest.TrCtl+ID, 3, MType.DOORS), MDest.TrMd+ID); 
					this.pcs.firePropertyChange("doors", -1 , 3);					
					break;
				default:
					break;
			} //End switch
        }
	}
	
	//Set temp
	public boolean setTemp(int temp) {
		if (temp >= 60 && temp <= 80) {
			this.temp = temp;
			messages.send(new Message(MDest.TrCtl+ID, temp, MType.TEMP), MDest.TrMd+ID); 
			return true;
		}
		return false;
	
	}
	
	//Change lights
	public void setLights(boolean lights) {
        this.lights = lights;
		if (lights)
			messages.send(new Message(MDest.TrCtl+ID, 1, MType.LIGHTS), MDest.TrMd+ID); 
		else 
			messages.send(new Message(MDest.TrCtl+ID, 0, MType.LIGHTS), MDest.TrMd+ID);
	}
	
	//Same as authority (Used because of testing UI)
	public void setMetersRemaining(int metersRemaining) {
        this.metersRemaining = metersRemaining;
	}
        
    public void setStart(boolean start) {
        this.starting = start;
	}
	
	public boolean getMode() {
        return mode;
    }
	
	//Handling failures given by the train model
	public void setFailure(int failure) {
		this.failure = failure;
		this.pcs.firePropertyChange("failure", -1, failure);
		if (failure == 4) 
			if(emergency) setEmergency(false);
		if (failure == 0 || failure == 2 || failure == 3) 
			if(!emergency) setEmergency(true);
	}
	
	public int getTrainID(){
		return trainID;
	}

	public boolean getEmergency() {
		// TODO Auto-generated method stub
		return emergency;
	}

	public boolean getService() {
		// TODO Auto-generated method stub
		return service;
	}

	public double getAuthority() {
		// TODO Auto-generated method stub
		return authority;
	}

	public boolean isLeftDoors() {
		// TODO Auto-generated method stub
		return leftDoors;
	}

	public boolean isRightDoors() {
		// TODO Auto-generated method stub
		return rightDoors;
	}

	public int getTemp() {
		// TODO Auto-generated method stub
		return temp;
	}

	public boolean getLights() {
		// TODO Auto-generated method stub
		return lights;
	}
}
