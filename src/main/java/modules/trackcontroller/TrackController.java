package modules.trackcontroller;

import java.util.*;
import shared.*;

public class TrackController {
     ArrayList<Integer> speeds = new ArrayList<Integer>();
     int auth;
     public MessageQueue mq = new MessageQueue();
     private Stack<Message> messages;
     private Message m;
     public TrackController(MessageQueue i){
          mq = i;
     }
     public void run(){
          mReceive();
          mSend();
     }
     public void mReceive(){
          messages = mq.receive(MDest.TcCtl);
          while(!messages.isEmpty()){
               m = messages.pop();
               if(m.type() == MType.AUTH){
                    auth = m.dataI();
               }
               else if(m.type() == MType.SPEED){
                    speeds.add(m.dataI());
                    System.out.println("TkCon: "+m.dataI());
               }
          }
     }
     public void mSend(){
          for(int i=0; i<speeds.size(); i++){
               m = new Message(MDest.TcCtl, speeds.get(i), MType.SPEED);
               mq.send(m, MDest.TcMd);
               System.out.println("TkCon SEND");
          }
     }
}
