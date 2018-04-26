package modules.ctcoffice;
import java.io.*;
import java.util.*;
import shared.*;

/**
 * @author Ben Posey
 * TODO:
 *  o Skeleton code of variables and functions
 *  o Schedule Class
 *  o Time Class
 *  o Graph traversal of track for single destination
 *  o Message type for dispatched train
 *  o Send switch states
 */
public class CTCOffice {
	private Trainwreck tw;
	private CTCOfficeUI gui;
	private ArrayList<Queue<Schedule>> schedules;
	//private Track redLine;
	//private Track greenLine;
	//private Time currentTime;
	private int time;
	
	private double throughput;
	private double redThroughput;
	private double greenThroughput;
	private int ticketCount;
	private ArrayList<Object[]> trainList;
	private Boolean[] redSwitches = new Boolean[6];
	private Boolean[] greenSwitches;
	private double speed = 40.0; // km/h => 24.8548 mph
	/* - - - - - - - - - - - - - - - - - - - - - */
	public MessageQueue mq = new MessageQueue();
	private Stack<Message> messages;
	private Message m;
	private boolean dispatchReady = false;

	/* Graph Testing - - - - - - - - - - - - - - */
	private int dispatchLine = 1;
	private TrackGraph redTrack;
	private TrackGraph greenTrack;
	
	//private boolean[] redSwitches = new boolean[6];
	private double authority;

	/* UI variables- - - - - - - - - - - - - - - */
	public ArrayList<BlockTemp> redStops = new ArrayList<BlockTemp>();
	public ArrayList<String> redLineData = new ArrayList<String>();
	public ArrayList<BlockTemp> greenStops = new ArrayList<BlockTemp>();
	public ArrayList<String> greenLineData = new ArrayList<String>();

	private boolean dispatched = false;
	private boolean threadSuspended = false;

	/* Occupancy - - - - - - - - - - - - - - - - */
	private boolean[] redOcc;
	private boolean[] greenOcc;

	/* Maintenance - - - - - - - - - - - - - - - */
	private int maintenanceLine = 0;
	private int maintenanceBlock = 0;
	private boolean maintenanceReady = false;

	/* Multiple Trains - - - - - - - - - - - - - */
	private int trainCount = 0;

	private final double KMH_TO_MPH = (double)1/(double)1.609344;
	private final double M_TO_F = 3.280840;
	private final int RED = 1;
	private final int GREEN = 2;


	/* SETUP */
	public CTCOffice(MessageQueue mq, Trainwreck tw) {
		this.tw = tw;
		time = 10;
		// Setup Message Queue
		this.mq = mq;

		// Read redline csv
		String filename = "./src/main/java/modules/ctcoffice/redline.csv";
		File f = new File(filename);
		redTrack = getTrackData(f);

		// Initialize switch states
		for (int i = 0; i < redSwitches.length; i ++) {
			redSwitches[i] = new Boolean(false);
		}


		try {
           for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
               if ("Nimbus".equals(info.getName())) {
                   javax.swing.UIManager.setLookAndFeel(info.getClassName());
                   break;
               }
           }
       } catch (ClassNotFoundException ex) {
           java.util.logging.Logger.getLogger(CTCOfficeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       } catch (InstantiationException ex) {
           java.util.logging.Logger.getLogger(CTCOfficeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       } catch (IllegalAccessException ex) {
           java.util.logging.Logger.getLogger(CTCOfficeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       } catch (javax.swing.UnsupportedLookAndFeelException ex) {
           java.util.logging.Logger.getLogger(CTCOfficeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       }
		// Create GUI
		gui = new CTCOfficeUI(this);

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				gui.setVisible(true);
			}
		});
	}

