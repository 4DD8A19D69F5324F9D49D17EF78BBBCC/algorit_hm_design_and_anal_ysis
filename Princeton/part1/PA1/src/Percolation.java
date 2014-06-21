public class Percolation {

    private WeightedQuickUnionUF uf;
    private boolean[][] opened;
    private boolean[] isBottom;
    private int N;

    private final int[] dx = {0, 0, 1, -1};
    private final int[] dy = {1, -1, 0, 0};
    private int TOP;


    public Percolation(int N) {
        // create N-by-N grid, with all sites blocked
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        TOP = N * N;
        uf = new WeightedQuickUnionUF(N * N + 1);
        opened = new boolean[N][N];
        isBottom = new boolean[N * N + 1];

        for (int i = N * N - N; i < N * N; i++) {
            isBottom[i] = true;
        }
    }

    private int toindex(int i, int j) {
        return i * N + j;
    }

    public void open(int i, int j) {
        // open site (row i, column j) if it is not already
        int ri = i - 1;
        int rj = j - 1;
        opened[ri][rj] = true;

        if (ri == 0) {
            uf.union(rj, TOP);
        }

        for (int p = 0; p < 4; p++) {
            int ni = ri + dx[p];
            int nj = rj + dy[p];
            if (ni >= 0 && ni < N && nj >= 0 && nj < N && opened[ni][nj]) {
                int p1 = uf.find(toindex(ri, rj));
                int p2 = uf.find(toindex(ni, nj));
                uf.union(p1, p2);
                isBottom[p1] |= isBottom[p2];
                isBottom[p2] |= isBottom[p1];
            }
        }
    }

    public boolean isOpen(int i, int j) { // is site (row i, column j) open?
        return opened[i - 1][j - 1];
    }

    public boolean isFull(int i, int j) { // is site (row i, column j) full?

        if (!(i >= 1 && i <= N && j >= 1 && j <= N)) {
            throw new IndexOutOfBoundsException();
        }
        return uf.connected(toindex(i - 1, j - 1), TOP);
    }

    public boolean percolates() { // does the system percolate?
        return isBottom[uf.find(TOP)];
    }
}
