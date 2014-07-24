import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> set = new TreeSet<>();

    public PointSET() {
    }

    public boolean isEmpty() { // is the set empty?
        return set.isEmpty();
    }

    public int size() { // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) {
        set.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain the point p?
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> ret = new ArrayList<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                ret.add(p);
            }
        }
        return ret;
    }

    public Point2D nearest(Point2D p) {
        double tdist = Double.POSITIVE_INFINITY;
        Point2D tpoint = null;
        for (Point2D tp : set) {
            if (p.distanceTo(tp) < tdist) {
                tdist = p.distanceTo(tp);
                tpoint = tp;
            }
        }
        return tpoint;
    }
}
