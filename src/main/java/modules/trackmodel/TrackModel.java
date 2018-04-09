/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules.trackmodel;
import shared.*;
import java.util.regex.Pattern;
import java.util.*;

/*
make ghost train circ buffer with front of train occupying and back freeing

*/
/**
 *
 * @author Devon
 */
public class TrackModel {
    
	private static double DELTA_T = .001;
    Track redline;
	Track greenline;
	
	Train[] trains;
	int numTrains;
	
	private MessageQueue m;
	private Stack<Message> mailbox;
	
	
    public TrackModel(MessageQueue m) {
		this.m = m;
		numTrains = 0;
        redline = new Track();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrackmodelGUI(redline).setVisible(true);
            }
        });
    }
    
    public void update() {
        /* 1. check message queue
         * 2. update values
         * 3. send messages
         */
        
        /*
        while (!messenger[TrackModel].empty)
            message = check messenger[Trackmodel].pop();
            check for types of message: update train speed; update switch positions
        do calculations to find new train location
        send message of occupancy and foreward speed and authority
        
        */
		
		
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
				case MType.NEWTRAIN:
					Train[] temp = new Train[numTrains + 1];
					
					for (int i = 0; i < numTrains; i++) {
						temp[i] = trains[i];
					}
					trains = temp;
					numTrains++;
					break;		
				case MType.FEEDBACK:
					trains[(mail.from() - MDest.TrMd)/2].speed = mail.dataD();
			}
				
			
		}
		
		/*
			Move the train -------------------------------------------
		*/
		
		for (int i = 0; i < numTrains; i++) {
			Train train = trains[i];
			double traveled = train.move();
			double overflow = redline.getBlock(train.location).length - traveled;
			if (overflow > 0) {
				Block nextBlock = redline.next(redline.getBlock(train.location), train.frontNode);
				redline.setOccupancy(train.location, false);
				train.location = nextBlock.number;
				redline.setOccupancy(train.location, true);
				train.frontNode = redline.getBlock(train.location).other(train.frontNode);
				train.distanceIn = overflow;
			}
		}
		
		while (redline.getSize() == 0){
			System.out.println("boo");
		}
        int i = 0;
        int j = 0;
        Block curr = redline.getBlock(1);
        Block temp = curr;
        int lastNode = 1;
        System.out.println(lastNode + " " + curr);
        redline.setOccupancy(curr.number, true);
        while(true) {
            System.out.print(lastNode + " " + curr);

            temp = redline.next(curr, lastNode);
            lastNode = curr.other(lastNode);
            redline.setOccupancy(curr.number, false);
            curr = temp;
            redline.setOccupancy(curr.number, true);
            //System.out.println(lastNode);
            //redline.setOccupancy(lastNode, false);
            try {
            Thread.sleep(1000);
            }
            catch (Exception e) {
                System.out.println("didnt sleep");
            }
            //redline.setOccupancy(j%15, false);
            //j++;
        }
		
		
		
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TrackModel tm = new TrackModel(new MessageQueue());
        tm.update();
        
        /*
        Pattern p = Pattern.compile("[,\\s]");
        java.io.File file;
        java.util.Scanner scan = null;
        try {
            file = new java.io.File("src/data/redline.csv");
            scan = new java.util.Scanner(file).useDelimiter(",|\\r\\n");
        }
        catch (Exception e) {
            System.err.println(e);
        }
        
        
        redline = new Track(scan.nextInt() + 1);
        while (scan.hasNext()) {
            //A,1,50,0.5,40, ,1,15
            String section = scan.next();
            //System.out.println(section);
            int number = scan.nextInt();
            //System.out.println(number);
            int length = scan.nextInt();
            //System.out.println(length);
            double grade = scan.nextDouble();
            //System.out.println(grade);
            int limit = scan.nextInt();
            //System.out.println(limit);
            String station = scan.next();
            //System.out.println(station);
            int to = scan.nextInt();
            //System.out.println(to);
            int from = scan.nextInt();
            //System.out.println(from);
            int branch = scan.nextInt();
            int dir = scan.nextInt();
            Block b = new Block(to, from, section, number, station, grade, length, limit, branch); 
            redline.addBlock(b);
            b.dir = dir;
            redline.setSwitch(number, branch, dir);
        }
        redline.printTrack();
        //System.out.println(redline);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrackmodelGUI(redline).setVisible(true);
            }
        });
        */

        

    }
    
    
}
