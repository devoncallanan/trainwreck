package shared;

import modules.ctcoffice.*;
import modules.trackcontroller.*;
import modules.trackmodel.*;
import modules.traincontroller.*;
import modules.trainmodel.*;
import java.io.*;
import java.util.*;

public class Trainwreck {

	private MessageQueue messagequeue = new MessageQueue();
	private static Trainwreck tw;

	private ArrayList<TrainModelMain> trainmodels;
	private ArrayList<TrainController> trainctls;

	public static void main(String[] args) {
		tw = new Trainwreck();

		tw.run();
	}

	private void run() {
		java.util.Scanner pauseScan = new java.util.Scanner(System.in);
		int time = 10;
		boolean dispatched = false;

		boolean [] crossingRed = {false,true,false,false,false,false};
		boolean [] crossingGreen = {false, true, false, false, false, false, false, false, false, false, false, false};
		CTCOffice ctc = new CTCOffice(messagequeue, tw);
		PLC plc = new PLC();
		TrackController trackctl_0 = new TrackController(messagequeue,new boolean[9],new boolean[8],new boolean[9],0,plc);
		TrackController trackctl_1 = new TrackController(messagequeue,new boolean[6],new boolean[3],new boolean[4],1,plc);
		TrackController trackctl_2 = new TrackController(messagequeue,new boolean[5],new boolean[3],new boolean[3],2,plc);
		TrackController trackctl_3 = new TrackController(messagequeue,new boolean[4],new boolean[3],new boolean[4],3,plc);
		TrackController trackctl_4 = new TrackController(messagequeue,crossingRed,new boolean[3],new boolean[4],4,plc);
		TrackController trackctl_5 = new TrackController(messagequeue,new boolean[6],new boolean[8],new boolean[8],5,plc);
		TrackController trackctl_6 = new TrackController(messagequeue,new boolean[8],new boolean[7],new boolean[6],6,plc);
		TrackController trackctl_7 = new TrackController(messagequeue,crossingGreen, new boolean[37],new boolean[39],7,plc);
		TrackController trackctl_8 = new TrackController(messagequeue,new boolean[7],new boolean[11],new boolean[14],8,plc);
		TrackController trackctl_9 = new TrackController(messagequeue,new boolean[7],new boolean[8],new boolean[8],9,plc);
		
		TrackModel trackmodel = new TrackModel(messagequeue);
		// TrainModelMain trainmodel = new TrainModelMain(messagequeue);
		// TrainController trainctl = new TrainController(messagequeue, 0);

		trainmodels = new ArrayList<TrainModelMain>();
		trainctls = new ArrayList<TrainController>();

		try {
			while (true) {
				dispatched = ctc.run();
				trackctl_0.run();
				trackctl_1.run();
				trackctl_2.run();
				trackctl_3.run();
				trackctl_4.run();
				trackctl_5.run();
				trackctl_6.run();
				trackctl_7.run();
				trackctl_8.run();
				trackctl_9.run();
				trackmodel.run();
				// Don't run trains until dispatched from CTC
				if (dispatched) {
					for (int i = 0; i < trainmodels.size(); i++) {
						trainmodels.get(i).run();
						trainctls.get(i).run();
					}					
				}

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

	public void addTrain(int id) {
		trainmodels.add(id, new TrainModelMain(messagequeue, id));
		trainctls.add(id, new TrainController(messagequeue, id));
	}
}
