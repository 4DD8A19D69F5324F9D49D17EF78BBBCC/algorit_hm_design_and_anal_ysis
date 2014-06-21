
public class PercolationStats {


    private int[] result;
    private int N;
    private int T;

    public PercolationStats(int N, int T) {
        // perform T independent computational experiments on an N-by-N grid
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();

        this.N = N;
        this.T = T;
        result = new int[T];
        for (int i = 0; i < T; i++) {
            Percolation per = new Percolation(N);
            int tresult = 0;
            while (!per.percolates()) {
                int x = StdRandom.uniform(1, N + 1);
                int y = StdRandom.uniform(1, N + 1);
                if (!per.isOpen(x, y)) {
                    per.open(x, y);
                    tresult++;
                }

            }
            result[i] = tresult;
        }
    }

    public double mean() {
        // sample mean of percolation threshold
        double sum = 0;
        for (int i = 0; i < T; i++) {
            sum += result[i];
        }
        return sum / N / N / T;
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        double m = mean();
        double var = 0;
        for (int i = 0; i < T; i++) {
            double frac = 1.0 * result[i] / N / N;
            var += (frac - m) * (frac - m);
        }
        var /= T - 1;
        return Math.sqrt(var);
    }

    public double confidenceLo() {
        // returns lower bound of the 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    public double confidenceHi() {
        // returns upper bound of the 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    public static void main(String[] args) {
        // test client, described below
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        StdOut.println("N=" + N + " " + "T=" + T);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.println("mean = " + stats.mean());
        StdOut.println("stddev = " + stats.stddev());
        StdOut.println("95% CI = " + stats.confidenceLo()
                + " "
                + stats.confidenceHi());
    }
}
