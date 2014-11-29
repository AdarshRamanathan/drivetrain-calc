package catmullrom;

public class CatmullRomSplineUtils {
    /**
     * Creates catmull spline curves between the points array.
     *
     * @param points The current 2D points array
     * @param subdivisions The number of subdivisions to add between each of the points.
     *
     * @return A larger array with the points subdivided.
     */
    public static Point[] subdividePoints(Point[] points, int subdivisions) {
        assert points != null;
        assert points.length >= 3;

        Point[] subdividedPoints = new Point[((points.length-1) * subdivisions) + 1];

        double increments = 1.0 / (double)subdivisions;

        for (int i = 0; i < points.length-1; i++) {
            Point p0 = i == 0 ? points[i] : points[i-1];
            Point p1 = points[i];
            Point p2 = points[i+1];
            Point p3 = (i+2 == points.length) ? points[i+1] : points[i+2];

            CatmullRomSpline2D crs = new CatmullRomSpline2D(p0, p1, p2, p3);

            for (int j = 0; j <= subdivisions; j++) {
                subdividedPoints[(i*subdivisions)+j] = crs.q(j * increments);
            }
        }

        return subdividedPoints;
    }

	public static double interpolate(Point[] points, double x)
	{
		assert points != null;
		assert points.length >= 3;
		
		System.out.println(points.length);

		int i = 0;
		while(i < points.length && points[i].getX() < x)
			i++;
		i--;

		if(i < 0)
			return points[0].getY();
		else if(i >= points.length)
			return points[points.length - 1].getY();

		System.out.println(i);

		Point p0 = (i == 0) ? points[i] : points[i - 1];
		Point p1 = points[i];
		Point p2 = points[i + 1];
		Point p3 = (i + 2 == points.length) ? points[i + 1] : points[i + 2];

		CatmullRomSpline2D crs = new CatmullRomSpline2D(p0, p1, p2, p3);

		double t = (x - p1.getX()) / (p2.getX() - p1.getX());
		
		return crs.q(t).getY();
	}

	public static Point[] createPoints(double[] x, double[] y)
	{
		assert x.length == y.length;
		Point[] ret = new Point[x.length];
		
		for(int i = 0 ; i < ret.length ; i++)
		{
			ret[i] = new Point(x[i], y[i]);
		}

		return ret;
	}


    public static void main(String[] args) {
        Point[] pointArray = new Point[4];

        pointArray[0] = new Point(1.0, 1.0);
        pointArray[1] = new Point(2.0, 2.0);
        pointArray[2] = new Point(3.0, 2.0);
        pointArray[3] = new Point(4.0, 1.0);

        Point[] subdividedPoints = CatmullRomSplineUtils.subdividePoints(pointArray, 4);

        for (Point point : subdividedPoints) {
            System.out.println("" + point);
        }
    }
}
