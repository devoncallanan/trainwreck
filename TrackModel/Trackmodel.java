/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackmodelsubsystem;
import java.util.regex.Pattern;

/*
make ghost train circ buffer with front of train occupying and back freeing

*/
/**
 *
 * @author Devon
 */
public class Trackmodel {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Track redline;
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
    
    
}
