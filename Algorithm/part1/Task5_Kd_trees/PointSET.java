import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;

public class PointSET {

    private final SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }

        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }

        return points.contains(p);
    }

    public void draw() {
        for (Point2D point: points) {
            StdDraw.point(point.x(), point.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) { throw new java.lang.IllegalArgumentException(); }

        SET<Point2D> pps = new SET<>();

        for (Point2D point: points) {
            if (rect.contains(point)) {
                pps.add(point);
            }
        }

        return pps;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }

        if (isEmpty()) { return null; }

        Iterator<Point2D> pointIterator = points.iterator();

        Point2D minPoint = pointIterator.next();
        double minDistance = minPoint.distanceSquaredTo(p);

        while (pointIterator.hasNext()) {
            Point2D currentPoint = pointIterator.next();
            double currentDistance = currentPoint.distanceSquaredTo(p);

            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                minPoint = currentPoint;
            }
        }

        return minPoint;
    }
}
