/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules.trackmodel;

import shared.*;
import java.util.regex.Pattern;


/**
 *
 * @author Devon
 */
public class Track extends javax.swing.AbstractListModel<String>{
    private Block[][] track;
    private Block[] blocks;
    public int[][] switches;  //change back to private later [location][position]
    private int length;
    private int items;
    
    public Track() {
        this.length = 0;
        this.items = 0;
        switches = new int[0][0];
    }
    public Track(int length) {
        this.track = new Block[length][3];
        this.blocks = new Block[length];
        this.switches = new int[length][2];
        this.length = length;
        this.items = 0;
    }
    
    public void loadTrack(java.io.File trackfile) {
        Pattern p = Pattern.compile("[,\\s]");
        java.util.Scanner scan = null;
        try {
            scan = new java.util.Scanner(trackfile).useDelimiter(",|\\r\\n");
        }
        catch (Exception e) {
            System.err.println(e);
        }
        
        
        int length = 1 + 76;//+ scan.nextInt();
        this.track = new Block[length][3];
        this.blocks = new Block[length];
        this.switches = new int[length][2];
        this.length = length;
        this.items = 0;
        scan.nextLine();
        scan.nextLine();
        while (scan.hasNext()) {
			scan.next();
            String section = scan.next();
            int number = scan.nextInt();
            double bLength = scan.nextDouble();
            double grade = scan.nextDouble();
            double limit = scan.nextDouble();
			double elevation = scan.nextDouble();
			double cumElevation = scan.nextDouble();
            int from = scan.nextInt();
            int to = scan.nextInt();
            int branch = scan.nextInt();
			int switchloc = scan.nextInt();
			String infrastructure = scan.next();
			
            Block b = new Block(to, from, section, number, infrastructure, grade, bLength, limit, branch); 
            this.addBlock(b);
            //this.setSwitch(number, branch, dir);
            fireContentsChanged(this, number, number);
        }
        this.printTrack();

    }
    
    public void addBlock(Block b) {
        int to = b.to;
        int from = b.from;
        int err;
        err = addTo(to, b);
        err += addTo(from, b);
        if (err == -1) {
            System.err.println("couldnt add block");
        }
        items++;
        blocks[items] = b;        
        if (items != b.number) {
            System.err.println("the block number is not the index");
        }
    }
    
    private int addTo(int loc, Block b) {
        int i = 0;
        while (track[loc][i] != null && i < 3) {
            i++;
        }
		if (i == 2) switches[loc][0] = 1;
        if (i < 3) {
            track[loc][i] = b;
        }
        else return -1;
        return 0;
    }
	/*
	 *next()
	 *return the next block a train would occupy given the state of the track;
	 *switch position 0 will keep the train off of the siding
	 *input arguments should be the current block and the node number that the train is coming from.
	 *keep track of both! We need the previous node number to prevent the train from switching directions
	 *The general idea is:
	 *	1.Look at the edges that do not come from the node you were just at
	 *	2.If there is no switch, just get the block from the adjacency list of the next node
	 *	3.If there is a switch, make sure you can travel accross it
	 */
    
    public Block next(Block b, int node) {
        System.out.println("call to next");
        Block nextBlock = b;
        int i = 0;
        boolean switchHere = (switches[b.other(node)][0] != 0);
        int switchDir = switches[b.other(node)][0];
        if (b.to != node) {
            //look at b.to options
            System.out.println("looking where b.to is not lastNode " + b.to + switchHere + " : " + track[b.to][i].occupied);
            while (track[b.to][i] != null) {
                if (!track[b.to][i].occupied) {
                    if (switchHere) {
                        if (b.branch != 0 && (b.branch + track[b.to][i].branch) != 0) { //test right angle turns
                            System.out.print("right angle");
                            if (b.branch == switchDir) { //test merging into switch
                                nextBlock = track[b.to][i];
                            }
                        }                         
                        else if (b.branch == 0 && track[b.to][i].branch == switchDir) { //check to make sure it is correct direction for fork
                            nextBlock = track[b.to][i];
                        }
						else System.out.println("Switch: " + switchDir);

                    }
                    else {
                        nextBlock = track[b.to][i];
                        //System.out.println("putting " + nextBlock);
                    }
                }
                if (i < 2) i++;
                else break;
            }
        }
        else if (b.from != node) {
            //look at b.from options
            System.out.println("looking where b.from is not lastNode " + b.from + switchHere + " @ " + b.other(node));
            while (track[b.from][i] != null) {
                if (!track[b.from][i].occupied) {
                    if (switchHere) {
                        if (b.branch != 0 && (b.branch + track[b.from][i].branch) != 0) { //test right angle turns
                            System.out.println("right angle");
                            if (b.branch == switchDir) { //test merging into switch
                                nextBlock = track[b.from][i];
                            }
                        }                         
                        else if (b.branch == 0 && track[b.from][i].branch == switchDir) { //check to make sure it is correct direction for fork
                            nextBlock = track[b.from][i];
                        }
						else System.out.println("Switch: " + switchDir);

                    } 
                    else {
                        nextBlock = track[b.from][i];
                    }                  
                }
                if (i < 2) i++;
                else break;
            }
        }
        return nextBlock;
    }
    
    public Block getBlock(int index) {
        return blocks[index];
    }
    
    public int getSize() {
        return items;
    }
    
    public void setOccupancy(int blockNumber, boolean occupied) {
        blocks[blockNumber].occupied = occupied;
        fireContentsChanged(this, blockNumber, blockNumber);
    }
    
    public void setSwitch(int index, int branch, int dir) {
        switches[index][0] = branch;
        switches[index][1] = dir;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= items; i++) {
            Block temp = blocks[i];
            sb.append(temp.number + " : ");
            sb.append(temp.length + " : ");
            sb.append(temp.station);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    //for listmodel
    
    @Override
    public String getElementAt(int index) {
        return this.getBlock(index + 1).toString(); 
    }
    
    public void printTrack() {
        for (int i = 0; i < length; i++) {
            System.out.print(" switch " + switches[i][0] + " ");
            for (int j = 0; j < 3; j++) {
                if (track[i][j] != null) {
                    if (track[i][j].to != i) {
                    System.out.print(track[i][j].to + " ");
                    }
                    else {
                    System.out.print(track[i][j].from + " ");                        
                    }
                }
            }
			System.out.println();
        }
    }

}


