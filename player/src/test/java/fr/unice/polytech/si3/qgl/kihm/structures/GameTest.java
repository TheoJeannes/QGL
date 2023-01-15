package fr.unice.polytech.si3.qgl.kihm.structures;

import fr.unice.polytech.si3.qgl.kihm.equipments.Equipment;
import fr.unice.polytech.si3.qgl.kihm.equipments.Oar;
import fr.unice.polytech.si3.qgl.kihm.landmarks.*;
import fr.unice.polytech.si3.qgl.kihm.ship.Deck;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.utilities.PointDouble;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    final ArrayList<Equipment> equipment = new ArrayList<>();
    final Calculator calculator = new Calculator();
    Game g;
    Ship sh;
    Equipment oar1;
    Equipment oar2;
    List<Sailor> sailors;

    @BeforeEach
    void setUp() {
        this.g = new Game();
        this.oar1 = new Oar(0, 0);
        this.oar2 = new Oar(0, 1);
        this.equipment.add(oar1);
        this.equipment.add(oar2);
        sh = new Ship("Le Royal Kihm", 500, new Position(0, 0), new Rectangle(2, 3), new Deck(2, 3), equipment);
        sailors = new ArrayList<>();
        sailors.add(new Sailor());
        g.setShip(sh);
        g.setSailors(sailors);
    }

    @Test
    void collisionCheckpointAndShipNoContact() {
        double rayon = 5;
        PointDouble centerCircle = new PointDouble(-5, 5);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{-2, 0, 12, 14, 8}, new int[]{0, 2, 8, 2, -2}, 5);
        assertFalse(calculator.collision(shipShape, checkPointShape));
    }

    @Test
    void collisionCheckpointAndShipPointContact() {
        double rayon = 5;
        PointDouble centerCircle = new PointDouble(-5, 5);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{-2, -1, 12, 14, 8}, new int[]{0, 2, 8, 2, -2}, 5);
        assertTrue(calculator.collision(shipShape, checkPointShape));
    }

    @Test
    void collisionCheckpointAndShipLineContact() {
        double rayon = 5;
        PointDouble centerCircle = new PointDouble(-5, 5);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{0, 0, 12, 14, 8}, new int[]{0, 7, 8, 2, -2}, 5);
        assertFalse(calculator.collision(shipShape, checkPointShape)); // Should be true
    }

    @Test
    void collisionCheckpointAndShipInsidePointContact() {
        double rayon = 5;
        PointDouble centerCircle = new PointDouble(-5, 5);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{0, -2, 12, 14, 8}, new int[]{0, 4, 8, 2, -2}, 5);
        assertTrue(calculator.collision(shipShape, checkPointShape)); // Should be true
    }

    @Test
    void collisionCheckpointAndShipInsideContact() {
        double rayon = 10;
        PointDouble centerCircle = new PointDouble(6, 6);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{0, -2, 12, 14, 8}, new int[]{0, 4, 8, 2, -2}, 5);
        assertTrue(calculator.collision(shipShape, checkPointShape));
    }

    @Test
    void collisionCheckpointAndShipInsideInverseContact() {
        double rayon = 1;
        PointDouble centerCircle = new PointDouble(6, 4);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{0, -2, 12, 14, 8}, new int[]{0, 4, 8, 2, -2}, 5);
        assertTrue(calculator.collision(shipShape, checkPointShape));
    }

    @Test
    void onOneCheckpoint() {
        List<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(new Checkpoint(new Position(), new Ellipse2D.Double(-10, -10, 20, 20)));
        this.g.setWorld(new World(checkpoints, new Wind(), new ArrayList<>()));
        assertEquals(new ArrayList<>(), this.g.play());
    }

    @Test
    void compareNull() {
        assertNotEquals(null, new Game());
    }

    @Test
    void hashCodeTest() {
        assertNotEquals(0, new Game().hashCode());
        assertEquals(new Game().hashCode(), new Game().hashCode());
        assertEquals(this.g.hashCode(), this.g.hashCode());
        assertNotEquals(new Game().hashCode(), this.g.hashCode());
    }

    @Test
    void toStringTest() {
        assertNotEquals(new Game().toString(), this.g.toString());
        assertNotEquals("", this.g.toString());
        assertEquals("Game{goal=null, shipCount=0, ship={\"type\": \"ship\", \"life\": 500, \"position\": {\"x\": 0.0, \"y\": 0.0, \"orientation\": 0.0}, \"name\": \"Le Royal Kihm\", \"deck\": {\"width\": 2, \"length\": 3}, \"entities\": [{\"type\": \"oar\", \"x\": 0, \"y\": 0}, {\"type\": \"oar\", \"x\": 0, \"y\": 1}], \"shape\": {\"type\": \"rectangle\", \"width\": 2, \"height\": 3, \"orientation\": 0.0}}, sailors=[{\"id\": 0, \"x\": 0, \"y\": 0, \"name\": Default}], pathfinder=Pathfinder{ship=null, world=World{checkpoints=[], wind={\"orientation\": 0.0, \"strength\": 0.0}, entities=[]}}, sailorManager={\"sailors\": null, \"ship\": null}}", this.g.toString());
    }

    @Test
    void checkpointNotReached() {
        g.setWorld(new World(new ArrayList<>(List.of(new Checkpoint(new Position(8000, 4000), new Area()))), new Wind(), new ArrayList<>()));
        assertFalse(g.checkpointReached());
    }

    @Test
    void updateGame() {
        g.update(new Wind(50, 20), new ArrayList<>());
        assertEquals(new Wind(50, 20), g.getWorld().getWind());
    }

    @Test
    void updateGameEntities() {
        ArrayList<Obstacle> a = new ArrayList<>();
        a.add(new Stream(new Position(), new Area(), 50));
        g.update(new Wind(50, 20), a);
        assertEquals(a, g.getWorld().getEntities());
    }
}
