package fr.unice.polytech.si3.qgl.kihm.utilities;

import fr.unice.polytech.si3.qgl.kihm.landmarks.Checkpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Ellipse2D;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    static double precision = 1e9;
    Position origin;
    Position north;
    Position north_east;
    Position east;
    Position south_east;
    Position south;
    Position south_west;
    Position west;
    Position north_west;

    Position p;
    @BeforeEach
    void setUp() {
        this.origin = new Position(PI / 2); // Facing North
        this.north = new Position(0, 10, 0); // 0°
        this.north_east = new Position(10, 10, 0); // -45°
        this.east = new Position(10, 0, 0); // -90°
        this.south_east = new Position(10, -10, 0); // -135°
        this.south = new Position(0, -10, 0); // 180°
        this.south_west = new Position(-10, -10, 0); // 135°
        this.west = new Position(-10, 0, 0); // 90°
        this.north_west = new Position(-10, 10, 0); // 45°
        p = new Position();
    }

    private double round(double value) {
        return Math.round(value * precision) / precision;
    }

    @Test
    void testAngleFromFacingUp() {
        this.origin.setOrientation(PI / 2);
        assertEquals(0, Math.toDegrees(this.origin.getAngleBetween(this.north)));
        assertEquals(-45, Math.toDegrees(this.origin.getAngleBetween(this.north_east)));
        assertEquals(-90, Math.toDegrees(this.origin.getAngleBetween(this.east)));
        assertEquals(-135, Math.toDegrees(this.origin.getAngleBetween(this.south_east)));
        assertEquals(180, Math.toDegrees(this.origin.getAngleBetween(this.south)));
        assertEquals(135, Math.toDegrees(this.origin.getAngleBetween(this.south_west)));
        assertEquals(90, Math.toDegrees(this.origin.getAngleBetween(this.west)));
        assertEquals(45, Math.toDegrees(this.origin.getAngleBetween(this.north_west)));
    }

    @Test
    void testAngleFromFacingDown() {
        this.origin.setOrientation(-1 * PI / 2);
        assertEquals(180, Math.toDegrees(this.origin.getAngleBetween(this.north)));
        assertEquals(135, Math.toDegrees(this.origin.getAngleBetween(this.north_east)));
        assertEquals(90, Math.toDegrees(this.origin.getAngleBetween(this.east)));
        assertEquals(45, Math.toDegrees(this.origin.getAngleBetween(this.south_east)));
        assertEquals(0, Math.toDegrees(this.origin.getAngleBetween(this.south)));
        assertEquals(-45, Math.toDegrees(this.origin.getAngleBetween(this.south_west)));
        assertEquals(-90, Math.toDegrees(this.origin.getAngleBetween(this.west)));
        assertEquals(-135, Math.toDegrees(this.origin.getAngleBetween(this.north_west)));
    }

    @Test
    void testAngleFromFacingRight() {
        this.origin.setOrientation(0);
        assertEquals(90, Math.toDegrees(this.origin.getAngleBetween(this.north)));
        assertEquals(45, Math.toDegrees(this.origin.getAngleBetween(this.north_east)));
        assertEquals(0, Math.toDegrees(this.origin.getAngleBetween(this.east)));
        assertEquals(-45, Math.toDegrees(this.origin.getAngleBetween(this.south_east)));
        assertEquals(-90, Math.toDegrees(this.origin.getAngleBetween(this.south)));
        assertEquals(-135, Math.toDegrees(this.origin.getAngleBetween(this.south_west)));
        assertEquals(180, Math.toDegrees(this.origin.getAngleBetween(this.west)));
        assertEquals(135, Math.toDegrees(this.origin.getAngleBetween(this.north_west)));
    }

    @Test
    void testAngleFromFacingLeft() {
        this.origin.setOrientation(PI);
        assertEquals(-90, Math.toDegrees(this.origin.getAngleBetween(this.north)));
        assertEquals(-135, Math.toDegrees(this.origin.getAngleBetween(this.north_east)));
        assertEquals(180, Math.toDegrees(this.origin.getAngleBetween(this.east)));
        assertEquals(135, Math.toDegrees(this.origin.getAngleBetween(this.south_east)));
        assertEquals(90, Math.toDegrees(this.origin.getAngleBetween(this.south)));
        assertEquals(45, Math.toDegrees(this.origin.getAngleBetween(this.south_west)));
        assertEquals(0, Math.toDegrees(this.origin.getAngleBetween(this.west)));
        assertEquals(-45, Math.toDegrees(this.origin.getAngleBetween(this.north_west)));
    }

    @Test
    void equalsPosition() {
        assertNotEquals(new Position(0, 0), new Position(0, 1));
        assertNotEquals(new Position(0, 0), new Position(1, 0));
        assertNotEquals(new Position(PI / 2), new Position(PI));
        assertNotEquals(null, new Position(0, 0));
        assertEquals(new Position(0, 2), new Position(0, 2));
    }

    @Test
    void testEquals() {
        assertNotEquals(new Position(), this.origin);
        assertEquals(new Position(this.origin.getX(), this.origin.getY(), this.origin.getOrientation()), this.origin);
        assertEquals(this.origin, this.origin);
        assertNotEquals(this.north, this.origin);
    }

    @Test
    void testHashCode() {
        assertNotEquals(new Position().hashCode(), this.origin.hashCode());
        assertNotEquals(new Position().hashCode(), this.south.hashCode());
        assertEquals(new Position(this.origin.getX(), this.origin.getY(), this.origin.getOrientation()).hashCode(), this.origin.hashCode());
        assertEquals(this.origin.hashCode(), this.origin.hashCode());
        assertNotEquals(this.north.hashCode(), this.origin.hashCode());
    }

    @Test
    void algoTimeClosestPointTest() {
        Checkpoint massive = new Checkpoint(new Position(10000, 10000), new Ellipse2D.Double(10000, 10000, 5000, 5000));
        long tempsDepart = System.currentTimeMillis();
        assertEquals(new Position(10732.27, 10732.2), new Position(0, 0).getClosestPoint(massive.getShape()));
        assertTrue(System.currentTimeMillis() - tempsDepart <= 100);
        massive = new Checkpoint(new Position(-10000, -10000), new Ellipse2D.Double(-15000, -15000, 5000, 5000));
        tempsDepart = System.currentTimeMillis();
        assertEquals(new Position(-10732.27, -10732.2), new Position(0, 0).getClosestPoint(massive.getShape()));
        assertTrue(System.currentTimeMillis() - tempsDepart <= 100);
    }

    @Test
    void angleBetweenConsecutiveCardinalPoints() {
        assertEquals(this.round(Math.toRadians(90)), this.round(new Position().angleBetweenTwoPositions(east, south_east)));
        assertEquals(this.round(Math.toRadians(45)), this.round(new Position().angleBetweenTwoPositions(south_east, south)));
        assertEquals(this.round(Math.toRadians(90)), this.round(new Position().angleBetweenTwoPositions(south, south_west)));
        assertEquals(this.round(Math.toRadians(45)), this.round(new Position().angleBetweenTwoPositions(south_west, west)));
        assertEquals(this.round(Math.toRadians(90)), this.round(new Position().angleBetweenTwoPositions(west, north_west)));
        assertEquals(this.round(Math.toRadians(45)), this.round(new Position().angleBetweenTwoPositions(north_west, north)));
        assertEquals(this.round(Math.toRadians(90)), this.round(new Position().angleBetweenTwoPositions(north, north_east)));
        assertEquals(this.round(Math.toRadians(45)), this.round(new Position().angleBetweenTwoPositions(north_east, east)));
    }

    @Test
    void angleBetweenFacingCardinalPoints() {
        assertEquals(this.round(Math.toRadians(0)), this.round(new Position().angleBetweenTwoPositions(east, west)));
        assertEquals(this.round(Math.toRadians(0)), this.round(new Position().angleBetweenTwoPositions(south_east, north_west)));
        assertEquals(this.round(Math.toRadians(0)), this.round(new Position().angleBetweenTwoPositions(south, north)));
        assertEquals(this.round(Math.toRadians(0)), this.round(new Position().angleBetweenTwoPositions(south_west, north_east)));
        assertEquals(this.round(Math.toRadians(0)), this.round(new Position().angleBetweenTwoPositions(west, east)));
        assertEquals(this.round(Math.toRadians(0)), this.round(new Position().angleBetweenTwoPositions(north_west, south_east)));
        assertEquals(this.round(Math.toRadians(0)), this.round(new Position().angleBetweenTwoPositions(north, south)));
        assertEquals(this.round(Math.toRadians(0)), this.round(new Position().angleBetweenTwoPositions(north_east, south_west)));
    }

    @Test
    void absoluteTravelTest() {
        assertEquals(10, this.origin.absoluteTravel(this.north));
        assertEquals(20, this.origin.absoluteTravel(this.north_east));
        assertEquals(10, this.origin.absoluteTravel(this.east));
        assertEquals(20, this.origin.absoluteTravel(this.south_east));
        assertEquals(10, this.origin.absoluteTravel(this.south));
        assertEquals(20, this.origin.absoluteTravel(this.south_west));
        assertEquals(10, this.origin.absoluteTravel(this.west));
        assertEquals(20, this.origin.absoluteTravel(this.north_west));
    }

    @Test
    void createCopieInstanceTest() {
        Position positionCopie = new Position(this.origin);
        assertEquals(this.origin, positionCopie);
        positionCopie = new Position(this.north);
        assertEquals(this.north, positionCopie);
        positionCopie = new Position(this.east);
        assertEquals(this.east, positionCopie);
        positionCopie = new Position(this.south);
        assertEquals(this.south, positionCopie);
        positionCopie = new Position(this.west);
        assertEquals(this.west, positionCopie);
    }

    @Test
    void getOrientationTau() {
        assertEquals(0, new Position(0, 0, PI * 2).getOrientation());
    }

    @Test
    void getOrientationHalf() {
        assertEquals(-PI / 2, new Position(0, 0, 3 * PI / 2).getOrientation());
    }

    @Test
    void setOrientationTau() {
        p.setOrientation(2 * PI);
        assertEquals(0, p.getOrientation());
    }

    @Test
    void setOrientationTauNeg() {
        p.setOrientation(-2 * PI);
        assertEquals(0, p.getOrientation());
    }

    @Test
    void setOrientationNeg() {
        p.setOrientation(-3 * PI / 2);
        assertEquals(PI / 2, p.getOrientation());
    }

    @Test
    void setOrientationHalf() {
        p.setOrientation(3 * PI / 2);
        assertEquals(-PI / 2, p.getOrientation());
    }

    @Test
    void rotatePi() {
        p.rotate(PI);
        assertEquals(PI, p.getOrientation());
    }


    @Test
    void rotatePiHalf() {
        p.setOrientation(3 * PI / 2);
        p.rotate(2 * PI);
        assertEquals(-PI / 2, p.getOrientation());
    }

    @Test
    void comparePointDouble() {
        assertNotEquals(new Position(), new PointDouble());
    }

    @Test
    void absoluteTravelNegNeg() {
        assertEquals(5, new Position(-5, -5).absoluteTravel(new Position(-8, -3)));
    }

    @Test
    void absoluteTravelNegPos() {
        assertEquals(5, new Position(-5, -5).absoluteTravel(new Position(8, 3)));
    }

    @Test
    void absoluteTravelPosNeg() {
        assertEquals(5, new Position(5, 5).absoluteTravel(new Position(-8, -3)));
    }

    @Test
    void getAngleBetweenNegNeg() {
        assertEquals(982, (int) (new Position(-5, -5, PI / 2).getAngleBetween(new Position(-8, -3, 5 * PI / 2)) * 1000));
    }

    @Test
    void getAngleBetweenPosNeg() {
        assertEquals(-1138, (int) (new Position(5, 3, 6 * PI / 4).getAngleBetween(new Position(-8, -3, -PI / 2)) * 1000));
    }
}