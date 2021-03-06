/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules.trainmodel;

/**
 *
 * @author bkelly088
 */
public class Train {
    
        private double force;
        private double velocityFeedback = 1;
        private final double MS_TO_KMH = (double)3600/(double)1000;
        private final double KMH_TO_MS = (double)1000/(double)3600;
        private double trainMass = 81800;
        private double totalMass;
	private final double trainLength = 32.2; 	// meters
	private final double trainWidth = 2.65; 	// meters
	private final double trainHeight = 3.42; 	// meters
	private final double maxAcc = 0.5; 	// m/s^2
	private final double serviceBrakeDecell = -1.2; 	// m/s^2
	private final double eBrakeDecell = -2.73; 	// m/s^2
	private final int numWheels = 12; // 6 wheels per car and 2 cars per train
	private final double grav = 9.8; 	// m/s^2
	private final double frictionCoeff = 0.16;
        private double deltaT = .01;
	
	public Train(){
        
	}
	
	public double calculateVelocity(double power,double currentSpeed,double grade,int brakes,double speedLimit,double passengers){
		//input power and convert the power to a force based on the starting velocity
    	totalMass = trainMass + 150*passengers; //add passenger weight to train
    	totalMass = totalMass * .454; //convert train mass to kg
    	//System.out.println(totalMass);
    	
        currentSpeed = currentSpeed*KMH_TO_MS; //convert kmh to m/s
    	if (currentSpeed == 0) {
    		force = (power*1000)/1;
    	} else {
    		force = (power*1000)/currentSpeed;
    	}
    	    //	System.out.println(force);

    	
    	// Calculate the slope
    	double slope = Math.atan2(grade,100);
    	double angle = Math.toDegrees(slope);
    	
    	// Calculate the forces acting on the train using the coefficient of friction
    	// and the train's weight
    	double normalForce = (totalMass/numWheels) * grav * Math.sin((angle*Math.PI)/180);	// divide by 12 for the number of wheels
    	double downwardForce = (totalMass/numWheels) * grav * Math.cos((angle*Math.PI)/180);	// divide by 12 for the number of wheels

    	// compute friction force
    	double friction = (frictionCoeff * downwardForce) + normalForce;
    	    //	    	System.out.println(friction);

    	// sum of the forces
    	double totalForce = force - friction;
    	    //	System.out.println("totalForce" + totalForce);
    	
    	
    	//Calculate acceleration using the F = ma
    	double trainAcceleration = totalForce/totalMass;
    	
    	// check to make sure this acceleration does not exceed max.
    	if (trainAcceleration >= (maxAcc)) {	
    		trainAcceleration = (maxAcc);	
    	}
    	
		
    	// decelerates the train based on the values given in the spec sheet for the emergency brake
    	if (brakes == 3) {
    		trainAcceleration += (eBrakeDecell);
    	}
    	
    	// decelerates the train based onthe values given in the spec sheet for the service brake
    	if (brakes == 1) {
			
    		trainAcceleration += (serviceBrakeDecell);
    	}
    	
    	// Step 5: Calculate the final speed by adding the old speed with the acceleration x the time elapsed
          //  	System.out.println("Current Acc " + trainAcceleration);
		//System.out.println("trainAcc: " + trainAcceleration + "\tcurrent: " + currentSpeed);
    	velocityFeedback = currentSpeed + (trainAcceleration*deltaT);
		//System.out.println("currentSpeed2: " + velocityFeedback);
    	velocityFeedback = velocityFeedback * MS_TO_KMH; //converts to km/h
        
    	// no negative speed
    	if(velocityFeedback < 0) {
            velocityFeedback = 0;
        } // if the speed exceeds the speed limit then keep at speed limit ( now done in train controller only)
        /*else if (velocityFeedback > speedLimit){
            velocityFeedback = speedLimit;
        }*/
    	
    	// resetting the current speed based on our calculations
    	//currentSpeed = velocityFeedback;
          
    	//System.out.println(velocityFeedback);
        return velocityFeedback;
    }
}
     
	
	


    
