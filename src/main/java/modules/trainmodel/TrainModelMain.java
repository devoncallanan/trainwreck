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
     public TrainModelUI ui;
     public Train train;
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
     private int trainID;
	 private boolean nullPower;

     private int ID;

    public TrainModelMain(MessageQueue mq, int trainID) {
      this.mq = mq;
      this.trainID = trainID;
	  this.ui = new TrainModelUI();
	        ui.setVisible(true);
	  this.train  = new Train();
	  this.ID  = 2*trainID;
	  System.out.println("Bry " + ID + " trainid " + trainID);

    }

     public void run(){
          mReceive();
		  if (nullPower) this.power = 0;
          velocityFeedback = train.calculateVelocity(power, velocityFeedback, grade, brakes, speedLimit, passengers);
          //System.out.println("TrMod_vF(afterRec):"+velocityFeedback);
          ui.update(1,this.power,this.velocityFeedback,this.grade,this.brakes,this.speedLimit,this.passengers,this.lights,this.auth,this.temp,this.doors,this.advertisement);
          failures = ui.getFailures();
          //System.out.println(velocityFeedback);
          mSend();
     }
     public void mReceive(){
          messages = mq.receive(MDest.TrMd+ID);
		  //System.out.println("Bryan" + MDest.TrMd+ID);
          while(!messages.isEmpty()){
               m = messages.pop();
               if(m.type() == MType.AUTH){
                    this.auth = m.dataD();
					System.out.println("Train " + m.trainID + " sending to " + ID + " auth " + m.dataD());
                    //System.out.println("TrMod_Auth: "+auth);
                    m = new Message(MDest.TrMd+ID, auth, MType.AUTH);
                    mq.send(m, MDest.TrCtl+ID);
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
			   else if (m.type() == MType.POWERFAILURE) {
				   this.nullPower = 1 == m.dataI();
			   }
          }
     }
     public void mSend(){

               m = new Message(MDest.TrMd+ID, velocityFeedback, MType.FEEDBACK);
               mq.send(m, MDest.TrCtl+ID);

               m = new Message(MDest.TrMd+ID, velocityFeedback, MType.FEEDBACK);
               mq.send(m, MDest.TcMd);
               //System.out.println("TrMd+ID_vF:"+velocityFeedback);

               if (speed > 0) {
                 m = new Message(MDest.TrMd+ID, speed, MType.SPEED);
                 mq.send(m, MDest.TrCtl+ID);
                 speed = 0;
               }

	       if (failures != 4) {
	         m = new Message(MDest.TrMd+ID, failures, MType.FAILURE);
                 mq.send(m, MDest.TrCtl+ID);
				failures = 4;
				ui.setFailures(4);
	       }

               //System.out.println("TrMod SEND");
          }
     }
