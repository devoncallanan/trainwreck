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

    public Block(int to, int from, String section, int number, String station, double grade, double length, double limit, int branch, String beacon) {
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
		this.beacon = beacon;
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
        String s = new String(this.section + " " + this.number + " " + this.branch + " ");
        if (occupied) {
            s = s+ "CHOO-CHOO";
        }
        return s;
    }
    
    public String extendedInfo() {
        String s = new String("Section: " + section + "\t\t\tBlock Number: " + number + "\nStation: " + station + "\t\t\tGrade: " + grade + "\nLength: " + length + "\t\t\t Speed Limit: " + limit + "\nOccupied: " + occupied);
        return s;
    }
}
