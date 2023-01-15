package fr.unice.polytech.si3.qgl.kihm.ship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LayoutTest {
    private Layout defaultLayout;
    private Layout layoutWithOars;
    private Layout layoutWithOarsAndRudder;
    private Layout layoutWithOarsAndRudderAndSail;
    private Layout layoutWithOarsAndRudderAndSailAndWatch;

    @BeforeEach
    void setUp() {
        this.defaultLayout = new Layout();
        this.layoutWithOars = new Layout(1, 2);
        this.layoutWithOarsAndRudder = new Layout(2, 3, 0.456);
        this.layoutWithOarsAndRudderAndSail = new Layout(0, 4, 0.741, 2);
        this.layoutWithOarsAndRudderAndSailAndWatch = new Layout(4, 1, 0.862, 3, true);
    }

    @Test
    void defaultConstructTest() {
        assertEquals(0, defaultLayout.getNumberOarLeft());
        assertEquals(0, defaultLayout.getNumberOarRight());
        assertEquals(0.0, defaultLayout.getAngleRudder());
        assertEquals(0, defaultLayout.getNumberSail());
        assertFalse(defaultLayout.isAssignedToWatch());
    }

    @Test
    void oarConstructTest() {
        assertEquals(1, layoutWithOars.getNumberOarLeft());
        assertEquals(2, layoutWithOars.getNumberOarRight());
        assertEquals(0.0, layoutWithOars.getAngleRudder());
        assertEquals(0, layoutWithOars.getNumberSail());
        assertFalse(layoutWithOars.isAssignedToWatch());
    }

    @Test
    void oarAndRudderConstructTest() {
        assertEquals(2, layoutWithOarsAndRudder.getNumberOarLeft());
        assertEquals(3, layoutWithOarsAndRudder.getNumberOarRight());
        assertEquals(0.456, layoutWithOarsAndRudder.getAngleRudder());
        assertEquals(0, layoutWithOarsAndRudder.getNumberSail());
        assertFalse(layoutWithOarsAndRudder.isAssignedToWatch());
    }

    @Test
    void oarAndRudderAndSailConstructTest() {
        assertEquals(0, layoutWithOarsAndRudderAndSail.getNumberOarLeft());
        assertEquals(4, layoutWithOarsAndRudderAndSail.getNumberOarRight());
        assertEquals(0.741, layoutWithOarsAndRudderAndSail.getAngleRudder());
        assertEquals(2, layoutWithOarsAndRudderAndSail.getNumberSail());
        assertFalse(layoutWithOarsAndRudderAndSail.isAssignedToWatch());
    }

    @Test
    void oarAndRudderAndSailAndWatchConstructTest() {
        assertEquals(4, layoutWithOarsAndRudderAndSailAndWatch.getNumberOarLeft());
        assertEquals(1, layoutWithOarsAndRudderAndSailAndWatch.getNumberOarRight());
        assertEquals(0.862, layoutWithOarsAndRudderAndSailAndWatch.getAngleRudder());
        assertEquals(3, layoutWithOarsAndRudderAndSailAndWatch.getNumberSail());
        assertTrue(layoutWithOarsAndRudderAndSailAndWatch.isAssignedToWatch());
    }

    @Test
    void toStringTest() {
        assertEquals("Layout{numberOarLeft=0, numberOarRight=0, angleRudder=0.0, numberSail=0, assignedToWatch=false}", this.defaultLayout.toString());
        assertEquals("Layout{numberOarLeft=1, numberOarRight=2, angleRudder=0.0, numberSail=0, assignedToWatch=false}", this.layoutWithOars.toString());
        assertEquals("Layout{numberOarLeft=2, numberOarRight=3, angleRudder=0.456, numberSail=0, assignedToWatch=false}", this.layoutWithOarsAndRudder.toString());
        assertEquals("Layout{numberOarLeft=0, numberOarRight=4, angleRudder=0.741, numberSail=2, assignedToWatch=false}", this.layoutWithOarsAndRudderAndSail.toString());
        assertEquals("Layout{numberOarLeft=4, numberOarRight=1, angleRudder=0.862, numberSail=3, assignedToWatch=true}", this.layoutWithOarsAndRudderAndSailAndWatch.toString());
    }
}