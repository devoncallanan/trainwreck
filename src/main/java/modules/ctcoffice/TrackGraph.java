package modules.ctcoffice;
import java.util.*;

public class TrackGraph {
	private final int V;
	private int E;
	private ArrayList<BlockTemp>[] adj;
	
	@SuppressWarnings("unchecked")
	public TrackGraph(int V) {
		this.V = V;
		this.E = 0;
		adj = (ArrayList<BlockTemp>[]) new ArrayList[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new ArrayList<BlockTemp>();
		}
	}
	
	public int V() {
		return V;
	}
	
	public int E() {
		return E;
	}
	
	private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("Vertex " + v + " is not between 0 and " + (V-1));
    }
	
	public void addBlockTemp(BlockTemp e) {
		int v = e.either();
		int w = e.other(v);
		validateVertex(v);
		validateVertex(w);
		adj[v].add(e);
		adj[w].add(e);
		E++;
	}
	
	public void removeBlockTemp(int v, int w) {
		for (int i = 0; i < adj[v].size(); i++) {
			if (adj[v].get(i).other(v) == w) {
				adj[v].remove(i);
				break;
			}
		}
		for (int j = 0; j < adj[w].size(); j++) {
			if (adj[w].get(j).other(w) == v) {
				adj[w].remove(j);
			}
		}
		
	}
	
	public ArrayList<BlockTemp> adj(int v) {
		validateVertex(v);
		return adj[v];
	}
	
	public int degree(int v) {
		validateVertex(v);
		return adj[v].size();
	}
	
	public ArrayList<BlockTemp> blocks() {
		ArrayList<BlockTemp> list = new ArrayList<BlockTemp>();
		for (int v = 0; v < V; v++) {
			int selfLoops = 0;
			for (BlockTemp e: adj(v)) {
				if (e.other(v) > v) {
					list.add(e);
				} else if (e.other(v) == v) {
					if (selfLoops % 2 == 0) list.add(e);
					selfLoops++;
				}
			}
		}
		return list;
	}
}
