package fr.unice.polytech.si3.qgl.kihm.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointDoubleTest {
    PointDouble origin;
    PointDouble troisDeux;

    @BeforeEach
    void setUp() {
        origin = new PointDouble(0, 0);
        troisDeux = new PointDouble(3, 2);
    }

    @Test
    void moveTo() {
        origin.moveTo(3, 2);
        assertEquals(troisDeux, origin);
    }

    @Test
    void wrongMatrixOne() {
        PointDouble p = troisDeux;
        p.multiplyMatrix(new double[2][0]);
        assertEquals(troisDeux, p);
    }

    @Test
    void wrongMatrixTwo() {
        PointDouble p = troisDeux;
        p.multiplyMatrix(new double[0][2]);
        assertEquals(troisDeux, p);
    }

    @Test
    void testToString() {
        assertEquals("{\"x\": " + this.origin.getX() + ", \"y\": " + this.origin.getY() + '}', this.origin.toString());
        assertEquals("{\"x\": " + this.troisDeux.getX() + ", \"y\": " + this.troisDeux.getY() + '}', this.troisDeux.toString());
    }
}