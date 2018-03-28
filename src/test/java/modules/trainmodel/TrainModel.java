import java.util.*;

public class TrainModel {
     ArrayList<int> speeds = new ArrayList<int>();
     int auth;
     public MessageQueue mq = new MessageQueue();
     private Stack<Message> messages;
     private Message m;
     public TrainModel(MessageQueue i){
          mq = i;
          speed = 0;
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
                    auth = m.dataI()
               }
               else if(m.type() == MType.SPEED){
                    speeds.add(m.dataI());
               }
          }
     }
     public void mSend(){
          for(int i=0; i<speeds.length(); i++){
               m = new Message(MDest.TrMd, speeds.get(i), Mtype.SPEED);
               mq.send(m, MDest.TrCtl);
          }
     }
}