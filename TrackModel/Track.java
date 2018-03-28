/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package trackmodelsubsystem;

import java.util.regex.Pattern;


/**
 *
 * @author Devon
 */
public class Track extends javax.swing.AbstractListModel<String>{
    private Block[][] track;
    private Block[] blocks;
    public int[][] switches;  //change back to private later
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
        
        
        int length = scan.nextInt() + 1;
        this.track = new Block[length][3];
        this.blocks = new Block[length];
        this.switches = new int[length][2];
        this.length = length;
        this.items = 0;
        
        while (scan.hasNext()) {
            //A,1,50,0.5,40, ,1,15
            String section = scan.next();
            //System.out.println(section);
            int number = scan.nextInt();
            //System.out.println(number);
            int bLength = scan.nextInt();
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
            Block b = new Block(to, from, section, number, station, grade, bLength, limit, branch); 
            this.addBlock(b);
            b.dir = dir;
            this.setSwitch(number, branch, dir);
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
        if (i < 3) {
            track[loc][i] = b;
        }
        else return -1;
        return 0;
    }
    
    public Block next(Block b, int node) {
        Block nextBlock = b;
        int i = 0;
        boolean switchHere = (switches[b.other(node)][0] != 0);
        int switchDir = switches[b.other(node)][0];
        if (b.to != node) {
            //look at b.to options
            //System.out.println("looking where b.to is not lastNode " + b.to + switchHere + " : " + track[b.to][i].occupied);
            while (track[b.to][i] != null) {
                if (!track[b.to][i].occupied) {
                    if (switchHere) {
                        if (b.dir != 0 && (b.dir + track[b.to][i].dir) != 0) { //test right angle turns
                            System.out.print("right angle");
                            if (b.dir == switchDir) { //test merging into switch
                                nextBlock = track[b.to][i];
                            }
                        }                         
                        else if (b.dir == 0 && track[b.to][i].dir == switchDir) { //check to make sure it is correct direction for fork
                            nextBlock = track[b.to][i];
                        }

                    }
                    else {
                        nextBlock = track[b.to][i];
                        System.out.println("putting " + nextBlock);
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
                        if (b.dir != 0 && (b.dir + track[b.from][i].dir) != 0) { //test right angle turns
                            System.out.println("right angle");
                            if (b.dir == switchDir) { //test merging into switch
                                nextBlock = track[b.from][i];
                            }
                        }                         
                        else if (b.dir == 0 && track[b.from][i].dir == switchDir) { //check to make sure it is correct direction for fork
                            nextBlock = track[b.from][i];
                        }

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
            System.out.println(" node " + i);
            for (int j = 0; j < 3; j++) {
                if (track[i][j] != null) {
                    if (track[i][j].to != i) {
                    System.out.println(track[i][j].to + " ");
                    }
                    else {
                    System.out.println(track[i][j].from + " ");                        
                    }
                }
            }
        }
    }

}


