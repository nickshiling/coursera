import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_96 = 1.96;

    private final int n;
    private final double[] percolationThresholds;

    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new java.lang.IllegalArgumentException("n should be >0");
        if (trials <= 0) throw new java.lang.IllegalArgumentException("trials should be >0");

        this.n = n;

        int[] cells = new int[n*n];

        for (int i = 0; i < cells.length; i++) {
            cells[i] = i;
        }

        percolationThresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            StdRandom.shuffle(cells); // create random list of cells
            Percolation percolation = new Percolation(n);

            int iteration = 0;
            while (!percolation.percolates()) {
                int[] siteToOpen = getRowAndColByIndex(cells[iteration]);

                percolation.open(siteToOpen[0], siteToOpen[1]);
                iteration++;
            }

            percolationThresholds[i] = percolation.numberOfOpenSites() / (double) (n * n);
        }
    }

    private int[] getRowAndColByIndex(int index) {
        int[] result = new int[2];
        result[0] = (int) Math.floor(index / n) + 1;
        result[1] = index % n + 1;

        return result;
    }

    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }
    public double confidenceLo() {
        return mean() - (CONFIDENCE_96 * stddev() / Math.sqrt(n));
    }
    public double confidenceHi() {
        return mean() + (CONFIDENCE_96 * stddev() / Math.sqrt(n));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int iteration = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, iteration);
        System.out.printf("mean=%f%n", stats.mean());
        System.out.printf("stddev=%f%n", stats.stddev());
        System.out.printf("95%% confidence interval=[%f, %f]%n", stats.confidenceLo(), stats.confidenceHi());
    }
}
