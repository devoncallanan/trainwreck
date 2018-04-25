package modules.trackcontroller;

import java.util.*;
import java.io.*;


public class PLC {
     boolean switchBias, loop, priority, lights;
     Boolean oneWay;
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
     }
     public boolean getSwitchBias(){
          return switchBias;
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
