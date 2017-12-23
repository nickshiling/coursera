public class Main {

    public static void main(String[] args) {
	    Point[] points = new Point[10];
	    points[0] = new Point(7, 7);
        points[1] = new Point(7, 4);
        points[2] = new Point(7, 6);
        points[3] = new Point(1, 7);
        points[4] = new Point(4, 7);
        points[5] = new Point(5, 7);
        points[6] = new Point(7, 3);
        points[7] = new Point(4, 1);
        points[8] = new Point(5, 6);
        points[9] = new Point(6, 9);

        FastCollinearPoints fast = new FastCollinearPoints(points);
    }
}
