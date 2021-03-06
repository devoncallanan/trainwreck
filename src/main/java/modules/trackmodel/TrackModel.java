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
    
public static double KMH_TO_MS = 1000.0/3600.0; 
public static double MS_TO_KMH = 3600.0/10000.0; 
	private static double DELTA_T = .1;
    Track redline;
	Track greenline;
	
	Controller conts;
	Train[] trains;
	int numTrains;
	
	private MessageQueue m;
	private Stack<Message> mailbox;
	
	Random rnd;
	boolean messaging;
	boolean power;
	
	
    public TrackModel(MessageQueue m) {
		this.m = m;
		numTrains = 0;
        redline = new Track();
		greenline = new Track();
		conts = new Controller(10);
		conts.init();
		java.awt.EventQueue.invokeLater(() -> {
		                new TrackmodelGUI(this, redline, greenline, conts).setVisible(true);
        });
		
		messaging = true;
		power = true;
		rnd = new Random();
    }
    
    public void run() {
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
		
		Message tempM;
		mailbox = m.receive(MDest.TcMd);
		

		while (!mailbox.isEmpty()) {
			Message mail = mailbox.pop();
			switch (mail.type()) {
				case MType.AUTH:
					if (messaging) m.send(mail, MDest.TrMd +  (2 * mail.trainID));
					break;
				case MType.SPEED:
					if (messaging) m.send(mail, MDest.TrMd + (2 * mail.trainID));
					break;	
				case MType.NEWTRAIN:
					Train[] temp = new Train[numTrains + 1];
					
					for (int i = 0; i < numTrains; i++) {
						temp[i] = trains[i];
					}
					trains = temp;
					if (mail.dataI() == 1) {
						trains[numTrains] = new Train(numTrains,9, 10, 1);
						redline.setOccupancy(9, true);
						conts.update(9, 1, 1);
						trains[numTrains].blocks.add(redline.getBlock(9));
						numTrains++;
						tempM = new Message(MDest.TcMd, redline.getBlock(9).limit, MType.SPEEDLIMIT);
						if (messaging) m.send(tempM, MDest.TrCtl);	
					}
					else {
						trains[numTrains] = new Train(numTrains,62, 61, 2);
						greenline.setOccupancy(62, true);
						conts.update(62, 1, 2);
						trains[numTrains].blocks.add(greenline.getBlock(62));
						numTrains++;	
						tempM = new Message(MDest.TcMd, greenline.getBlock(62).limit, MType.SPEEDLIMIT);
						if (messaging) m.send(tempM, MDest.TrCtl);							
					}	
					break;		
				case MType.FEEDBACK:
					trains[(mail.from() - MDest.TrMd)/2].speed = mail.dataD();
                    break;
				case MType.SWITCH:
                    if (redline.isLoaded() && greenline.isLoaded()) {
    					int contid = mail.from() - MDest.TcCtl;
    					int realBlock = conts.getSwitchConvert(contid);
    					int branch = 1;
    					if (!mail.dataB()) branch = -1;
						conts.setSwitch(contid, branch);
						if (contid < 6) {
							redline.setSwitch(realBlock, branch, 0);							
						}
						else {
							greenline.setSwitch(realBlock, branch, 0);	
						}
						
                    }
                    break;
				case MType.MAINTENANCE:
					if (mail.from() < MDest.TrCtl + 5) {
						if (mail.dataI() > 0){
							redline.setOccupancy(mail.dataI(), false);
						}
						else {
							redline.setOccupancy(-(mail.dataI()), true);							
						}
					}
					else {
						if (mail.dataI() > 0){
							greenline.setOccupancy(mail.dataI(), false);
						}
						else {
							greenline.setOccupancy(-(mail.dataI()), true);							
						}					
					}
					break;
				case MType.ZEROSPEED:
					for (int i = 0; i < trains.length; i++) {
						if (trains[i].location == mail.dataI()) {
							tempM = new Message(MDest.TcMd, 0, MType.ZEROSPEED);
							System.out.println("Zero received: from "+mail.from());
							//if (messaging) m.send(tempM, MDest.TrCtl + trains[i].id*2);
						}
					}
			}
				
			
		}
		
		/*
			Move the train -------------------------------------------        
            while (redline.getSize() == 0){
            System.out.println(redline.getSize());
        }
		*/

		boolean changedBlock = false;
		Block nextBlock = null;
		if (redline.isLoaded() && greenline.isLoaded()) {
    		for (int i = 0; i < numTrains; i++) {
    			Train train = trains[i];
				if (!power) {
					tempM = new Message(MDest.TcMd, 1, MType.POWERFAILURE);
					m.send(tempM, MDest.TrMd +i*2);
				}
				else {
					tempM = new Message(MDest.TcMd, 0, MType.POWERFAILURE);
					m.send(tempM, MDest.TrMd +i*2);					
				}
				if (train.track == 1) {
					chugR(train, redline, conts, i);
				}
				else {
					chugG(train,greenline,conts,i);
				}

    		}
        }
		
		
		/* ---------------- send messages -------------------------- */
		
		//tempM = new Message(MDest.TcMd, 70.0, MType.SPEEDLIMIT);
		//m.send(tempM, MDest.TrMd);
		for (int i = 0; i < 10; i++) {
			tempM = new Message(MDest.TcMd, conts.getOccArray(i), MType.TRACK);
			m.send(tempM, MDest.TcCtl + i);
			if (numTrains > 0) {
				tempM = new Message(MDest.TcMd, redline.getOccupancies(), MType.REALTRACK);
				m.send(tempM, MDest.TcCtl + i);
			}
		}
		

		
		// if (changedBlock) {
			//grade
			// tempM = new Message(MDest.TcMd, nextBlock.grade, MType.GRADE);
			// m.send(tempM, MDest.TrMd);
			//beacon
			// if (nextBlock.beacon != null) {
				// tempM = new Message(MDest.TcMd, nextBlock.beacon, nextBlock.stationSide, MType.BEACON);
				// m.send(tempM, MDest.TrCtl);
			// }
			//speedlimit
			// tempM = new Message(MDest.TcMd, nextBlock.limit, MType.SPEEDLIMIT);
			// m.send(tempM, MDest.TrCtl);			
		// }
		

        
		/*
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
		*/
		
		
        
    }
	
	
	
	public void chugR(Train train, Track redline, Controller conts, int id) {
		boolean changedBlock = false;
		Block nextBlock = null;
		double traveled = train.move();
		double overflow = traveled - redline.getBlock(train.location).length ;
		Message tempM;
		//System.out.println("moving trains " + traveled + " ovf " + overflow);
		if (overflow > 0) {
			nextBlock = redline.next(redline.getBlock(train.location), train.backNode);
			train.backNode = redline.getBlock(train.location).other(train.backNode);
			train.location = nextBlock.number;
			redline.setOccupancy(train.location, true);
			conts.update(train.location, 1, 1);
			train.blocks.add(nextBlock);
			train.distanceIn = overflow;
			changedBlock = true;
		}
		if (train.tailOverflow > 0) {
			Block exited = train.blocks.remove(0);
			redline.setOccupancy(exited.number, false);
			conts.update(exited.number, 0, 1);	
			train.distanceInTail = 0;
		}		
		if (changedBlock) {
			//grade
			tempM = new Message(MDest.TcMd, nextBlock.grade, MType.GRADE);
			m.send(tempM, MDest.TrMd + id*2);
			//beacon
			if (nextBlock.beacon != null) {
				tempM = new Message(MDest.TcMd, nextBlock.beacon, nextBlock.stationSide, MType.BEACON);
				if (messaging) m.send(tempM, MDest.TrCtl + id*2);
				tempM.trainID = id;
				if (messaging) m.send(tempM, MDest.CTC);
			}
			//speedlimit
			tempM = new Message(MDest.TcMd, nextBlock.limit, MType.SPEEDLIMIT);
			if (messaging) m.send(tempM, MDest.TrCtl + id*2);	
			//tickets/passengers
			if (nextBlock.station != null) {
				int temppass = rnd.nextInt(148);
				int delta = temppass - train.passengers;
				train.passengers = temppass;
				if (delta > 0) {
					tempM = new Message(MDest.TcMd, delta, MType.TICKETS);
					tempM.trainID = 1;
					m.send(tempM, MDest.CTC);
				}
				tempM = new Message(MDest.TcMd, train.passengers, MType.PASSENGERS);
				m.send(tempM, MDest.TrMd);
				tempM.trainID = id;
				m.send(tempM, MDest.CTC);
			}
		}		
	}
	
	
	public void chugG(Train train, Track redline, Controller conts, int id) {
		boolean changedBlock = false;
		Block nextBlock = null;
		double traveled = train.move();
		double overflow = traveled - redline.getBlock(train.location).length ;
		Message tempM;
		//System.out.println("moving trains " + traveled + " ovf " + overflow);
		if (overflow > 0) {
			nextBlock = redline.next(redline.getBlock(train.location), train.backNode);
			train.backNode = redline.getBlock(train.location).other(train.backNode);
			train.location = nextBlock.number;
			redline.setOccupancy(train.location, true);
			conts.update(train.location, 1, 2);
			train.blocks.add(nextBlock);
			train.distanceIn = overflow;
			changedBlock = true;
		}
		if (train.tailOverflow > 0) {
			Block exited = train.blocks.remove(0);
			redline.setOccupancy(exited.number, false);
			conts.update(exited.number, 0, 2);	
			train.distanceInTail = 0;
		}		
		if (changedBlock) {
			//grade
			tempM = new Message(MDest.TcMd, nextBlock.grade, MType.GRADE);
			m.send(tempM, MDest.TrMd + id*2);
			//beacon
			if (nextBlock.beacon != null) {
				tempM = new Message(MDest.TcMd, nextBlock.beacon, nextBlock.stationSide, MType.BEACON);
				if (messaging) m.send(tempM, MDest.TrCtl + id*2);
				tempM.trainID = id;
				if (messaging) m.send(tempM, MDest.CTC);
			}
			//speedlimit
			tempM = new Message(MDest.TcMd, nextBlock.limit, MType.SPEEDLIMIT);
			if (messaging) m.send(tempM, MDest.TrCtl + id*2);			
		}		
		if (nextBlock.station != null) {
			int temppass = rnd.nextInt(148);
			int delta = temppass - train.passengers;
			train.passengers = temppass;
			if (delta > 0) {
				tempM = new Message(MDest.TcMd, delta, MType.TICKETS);
				tempM.trainID = 2;
				m.send(tempM, MDest.CTC);
			}
			tempM = new Message(MDest.TcMd, train.passengers, MType.PASSENGERS);
			m.send(tempM, MDest.TrMd);
			tempM.trainID = id;
			m.send(tempM, MDest.CTC);
		}
	}
	
	public void breakCircuits(boolean op) {
		this.messaging = op;
	}
	
	public void setPower(boolean op) {
		this.power = op;
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
   
        
		
		

		
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
