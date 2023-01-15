package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.STREAM;

/**
 * Class that defines the stream on the map
 */
public class Stream extends Obstacle {

    /**
     * The strength of the stream.
     */
    private final double strength;

    /**
     * Constructor of the stream.
     *
     * @param position The position of the stream.
     * @param shape    The shape of the stream.
     * @param strength The strength of the stream.
     */
    public Stream(Position position, Shape shape, double strength) {
        super(STREAM, position, shape);
        this.strength = strength;
    }

    public double getStrength() {
        return strength;
    }
}
