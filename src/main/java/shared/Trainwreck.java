package shared;

import modules.ctcoffice.*;
import modules.trackcontroller.*;
import modules.trackmodel.*;
import modules.traincontroller.*;
import modules.trainmodel.*;

public class Trainwreck {
	public static void main(String[] args) {

		java.util.Scanner pauseScan = new java.util.Scanner(System.in);
		int time = 10;
		boolean dispatched = false;

		MessageQueue messagequeue = new MessageQueue();
		
		CTCOffice ctc = new CTCOffice(messagequeue);
		// PLC plc = new PLC();
		// TrackController trackctl_0 = new TrackController(messagequeue,new boolean[9],new boolean[8],new boolean[9],0,plc);
		// TrackController trackctl_1 = new TrackController(messagequeue,new boolean[6],new boolean[3],new boolean[4],1,plc);
		// TrackController trackctl_2 = new TrackController(messagequeue,new boolean[5],new boolean[3],new boolean[3],2,plc);
		// TrackController trackctl_3 = new TrackController(messagequeue,new boolean[4],new boolean[3],new boolean[4],3,plc);
		// TrackController trackctl_4 = new TrackController(messagequeue,new boolean[6],new boolean[3],new boolean[4],4,plc);
		// TrackController trackctl_5 = new TrackController(messagequeue,new boolean[6],new boolean[8],new boolean[8],5,plc);
		// TrackModel trackmodel = new TrackModel(messagequeue);
		// TrainModelMain trainmodel = new TrainModelMain(messagequeue);
		// TrainController trainctl = new TrainController(messagequeue);
		
		try {
			while (true) {
				dispatched = ctc.run();
				// trackctl_0.run();
				// trackctl_1.run();
				// trackctl_2.run();
				// trackctl_3.run();
				// trackctl_4.run();
				// trackctl_5.run();
				// trackmodel.run();
				// // Don't run trains until dispatched from CTC
				// if (dispatched) {
				// 	trainmodel.run();
				// 	trainctl.run();
				// }
				
				//Will get the time to sleep from CTC once implemented
				time = ctc.getTime();
				//System.out.println("- - - - - Pause - - - - -");
				Thread.sleep(time);
				// Update clock in UI
				ctc.repaint();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}