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
public class Power {
	
	private double powerCmd;
	private double previousPowerCmd;
	private double Ki;
	private double Kp;
	private double uk;
	private double previousUk;
	private double Time = 0.01;

	private final double MAX_POWER = 120;
	private final double DEFAULT_KI = 5;
	private final double DEFAULT_KP = 75;
	private final double KMH_TO_MS = (double)1000/(double)3600;
	
	public Power() {
		powerCmd = 0;
		uk = 0;
		previousUk = 0;
		Kp = DEFAULT_KP;
		Ki = DEFAULT_KI;
		
	}
	
	public double generatePower(double velocityError, double previousVelocityError) {
		
        //Calculate Uk
        if (previousPowerCmd < MAX_POWER) {
            uk = previousUk + (Time/(double)2)*(velocityError*KMH_TO_MS + previousVelocityError*KMH_TO_MS); 
        } else {
            uk = previousUk;
        }
        
        //Final Calculation
        if ((Kp*velocityError*KMH_TO_MS + Ki*uk) < MAX_POWER) {
            powerCmd = Kp*velocityError*KMH_TO_MS + Ki*uk;
        } else {
            powerCmd = MAX_POWER;
        }

		
		previousPowerCmd = powerCmd;
		previousUk = uk;
		return powerCmd;
	}
	
	public void setKi(double Ki) {
            if (Ki > -1)
				this.Ki = Ki;
	}
	
	public void setKp(double Kp) {
            if (Kp > -1)
				this.Kp = Kp;
	}
	
    public double getKp() {
		return Kp;
	}
        
    public double getKi() {
		return Ki;
	}
        

	public void resetPower() {
		uk = 0;
		previousUk = 0;
		powerCmd = 0;
		previousPowerCmd = 0;
	}
	
	public double getUk() {
		return uk;
	}
	
	public double getPreviousUk() {
		return previousUk;
	}
	
	public double getPreviousPowerCmd() {
		return previousPowerCmd;
	}
	
	public double getPowerCmd() {
		return powerCmd;
	}
}
