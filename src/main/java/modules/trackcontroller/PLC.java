package modules.trackcontroller;

import java.util.*;
import java.io.*;


public class PLC {
     boolean switchBias, loop, priority, lights, error = false;
     boolean switchBias1, loop1, priority1, lights1;
     boolean switchBias2, loop2, priority2, lights2;
     Boolean oneWay = null, oneWay1 = null, oneWay2 = null;
     public PLC(){

     }
     public void importPLC(File f){
          BufferedReader file = null;
        try {
            file = new BufferedReader (new FileReader(f));
        } catch (FileNotFoundException ex) {
             System.out.println("Error: File not found!");
        }
        try {
            String line;
            while( (line = file.readLine()) != null)
            {
                String[] parts = line.split(">");
                if(parts[0].equals("switch")){
                    switchBias = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("loop")){
                    loop = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("priority")){
                    priority = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("lights")){
                    lights = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("oneway")){
                     oneWay = Boolean.parseBoolean(parts[1]);
                }
            }
        } catch (IOException ex) {
             System.out.println("IO Exception!");
        }
        try {
            file.close();
       } catch (IOException ex) {
            System.out.println("IO Exception!");
        }
		
		BufferedReader file = null;
        try {
            file = new BufferedReader (new FileReader(f));
        } catch (FileNotFoundException ex) {
             System.out.println("Error: File not found!");
        }
        try {
            String line;
            while( (line = file.readLine()) != null)
            {
                String[] parts = line.split(">");
                if(parts[0].equals("switch")){
                    switchBias1 = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("loop")){
                    loop1 = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("priority")){
                    priority1 = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("lights")){
                    lights1 = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("oneway")){
                     oneWay1 = Boolean.parseBoolean(parts[1]);
                }
            }
        } catch (IOException ex) {
             System.out.println("IO Exception!");
        }
        try {
            file.close();
       } catch (IOException ex) {
            System.out.println("IO Exception!");
        }
		
		BufferedReader file = null;
        try {
            file = new BufferedReader (new FileReader(f));
        } catch (FileNotFoundException ex) {
             System.out.println("Error: File not found!");
        }
        try {
            String line;
            while( (line = file.readLine()) != null)
            {
                String[] parts = line.split(">");
                if(parts[0].equals("switch")){
                    switchBias2 = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("loop")){
                    loop2 = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("priority")){
                    priority2 = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("lights")){
                    lights2 = Boolean.parseBoolean(parts[1]);
                }
                else if(parts[0].equals("oneway")){
                     oneWay2 = Boolean.parseBoolean(parts[1]);
                }
            }
        } catch (IOException ex) {
             System.out.println("IO Exception!");
        }
        try {
            file.close();
       } catch (IOException ex) {
            System.out.println("IO Exception!");
        }
		if(!(switchBias == switchBias1 == switchBias2)){		//Redundancy for vital nature;
			  System.out.println("PLC ERROR IN SWITCHBIAS");
			  error = true;
		}
		if(!(loop == loop1 == loop2)){
			  System.out.println("PLC ERROR IN LOOP");
			  error = true;
		}
		 if(!(priority == priority1 == priority2)){
			  System.out.println("PLC ERROR IN PRIORITY");
			  error = true;
		 }
		 if(!(lights == lights1 == lights2)){
			  System.out.println("PLC ERROR IN LIGHTS);
			  error = true;
		 }
		 if(oneWay!=null && !(oneWay == oneWay1 == oneWay2)){
			  System.out.println("PLC ERROR IN ONEWAY");
			  error = true;
		 }
     }
     public boolean getSwitchBias(){
		 if(!error)
          return switchBias;
		else
			return null; 
     }
     public boolean getLoop(){
          return loop;
     }
     public boolean getPriority(){
          return priority;
     }
     public boolean getLights(){
          return lights;
     }
     public boolean getOneWay(){
          return oneWay;
     }
}
