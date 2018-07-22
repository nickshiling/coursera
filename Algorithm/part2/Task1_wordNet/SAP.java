import edu.princeton.cs.algs4.Digraph;

import java.util.Collections;

public class SAP {
    private final Digraph graph;
    private final OptimizedBFS bfsV;
    private final OptimizedBFS bfsW;

    private class AncestralInfo {
        public final int length;
        public final int commonVertex;

        public AncestralInfo(int length, int commonVertex) {
            this.length = length;
            this.commonVertex = commonVertex;
        }
    }


    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.IllegalArgumentException();
        }

        graph = new Digraph(G);
        bfsV = new OptimizedBFS(graph);
        bfsW = new OptimizedBFS(graph);
    }

    private AncestralInfo findMinAncestralPath(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException();
        }

        bfsV.reinit(v);
        bfsW.reinit(w);

        int minPathLength = Integer.MAX_VALUE;
        int commonVertex = -1;

        Iterable<Integer> visitedV = bfsV.affectedVerts();

        for (int vertex : visitedV) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                int path = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (path < minPathLength) {
                    minPathLength = path;
                    commonVertex = vertex;
                }
            }
        }

        if (minPathLength == Integer.MAX_VALUE) { minPathLength = -1; }

        return new AncestralInfo(minPathLength, commonVertex);
    }

    private AncestralInfo findMinAncestralPath(int v, int w) {
        assertVertex(v);
        assertVertex(w);

       return findMinAncestralPath(Collections.singletonList(v), Collections.singletonList(w));
    }

    private void assertVertex(int vertex) {
        if (vertex < 0 || vertex > graph.V()) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return findMinAncestralPath(v, w).length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return findMinAncestralPath(v, w).commonVertex;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return findMinAncestralPath(v, w).length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return findMinAncestralPath(v, w).commonVertex;
    }
}
