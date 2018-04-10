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
	//private CTCOffice ctc;
	//private CTCOfficeUI gui;
	private ArrayList<Queue<Schedule>> schedules;
	//private Track redLine;
	//private Track greenLine;
	//private Time currentTime;
	private int timeMult;
	private int trainCount;
	private double throughput;
	private double redThroughput;
	private double greenThroughput;
	private int ticketCount;
	private ArrayList<Object[]> trainList;
	private Boolean[] redSwitches = new Boolean[6];
	private Boolean[] greenSwitches;
	private double speed = 40.0;
	/* - - - - - - - - - - - - - - - - - - - - - */
	public MessageQueue mq = new MessageQueue();
	private Stack<Message> messages;
	private Message m;
	private boolean dispatchReady = false;

	/* Graph Testing */
	private TrackGraph track;
	private ArrayList<String> stops;
	//private boolean[] redSwitches = new boolean[6];
	private double authority;

	/* SETUP */
	public CTCOffice(MessageQueue mq) {
		//gui = new CTCOfficeUI();

		// java.awt.EventQueue.invokeLater(new Runnable() {
		// 	public void run() {
		// 		gui.setVisible(true);
		// 	}
		// });

		this.mq = mq;

		// Read redline csv
		String filename = "./src/main/java/modules/ctcoffice/redline.csv";
		File f = new File(filename);
		track = getTrackData(f);

		// Initialize switch states
		for (int i = 0; i < redSwitches.length; i ++) {
			redSwitches[i] = new Boolean(false);
		}
		// BreadthFirstPaths bfs = new BreadthFirstPaths(track, src);
		// System.out.println("SHORTEST DISTANCE : "+bfs.distTo(dest));
		// bfs.pathTo(dest);
	}

	public void dispatchTrain(int src, int dest) {
		src--;
		dest--;
		DijkstraSPD spd = new DijkstraSPD(track, src);
		authority = spd.distTo(dest);
		//System.out.println("SHORTEST DISTANCE : "+authority);
		//spd.pathTo(dest);
		ArrayList<Integer> path = spd.getPath(dest);
		setSwitches(path);
		// for (int i = 0; i < redSwitches.length; i ++) {
		// 	System.out.println(i+": "+redSwitches[i].booleanValue());
		// }
		dispatchReady = true;
	}

	public void setSwitches(ArrayList<Integer> path) {
		for (int i = 0; i < path.size()-1; i++) {
			int current = path.get(i);
			//System.out.println("C:"+current);
			int next = path.get(i+1);
			//System.out.println("N:"+next+"\n-----");
			// Switch 1
			if (((current == 1) && (next == 16)) || ((current == 16) && (next == 1))) {
				redSwitches[0] = new Boolean(true);
			} else if (((current == 15) && (next == 16)) || ((current == 16) && (next == 15))) {
				redSwitches[0] = new Boolean(false);
			}

			// Switch 27
			if (((current == 27) && (next == 76)) || ((current == 76) && (next == 27))) {
				redSwitches[1] = new Boolean(true);
			} else if (((current == 27) && (next == 28)) || ((current == 28) && (next == 27))) {
				redSwitches[1] = new Boolean(false);
			}

			// Switch 32
			if (((current == 32) && (next == 72)) || ((current == 72) && (next == 32))) {
				redSwitches[2] = new Boolean(true);
			} else if (((current == 32) && (next == 31)) || ((current == 31) && (next == 32))) {
				redSwitches[2] = new Boolean(false);
			}

			// Switch 38
			if (((current == 38) && (next == 71)) || ((current == 71) && (next == 38))) {
				redSwitches[3] = new Boolean(true);
			} else if (((current == 38) && (next == 39)) || ((current == 39) && (next == 38))) {
				redSwitches[3] = new Boolean(false);
			}

			// Switch 43
			if (((current == 43) && (next == 67)) || ((current == 67) && (next == 43))) {
				redSwitches[4] = new Boolean(true);
			} else if (((current == 43) && (next == 42)) || ((current == 42) && (next == 43))) {
				redSwitches[4] = new Boolean(false);
			}

			// Switch 52
			if (((current == 51) && (next == 66)) || ((current == 66) && (next == 51))) {
				redSwitches[5] = new Boolean(true);
			} else if (((current == 51) && (next == 52)) || ((current == 52) && (next == 51))) {
				redSwitches[5] = new Boolean(false);
			}
		}
	}

	public void run(){
		mReceive();
		dispatchTrain(74,32);
		mSend();
	}

	public void mReceive() {
		messages = mq.receive(MDest.CTC);
		while(!messages.isEmpty()){
			m = messages.pop();
			if(m.type() == MType.SPEED) {
				speed = m.dataI();
			}
		}
	}

	public void mSend() {
		
		if (dispatchReady) {
			// Send Speed (default limit for now);
			m = new Message(MDest.CTC, speed, MType.SPEED);
			System.out.println("CTC_Speed: "+speed);
			mq.send(m, MDest.TcCtl);
			// Send Authority
			m = new Message(MDest.CTC, authority, MType.AUTH);
			System.out.println("CTC_Authority: "+authority);
			mq.send(m, MDest.TcCtl);
			// Send Switch Positions
			for (int i = 0; i < 6; i ++) {
				m = new Message(MDest.CTC, redSwitches[i], 13); // MType.SWITCH
				System.out.println("CTC_Switch: "+i+": "+redSwitches[i]);
				mq.send(m, MDest.TcCtl);
			}
			dispatchReady = false;
		}
	}

	// private double calcThroughput(Line line, int ticketCount) {

	// }

	// public Time modifyTime(int timeCommand) {

	// }

	// public Queue<Schedule> importSchedule(String filename) {

	// }

	// public Object[] dispatchTrain() {

	// }

	private static TrackGraph getTrackData(File f) {
		try {
			BufferedReader fr = new BufferedReader(new FileReader(f));
			
			// Initialize graph for number of vertices
			//int size = Integer.parseInt(fr.readLine());
			TrackGraph tg = new TrackGraph(74);
			
			// for (int i = 0; i < num; i++) {
			// 	stops.add(fr.readLine());
			// }
			String delim = ",";

			String line = fr.readLine();

			while ((line = fr.readLine()) != null) {
				String[] str = line.split(delim);
				String trackLine = str[0];
				String section = str[1];
				int num = Integer.parseInt(str[2]);
				int distance = (int)Double.parseDouble(str[3]);
				//str 4 - 7
				int v = Integer.parseInt(str[8]) - 1;
				int w = Integer.parseInt(str[9]) - 1;
				int branch = Integer.parseInt(str[10]);

				BlockTemp insert = new BlockTemp(v, w, distance, section, num, branch);

				tg.addBlockTemp(insert);
			}

			return tg;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}