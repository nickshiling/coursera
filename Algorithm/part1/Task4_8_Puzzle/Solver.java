import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final SearchNode rootNode;

    private final class SearchNode implements Comparable<SearchNode> {

        private final Board currentBoard;
        private final SearchNode previousNode;
        private final int numberOfSteps;
        private final int manhattan;

        public SearchNode(Board currentBoard, SearchNode previousNode, int numberOfSteps) {
            this.currentBoard = currentBoard;
            this.previousNode = previousNode;
            this.numberOfSteps = numberOfSteps;

            manhattan = currentBoard.manhattan() + numberOfSteps;
        }

        public int compareTo(SearchNode node) {
            if (node == null) { throw new java.lang.IllegalArgumentException(); }

            return  manhattan - node.manhattan;
        }
    }

    private final class BoardHammingComparator implements Comparator<Board> {

        public int compare(Board board1, Board board2) {
            if (board1 == null) { throw new java.lang.IllegalArgumentException(); }
            if (board2 == null) { throw new java.lang.IllegalArgumentException(); }

            return board1.manhattan() - board2.manhattan();
        }
    }

    public Solver(Board initial) {
        if (initial == null) { throw new java.lang.IllegalArgumentException(); }

        SearchNode currentNode = new SearchNode(initial, null, 0);
        SearchNode twinNode = new SearchNode(initial.twin(), null, 0);

        MinPQ<SearchNode> directQueue = new MinPQ<>();
        MinPQ<SearchNode> twinQueue = new MinPQ<>();

        directQueue.insert(currentNode);
        twinQueue.insert(twinNode);

        while (!currentNode.currentBoard.isGoal()
                && !twinNode.currentBoard.isGoal()) {

            currentNode = directQueue.delMin();

            for (Board neighbor: currentNode.currentBoard.neighbors()) {
                if (currentNode.previousNode == null ||
                        !neighbor.equals(currentNode.previousNode.currentBoard)) {
                    directQueue.insert(new SearchNode(neighbor,
                            currentNode,
                            currentNode.numberOfSteps + 1));
                }
            }

            twinNode = twinQueue.delMin();

            for (Board neighbor: twinNode.currentBoard.neighbors()) {
                if (twinNode.previousNode == null ||
                        !neighbor.equals(twinNode.previousNode.currentBoard)) {
                    twinQueue.insert(new SearchNode(neighbor,
                            twinNode,
                            currentNode.numberOfSteps + 1));
                }
            }
        }

        rootNode = currentNode;
    }

    public boolean isSolvable() {
        return rootNode != null && rootNode.currentBoard.isGoal();
    }

    public int moves() {
        if (isSolvable()) { return rootNode.numberOfSteps; }

        return -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) { return null; }

        Board[] solutionPath = new Board[rootNode.numberOfSteps + 1];

        SearchNode current = rootNode;

        int index = solutionPath.length;

        while (current != null) {
            solutionPath[--index] = current.currentBoard;
            current = current.previousNode;
        }

        return Arrays.asList(solutionPath);
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
