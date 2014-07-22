import java.util.Arrays;

public class Board {

    private static final int[] DX = {1, -1, 0, 0};
    private static final int[] DY = {0, 0, 1, -1};

    private final int[][] array;
    private final int N;

    private final int hdist;
    private final int mdist;

    public Board(int[][] blocks) {
        array = copy2d(blocks);
        N = blocks.length;
        mdist = manhattanP();
        hdist = hammingP();
    }

    public int dimension() {
        return N;
    }

    private int hammingP() {
        int ret = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (array[i][j] != i * N + j + 1 && array[i][j] != 0) {
                    ret++;
                }
            }
        }
        return ret;
    }

    private int manhattanP() {
        int ret = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (array[i][j] != 0) {
                    int x = array[i][j] - 1;
                    int oi = x / N;
                    int oj = x % N;
                    ret += Math.abs(i - oi) + Math.abs(j - oj);
                }
            }
        }
        return ret;
    }


    public int hamming() {
        return hdist;
    }

    public int manhattan() {
        return mdist;
    }


    private static int[][] copy2d(int[][] orig) {
        int[][] ret = new int[orig.length][orig[0].length];
        for (int i = 0; i < orig.length; i++) {
            for (int j = 0; j < orig[0].length; j++) {
                ret[i][j] = orig[i][j];
            }
        }
        return ret;
    }

    private int zeroPos() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (array[i][j] == 0) {
                    return i * dimension() + j;
                }
            }
        }
        return -1;
    }

    public boolean isGoal() {
        return manhattan() == 0;
    }

    public Board twin() {
        int[][] retArray = copy2d(array);

        int x = 0;
        for (int i = 0; i < N; i++) {
            if (array[i][0] != 0 && array[i][1] != 0) {
                x = i;
                break;
            }
        }

        int t = retArray[x][0];
        retArray[x][0] = retArray[x][1];
        retArray[x][1] = t;
        return new Board(retArray);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;

        if (hamming() != that.hamming()) return false;
        return Arrays.deepEquals(this.array, that.array);
    }


    public Iterable<Board> neighbors() {
        Stack<Board> list = new Stack<Board>();
        int zpos = zeroPos();
        int zx = zpos / N;
        int zy = zpos % N;
        int[][] retArray = copy2d(array);

        for (int i = 0; i < 4; i++) {
            int nx = zx + DX[i];
            int ny = zy + DY[i];
            if (nx >= 0 && nx < N && ny >= 0 && ny < N) {
                retArray[zx][zy] = retArray[nx][ny];
                retArray[nx][ny] = 0;
                list.push(new Board(retArray));
                retArray[nx][ny] = retArray[zx][zy];
                retArray[zx][zy] = 0;
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", array[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
