package fr.unice.polytech.si3.qgl.kihm;

import fr.unice.polytech.si3.qgl.kihm.equipments.Oar;
import fr.unice.polytech.si3.qgl.kihm.equipments.Rudder;
import fr.unice.polytech.si3.qgl.kihm.equipments.Sail;
import fr.unice.polytech.si3.qgl.kihm.equipments.Watch;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Checkpoint;
import fr.unice.polytech.si3.qgl.kihm.ship.Deck;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.structures.Game;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CockpitTest {
    Game g;
    Cockpit cockpit;

    @BeforeEach
    void setUp() {
        g = new Game();
        this.cockpit = new Cockpit();
    }

    @Test
    void initGameEmpty() {
        cockpit.initGame("");
        assertEquals(g, cockpit.getGame());
    }

    @Test
    void initGameWithGame() {
        cockpit.initGame("");
        assertEquals(new Cockpit(this.g), cockpit);
        assertNotEquals(null, cockpit);
    }

    @Test
    void initWrongGame() {
        cockpit.initGame("{\"Test\":18}");
        assertEquals(g, cockpit.getGame());
    }

    @Test
    void goalBattleCheckpoint() {
        cockpit.initGame("{\"goal\": {\"mode\": \"BATTLE\",\"checkpoints\":[{\"position\":{\"x\": 1000,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}}]}}");
        assertEquals(new ArrayList<>(), cockpit.getGame().getWorld().getCheckpoints());
    }

    @Test
    void goalBattleCheckpointOrientation() {
        cockpit.initGame("{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\":[{\"position\":{\"x\": 1000,\"y\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 35}}]}}");
        assertEquals(List.of(new Checkpoint(new Position(1000, 0, 0), new Ellipse2D.Double(965, -35, 70, 70))), cockpit.getGame().getWorld().getCheckpoints());
    }

    @Test
    void goalBattleCheckpointX() {
        cockpit.initGame("{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\":[{\"position\":{\"y\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 35}}]}}");
        assertEquals(List.of(new Checkpoint(new Position(0, 0, 0), new Ellipse2D.Double(-35, -35, 70, 70))), cockpit.getGame().getWorld().getCheckpoints());
    }

    @Test
    void goalBattleCheckpointEmpty() {
        cockpit.initGame("{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\":[{\"position\":{},\"shape\": {\"type\": \"circle\",\"radius\": 35}}]}}");
        assertEquals(List.of(new Checkpoint(new Position(0, 0, 0), new Ellipse2D.Double(-35, -35, 70, 70))), cockpit.getGame().getWorld().getCheckpoints());
    }

    @Test
    void goalBattleCheckpointZ() {
        cockpit.initGame("{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\":[{\"position\":{\"z\" : 12 },\"shape\": {\"type\": \"circle\",\"radius\": 35}}]}}");
        assertNull(cockpit.getGame().getGoalName());
    }

    @Test
    void shipCountFloat() {
        cockpit.initGame("{\"shipCount\": 1.6}");
        assertEquals(1, cockpit.getGame().getShipCount());
    }

    @Test
    void shipCountError() {
        cockpit.initGame("{\"shipCount\": 1,2}");
        assertEquals(0, cockpit.getGame().getShipCount());
    }

    @Test
    void shipCountErrorEmpty() {
        cockpit.initGame("{\"shipCount\": }");
        assertEquals(0, cockpit.getGame().getShipCount());
    }

    @Test
    void sailorsName() {
        cockpit.initGame("{\"sailors\": [{\"x\": 1,\"y\": 1,\"id\": 4,\"name\": 3}]}");
        assertEquals(List.of(new Sailor(4, "3", new Point(1, 1))), cockpit.getGame().getSailors());
    }

    @Test
    void sailorsEmpty() {
        cockpit.initGame("{\"sailors\": [{\"x\": 1,\"y\": 1,\"id\": 4}]}");
        assertEquals(List.of(new Sailor(4, "Default", new Point(1, 1))), cockpit.getGame().getSailors());
    }

    @Test
    void sailorsMoreFields() {
        cockpit.initGame("{\"sailors\": [{\"x\": 1,\"y\": 1,\"id\": 4,\"z\":3}]}");
        assertEquals(List.of(new Sailor(4, "Default", new Point(1, 1))), cockpit.getGame().getSailors());
    }

    @Test
    void sailorsPosition() {
        cockpit.initGame("{\"sailors\": [{\"x\": 1.2,\"id\": -2,\"z\":3}]}");
        assertEquals(List.of(new Sailor(-2, "Default", new Point(1, 0))), cockpit.getGame().getSailors());
    }

    @Test
    void nextRoundEmpty() {
        cockpit.initGame("{}");
        assertEquals("[]", this.cockpit.nextRound("{}"));
    }

    @Test
    void shipNoDeck() {
        cockpit.initGame("{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"shape\": {\"type\": \"circle\",\"radius\": 20,\"orientation\": 0}}}");
        assertEquals(new Ship("Les copaings d'abord!", 100, new Position(0, 0, 0), new Ellipse2D.Double(-20, -20, 40, 40), new Deck(0, 0), new ArrayList<>()), cockpit.getGame().getShip());
    }

    @Test
    void shipDeckX() {
        cockpit.initGame("{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 3.2},\"entities\": [],\"shape\": {\"type\": \"circle\",\"radius\": 20,\"orientation\": 0}}}");
        assertEquals(new Ship("Les copaings d'abord!", 100, new Position(0, 0, 0), new Ellipse2D.Double(-20, -20, 40, 40), new Deck(3, 0), new ArrayList<>()), cockpit.getGame().getShip());
    }

    @Test
    void shipDeckZ() {
        cockpit.initGame("{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 3.2, \"length\":2,\"z\":25},\"entities\": [],\"shape\": {\"type\": \"circle\",\"radius\": 20,\"orientation\": 0}}}");
        assertEquals(new Ship("Les copaings d'abord!", 100, new Position(0, 0, 0), new Ellipse2D.Double(-20, -20, 40, 40), new Deck(3, 2), new ArrayList<>()), cockpit.getGame().getShip());
    }

    @Test
    void shipNoEntities() {
        cockpit.initGame("{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 3.2, \"length\":2,\"z\":25},\"shape\": {\"type\": \"circle\",\"radius\": 20,\"orientation\": 0}}}");
        assertEquals(new Ship("Les copaings d'abord!", 100, new Position(0, 0, 0), new Ellipse2D.Double(-20, -20, 40, 40), new Deck(3, 2), new ArrayList<>()), cockpit.getGame().getShip());
    }

    @Test
    void shipWrongEntities() {
        cockpit.initGame("{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"entities\":[{\"x\":0, \"y\":0, \"type\":\"rame\"}, {\"x\":0, \"y\":1, \"type\":\"oar\"}],\"deck\": {\"width\": 3.2, \"length\":2,\"z\":25},\"shape\": {\"type\": \"circle\",\"radius\": 20,\"orientation\": 0}}}");
        assertEquals(new Ship("Les copaings d'abord!", 100, new Position(0, 0, 0), new Ellipse2D.Double(-20, -20, 40, 40), new Deck(3, 2), List.of(new Oar(0, 1))), cockpit.getGame().getShip());
    }

    @Test
    void shipRudder() {
        cockpit.initGame("{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"entities\":[{\"x\":0, \"y\":1, \"type\":\"rudder\"}],\"deck\": {\"width\": 3.2, \"length\":2,\"z\":25},\"shape\": {\"type\": \"circle\",\"radius\": 20,\"orientation\": 0}}}");
        assertEquals(new Ship("Les copaings d'abord!", 100, new Position(0, 0, 0), new Ellipse2D.Double(-20, -20, 40, 40), new Deck(3, 2), List.of(new Rudder(0, 1))), cockpit.getGame().getShip());
    }

    @Test
    void shipSail() {
        cockpit.initGame("{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"entities\":[{\"x\":0, \"y\":1, \"type\":\"sail\"}],\"deck\": {\"width\": 3.2, \"length\":2,\"z\":25},\"shape\": {\"type\": \"circle\",\"radius\": 20,\"orientation\": 0}}}");
        assertEquals(new Ship("Les copaings d'abord!", 100, new Position(0, 0, 0), new Ellipse2D.Double(-20, -20, 40, 40), new Deck(3, 2), List.of(new Sail(0, 1))), cockpit.getGame().getShip());
    }

    @Test
    void shipSailOpened() {
        cockpit.initGame("{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"entities\":[{\"x\":0, \"y\":1, \"type\":\"sail\"}],\"deck\": {\"width\": 3.2, \"length\":2,\"z\":25},\"shape\": {\"type\": \"circle\",\"radius\": 20,\"orientation\": 0}}}");
        assertNotEquals(new Ship("Les copaings d'abord!", 100, new Position(0, 0, 0), new Ellipse2D.Double(-20, -20, 40, 40), new Deck(3, 2), List.of(new Sail(0, 1, true))), cockpit.getGame().getShip());
    }


    @Test
    void shipCanon() {
        cockpit.initGame("{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"entities\":[{\"x\":0, \"y\":1, \"type\":\"watch\"}],\"deck\": {\"width\": 3.2, \"length\":2,\"z\":25},\"shape\": {\"type\": \"circle\",\"radius\": 20,\"orientation\": 0}}}");
        Ship s = new Ship("Les copaings d'abord!", 100, new Position(0, 0, 0), new Ellipse2D.Double(-20, -20, 40, 40), new Deck(3, 2), List.of(new Watch(0, 1)));
        assertEquals(s, cockpit.getGame().getShip());
    }


    @Test
    void nextRound() {
        cockpit.initGame("{\"goal\":{\"mode\":\"REGATTA\", \"checkpoints\":[{\"position\":{\"x\":-6, \"y\":0, \"orientation\":0}, \"shape\":{\"type\":\"circle\", \"radius\":4}}]}, \"ship\":{\"type\":\"ship\", \"life\":100, \"position\":{\"x\":0, \"y\":0, \"orientation\":0}, \"name\":\"Lescopaingsd'abord!\", \"deck\":{\"width\":4, \"length\":2}, \"entities\":[{\"type\":\"oar\", \"x\":0, \"y\":0}, {\"type\":\"oar\", \"x\":1, \"y\":1}], \"shape\":{\"type\":\"polygon\", \"vertices\":[{\"x\":3, \"y\":1}, {\"x\":2, \"y\":1}, {\"x\":2, \"y\":2}], \"orientation\":0}}, \"sailors\":[{\"x\":0, \"y\":0, \"id\":0, \"name\":\"EdwardTeach\"}, {\"x\":0, \"y\":0, \"id\":1, \"name\":\"EdwardPouce\"}, {\"x\":1, \"y\":1, \"id\":2, \"name\":\"TomPouce\"}, {\"x\":1, \"y\":1, \"id\":3, \"name\":\"JackTeach\"}], \"shipCount\":1}");
        String s = cockpit.nextRound("{\"ship\":{\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": -200,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 3,\"length\": 4},\"entities\": [{\"type\": \"oar\",\"x\": 2,\"y\": 0},{\"type\": \"oar\",\"x\": 1,\"y\": 2}],\"shape\": {\"type\": \"rectangle\",\"width\": 3,\"height\": 6,\"orientation\": 0}}}");
        assertEquals("[{\"sailorId\":0,\"type\":\"MOVING\",\"xdistance\":2,\"ydistance\":0},{\"sailorId\":1,\"type\":\"MOVING\",\"xdistance\":1,\"ydistance\":2},{\"sailorId\":0,\"type\":\"OAR\"},{\"sailorId\":1,\"type\":\"OAR\"}]", s);
    }

    @Test
    void testLogs() {
        assertNotEquals(null, this.cockpit.getLogs());
        assertEquals(new ArrayList<>(), this.cockpit.getLogs());
    }

    @Test
    void compareNull() {
        assertNotEquals(null, new Cockpit());
    }

    @Test
    void hashCodeTest() {
        assertNotEquals(0, new Cockpit().hashCode());
        assertEquals(this.cockpit.hashCode(), this.cockpit.hashCode());
        assertNotEquals(new Cockpit().hashCode(), this.cockpit.hashCode());
    }
}
