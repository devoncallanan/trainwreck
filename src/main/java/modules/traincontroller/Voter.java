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
public class Voter {
	
	double avgPower;
	
	public Voter (){
		
	}
	
	public double vote(double power1, double power2, double power3) {
		
		avgPower = (power1 + power2 + power3)/ (double)3;
		
		//Take the average of the power commands and if they are within +- 10 kW of each other
		//then the avgPower is acceptable to send as a power commands
		if (avgPower <= power1 + 10 && avgPower >= power1 - 10) {
			if (avgPower <= power2 + 10 && avgPower >= power2 - 10) {
				if (avgPower <= power3 + 10 && avgPower >= power3 - 10) {
					return avgPower;
				}
			}
		}
		
		//If the average power command is too drastic between the 3 calculated powers
		//then there is an issue calculating power and 0 is returned
		return 0;
		
		
	}
	
}