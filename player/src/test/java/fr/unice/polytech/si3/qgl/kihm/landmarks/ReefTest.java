package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.REEF;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReefTest {

    Obstacle reef;
    Position position;
    Shape shape;

    @BeforeEach
    void setUp() {
        this.position = new Position(40, 65, 1);
        this.shape = new Rectangle(0, 0, 5, 5);
        this.reef = new Obstacle(REEF, this.position, this.shape);
    }

    @Test
    void testType() {
        assertEquals(REEF, reef.getType());
    }

    @Test
    void testToString() {
        assertEquals("{\"type\": \"" + Obstacle.obstacleTypeEnum.REEF.toString().toLowerCase() + "\", \"position\": " + this.position + ", \"shape\": " + "{\"type\":\"polygon\",\"vertices\":[{\"x\":-40.0, \"y\":-65.0},{\"x\":-35.0, \"y\":-65.0},{\"x\":-35.0, \"y\":-60.0},{\"x\":-40.0, \"y\":-60.0},{\"x\":-40.0, \"y\":-65.0}],\"orientation\":-1.0}" + "}", this.reef.toString());
    }
}