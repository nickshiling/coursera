import java.util.LinkedList;

public class BruteCollinearPoints {
    private final LinkedList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) { throw new java.lang.IllegalArgumentException(); }

        segments = new LinkedList<LineSegment>();

        if (points.length >= 4) {
            for (int i1 = 0; i1 < points.length-3; i1++) {
                for (int i2 = i1+1; i2 < points.length-2; i2++) {
                    for (int i3 = i2+1; i3 < points.length-1; i3++) {
                        for (int i4 = i3+1; i4 < points.length; i4++) {
                            Point p1 = points[i1];
                            Point p2 = points[i2];
                            Point p3 = points[i3];
                            Point p4 = points[i4];

                            if (p1 == null ||
                                p2 == null ||
                                p3 == null ||
                                p4 == null ||
                                p1.compareTo(p2) == 0 ||
                                p1.compareTo(p3) == 0 ||
                                p1.compareTo(p4) == 0 ||
                                p2.compareTo(p3) == 0 ||
                                p2.compareTo(p4) == 0 ||
                                p3.compareTo(p4) == 0) { throw new java.lang.IllegalArgumentException(); }

                            double slopep1p2 = p1.slopeTo(p2);
                            double slopep1p3 = p1.slopeTo(p3);
                            double slopep1p4 = p1.slopeTo(p4);

                            if (slopep1p2 == slopep1p3 &&
                                    slopep1p3 == slopep1p4) {

                                Point start = getMinPoint(p1, p2, p3, p4);
                                Point end = getMaxPoint(p1, p2, p3, p4);

                                LineSegment segment = new LineSegment(start, end);
                                segments.add(segment);
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < points.length; i++) {
                if (points[i] == null) { throw new java.lang.IllegalArgumentException(); }
                for (int j = i+1; j < points.length; j++) {
                    if (points[j] == null ||
                            points[i].compareTo(points[j]) == 0) {
                        throw new java.lang.IllegalArgumentException();
                    }
                }
            }
        }
    }

    private static Point getMinPoint(Point p1, Point p2, Point p3, Point p4) {
        Point minPoint = p1;

        if (minPoint.compareTo(p2) > 0) { minPoint = p2; }
        if (minPoint.compareTo(p3) > 0) { minPoint = p3; }
        if (minPoint.compareTo(p4) > 0) { minPoint = p4; }

        return minPoint;
    }

    private static Point getMaxPoint(Point p1, Point p2, Point p3, Point p4) {
        Point maxPoint = p1;

        if (maxPoint.compareTo(p2) < 0) { maxPoint = p2; }
        if (maxPoint.compareTo(p3) < 0) { maxPoint = p3; }
        if (maxPoint.compareTo(p4) < 0) { maxPoint = p4; }

        return maxPoint;
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[segments.size()];
        segments.toArray(result);
        return result;
    }
}
