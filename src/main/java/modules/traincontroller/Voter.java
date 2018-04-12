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
	
	boolean one = false;
	boolean two = false;
	boolean three = false;
	
	public Voter (){
		
	}
	
	public double vote(double power1, double power2, double power3) {
		
		
		if ((power1 <= (power2 + 1)) && (power1 >= (power2 - 1))) {
			one = true;
		}
		if ((power1 <= (power3 + 1)) && (power1 >= (power3 - 1))) {
			two = true;
		}
		if ((power3 <= (power2 + 1)) && (power3 >= (power2 - 1))) {
			three = true;
		}
		
		if (one && two && three) {
			return (power1+power2+power3)/(double)3;
		} else {
			return power1;
		}
		
	}
	
}