package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Area;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.REEF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class ShapeFunctionsTest {
    private JsonNode circleNodeT, circleNodeDouble, rectangle, carreNode, carre45Node, triangleNode, triangle180Node, circleNode, circleNodeAngle, triangleNodeAngleD;
    private ShapeFunctions sf;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        sf = new ShapeFunctions();
        rectangle = om.readTree("{\"type\":\"rectangle\"," + "\"height\":2,\"width\":1,\"orientation\":0}");
        carreNode = om.readTree("{\"type\":\"rectangle\"," + "\"height\":10,\"width\":12,\"orientation\":0}");
        carre45Node = om.readTree("{\"type\":\"rectangle\"," + "\"height\":10,\"width\":12,\"orientation\":" + Math.PI / 4 + "}");
        triangleNode = om.readTree("{\"type\":\"polygon\",\"vertices\":" + "[{\"x\":" + -1 + ",\"y\":" + -1 + "}," + "{\"x\":" + -1 + ",\"y\":" + 1 + "}," + "{\"x\":" + 3 + ",\"y\":" + 0 + "}]," + "\"orientation\":" + 0 + "}");
        triangle180Node = om.readTree("{\"type\":\"polygon\",\"vertices\":" + "[{\"x\":" + -1 + ",\"y\":" + -1 + "}," + "{\"x\":" + -1 + ",\"y\":" + 1 + "}," + "{\"x\":" + 3 + ",\"y\":" + 0 + "}]," + "\"orientation\":" + Math.PI + "}");

        triangleNodeAngleD = om.readTree("{\"type\":\"polygon\",\"vertices\":" + "[{\"x\":" + -1 + ",\"y\":" + -1 + "}," + "{\"x\":" + -1 + ",\"y\":" + 1 + "}," + "{\"x\":" + 3 + ",\"y\":" + 0 + "}]," + "\"orientation\":" + Math.PI / 2 + "}");
        circleNode = om.readTree("{\"type\":\"circle\",\"radius\":" + 10 + ",\"orientation\":" + 0 + "}");
        circleNodeDouble = om.readTree("{\"type\":\"circle\",\"radius\":" + 20 + ",\"orientation\":" + 0 + "}");
        circleNodeT = om.readTree("{\"type\":\"circle\",\"radius\":" + 13 + ",\"orientation\":" + 0 + "}");
        circleNodeAngle = om.readTree("{\"type\":\"circle\",\"radius\":" + 10 + ",\"orientation\":" + 2.15 + "}");
    }

    @Test
    void rectangleToShape() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(), carreNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals("5.0,6.0\n5.0,-6.0\n-5.0,-6.0\n-5.0,6.0", sf.shapeToString(s));
        }
    }

    @Test
    void rectangleAngleToShape() {
        try {
            Shape s = sf.nodeToShape(new Position(), carre45Node);
            assertEquals("-0.70711,7.77821\n7.77821,-0.70711\n0.70711,-7.77821\n-7.77821,0.70711", sf.shapeToString(s));
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    void circleToShape() {
        try {
            assertEquals("10.0,10.0\nRadius :10.0", sf.shapeToString(sf.nodeToShape(new Position(10, 10, 0), circleNode)));
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    void circleToShapeAngle() {
        try {
            assertEquals("0.0,0.0\nRadius :10.0", sf.shapeToString(sf.nodeToShape(new Position(0, 0, 1.18), circleNodeAngle)));
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    void triangleToNode() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(), triangleNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals("-1.0,-1.0\n-1.0,1.0\n3.0,0.0", sf.shapeToString(s));
        }
    }

    @Test
    void triangleAngleToNode() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(), triangle180Node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals("1.0,1.0\n1.0,-1.0\n-3.0,0.0", sf.shapeToString(s));
        }
    }

    @Test
    void triangleToNodeAngle() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(0, 0, Math.PI), triangleNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals("1.0,1.0\n1.0,-1.0\n-3.0,0.0", sf.shapeToString(s));
        }
    }

    @Test
    void triangleToNodeAngleCumul() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(0, 0, Math.PI / 2), triangleNodeAngleD);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals("1.0,1.0\n1.0,-1.0\n-3.0,0.0", sf.shapeToString(s));
        }
    }

    @Test
    void areaShape() {
        Shape s = new Area();
        assertEquals("", sf.shapeToString(s));
    }

    @Test
    void scaleObstacleRectangle() {
        try {
            Obstacle o = new Obstacle(REEF, new Position(3, 3), sf.nodeToShape(new Position(3, 3), rectangle));
            assertEquals("5.0,4.0\n5.0,2.0\n1.0,2.0\n1.0,4.0", sf.shapeToString(sf.scaleObstacle(o, 2)));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void scaleObstacleCircle() {
        try {
            Obstacle o = new Obstacle(REEF, new Position(10, 10), sf.nodeToShape(new Position(10, 10), circleNode));
            assertEquals(sf.shapeToString(sf.nodeToShape(new Position(10, 10), circleNodeDouble)), sf.shapeToString(sf.scaleObstacle(o, 2)));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void scaleObstacleCircleNegatif() {
        try {
            Position p = new Position(-3, 6);
            Obstacle o = new Obstacle(REEF, p, sf.nodeToShape(p, circleNode));
            assertEquals(sf.shapeToString(sf.nodeToShape(p, circleNodeT)), sf.shapeToString(sf.scaleObstacle(o, 1.3)));
        } catch (Exception e) {
            fail();
        }
    }
}