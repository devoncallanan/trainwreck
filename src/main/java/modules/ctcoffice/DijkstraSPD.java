// Ben Posey
// Shortest path for distance

package modules.ctcoffice;
import java.util.*;

public class DijkstraSPD {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private BlockTemp[] blockTempTo;    // BlockTempTo[v] = last BlockTemp on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private int prevBranch = 1;

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the BlockTemp-weighted digraph {@code G}.
     *
     * @param  G the graph
     * @param  s the source vertex
     * @throws IllegalArgumentException if an BlockTemp weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DijkstraSPD(TrackGraph G, int s) {
        for (BlockTemp e : G.blocks()) {
            if (e.distance() < 0)
                throw new IllegalArgumentException("BlockTemp " + e + " has negative distance");
        }

        distTo = new double[G.V()];
        blockTempTo = new BlockTemp[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        //pq.insert(prevBlock, Double.POSITIVE_INFINITY);
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            //System.out.println("- - - - - -");
            int del = pq.delMin();
            //System.out.println("DELETE: "+(del+1));
        	relax(G, del);
        }
    }

    private void relax(TrackGraph G, int v)
    {
        //System.out.println("prevBranch: "+prevBranch);
        int tempBranch = 0;
        if (G.adj(v).size() > 2) {
            //System.out.println("BRANCH DETECTED: "+(v+1));
            for(BlockTemp e : G.adj(v))
            {
                //System.out.println(e.toString());
                //System.out.println(G.adj(v).size());
                // if (e.branch() == prevBranch)

                int w = e.other(v);
                if (distTo[w] > distTo[v] + e.distance()) {
                    distTo[w] = distTo[v] + e.distance();
                    blockTempTo[w] = e;
                    if (pq.contains(w))  {
                        //System.out.println("DECREASE:"+ w);
                        pq.decreaseKey(w, distTo[w]);
                    } else {
                        if (prevBranch != e.branch()) {
                            tempBranch = e.branch();
                            //System.out.println("INSERT:"+ (w+1)+" b:"+tempBranch);
                            pq.insert(w, distTo[w]);
                        }
                    }
                }
            }
        } else {
            for(BlockTemp e : G.adj(v))
            {
                //System.out.println(e.toString());
                //System.out.println(G.adj(v).size());

                int w = e.other(v);
                if (distTo[w] > distTo[v] + e.distance()) {
                    distTo[w] = distTo[v] + e.distance();
                    blockTempTo[w] = e;
                    if (pq.contains(w))  {
                        //System.out.println("DECREASE:"+ w);
                        pq.decreaseKey(w, distTo[w]);
                    } else {
                        tempBranch = e.branch();
                        //System.out.println("INSERT:"+ (w+1)+" b:"+tempBranch);
                        pq.insert(w, distTo[w]);

                    }
                }
            }
        }
    	prevBranch = tempBranch;
    }
    
    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     */
    public double distTo(int v) {
        return distTo[v];
    }

    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     */
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of BlockTemps, and {@code null} if no such path
     */
    public void pathTo(int v) {
        if (!hasPathTo(v)) System.out.println("No path detected");
        for (BlockTemp e = blockTempTo[v]; e != null; e = blockTempTo[v = e.other(v)]) {
            System.out.println(e.toString());
        }
    }

    public ArrayList<Integer> getPath(int v) {
        ArrayList<Integer> tempPath = new ArrayList<Integer>();
        if (!hasPathTo(v)) System.out.println("No path detected");
        for (BlockTemp e = blockTempTo[v]; e != null; e = blockTempTo[v = e.other(v)]) {
            tempPath.add(e.number());
        }
        return tempPath;
    }
}