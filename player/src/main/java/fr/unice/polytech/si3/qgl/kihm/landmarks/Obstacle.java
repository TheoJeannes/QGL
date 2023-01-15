package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.serializers.ShapeFunctions;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.REEF;

/**
 * Class that defines the obstacles
 */
public class Obstacle {

    /**
     * The position of the obstacle.
     */
    protected final Position position;

    /**
     * The shape of the obstacle.
     */
    protected final Shape shape;

    /**
     * The type of the obstacle.
     */
    protected final obstacleTypeEnum type;

    /**
     * The constructor by default of the obstacle, which is a reef.
     *
     * @param position The position of the reef.
     * @param shape    The shape of the reef.
     */
    public Obstacle(Position position, Shape shape) {
        this.position = position;
        this.shape = shape;
        this.type = REEF;
    }

    /**
     * The constructor of an obstacle with the type of the obstacle.
     *
     * @param type     The type of obstacle.
     * @param position The position of the obstacle.
     * @param shape    The shape of the obstacle.
     */
    public Obstacle(obstacleTypeEnum type, Position position, Shape shape) {
        this.position = position;
        this.shape = shape;
        this.type = type;
    }

    /**
     * Calculate the distance between an object and the checkpoint
     *
     * @param position the position of an object
     * @return the distance between an object and the checkpoint
     */
    public double distance(Position position) {
        return this.getPosition().distance(position);
    }

    public Position getPosition() {
        return position;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public Shape getShape() {
        return shape;
    }

    public obstacleTypeEnum getType() {
        return type;
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + this.type.toString().toLowerCase() + "\", \"position\": " + this.position + ", \"shape\": " + new ShapeFunctions().shapeToNode(shape, position) + "}";
    }

    /**
     * Enumeration of the type of obstacles in this world.
     */
    public enum obstacleTypeEnum {
        STREAM,
        SHIP,
        REEF,
        CHECKPOINT
    }
}
