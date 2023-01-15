package fr.unice.polytech.si3.qgl.kihm.utilities;

import fr.unice.polytech.si3.qgl.kihm.logger.Printer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import static java.lang.Math.round;

/**
 * Create the position of an object
 */

public class Position extends PointDouble {
    /**
     * The orientation of a point
     */
    private double orientation = 0;

    /**
     * The basic constructor of a position with nothing in it.
     */
    public Position() {
    }

    @SuppressWarnings("all")
    public Position(Position position) {
        this(position.getX(), position.getY(), position.getOrientation());
        this.calibrate();
    }

    /**
     * The constructor of the position of something
     *
     * @param x the x position
     * @param y the y position
     */
    public Position(double x, double y) {
        super(x, y);
        this.calibrate();
    }

    /**
     * The constructor of the position of something
     *
     * @param orientation the orientation of something
     */
    public Position(double orientation) {
        super();
        this.orientation = orientation;
        this.calibrate();
    }

    /**
     * The constructor of the position of something
     *
     * @param x           the x position
     * @param y           the y position
     * @param orientation the orientation of something
     */
    public Position(double x, double y, double orientation) {
        super(x, y);
        this.orientation = orientation;
        this.calibrate();
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
        calibrate();
    }

    /**
     * The method that rotates the object
     *
     * @param theta the angle to rotate
     */
    public void rotate(double theta) {
        this.orientation += theta;
        this.calibrate();
    }

    /**
     * Put between pi and -pi all values
     */
    private void calibrate() {
        this.orientation %= 2 * Math.PI;
        if (this.orientation > Math.PI) this.orientation -= 2 * Math.PI;
        if (this.orientation < -Math.PI) this.orientation += 2 * Math.PI;
        if (this.orientation == -0) this.orientation = 0;
    }

    /**
     * Compute angle between a moving object e.g a ship, and a non-moving point, e.g a checkpoint
     *
     * @param p Position of the non-moving object
     * @return angle needed to align the moving object on the non-moving one
     */
    public double getAngleBetween(Position p) {
        this.orientation %= 2 * Math.PI;
        double a = p.getX() - this.getX();
        double b = p.getY() - this.getY();
        double theta = Math.atan2(b, a) - this.orientation;
        if (theta > Math.PI) {
            theta -= 2 * Math.PI;
        }
        if (theta < -Math.PI) {
            theta += 2 * Math.PI;
        }
        return theta == -1 * Math.PI ? Math.PI : theta;
    }

    /**
     * Compute distance between a moving object e.g a ship, and a non-moving point, e.g a checkpoint
     *
     * @param position Position of the non-moving object
     * @return distance between the moving object and the non-moving one
     */
    public double distance(Position position) {
        return Math.sqrt(Math.pow(this.getX() - position.getX(), 2) + Math.pow(this.getY() - position.getY(), 2));
    }

    /**
     * Find the angle between two positions and the ship
     * this is the current ship
     *
     * @param cp1 the first position
     * @param cp2 the second position
     * @return the angle between two positions and the ship
     */
    public double angleBetweenTwoPositions(Position cp1, Position cp2) {
        // arccos[(a² + c² − b²) ÷ 2ac]
        double a = cp1.distance(cp2);
        double b = cp2.distance(this);
        double c = cp1.distance(this);
        return Math.acos((Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2)) / (2 * a * c));
    }

    /**
     * Find the closest point between a position and a shape
     *
     * @param shape the shape to look the closest point
     * @return the closest point between the position and a shape
     */
    public Position getClosestPoint(Shape shape) {
        return shape.getClass().equals(Ellipse2D.Double.class) ? this.closestPointCircle(shape) : this.closestPointPathIterator(shape);
    }

    private Position closestPointCircle(Shape shape) {
        double distanceCarre = Integer.MAX_VALUE;
        Position closestPoint = new Position();
        Rectangle2D bounds = shape.getBounds2D();

        double radius = bounds.getHeight() * 0.5;
        double centerX = bounds.getX() + radius;
        double centerY = bounds.getY() + radius;

        for (double y = centerY - radius; y <= centerY + radius; y += 0.1) {

            double dx = Math.pow(radius, 2) - Math.pow(y - centerY, 2);
            boolean negative = dx < 0.0;
            dx = Math.sqrt(Math.abs(dx));
            if (negative) dx *= -1;
            dx = radius - dx;

            if (distanceCarre > (Math.pow(centerX - radius + dx - this.getX(), 2) + Math.pow(y - this.getY(), 2))) {
                distanceCarre = Math.pow(centerX - radius + dx - this.getX(), 2) + Math.pow(y - this.getY(), 2);
                closestPoint.setX(centerX - radius + dx);
                closestPoint.setY(y);
            }

            if (distanceCarre > (Math.pow(centerX + radius - dx - this.getX(), 2) + Math.pow(y - this.getY(), 2))) {
                distanceCarre = Math.pow(centerX + radius - dx - this.getX(), 2) + Math.pow(y - this.getY(), 2);
                closestPoint.setX(centerX + radius - dx);
                closestPoint.setY(y);
            }
        }
        closestPoint.setX((round(closestPoint.getX() * 100)) / 100.0);
        closestPoint.setY((round(closestPoint.getY() * 100)) / 100.0);
        return closestPoint;
    }

    private Position closestPointPathIterator(Shape shape) {
        double distanceCarre = Integer.MAX_VALUE;
        Position closestPoint = new Position();
        PathIterator pathIterator = shape.getPathIterator(new AffineTransform());

        while (!pathIterator.isDone()) {
            double[] data = new double[6];
            Printer.get().severe("Path: " + pathIterator.currentSegment(data) + " - " + Arrays.toString(data));
            if (Arrays.equals(data, new double[]{0, 0, 0, 0, 0, 0})) break;
            double x = data[0];
            double y = data[1];

            double tmp = Math.pow(x - this.getX(), 2) + Math.pow(y - this.getY(), 2);
            if (distanceCarre > tmp) {
                distanceCarre = tmp;
                closestPoint.setX(x);
                closestPoint.setY(y);
            }
            pathIterator.next();
        }
        return closestPoint;
    }

    public double absoluteTravel(Position position) {
        return Math.abs(Math.abs(position.getX()) - Math.abs(this.getX())) + Math.abs(Math.abs(position.getY()) - Math.abs(this.getY()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;
        if (!super.equals(o)) return false;

        return Double.compare(position.orientation, orientation) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(orientation);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "{\"x\": " + this.getX() + ", \"y\": " + this.getY() + ", \"orientation\": " + orientation + '}';
    }
}
