package modules.ctcoffice;
import java.util.*;
import shared.*;

public class CTCOffice {
	private int speed;
	public MessageQueue mq = new MessageQueue();
	private Stack<Message> messages;
	private Message m;

	public CTCOffice(MessageQueue i){
	  mq = i;
	  speed = 1186;
	}

	public void run(){
	  mReceive();
	  mSend();
	}

	public void mReceive(){
	  messages = mq.receive(MDest.CTC);
	  while(!messages.isEmpty()){
	       m = messages.pop();
	       if(m.type() == MType.SPEED){
	            speed = m.dataI();
	       }
	  }
	}

	public void mSend(){
       m = new Message(MDest.CTC, speed, MType.SPEED);
       mq.send(m, MDest.TcCtl);
       System.out.println("CTC SEND");
	}
}