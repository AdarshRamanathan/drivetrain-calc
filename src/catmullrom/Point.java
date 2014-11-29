package catmullrom;

public class Point {
    private double x, y;

    public Point() {
        this(0.0, 0.0);
    }

    public Point(double x, double y) {

        this.x = x;
        this.y = y;
    }

    /**:
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }
}
