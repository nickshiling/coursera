import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) { throw new java.lang.IllegalArgumentException(); }

        segments = new ArrayList<LineSegment>();

        Point[] sortedPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) { throw new java.lang.IllegalArgumentException(); }
            sortedPoints[i] = points[i];
        }

        Arrays.sort(sortedPoints);

        for (int i = 0; i < sortedPoints.length - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i+1]) == 0) { throw new java.lang.IllegalArgumentException(); }
        }

        if (points.length < 4) { return; }

        for (int i = 0; i < points.length; i++) {
            Point originalPoint = sortedPoints[i];

            Arrays.sort(sortedPoints, originalPoint.slopeOrder());

            int segmentStart = 1;
            int segmentEnd = 2;
            double previouseSlope = originalPoint.slopeTo(sortedPoints[segmentStart]);

            for (int j = 2; j < sortedPoints.length; j++) {
                Point currentPoint = sortedPoints[j];
                if (originalPoint != currentPoint) {
                    double slope = originalPoint.slopeTo(currentPoint);
                    if (previouseSlope != slope) {
                         addSegment(sortedPoints, originalPoint, segmentStart, segmentEnd);

                         segmentStart = j;
                         segmentEnd = j;
                         previouseSlope = slope;
                     } else {
                         segmentEnd = j;
                     }
                }
            }

            addSegment(sortedPoints, originalPoint, segmentStart, segmentEnd);
            Arrays.sort(sortedPoints);
        }
    }

    private void addSegment(Point[] points, Point origin, int start, int end) {
        int segmentLength = end - start;

        if (segmentLength >= 2) {
            for (int i = start; i < end; i++) {
                if (origin.compareTo(points[i]) > 0 || points[i].compareTo(points[i+1]) > 0) { return; }
            }

            LineSegment segment = new LineSegment(origin, points[end]);
            segments.add(segment);
        }
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
