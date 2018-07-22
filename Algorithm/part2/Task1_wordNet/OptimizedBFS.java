import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Digraph;

import java.util.LinkedList;

public class OptimizedBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;
    private final LinkedList<Integer> changedVerts;
    private final Digraph graph;

    public OptimizedBFS(Digraph G) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        changedVerts = new LinkedList<Integer>();

        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;

        graph = G;
    }

    private void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            validateVertex(s);
            marked[s] = true;
            distTo[s] = 0;
            changedVerts.add(s);
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    changedVerts.add(w);
                    q.enqueue(w);
                }
            }
        }
    }

    public void reinit(Iterable<Integer> v) {
        for (int w : changedVerts) {
            edgeTo[w] = 0;
            marked[w] = false;
            distTo[w] = INFINITY;
        }

        changedVerts.clear();

        bfs(graph, v);
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public int distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);

        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }

    public Iterable<Integer> affectedVerts() {
        return changedVerts;
    }

    private void validateVertex(int v) {
        int length = marked.length;
        if (v < 0 || v >= length)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (length-1));
    }
}