package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.SHIP;

/**
 * Class that defines the other ships on the sea
 */
public class OtherShip extends Obstacle {

    /**
     * Life points of another ship.
     */
    private final int life;

    /**
     * Constructor for another ship.
     *
     * @param position The position of the other ship.
     * @param shape    The shape of the other ship.
     * @param life     The life points of the other ship.
     */
    public OtherShip(Position position, Shape shape, int life) {
        super(SHIP, position, shape);
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + this.getType().toString().toLowerCase() + "\", \"position\": " + this.position + ", \"shape\": " + this.shape.getBounds2D() + ", \"life\": " + this.life + "}";
    }
}
