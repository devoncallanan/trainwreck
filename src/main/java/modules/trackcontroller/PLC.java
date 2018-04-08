package modules.trackcontroller;

import java.util.*;
import shared.*;

public class PLC {
     boolean switchBias, loop, priority, lights;
     public PLC(){

     }
     public void import(File f){
          BufferedReader file = null;
        try {
            file = new BufferedReader (new FileReader(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TrackController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String line;
            while( (line = file.readLine()) != null)
            {
                String[] parts = line.split(">");
                if(parts[0].equals("switch")){
                    switchBias = Integer.parseInt(parts[1]);
                }
                else if(parts[0].equals("loop")){
                    loop = Integer.parseInt(parts[1]);
                }
                else if(parts[0].equals("priority")){
                    priority = Integer.parseInt(parts[1]);
                }
                else if(parts[0].equals("lights")){
                    lights = Integer.parseInt(parts[1]);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TrackController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(TrackController.class.getName()).log(Level.SEVERE, null, ex);
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
}
