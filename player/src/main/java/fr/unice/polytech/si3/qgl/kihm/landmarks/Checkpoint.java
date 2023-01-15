package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;
import java.awt.geom.Area;
import java.util.Objects;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.CHECKPOINT;

/**
 * Class that defines the checkpoints of the map
 */
public class Checkpoint extends Obstacle {

    /**
     * The constructor for the checkpoint with only its position.
     *
     * @param position The position of the checkpoint.
     */
    public Checkpoint(Position position) {
        this(position, new Area());
    }

    /**
     * The constructor for the checkpoint with its position and its shape.
     *
     * @param position The position of the checkpoint.
     * @param shape    The shape of the checkpoint.
     */
    public Checkpoint(Position position, Shape shape) {
        super(CHECKPOINT, position, shape);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Checkpoint checkpoint = (Checkpoint) o;
        return Objects.equals(position, checkpoint.position) && Objects.equals(shape.getBounds2D(), checkpoint.shape.getBounds2D());
    }

    @Override
    public String toString() {
        return "CheckPoint[" + this.getPosition() + ", " + this.getShape().getBounds2D() + "]";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
