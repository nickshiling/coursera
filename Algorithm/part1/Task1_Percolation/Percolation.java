import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] sites;
    private int openSites = 0;
    private final int bottomRootIndex;
    private final WeightedQuickUnionUF quickUnion;

    public Percolation(int n) {
        if (n <= 0) throw new java.lang.IllegalArgumentException("n should be > 0");
        sites = new boolean[n][n];

        bottomRootIndex = n * n + 1;
        quickUnion = new WeightedQuickUnionUF(n*n+2); // n^2 + 2 additional sites for roots
    }

    public void open(int row, int col) {
        validateGridRange(row, col);

        if (!isOpen(row, col)) {
            sites[row - 1][col - 1] = true;
            openSites++;

            int unionFindIndex = getUnionFindIndex(row, col);

            if (row == 1) { // first row - connect to root site
                quickUnion.union(0, unionFindIndex);
            } else if (isOpen(row - 1, col)) {
                quickUnion.union(unionFindIndex, getUnionFindIndex(row - 1, col));
            }

            if (row == sites.length) { // last row - connect to bottom root site
                quickUnion.union(unionFindIndex, bottomRootIndex);
            } else if (isOpen(row+1, col)) {
                quickUnion.union(unionFindIndex, getUnionFindIndex(row + 1, col));
            }

            if (col > 1 && isOpen(row, col - 1)) {
                quickUnion.union(unionFindIndex, getUnionFindIndex(row, col-1));
            }

            if (col < sites.length && isOpen(row, col+1)) {
                quickUnion.union(unionFindIndex, getUnionFindIndex(row, col + 1));
            }
        }
    }

    /**
     * Convert row and col index to flat union find cell index
     */
    private int getUnionFindIndex(int row, int col) {
        return (row - 1) * sites.length + col;
    }

    private void validateGridRange(int row, int col) {
        if (row < 1 || row > sites.length) throw new java.lang.IllegalArgumentException("row should be between 1 and n");
        if (col < 1 || col > sites.length) throw new java.lang.IllegalArgumentException("col should be between 1 and n");
    }

    public boolean isOpen(int row, int col) {
        validateGridRange(row, col);

        return sites[row-1][col-1];
    }

    public boolean isFull(int row, int col) {
        validateGridRange(row, col);

        int ufCellIndex = getUnionFindIndex(row, col);

        return quickUnion.connected(0, ufCellIndex);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return quickUnion.connected(0, bottomRootIndex);
    }
}
