import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static final int LEFT_OVERLAP = 1;
    private static final int RIGHT_OVERLAP = 2;
    private static final int BOTH_OVERLAP = 3;

    private static final RectHV FULLRECT = new RectHV(0, 0, 1, 1);


    private class KdNode {
        private Point2D p;
        private KdNode l, r;

        KdNode(Point2D p) {
            this.p = p;
        }
    }

    private int size = 0;
    private KdNode root = null;

    public KdTree() { // construct an empty set of points

    }

    public boolean isEmpty() { // is the set empty?
        return size == 0;
    }

    public int size() { // number of points in the set
        return size;
    }

    private static boolean findNode(KdNode tree, Point2D p, boolean vertical) {
        if (p.equals(tree.p)) return true;
        double keyintree = vertical ? tree.p.y() : tree.p.x();
        double keytocmp = vertical ? p.y() : p.x();
        KdNode next = keytocmp < keyintree ? tree.l : tree.r;
        if (next == null) return false;
        else return findNode(next, p, !vertical);
    }

    private void insertNoEmpty(KdNode tree, Point2D p, boolean vertical) {
        if (p.equals(tree.p)) return;
        double keyintree = vertical ? tree.p.y() : tree.p.x();
        double keytocmp = vertical ? p.y() : p.x();
        if (keytocmp < keyintree) {
            if (tree.l == null) {
                size++;
                tree.l = new KdNode(p);
            } else insertNoEmpty(tree.l, p, !vertical);
        } else {
            if (tree.r == null) {
                size++;
                tree.r = new KdNode(p);
            } else insertNoEmpty(tree.r, p, !vertical);
        }
    }

    public void insert(Point2D p) { // add the point p to the set (if it is not already in the set)

        if (root == null) {
            root = new KdNode(p);
            size++;
        } else {
            insertNoEmpty(root, p, true);
        }
    }

    public boolean contains(Point2D p) { // does the set contain the point p?
        if (root == null) return false;
        return findNode(root, p, true);
    }

    private static void draw(KdNode tree, boolean vertical) {
        if (tree == null) return;
        tree.p.draw();

        if (vertical) {
            StdDraw.setPenColor(255, 0, 0);
            StdDraw.line(0, tree.p.y(), 1, tree.p.y());
        } else {
            StdDraw.setPenColor(0, 0, 255);
            StdDraw.line(tree.p.x(), 0, tree.p.x(), 1);
        }
        draw(tree.l, !vertical);
        draw(tree.r, !vertical);
    }

    public void draw() { // draw all of the points to standard draw
        draw(root, true);
    }


    private static int overlap(KdNode tree, RectHV r, boolean vertical) {
        Point2D o = tree.p;
        double keyintree, keylo, keyhi;
        if (vertical) {
            keyintree = o.y();
            keylo = r.ymin();
            keyhi = r.ymax();
        } else {
            keyintree = o.x();
            keylo = r.xmin();
            keyhi = r.xmax();
        }
        if (keyintree <= keylo) return RIGHT_OVERLAP;
        else if (keyintree <= keyhi) return BOTH_OVERLAP;
        else return LEFT_OVERLAP;
    }


    private static void range(KdNode tree, RectHV rect, boolean vertical, List<Point2D> list) {
        if (tree == null) return;
        int overlapcode = overlap(tree, rect, vertical);
        if (rect.contains(tree.p)) list.add(tree.p);
        if ((overlapcode & LEFT_OVERLAP) != 0) range(tree.l, rect, !vertical, list);
        if ((overlapcode & RIGHT_OVERLAP) != 0) range(tree.r, rect, !vertical, list);
    }

    public Iterable<Point2D> range(RectHV rect) { // all points in the set that are inside the rectangle
        List<Point2D> list = new ArrayList<Point2D>();
        range(root, rect, true, list);
        return list;
    }

    private class PointDist {
        private Point2D p;
        private double d;

        PointDist(Point2D pp, double dd) {
            p = pp;
            d = dd;
        }

        void update(Point2D o, Point2D newp) {
            double td = o.distanceSquaredTo(newp);
            if (td < d) {
                d = td;
                p = newp;
            }
        }
    }


    private void nearest(KdNode tree, Point2D p, boolean vertical, RectHV rect, PointDist pd) {
        if (tree == null) return;
        pd.update(p, tree.p);

        RectHV rl, rr;
        if (vertical) {
            rl = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), tree.p.y());
            rr = new RectHV(rect.xmin(), tree.p.y(), rect.xmax(), rect.ymax());
        } else {
            rl = new RectHV(rect.xmin(), rect.ymin(), tree.p.x(), rect.ymax());
            rr = new RectHV(tree.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
        }
        double dl = rl.distanceSquaredTo(p);
        double dr = rr.distanceSquaredTo(p);


        if (dl < dr) {
            if (dl < pd.d) nearest(tree.l, p, !vertical, rl, pd);
            if (dr < pd.d) nearest(tree.r, p, !vertical, rr, pd);
        } else {
            if (dr < pd.d) nearest(tree.r, p, !vertical, rr, pd);
            if (dl < pd.d) nearest(tree.l, p, !vertical, rl, pd);
        }

    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to p; null if set is empty
        PointDist pd = new PointDist(null, Double.POSITIVE_INFINITY);
        nearest(root, p, true, FULLRECT, pd);
        return pd.p;
    }
}
