package fr.unice.polytech.si3.qgl.kihm.structures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.kihm.equipments.Sail;
import fr.unice.polytech.si3.qgl.kihm.equipments.Watch;
import fr.unice.polytech.si3.qgl.kihm.landmarks.*;
import fr.unice.polytech.si3.qgl.kihm.serializers.ShapeFunctions;
import fr.unice.polytech.si3.qgl.kihm.ship.Deck;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    final Calculator calculator = new Calculator();
    Position position;
    Shape shape;

    @BeforeEach
    void setUp() {
        this.position = new Position(2345, 456, Math.toRadians(56));
        this.shape = this.getRectangle(3, 10, this.position.getOrientation());
    }

    private Shape getRectangle(double width, double height, double orientation) {
        String newShapeJson = "{\"type\":\"rectangle\",\"width\":" + width + ",\"height\":" + height + ",\"orientation\":" + orientation + "}";
        try {
            JsonNode shapeNode = new ObjectMapper().readTree(newShapeJson);
            return new ShapeFunctions().nodeToShape(this.position, shapeNode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Stream> getListOfStream(double angle) {
        return List.of(new Stream(this.position, this.getRectangle(50, 100, angle), 100));
    }

    @Test
    void calculatingNextTurnPositionFullSpeedTest() {
        assertEquals(new Position(2437.2668290726506, 592.7911994715791, 0.9773843811168246), this.calculator.calculatingNextRoundPosition(position, shape, 165, 0, 0, 0, new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionFullSpeedAndTurnTest() {
        assertEquals(new Position(2377.6753026876295, 613.4370962056813, 1.7627825445142684), this.calculator.calculatingNextRoundPosition(position, shape, 165, Math.toRadians(45), 0, 0, new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionNotFullSpeedAndTurnTest() {
        assertEquals(new Position(2331.4581131195787, 545.8597514166637, 2.4783675378319434), this.calculator.calculatingNextRoundPosition(position, shape, 100, Math.toRadians(86), 0, 0, new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindBehindTest() {
        Wind wind = new Wind(0.0, 100);
        assertEquals(new Position(2468.536499401871, 639.1503921999229, 0.9773843811168246), this.calculator.calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindInFrontTest() {
        Wind wind = new Wind(Math.toRadians(180), 100);
        assertEquals(new Position(2405.9971587434757, 546.4320067432423, 0.9773843811168246), this.calculator.calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindSideTest() {
        Wind wind = new Wind(Math.toRadians(90), 100);
        assertEquals(new Position(2483.6260218010193, 661.5215291423792, 0.9773843811168246), this.calculator.calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindDiagBehindTest() {
        Wind wind = new Wind(Math.toRadians(45), 100);
        assertEquals(new Position(2492.158724556448, 674.171781203533, 0.9773843811168246), this.calculator.calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindDiagFrontTest() {
        Wind wind = new Wind(Math.toRadians(135), 100);
        assertEquals(new Position(2447.9367326859756, 608.6099821064463, 0.9773843811168246), this.calculator.calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    private Position calculatingNextTurnPositionWithStream(double angle) {
        List<Stream> streams = this.getListOfStream(Math.toRadians(angle));
        return this.calculator.calculatingNextRoundPosition(position, shape, 165, 0, 0, 0, streams);
    }

    @Test
    void calculatingNextTurnPositionWithStreamFrontTest() {
        assertEquals(new Position(2493.1861194197336, 675.6949567270849, 0.9773843811168246), this.calculatingNextTurnPositionWithStream(180));
    }

    @Test
    void calculatingNextTurnPositionWithStreamSideTest() {
        assertEquals(new Position(2493.1861194197336, 675.6949567270849, 0.9773843811168246), this.calculatingNextTurnPositionWithStream(90));
    }

    @Test
    void calculatingNextTurnPositionWithStreamDiagTest() {
        assertEquals(new Position(2493.1861194197336, 675.6949567270849, 0.9773843811168246), this.calculatingNextTurnPositionWithStream(45));
    }

    @Test
    void calculatingNextTurnPositionWithMultipleStreamTest() {
        List<Stream> streams = new ArrayList<>(this.getListOfStream(Math.toRadians(45)));
        streams.add(new Stream(new Position(), new Rectangle(-10, -10, 500, 20), 100));
        assertEquals(new Position(2493.1861194197336, 675.6949567270849, 0.9773843811168246), this.calculator.calculatingNextRoundPosition(position, shape, 165, 0, 0, 0, streams));
    }

    @Test
    void noObstacleToAvoid() {
        assertFalse(this.calculator.isObstacleToAvoid(new Checkpoint(new Position(), new Area()), 0.0));
    }

    @Test
    void obstacleToAvoid() {
        assertTrue(this.calculator.isObstacleToAvoid(new Obstacle(new Position(), new Area()), 0.0));
    }

    @Test
    void shipToAvoid() {
        assertTrue(this.calculator.isObstacleToAvoid(new OtherShip(new Position(), new Area(), 100), 0.0));
    }

    @Test
    void streamNotToAvoid() {
        assertFalse(this.calculator.isObstacleToAvoid(new Stream(new Position(), new Area(), 100), 0.0));
    }

    @Test
    void streamToAvoid() {
        assertTrue(this.calculator.isObstacleToAvoid(new Stream(new Position(), new Area(), 100), Math.PI));
    }

    @Test
    void testOarSpeed(){
        Layout layout = new Layout(1,1);
        assertEquals(165.0, this.calculator.calculateOarSpeed(layout, 2));
        assertNotEquals(123, this.calculator.calculateOarSpeed(layout,2));
    }

    @Test
    void testOarSpeedLeftOnly(){
        Layout layout = new Layout(1,0);
        assertNotEquals(165.0, this.calculator.calculateOarSpeed(layout, 2));
    }

    @Test
    void testOarSpeedRightOnly(){
        Layout layout = new Layout(0,1);
        assertNotEquals(165.0, this.calculator.calculateOarSpeed(layout, 2));
    }

    @Test
    void testTurningAngle(){
        Layout layout = new Layout(1,1, Math.toRadians(12.5));
        assertEquals(Math.toRadians(12.5), this.calculator.calculateTurningAngle(layout, 2));
    }

    @Test
    void testTurningAngleWithOarLeft(){
        Layout layout = new Layout(1,0, Math.toRadians(12.5));
        assertNotEquals(Math.toRadians(12.5), this.calculator.calculateTurningAngle(layout, 2));
        assertEquals(-1.3526301702956054, this.calculator.calculateTurningAngle(layout,2));
    }

    @Test
    void testTurningAngleWithOarRigth(){
        Layout layout = new Layout(0,1, Math.toRadians(12.5));
        assertNotEquals(Math.toRadians(12.5), this.calculator.calculateTurningAngle(layout, 2));
        assertEquals(1.7889624832941877, this.calculator.calculateTurningAngle(layout,2));
    }

    @Test
    void testWindStrength(){
        Wind wind = new Wind(0.0, 100);
        Sail sail1 = new Sail(0,1, true);
        List<Sail> sails = new ArrayList<>();
        sails.add(sail1);
        assertEquals(100, this.calculator.calculateWindStrength(wind, sails));
    }

    @Test
    void collisionWithEntityTest(){
        double antenna = Watch.VISION_SIZE;
        double angle = 0;
        Deck deck = new Deck(3,10);
        List<Obstacle> visibleObstacles = new ArrayList<>();
        Obstacle cp1 = new Obstacle(new Position(2340, 450, Math.toRadians(56)), new Area(shape));
        visibleObstacles.add(cp1);

        assertTrue(this.calculator.collisionWithEntity(antenna, position, angle, deck, visibleObstacles));
    }

    @Test
    void antennaLengthTest() {
        double antenna = -1000;
        double angle = 0;
        Deck deck = new Deck(3, 10);
        List<Obstacle> visibleObstacles = new ArrayList<>();
        Obstacle cp1 = new Obstacle(new Position(2340, 450, Math.toRadians(56)), new Area(shape));
        visibleObstacles.add(cp1);

        assertFalse(this.calculator.collisionWithEntity(antenna, position, angle, deck, visibleObstacles));
    }
}