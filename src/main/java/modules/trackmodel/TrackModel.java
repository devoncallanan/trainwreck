package modules.trackmodel;


public class TrackModel {
	
	MessageQueue m

	public TrackModel(MessageQueue m) {
		this.m = m;
	}
	
	public run() {
		
		//check messages
		
		java.util.Stack mailbox = m.recieve(MDest.TrMd);
		
		while (!mailbox.empty()) {
			Message mail = mailbox.pop();
			
			switch (mail.type()) {
				case MType.AUTH:
					System.out.println(mail.dataI());
					mailbox.send(mail, MDest.TrMd);
					break;
				case MType.SPEED:
					System.out.println(mail.dataI());
					mailbox.send(mail, MDest.TrMd);
					break;					
			}
				
			
		}
	}
	
	public static void main(String[] args) {
		
		
		//check messages
		
		
	}
}