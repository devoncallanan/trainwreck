package modules.trainmodel;

import shared.*;
import java.util.*;

public class TrainModel {
     ArrayList<Double> speeds = new ArrayList<Double>();
     double auth;
     public MessageQueue mq = new MessageQueue();
     private Stack<Message> messages;
     private Message m;
     public TrainModel(MessageQueue i){
          mq = i;
     }
     public void run(){
          mReceive();
          mSend();
     }
     public void mReceive(){
          messages = mq.receive(MDest.TrMd);
          while(!messages.isEmpty()){
               m = messages.pop();
               if(m.type() == MType.AUTH){
                    auth = m.dataD();
               }
               else if(m.type() == MType.SPEED){
                    speeds.add(m.dataD());
                    System.out.println("TrMod: "+m.dataD());
               }
          }
     }
     public void mSend(){
          for(int i=0; i<speeds.size(); i++){
               m = new Message(MDest.TrMd, speeds.get(i), MType.SPEED);
               mq.send(m, MDest.TrCtl);
               System.out.println("TrMod SEND");
          }
     }
}