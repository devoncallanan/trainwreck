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
     public MessageQueue mq = new MessageQueue();
     public TrainModelUI ui = new TrainModelUI();
     public Train train = new Train();
     private Stack<Message> messages;
     private Message m;
     private double velocityFeedback;
     private double auth;
     private int passengers;
     private int doors = 0;
     private int temp = 65;
     private int brakes = 0;
     private int lights = 0;
     private int failures = 4;
     private double power,grade,speed,speedLimit;
     private String advertisement;

    public TrainModelMain(MessageQueue mq) {
      this.mq = mq;
      ui.setVisible(true);

    }

     public void run(){
          mReceive();
          velocityFeedback = train.calculateVelocity(power, velocityFeedback, grade, brakes, speedLimit, passengers);
          System.out.println("TrMod_vF(afterRec):"+velocityFeedback);
          ui.update(1,this.power,this.velocityFeedback,this.grade,this.brakes,this.speedLimit,this.passengers,this.lights,this.auth,this.temp,this.doors,this.advertisement);
          failures = ui.getFailures();
          //System.out.println(velocityFeedback);
          mSend();
     }
     public void mReceive(){
          messages = mq.receive(MDest.TrMd);
          while(!messages.isEmpty()){
               m = messages.pop();
               if(m.type() == MType.AUTH){
                    this.auth = m.dataD();
                    //System.out.println("TrMod_Auth: "+auth);
                    m = new Message(MDest.TrMd, auth, MType.AUTH);
                    mq.send(m, MDest.TrCtl);
               }
                else if(m.type() == MType.SPEED){
                    this.speed = (m.dataD());
                    //System.out.println("TrMod_Speed: "+speed);
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
                    //System.out.println("TrMod_power: "+power);
                    this.power = m.dataD();
               }
                else if(m.type() == MType.FEEDBACK){
                    //System.out.println("TrMod_feedback: "+velocityFeedback);
                    this.velocityFeedback = m.dataD();
               }
               else if(m.type() == MType.SPEEDLIMIT){

                    this.speedLimit = m.dataD();
                    //System.out.println("TrMod_limit: "+speedLimit);
               }
                else if (m.type() == MType.PASSENGERS){
                    this.passengers = m.dataI();
               }
		else if (m.type() == MType.GRADE){
                    this.passengers = m.dataI();
               }
		else if (m.type() == MType.ADVERTISEMENT){
                    this.advertisement = m.dataS();
               }
          }
     }
     public void mSend(){

               m = new Message(MDest.TrMd, velocityFeedback, MType.FEEDBACK);
               mq.send(m, MDest.TrCtl);

               m = new Message(MDest.TrMd, velocityFeedback, MType.FEEDBACK);
               mq.send(m, MDest.TcMd);
               //System.out.println("TrMd_vF:"+velocityFeedback);

               if (speed > 0) {
                 m = new Message(MDest.TrMd, speed, MType.SPEED);
                 mq.send(m, MDest.TrCtl);
                 speed = 0;
               }

	       if (failures != 4) {
	         m = new Message(MDest.TrMd, failures, MType.FAILURE);
                 mq.send(m, MDest.TrCtl);
				failures = 4;
				ui.setFailures(4);
	       }

               //System.out.println("TrMod SEND");
          }
     }
