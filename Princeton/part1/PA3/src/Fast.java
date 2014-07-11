import java.util.*;


public class Fast {

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


        Point[] p = Arrays.copyOf(points, n);
        Set<LinkedList<Point>> result = new HashSet<LinkedList<Point>>();

        for (int i = 0; i < n; i++) {
            Arrays.sort(p, points[i].SLOPE_ORDER);

            LinkedList<LinkedList<Point>> llist = new LinkedList<LinkedList<Point>>();
            LinkedList<Point> curlist = null;
            double curslope = -1;

            for (Point pt : p) {
                if (points[i].slopeTo(pt) == curslope) {
                    curlist.add(pt);
                } else {
                    curslope = points[i].slopeTo(pt);
                    curlist = new LinkedList<Point>();
                    llist.add(curlist);
                    curlist.add(pt);
                }
            }

            for (LinkedList<Point> lp : llist) {
                if (lp.size() >= 3) {
                    lp.add(points[i]);
                    Collections.sort(lp);
                    result.add(lp);
                }
            }
        }


        for (LinkedList<Point> lst : result) {

            boolean first = true;
            for (Point pt : lst) {
                if (first) {
                    first = false;
                } else {
                    StdOut.print(" -> ");
                }
                StdOut.print(pt);

            }
            StdOut.println();
            lst.getFirst().drawTo(lst.getLast());
        }

    }

}
