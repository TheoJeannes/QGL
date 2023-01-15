package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ObstacleTest {
    Checkpoint c, circle;

    @BeforeEach
    void setUp() {
        c = new Checkpoint(new Position(2, 3));
        circle = new Checkpoint(new Position(1, 3), new Ellipse2D.Double(10, 10, 10, 10));
    }

    @Test
    void compareCheckPoints() {
        assertEquals(c, new Checkpoint(new Position(2, 3)));
    }

    @Test
    void compareCheckPointsPosition() {
        assertNotEquals(c, new Checkpoint(new Position(1, 3)));
    }

    @Test
    void compareCheckPointsShape() {
        assertEquals(circle, new Checkpoint(new Position(1, 3), new Ellipse2D.Double(10, 10, 10, 10)));
    }

    @Test
    void compareCheckPointsNot() {
        assertNotEquals(circle, new Checkpoint(new Position(2, 3), new Rectangle2D.Double(10, 10, 10, 10)));
    }

    @Test
    void compareNull() {
        assertNotEquals(null, c);
    }

    @Test
    void testGetXANDY() {
        assertEquals(2.0, this.c.getX());
        assertEquals(3.0, this.c.getY());
        assertNotEquals(this.circle.getX(), this.c.getX());
        assertEquals(this.circle.getY(), this.c.getY());
    }
}