	public void repaint() {
		gui.increaseTime();
		gui.updateOccupancy();
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public boolean getThreadStatus() {
		return threadSuspended;
	}

	public void setThreadStatus(boolean threadSuspended) {
		this.threadSuspended = threadSuspended;
	}

	public double getAuthority() {
		return authority;
	}

	public double getSpeed() {
		return speed;
	}

	public boolean[] getRedOcc() {
		return redOcc;
	}

	public boolean[] getGreenOcc() {
		return greenOcc;
	}
	
	public BlockTemp getStop(int line, int index) {
		switch (line) {
		case 1: // Red Line
			for (int i = 0; i < redStops.size(); i++) {
				if (redStops.get(i).number() == index) {
					return redStops.get(i);
				}
			}
			break;
		case 2: // Green Line
			for (int i = 0; i < greenStops.size(); i++) {
				if (greenStops.get(i).number() == index) {
					return greenStops.get(i);
				}
			}
			break;
		default:
			return null;
		}
		return null;
	}

	public void dispatchTrain(int src, int dest) {
		src--;
		dest--;
		DijkstraSPD spd = new DijkstraSPD(redTrack, src);
		authority = spd.distTo(dest);
		System.out.println("SHORTEST DISTANCE : "+authority);
		//spd.pathTo(dest);
		ArrayList<Integer> path = spd.getPath(dest);
		setSwitches(path);
		for (int i = 0; i < redSwitches.length; i ++) {
			//System.out.println(i+": "+redSwitches[i].booleanValue());
		}
		//System.out.println("DISPATCHED!");
		dispatchReady = true;
		dispatched = true;
		//System.out.println(dispatched);
	}

	public void trackMaintenance(int line, int block, boolean open) {
		maintenanceLine = line;
		if (open) { // Open block
			maintenanceBlock = block;
		} else { // Close block
			maintenanceBlock = -1*block;
		}
		maintenanceReady = true;
	}

	public void setSwitches(ArrayList<Integer> path) {
		for (int i = 0; i < path.size()-1; i++) {
			int current = path.get(i);
			//System.out.println("C:"+current);
			int next = path.get(i+1);
			//System.out.println("N:"+next+"\n-----");
			// Switch 1
			if (((current == 1) && (next == 16)) || ((current == 16) && (next == 1))) {
				redSwitches[0] = new Boolean(false);
			} else if (((current == 15) && (next == 16)) || ((current == 16) && (next == 15))) {
				redSwitches[0] = new Boolean(true);
			}

			// Switch 27
			if (((current == 27) && (next == 76)) || ((current == 76) && (next == 27))) {
				redSwitches[1] = new Boolean(false);
			} else if (((current == 27) && (next == 28)) || ((current == 28) && (next == 27))) {
				redSwitches[1] = new Boolean(true);
			}

			// Switch 32
			if (((current == 32) && (next == 72)) || ((current == 72) && (next == 32))) {
				redSwitches[2] = new Boolean(false);
			} else if (((current == 32) && (next == 31)) || ((current == 31) && (next == 32))) {
				redSwitches[2] = new Boolean(true);
			}

			// Switch 38
			if (((current == 38) && (next == 71)) || ((current == 71) && (next == 38))) {
				redSwitches[3] = new Boolean(false);
			} else if (((current == 38) && (next == 39)) || ((current == 39) && (next == 38))) {
				redSwitches[3] = new Boolean(true);
			}

			// Switch 43
			if (((current == 43) && (next == 67)) || ((current == 67) && (next == 43))) {
				redSwitches[4] = new Boolean(false);
			} else if (((current == 43) && (next == 42)) || ((current == 42) && (next == 43))) {
				redSwitches[4] = new Boolean(true);
			}

			// Switch 52
			if (((current == 51) && (next == 66)) || ((current == 66) && (next == 51))) {
				redSwitches[5] = new Boolean(false);
			} else if (((current == 51) && (next == 52)) || ((current == 52) && (next == 51))) {
				redSwitches[5] = new Boolean(true);
			}
		}
	}

	public boolean run(){
		mReceive();
		mSend();
		return dispatched;
	}

	public void mReceive() {
		messages = mq.receive(MDest.CTC);
		while(!messages.isEmpty()){
			m = messages.pop();
			if(m.type() == MType.SPEED) {
				speed = m.dataI();
			} else if(m.type() == MType.REALTRACK) {
	            redOcc = m.dataBA();
	            // for (int i = 0; i < redOcc.length; i++){
	            // 	System.out.println(i+": "+redOcc[i]);
	            // }
	            //System.out.println("-----");
	        } else if(m.type() == MType.PASSENGERS) {

	        }
        }
	}

	public void mSend() {
		
		if (dispatchReady) {

			// Send Speed (default limit for now);
			m = new Message(MDest.CTC, speed, MType.SPEED);
			System.out.println("CTC_Speed: "+speed);
			m.trainID = trainCount;
			mq.send(m, MDest.TcCtl);
			// Send Authority
			m = new Message(MDest.CTC, authority, MType.AUTH);
			System.out.println("CTC_Authority: "+authority);
			m.trainID = trainCount;
			mq.send(m, MDest.TcCtl);
			// Send Switch Positions
			for (int i = 0; i < 6; i++) {
				m = new Message(MDest.CTC, redSwitches[i], MType.SWITCH); // MType.SWITCH
				System.out.println("CTC_Switch: "+i+": "+redSwitches[i]);
				mq.send(m, MDest.TcCtl+i);
			}

			// Add Train to Message Queue & Trainwreck
			System.out.println("Adding Train: "+trainCount);
			mq.addTrain(dispatchLine);
			tw.addTrain(trainCount++);
			dispatchReady = false;
		}

		if (maintenanceReady) {
			m = new Message(MDest.CTC, maintenanceBlock, MType.MAINTENANCE);
			switch (maintenanceLine) {
				case RED:
					for (int i = 0; i < 6; i++) {
						mq.send(m, MDest.TcCtl+i);
					}
					break;
				case GREEN:
					for (int i = 6; i < 11; i++) {
						mq.send(m, MDest.TcCtl+i);
					}
					break;
				default:
					System.out.println("No Line Selected!");
			}
			maintenanceReady = false;
		}
	}



	// private double calcThroughput(Line line, int ticketCount) {

	// }


	// public Queue<Schedule> importSchedule(String filename) {

	// }


	private TrackGraph getTrackData(File f) {
		int color = 0;
		int size = 0;
		try {
			BufferedReader fr = new BufferedReader(new FileReader(f));
			
			// Initialize graph for number of vertices
			String delim = ",";
			String line = fr.readLine();
			String[] sizeLine = line.split(delim);
			size = Integer.parseInt(sizeLine[1]);
			System.out.println(size);

			TrackGraph tg = new TrackGraph(size);
			
			line = fr.readLine();

			while ((line = fr.readLine()) != null) {
				String[] str = line.split(delim);
				String trackLine = str[0];
				String section = str[1];
				int num = Integer.parseInt(str[2]);
				double distance = Double.parseDouble(str[3]);
				//str 4 - 7
				int v = Integer.parseInt(str[8]) - 1;
				int w = Integer.parseInt(str[9]) - 1;
				int branch = Integer.parseInt(str[10]);
				String info = "";
				if (str.length > 11) {
					info = str[11];
				}
				

				// Add block to graph
				BlockTemp insert = new BlockTemp(v, w, distance, section, num, branch);
				tg.addBlockTemp(insert);

				String secnum;
				// Add string to list
				if (info.length() > 0) {
					secnum = section+num+"|"+info;
				} else {
					secnum = section+num;	
				}
				if (trackLine.equals("Red")){
					color = 1;
					redLineData.add(secnum);
				} else {
					color = 2;
					greenLineData.add(secnum);
				}
				
			}
			if (color == RED) { // RED LINE
				redStops = tg.blocks();
				redOcc = new boolean[size];
			} else { // GREEN LINE
				greenStops = tg.blocks();
			}
			
			return tg;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
