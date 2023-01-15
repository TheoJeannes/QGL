package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Ellipse2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CheckpointTest {
    Checkpoint c;

    @BeforeEach
    void setUp() {
        c = new Checkpoint(new Position());
    }

    @Test
    void checkPointNull() {
        assertNotEquals(null, c);
    }

    @Test
    void checkPointPostion() {
        Checkpoint cp = new Checkpoint(new Position(1, 0));
        assertNotEquals(c, cp);
    }

    @Test
    void checkPointShape() {
        Checkpoint cs = new Checkpoint(new Position(), new Ellipse2D.Double(10, 10, 10, 10));
        assertNotEquals(c, cs);
    }

    @Test
    void testHashCode() {
        Checkpoint cs = new Checkpoint(new Position(), new Ellipse2D.Double(10, 10, 10, 10));
        assertNotEquals(0, c.hashCode());
        assertNotEquals(0, cs.hashCode());
        assertNotEquals(c.hashCode(), cs.hashCode());
    }

    @Test
    void isEqual() {
        assertEquals(new Checkpoint(new Position()), new Checkpoint(new Position()));
    }

    @Test
    void testToString() {
        assertEquals(new Checkpoint(new Position()).toString(), this.c.toString());
        assertNotEquals(new Checkpoint(new Position(1, 1, 0)).toString(), this.c.toString());
    }
}
