package shared;

public class Trainwreck {
	public static void main(String[] args) {
		System.out.println("Gradle Test");
		
		MessageQueue messagequeue = new MessageQueue();
		
		CTC ctc = new CTC(messagequeue);
		TrackController trackctl = new TrackController(messagequeue);
		TrackModel trackmodel = new TrackModel(messagequeue);
		TrainModel trainmodel = new TrainModel(messagequeue);
		TrainController trainctl = new TrainController(messagequeue);
		
		ctc.run();
		trackctl.run();
		trainmodel.run();
		trainmodel.run();
		trainctl.run();
	}
}