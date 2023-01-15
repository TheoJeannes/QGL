package fr.unice.polytech.si3.qgl.kihm.utilities;

/**
 * Create a Point with x,y coords typed as double
 */
public class PointDouble {
    /**
     * The abscissa axe.
     */
    private double x = 0;

    /**
     * The ordinate axe.
     */
    private double y = 0;

    /**
     * The basic constructor with nothing in it.
     */
    public PointDouble() {
    }

    /**
     * The constructor of the point of an object
     *
     * @param x the x position
     * @param y the y position
     */
    public PointDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * set x and y to the coords of the points
     *
     * @param x x-position
     * @param y y-position
     */
    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * add x and y to the coords of the points
     *
     * @param x x-movement
     * @param y y-movement
     */
    public void move(double x, double y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Add the x value to x
     *
     * @param x x-movement
     */
    public void moveX(double x) {
        this.x += x;
    }

    /**
     * Add the y value to y
     *
     * @param y y-movement
     */
    public void moveY(double y) {
        this.y += y;
    }

    /**
     * Multiply the coords by a 2-2 matrix,
     * representing x and y as a column matrix
     * as follows : [2-2 Matrix]*[x-y]
     *
     * @param m 2-2 matrix
     */
    public void multiplyMatrix(double[][] m) {
        double tx = x;
        double ty = y;
        if (m.length == 2 && m[0].length == 2) {
            x = m[0][0] * tx + m[0][1] * ty;
            y = m[1][0] * tx + m[1][1] * ty;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointDouble that)) return false;

        if (Double.compare(that.x, x) != 0) return false;
        return Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "{\"x\": " + this.x + ", \"y\": " + this.y + "}";
    }
}
