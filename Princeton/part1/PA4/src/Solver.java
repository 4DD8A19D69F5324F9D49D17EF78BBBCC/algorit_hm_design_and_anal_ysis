import java.util.*;

public class Solver {
    private class FastBoard implements Comparable<FastBoard> {
        public byte[] arr;
        private final int N;
        private FastBoard prev;
        private int steps;
        private int key;
        private final int[] DX = {1, -1, 0, 0};
        private final int[] DY = {0, 0, 1, -1};

        FastBoard(int[] data, int n, int step, FastBoard prev) {
            N = n;
            arr = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                arr[i] = (byte) data[i];
            }
            this.steps = step;
            this.prev = prev;
            this.key = hscore() + steps;
        }

        FastBoard(byte[] data, int n, int step, FastBoard prev) {
            N = n;
            arr = data;
            this.steps = step;
            this.prev = prev;
            this.key = hscore() + steps;
        }


        public int zeropos() {
            for (int i = 0; i < arr.length; i++) if (arr[i] == 0) return i;
            return -1;
        }

        public int hscore() {
            int ret = 0;
            for (int i = 0; i < arr.length; i++)
                if (arr[i] != 0) {
                    int x = arr[i] - 1;
                    int ox = x / N;
                    int oy = x % N;
                    ret += Math.abs(i / N - ox) + Math.abs(i % N - oy);
                }

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    int x = arr[i * N + j];
                    if (x != 0) {
                        x--;
                        if (x / N == i && x % N > j) {
                            if (arr[x] == i*N+j+1) ret+=2;
                        }
                    }
                }
            }

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    int x = arr[j * N + i];
                    if (x != 0) {
                        x--;
                        if (x % N == i && x / N > j) {
                            if (arr[x] == j*N+i+1) ret+=2;
                        }
                    }
                }
            }


            return ret;
        }

        public boolean isGoal() {
            return hscore() == 0;
        }

        public boolean equals(Object y) {
            if (y == this) return true;
            if (y == null) return false;
            if (y.getClass() != this.getClass()) return false;
            FastBoard that = (FastBoard) y;
            if (this.key - this.steps != that.key - that.steps) return false;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != that.arr[i]) return false;
            }
            return true;
        }

        public int hashCode() {
            int ret = 131;
            for (int i = 0; i < arr.length; i++) {
                ret = ret * 31 + arr[i];
            }
            return ret;
        }

        public FastBoard swapped(int p1, int p2) {
            byte[] tmp = Arrays.copyOf(arr, arr.length);
            byte t = tmp[p1];
            tmp[p1] = tmp[p2];
            tmp[p2] = t;
            return new FastBoard(tmp, N, steps + 1, this);
        }

        public List<FastBoard> neighbors() {
            List<FastBoard> list = new ArrayList<FastBoard>();

            int z = zeropos();
            for (int i = 0; i < 4; i++) {
                int x = z / N;
                int y = z % N;
                int nx = x + DX[i];
                int ny = y + DY[i];
                if (nx >= 0 && nx < N && ny >= 0 && ny < N) {
                    FastBoard temp = swapped(z, nx * N + ny);
                    if (prev == null || !temp.equals(prev)) {
                        list.add(temp);
                    }
                }
            }
            return list;
        }


        public Board toBoard() {
            int[][] arr2d = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++) arr2d[i][j] = arr[i * N + j];
            return new Board(arr2d);
        }

        @Override
        public int compareTo(FastBoard o) {
            return Integer.compare(key, o.key);
        }
    }


    private MinPQ<FastBoard> pq = new MinPQ<FastBoard>();
    private boolean solvable;
    private FastBoard tail;

    public Solver(Board initial) {
        int[] arr = extract(initial);
        int N = initial.dimension();

        if (initial.isGoal()) {
            tail = new FastBoard(arr, N, 0, null);
            solvable = true;
        } else {
            boolean invIsEven = inversions(arr) % 2 == 0;
            boolean gridEven = initial.dimension() % 2 == 0;
            int rowloc = blankpos(arr) / initial.dimension();
            boolean rowIsEven = rowloc % 2 == 0;
            if ((!gridEven && invIsEven) || (gridEven) && !rowIsEven == invIsEven) {
                solvable = true;
                pq.insert(new FastBoard(arr, N, 0, null));
                while (true) {
                    if (oneStep(pq)) {
                        break;
                    }
                }
            } else {
                solvable = false;
            }
        }
    }


    private static int[] extract(Board b) {
        String s = b.toString();

        Scanner in = new Scanner(s);
        in.nextInt();
        int[] arr = new int[b.dimension() * b.dimension()];
        for (int i = 0; i < b.dimension(); i++)
            for (int j = 0; j < b.dimension(); j++) {
                arr[i * b.dimension() + j] = in.nextInt();
            }
        return arr;
    }

    private static int inversions(int[] arr) {
        int ret = 0;
        for (int i = 0; i < arr.length; i++)
            if (arr[i] != 0) {
                for (int j = i + 1; j < arr.length; j++)
                    if (arr[j] != 0 && arr[i] > arr[j]) {
                        ret++;
                    }
            }
        return ret;
    }

    private static int blankpos(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) return i;
        }
        return -1;
    }


    private boolean oneStep(MinPQ<FastBoard> minpq) {
        FastBoard top = minpq.delMin();
        for (FastBoard next : top.neighbors()) {
            if (next.isGoal()) {
                tail = next;
                return true;
            }
            minpq.insert(next);
        }
        return false;

    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (isSolvable()) {
            return tail.steps;
        } else {
            return -1;
        }
    }

    public Iterable<Board> solution() {
        if (isSolvable()) {
            List<FastBoard> ret = new ArrayList<FastBoard>();
            FastBoard tmp = tail;
            while (tmp != null) {
                ret.add(tmp);
                tmp = tmp.prev;
            }
            Collections.reverse(ret);


            List<Board> fuckapi = new ArrayList<Board>();
            for (FastBoard f : ret) {
                fuckapi.add(f.toBoard());
            }
            return fuckapi;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
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
