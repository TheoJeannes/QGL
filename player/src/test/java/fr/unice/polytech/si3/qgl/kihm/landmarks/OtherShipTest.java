package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.SHIP;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OtherShipTest {

    OtherShip ship;
    Position position;
    Shape shape;
    int life;

    @BeforeEach
    void setUp() {
        this.position = new Position(40, 65, 1);
        this.shape = new Rectangle(0, 0, 5, 5);
        this.life = 483;
        this.ship = new OtherShip(this.position, this.shape, this.life);
    }

    @Test
    void testType() {
        assertEquals(SHIP, this.ship.getType());
    }

    @Test
    void testLife() {
        assertEquals(this.life, this.ship.getLife());
    }

    @Test
    void testToString() {
        assertEquals("{\"type\": \"" + Obstacle.obstacleTypeEnum.SHIP.toString().toLowerCase() + "\", \"position\": " + this.position + ", \"shape\": " + this.shape.getBounds2D() + ", \"life\": " + this.life + "}", this.ship.toString());
    }
}