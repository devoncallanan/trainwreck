/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules.trackmodel;

import shared.*;
/**
 *
 * @author Devon
 */



public class Block {
	
public static double KMH_TO_MS = 1000.0/3600.0; 
public static double MS_TO_KMH = 3600.0/10000.0; 
    //blocks are edges in a directed graph
    public int from;
    public int to;
    public int dir;
    
    public String section;
    public int number;
    public String station;
    public double grade;
    public double length;
    public double limit;
    public boolean occupied;
    public int branch;
	public String beacon;
	public int stationSide;
	public String extra;
	private int ways;

    public Block(int to, int from, String section, int number, double grade, double length, double limit, int branch, int switchloc,String station, String beacon, int stationSide, String extra, int ways) {
        this.to = to;
        this.from = from;
        this.section = section;
        this.number = number;
        this.station = station;
        this.grade = grade;
        this.length = length;
        this.limit = limit;
        occupied = false;
        this.branch = branch;
		this.station = station;
		this.beacon = beacon;
		this.stationSide = stationSide;
		this.extra = extra;
		this.ways = ways;
    }
    
    public Block(int to, int from, boolean branch) {
        this.to = to;
        this.from = from;
    }
    
    public int other(int end) {
        if (to == end) {
            return from;
        }
        else return to;
    }
    
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( new String(this.section + " " + this.number + " "));
		if (this.station != null) {
			sb.append(this.station + " ");
		}
		if (this.extra != null) {
			sb.append(this.extra + " ");
		}
        
        if (occupied) {
            sb.append("CHOO-CHOO");
        }
        return sb.toString();
    }
    
    public String extendedInfo() {
        String s = new String("Section: " + section + "\t\tBlock Number: " + number + "\nStation: " + station + "\t\tGrade: " + grade + "\nLength: " + length*3.28084 + "\t\t Speed Limit: " + limit*0.621371 + "\nOccupied: " + occupied + "\t\tWays of travel: " + ways);
        return s;
    }
}
