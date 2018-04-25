/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules.traincontroller;

import shared.*;
/**
 *
 * @author Jen Dudek
 */
public class Velocity {
	
	private double velocityFeedback;
	private double velocityFeedbackPrevious;
	private double velocityError;
	private double previousVelocityError;
	public double speedLimit;
	public double setpointSpeed;
	public double suggestedSpeed;
	private double originalSuggestedSpeed;
	private double zeroSpeed = 0;

	private final double MAX_SPEED = 70;
	
	public Velocity() {
		velocityFeedback = 0;
		velocityError = 0;
		previousVelocityError = 0;
		speedLimit = MAX_SPEED;
	}
	
	public double feedback() {
		return velocityFeedback;
	}
	
	public void setFeedback(double velocityFeedback, boolean mode, boolean emergency) {
		previousVelocityError = velocityError;
		if(!mode) {
			//Manual
			velocityError = setpointSpeed - velocityFeedback;
		} else {
			//Automatic
			velocityError = suggestedSpeed - velocityFeedback;			
		}
		
		if(emergency) {
            velocityError = zeroSpeed - velocityFeedback;
        }

		this.velocityFeedback = velocityFeedback;
	}
	
	public double error() {
		return velocityError;
	}
	
	public double previousError() {
		return previousVelocityError;
	}
	
	
	//If a new block is entered, the speed limit is set
	public void setSpeedLimit(double speedLimit, boolean mode) {
		this.speedLimit = speedLimit;
		if (mode) {
			//Automatic
			if (originalSuggestedSpeed <= speedLimit) {
				suggestedSpeed = originalSuggestedSpeed;
			} else if (suggestedSpeed > speedLimit) {
				suggestedSpeed = speedLimit;
			}
		} else {
			if (setpointSpeed > speedLimit) {
				setpointSpeed = speedLimit;
			}
		}
	}
	
	
	//Only called when the driver enters a new setpoint speed
	public void setSetpointSpeed(double setpointSpeed) {
		if(setpointSpeed <= speedLimit) {
			this.setpointSpeed = setpointSpeed;			
		} else {
			this.setpointSpeed = this.speedLimit;
		}
	}
	
	
	//Only called when a new suggested speed message comes from the train model
	public void setSuggestedSpeed(double suggestedSpeed) {
		if(suggestedSpeed <= speedLimit) {
			this.suggestedSpeed = suggestedSpeed;
			originalSuggestedSpeed = suggestedSpeed;
		} else {
			this.suggestedSpeed = speedLimit;
			originalSuggestedSpeed = speedLimit;
		}
		
	}
	
}
