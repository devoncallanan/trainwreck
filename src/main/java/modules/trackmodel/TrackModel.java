package modules.trackmodel;
import shared.*;
import java.util.*;

public class TrackModel {
	
	private MessageQueue m;
	private Stack<Message> mailbox;

	public TrackModel(MessageQueue m) {
		this.m = m;
	}
	
	public void run() {
		
		//check messages
		
		mailbox = m.receive(MDest.TcMd);
		

		while (!mailbox.isEmpty()) {
			Message mail = mailbox.pop();
			System.out.println("TkMod: "+mail.dataI());
			switch (mail.type()) {
				case MType.AUTH:
					System.out.println(mail.dataI());
					m.send(mail, MDest.TrMd);
					break;
				case MType.SPEED:
					System.out.println(mail.dataI());
					m.send(mail, MDest.TrMd);
					System.out.println("TkMod SEND");
					break;					
			}
				
			
		}
	}
	
	public static void main(String[] args) {
		
		
		//check messages
		
		
	}
}