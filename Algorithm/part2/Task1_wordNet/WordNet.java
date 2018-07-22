
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.HashMap;

public class WordNet {

    private HashMap<String, Bag<Integer>> wordVertexMapping;
    private HashMap<Integer, String> synsetVertexMapping;
    private Digraph wordGraph;
    private int vertexCount;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.IllegalArgumentException();
        }

        fillWordVertexMapping(synsets);
        fillwordGraph(hypernyms);

        DirectedCycle directedCicle = new DirectedCycle(wordGraph);
        if (directedCicle.hasCycle() || !isRooted(wordGraph)) {
            throw new java.lang.IllegalArgumentException();
        }

        sap = new SAP(wordGraph);
    }

    private void fillWordVertexMapping(String synsetsFileName) {
        wordVertexMapping = new HashMap<>();
        synsetVertexMapping = new HashMap<>();
        vertexCount = 0;

        In synsetsFile = new In(synsetsFileName);
        while (synsetsFile.hasNextLine()) {
            String fileLine  = synsetsFile.readLine();
            String[] splitedLine = fileLine.split(",");
            int synsetId = Integer.parseInt(splitedLine[0].trim());

            for (String word : splitedLine[1].split(" ")) {
                if (wordVertexMapping.containsKey(word)) {
                    wordVertexMapping.get(word).add(synsetId);
                } else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(synsetId);
                    wordVertexMapping.put(word, bag);
                }
            }

            synsetVertexMapping.put(synsetId, splitedLine[1]);
            vertexCount++;
        }

        synsetsFile.close();
    }

    private void fillwordGraph(String hypernymsPath) {
        wordGraph = new Digraph(vertexCount);

        In hypernymsFile = new In(hypernymsPath);

        while (hypernymsFile.hasNextLine()) {
            String hypernymLine = hypernymsFile.readLine();
            String[] splited = hypernymLine.split(",");

            int fromVertex = Integer.parseInt(splited[0].trim());

            for (int i = 1; i < splited.length; i++) {
                int toVertex = Integer.parseInt(splited[i].trim());
                wordGraph.addEdge(fromVertex, toVertex);
            }
        }

        hypernymsFile.close();
    }

    private boolean isRooted(Digraph graph) {
        int numberOfRoots = 0;
        for (int vertexInd = 0; vertexInd < graph.V(); vertexInd++) {
            if (graph.outdegree(vertexInd) == 0) {
                numberOfRoots++;
                if (numberOfRoots > 1) { return false; }
            }
        }

        return numberOfRoots == 1;
    }
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordVertexMapping.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return wordVertexMapping.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return sap.length(wordVertexMapping.get(nounA), wordVertexMapping.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int commonVertex = sap.ancestor(wordVertexMapping.get(nounA), wordVertexMapping.get(nounB));
        return synsetVertexMapping.get(commonVertex);
    }
}
