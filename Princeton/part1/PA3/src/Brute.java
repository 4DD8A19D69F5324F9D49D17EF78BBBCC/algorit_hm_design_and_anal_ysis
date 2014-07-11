import java.util.Arrays;

public class Brute {
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            points[i].draw();
        }

        Arrays.sort(points);


        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                for (int k = j + 1; k < n; k++)
                    if (points[i].slopeTo(points[j]) == points[j].slopeTo(points[k]))
                        for (int l = k + 1; l < n; l++)
                            if (points[j].slopeTo(points[k]) == points[k].slopeTo(points[l])) {
                                System.out.println(points[i] + " -> " + points[j] + " -> "
                                        + points[k] + " -> " + points[l]);
                                points[i].drawTo(points[l]);
                            }

    }
}
