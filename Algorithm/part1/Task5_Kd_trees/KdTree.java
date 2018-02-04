import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node rootNode = null;
    private int size = 0;

    private static class Node {
        private final Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private final boolean isVertical;

        private Node(Point2D point, boolean isVertical) {
            p = point;
            rect = new RectHV(0, 0, 1, 1);
            this.isVertical = isVertical;
        }
    }

    public KdTree() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }

        rootNode = put(rootNode, p, true);
    }

    private Node put(Node current, Point2D point, boolean isVertical) {
        if (current == null) {
            size++;
            return new Node(point, isVertical);
        }

        double pointVert = point.y();
        double nodeVert = current.p.y();

        if (isVertical) {
            pointVert = point.x();
            nodeVert = current.p.x();
        }

        if (pointVert == nodeVert
                && current.p.equals(point)) { return current; }

        Node newNode;
        if (pointVert < nodeVert) {
            newNode = put(current.lb, point, !isVertical);
            if (current.lb == null) {
                current.lb = newNode;
                if (isVertical) {
                    current.lb.rect = new RectHV(current.rect.xmin(), current.rect.ymin(), current.p.x(), current.rect.ymax());
                } else {
                    current.lb.rect = new RectHV(current.rect.xmin(), current.rect.ymin(), current.rect.xmax(), current.p.y());
                }
            }
        } else {
            newNode = put(current.rt, point, !isVertical);
            if (current.rt == null) {
                current.rt = newNode;
                if (isVertical) {
                    current.rt.rect = new RectHV(current.p.x(), current.rect.ymin(), current.rect.xmax(), current.rect.ymax());
                } else {
                    current.rt.rect = new RectHV(current.rect.xmin(), current.p.y(), current.rect.xmax(), current.rect.ymax());
                }
            }
        }

        return current;
    }

    public boolean contains(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }

        if (isEmpty()) { return false; }


        return get(rootNode, p) != null;
    }

    private Node get(Node current, Point2D point) {
        if (current == null || !current.rect.contains(point)) { return null; }

        double pointVert = point.y();
        double nodeVert = current.p.y();

        if (current.isVertical) {
            pointVert = point.x();
            nodeVert = current.p.x();
        }

        if (pointVert == nodeVert
                && current.p.equals(point)) { return current; }

        if (pointVert < nodeVert) {
            return get(current.lb, point);
        } else {
            return get(current.rt, point);
        }
    }

    public void draw() {
        drawNode(rootNode);
    }

    private void drawNode(Node node) {
        if (node == null) { return; }
        StdDraw.point(node.p.x(), node.p.y());
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        drawNode(node.lb);
        drawNode(node.rt);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.IllegalArgumentException();
        }

        Queue<Point2D> result = new Queue<>();

        if (isEmpty()) { return result; }

        fillPointsInRange(rootNode, rect, result);

        return result;
    }

    private void fillPointsInRange(Node node, RectHV rect, Queue<Point2D> queue) {
        if (node == null || !node.rect.intersects(rect)) {
            return;
        }

        if (rect.contains(node.p)) {
            queue.enqueue(node.p);
        }

        fillPointsInRange(node.lb, rect, queue);
        fillPointsInRange(node.rt, rect, queue);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }

        if (isEmpty()) { return null; }

        return findNearest(rootNode, rootNode.p, p);
    }

    private Point2D findNearest(Node node, Point2D curMinPoint, Point2D point) {
        double curDistance = curMinPoint.distanceSquaredTo(point);

        if (node == null || node.rect.distanceSquaredTo(point) > curDistance) { return curMinPoint; }

        double sqDistance = node.p.distanceSquaredTo(point);

        if (sqDistance == 0) { return node.p; }

        if (sqDistance < curDistance) {
            curMinPoint = node.p;
        }

        boolean lbFirst;
        if (node.isVertical) { lbFirst = point.x() < node.p.x(); }
        else { lbFirst = point.y() < node.p.y(); }

        if (lbFirst) {
            curMinPoint = findNearest(node.lb, curMinPoint, point);
            return findNearest(node.rt, curMinPoint, point);
        } else {
            curMinPoint = findNearest(node.rt, curMinPoint, point);
            return findNearest(node.lb, curMinPoint, point);
        }
    }
}
