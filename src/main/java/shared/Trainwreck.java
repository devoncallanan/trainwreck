package shared;

import modules.ctcoffice.*;
import modules.trackcontroller.*;
import modules.trackmodel.*;
import modules.traincontroller.*;
import modules.trainmodel.*;

public class Trainwreck {
	public static void main(String[] args) {
		System.out.println("Gradle Test");
		
		MessageQueue messagequeue = new MessageQueue();
		
		CTCOffice ctc = new CTCOffice(messagequeue);
		TrackController trackctl = new TrackController(messagequeue);
		TrackModel trackmodel = new TrackModel(messagequeue);
		TrainModel trainmodel = new TrainModel(messagequeue);
		TrainController trainctl = new TrainController(messagequeue);
		
		ctc.run();
		trackctl.run();
		trackmodel.run();
		trainmodel.run();
		trainctl.run();

	}
}