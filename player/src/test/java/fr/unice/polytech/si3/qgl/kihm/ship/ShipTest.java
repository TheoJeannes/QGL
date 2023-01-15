package fr.unice.polytech.si3.qgl.kihm.ship;

import fr.unice.polytech.si3.qgl.kihm.equipments.Equipment;
import fr.unice.polytech.si3.qgl.kihm.equipments.Oar;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ShipTest {

    final ArrayList<Equipment> equipment = new ArrayList<>();
    Ship ship;
    Ship shipCopy;
    Equipment oar1;
    Equipment oar2;

    @BeforeEach
    void setUp() {
        this.oar1 = new Oar(0, 0);
        this.oar2 = new Oar(0, 1);
        this.equipment.add(oar1);
        this.equipment.add(oar2);
        this.ship = new Ship("Le Royal Kihm", 500, new Position(0, 0), new Rectangle(2, 3), new Deck(2, 3), equipment);
        this.shipCopy = new Ship("Le Royal Kihm", 500, new Position(0, 0), new Rectangle(2, 3), new Deck(2, 3), equipment);

    }

    @Test
    void testNbOarTotal() {
        assertEquals(2, ship.getOarRight().size() + ship.getOarLeft().size());
    }

    @Test
    void compareNull() {
        assertNotEquals(null, ship);
    }

    @Test
    void compare() {
        assertEquals(ship, shipCopy);
    }

    @Test
    void compareShipLife() {
        shipCopy.setLife(0);
        assertNotEquals(shipCopy, ship);
    }

    @Test
    void compareShipName() {
        shipCopy.setName("run");
        assertNotEquals(shipCopy, ship);
    }

    @Test
    void compareShipShape() {
        shipCopy.setShape(new Ellipse2D.Double(0, 0, 12, 12));
        assertNotEquals(shipCopy, ship);
    }

    @Test
    void compareShipPosition() {
        shipCopy.setPosition(new Position(0, 12));
        assertNotEquals(shipCopy, ship);
        shipCopy.setPosition(new Position(12, 0));
        assertNotEquals(shipCopy, ship);
    }

    @Test
    void compareDeckDimension() {
        shipCopy.setDeck(new Deck(3, 3));
        assertNotEquals(shipCopy, ship);
    }

    @Test
    void testGetters() {
        assertEquals(1, this.ship.getOarLeft().size());
        assertEquals(1, this.ship.getOarRight().size());
        assertEquals(0, this.ship.getRudders().size());
        assertEquals(0, this.ship.getSails().size());
        assertEquals(0, this.ship.getWatches().size());
        assertEquals("Le Royal Kihm", this.ship.getName());
        assertEquals(500, this.ship.getLife());
        assertEquals(new Deck(2, 3), this.ship.getDeck());
        assertEquals(new Rectangle(0, 0, 2, 3), this.ship.getShape());
    }

    @Test
    void createCopieInstanceTest() {
        Ship shipCopie = new Ship(this.ship);
        assertEquals(this.ship, shipCopie);
    }
}
