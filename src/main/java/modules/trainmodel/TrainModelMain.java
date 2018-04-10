/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules.trainmodel;
import shared.*;
import java.util.*;
/**
 *
 * @author bkelly088
 */
public class TrainModelMain {

    
    /**
     * @param args the command line arguments
     */
    // public static void main(String[] args) {
    //   TrainModelMain main = new TrainModelMain();
    //   main.run();
    // }
    
     public MessageQueue mq = new MessageQueue();
     private Stack<Message> messages;
     private Message m;    
     private double velocityFeedback;
     private double auth; 
     private int doors,temp,brakes,lights,passengers;
     private double power,grade,speed,speedLimit;
     
    public TrainModelMain(MessageQueue mq) {
      this.mq = mq;
    }
    
     public void run(){
          mReceive();
	/*power=120000;
        speed=25;
        grade = 0;
        brakes = 0;
        speedLimit = 70;
        passengers = 20;*/
          Train train = new Train(1,this.power,this.speed,this.grade,this.brakes,this.speedLimit,this.passengers);    
          velocityFeedback = train.getVelocity();
          //TrainModelUI ui = new TrainModelUI(); 
          //ui.setVisible(true);
          //System.out.println(velocityFeedback);
          mSend();
     }
     public void mReceive(){
          messages = mq.receive(MDest.TrMd);
          while(!messages.isEmpty()){
               m = messages.pop();
               if(m.type() == MType.AUTH){
                    System.out.println("TrMod_Auth: "+auth);
                    this.auth = m.dataD();
               }
                else if(m.type() == MType.SPEED){
                    System.out.println("TrMod_Speed: "+speed);
                    this.speed = (m.dataD());
               }
                else if(m.type() == MType.DOORS){
                    this.doors = m.dataI();
               }
                else if(m.type() == MType.TEMP){
                    this.temp = m.dataI();
               }
                else if(m.type() == MType.BRAKES){
                    this.brakes = m.dataI();
               }
		else if(m.type() == MType.LIGHTS){
                    this.lights = m.dataI();
               }
		else if(m.type() == MType.POWER){
                    this.power = m.dataI();
               }
               else if(m.type() == MType.FEEDBACK){
                    this.velocityFeedback = m.dataI();
               }
               else if(m.type() == MType.SPEEDLIMIT){
                    this.speedLimit = m.dataI();
               }
               else if (m.type() == MType.PASSENGERS){
                    this.passengers = m.dataI();
               }
          }
     }
     public void mSend(){
         
               m = new Message(MDest.TrMd, velocityFeedback, MType.FEEDBACK);
               mq.send(m, MDest.TrCtl);
               
               m = new Message(MDest.TrMd, velocityFeedback, MType.FEEDBACK);
               mq.send(m, MDest.TcMd);
               
               m = new Message(MDest.TrMd, auth, MType.AUTH);
               mq.send(m, MDest.TrCtl);
               //System.out.println("TrMod SEND");
          }
     }
    
    
    
    
    

