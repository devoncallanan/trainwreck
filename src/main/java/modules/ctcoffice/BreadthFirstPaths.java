//Ben Posey
// Modified version of BreadthFirstPaths to use BlockTemps instead of ints
package modules.ctcoffice;

public class BreadthFirstPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private BlockTemp[] BlockTempTo;      // BlockTempTo[v] = previous BlockTemp on shortest s-v path
    private int[] distTo;      // distTo[v] = number of BlockTemps shortest s-v path
    private double[] distanceTo;

    /**
     * Computes the shortest path between the source vertex {@code s}
     * and every other vertex in the TrackGraph {@code G}.
     * @param G the TrackGraph
     * @param s the source vertex
     */
    public BreadthFirstPaths(TrackGraph G, int s) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        BlockTempTo = new BlockTemp[G.V()];
        bfs(G, s);
    }

    // breadth-first search from a single source
    private void bfs(TrackGraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (BlockTemp e : G.adj(v)) {
            	int w = e.other(v);
                if (!marked[w]) {
                    BlockTempTo[w] = e;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    /**
     * Is there a path between the source vertex {@code s} (or sources) and vertex {@code v}?
     * @param v the vertex
     * @return {@code true} if there is a path, and {@code false} otherwise
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * Returns the number of BlockTemps in a shortest path between the source vertex {@code s}
     * (or sources) and vertex {@code v}?
     * @param v the vertex
     * @return the number of BlockTemps in a shortest path
     */
    public int distTo(int v) {
        return distTo[v];
    }

    /**
    * Prints a shortest path between the source vertex {@code s} (or sources)
    * and {@code v}, or {@code null} if no such path.
    * @param v the vertex
    */
    public void pathTo(int v) {
    	if (!hasPathTo(v)) System.out.println("No path detected");
    	for (BlockTemp e = BlockTempTo[v]; e != null; e = BlockTempTo[v = e.other(v)]) {
            System.out.println(e.toString());
        }
    }
    
}