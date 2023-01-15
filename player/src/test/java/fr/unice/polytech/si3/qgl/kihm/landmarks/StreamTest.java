package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.STREAM;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamTest {

    Stream ship;
    Position position;
    Shape shape;
    double strength;

    @BeforeEach
    void setUp() {
        this.position = new Position(40, 65, 1);
        this.shape = new Rectangle(0, 0, 5, 5);
        this.strength = 483.456;
        this.ship = new Stream(this.position, this.shape, this.strength);
    }

    @Test
    void testType() {
        assertEquals(STREAM, this.ship.getType());
    }

    @Test
    void testStrength() {
        assertEquals(this.strength, this.ship.getStrength());
    }

    @Test
    void testToString() {
        assertEquals("{\"type\": \"stream\", \"position\": {\"x\": 40.0, \"y\": 65.0, \"orientation\": 1.0}, \"shape\": {\"type\":\"polygon\",\"vertices\":[{\"x\":-40.0, \"y\":-65.0},{\"x\":-35.0, \"y\":-65.0},{\"x\":-35.0, \"y\":-60.0},{\"x\":-40.0, \"y\":-60.0},{\"x\":-40.0, \"y\":-65.0}],\"orientation\":-1.0}}", this.ship.toString());
    }
}