package modules.ctcoffice;

public class BlockTemp {
    //blocks are edges in a directed graph
    private final int v;
    private final int w;
    private final int distance;
    private final String section;
    private final int number;
    private final int branch;
    
    /*public String section;
    public int number;
    public String station;
    public double grade;
    public int length;
    public int limit;
    public boolean occupied;
    public int branch;

    public BlockTemp(int to, int from, String section, int number, String station, double grade, int length, int limit, int branch) {
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
    }
    
    public BlockTemp(int to, int from, boolean branch) {
        this.to = to;
        this.from = from;
    }*/

    public BlockTemp(int v, int w, int distance, String section, int number, int branch) {
        this.v = v;
        this.w = w;
        this.distance = distance;
        this.section = section;
        this.number = number;
        this.branch = branch;
    }

    public int distance() {
        return distance;
    }

    public String section() {
        return section;
    }

    public int number() {
        return number;
    }

    public int branch() {
        return branch;
    }

    public int either() {
        return v;
    }

    public int w() {
        return w;
    }
    
    public int other(int vertex) {
        if (vertex == v) {
            return w;
        } else if (vertex == w) {
            return v;
        } else {
            throw new IllegalArgumentException("Illegal endpoint");
        }
    }

    public String toString() {
        return section + number + " : " + distance + " : " + branch;
    }
}
