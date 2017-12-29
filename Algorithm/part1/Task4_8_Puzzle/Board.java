import java.util.ArrayList;
import edu.princeton.cs.algs4.Queue;

public final class Board {
    private final int[][] board;
    private int hammingDistance = -1;
    private int manhattanDistance = -1;

    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new java.lang.IllegalArgumentException();
        }

        board = new int[blocks.length][blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                board[i][j] = blocks[i][j];
            }
        }
    }

    private int getExpectedValue(int i, int j) {
        int result = board.length * i + j + 1;

        if (result == board.length * board.length) { result = 0; }

        return result;
    }

    public int dimension() {
        return board.length;
    }

    public int hamming() {
        if (hammingDistance == -1) {
            hammingDistance = 0;

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    int currentBlock = board[i][j];
                    int expectedBlock = getExpectedValue(i, j);

                    if (currentBlock != 0 && currentBlock != expectedBlock) {
                        hammingDistance++;
                    }
                }
            }
        }
        return hammingDistance;
    }

    public int manhattan() {
        if (manhattanDistance == -1) {
            manhattanDistance = 0;

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    int currentBlock = board[i][j];

                    if (currentBlock != 0) {
                        int[] expectedPos = getExpectedPosition(currentBlock);
                        manhattanDistance += Math.abs(i - expectedPos[0]) + Math.abs(j - expectedPos[1]);
                    }
                }
            }
        }
        return manhattanDistance;
    }

    private int[] getExpectedPosition(int value) {
        int[] result = new int [2];

        result[0] = (value - 1)  / board.length;
        result[1] = value - result[0] * board.length - 1;

        return result;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        Board twinBoard = new Board(board);

        int start = 0, end = 0;

        while (twinBoard.board[0][start] == 0) { start++; }
        while (twinBoard.board[1][end] == 0) { end++; }

        int exchange = twinBoard.board[0][start];
        twinBoard.board[0][start] = twinBoard.board[1][end];
        twinBoard.board[1][end] = exchange;

        return twinBoard;
    }

    public boolean equals(Object y) {
        if (y == null) { return false; }
        if (this.getClass() != y.getClass()) { return false; }

        Board yBoard = (Board) y;

        if (this.board.length != yBoard.board.length) { return false; }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != yBoard.board[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(board.length);

        for (int i = 0; i < board.length; i++) {
            builder.append('\n');
            for (int j = 0; j < board.length; j++) {
                builder.append(board[i][j]);

                if (j != (board.length - 1)) {
                    builder.append(' ');
                }
            }
        }

        return builder.toString();
    }

    public Iterable<Board> neighbors() {
        int[] emptyBlock = getEmptyBlockPosition();
        ArrayList<int[]> moveIndexes = getBoardNeighboursRules(emptyBlock);


        Queue<Board> boards = new Queue<>();

        for (int i = 0; i < moveIndexes.size(); i++) {
            Board newBoard = new Board(board);

            int exchangeValue = newBoard.board[emptyBlock[0]][emptyBlock[1]];
            int[] neighbourIndex = moveIndexes.get(i);
            newBoard.board[emptyBlock[0]][emptyBlock[1]] = newBoard.board[neighbourIndex[0]][neighbourIndex[1]];
            newBoard.board[neighbourIndex[0]][neighbourIndex[1]] = exchangeValue;

            boards.enqueue(newBoard);
        }

        return boards;
    }

    private int[] getEmptyBlockPosition() {
        int[] result = null;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }

        return  result;
    }

    private ArrayList<int[]> getBoardNeighboursRules(int[] emptyBlock) {
        ArrayList<int[]> rules = new ArrayList<>();

        // empty block not on the first row
        if (emptyBlock[0] != 0) { rules.add(new int[]{emptyBlock[0] - 1, emptyBlock[1]}); }
        // empty block not on the last row
        if (emptyBlock[0] != (board.length-1)) { rules.add(new int[]{emptyBlock[0] + 1, emptyBlock[1]}); }
        // empty block not on the first column
        if (emptyBlock[1] != 0) { rules.add(new int[]{emptyBlock[0], emptyBlock[1] - 1}); }
        // empty block not on the last column
        if (emptyBlock[1] != (board.length-1)) { rules.add(new int[]{emptyBlock[0], emptyBlock[1] + 1}); }

        return rules;
    }
}
