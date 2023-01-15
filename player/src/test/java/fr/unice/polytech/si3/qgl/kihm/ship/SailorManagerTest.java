package fr.unice.polytech.si3.qgl.kihm.ship;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.actions.Moving;
import fr.unice.polytech.si3.qgl.kihm.actions.Turn;
import fr.unice.polytech.si3.qgl.kihm.equipments.*;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.si3.qgl.kihm.actions.Action.actionTypeEnum.OAR;
import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

class SailorManagerTest {
    Equipment oar1;
    Equipment oar3;
    Equipment rudder;
    List<Equipment> equipement;
    Ship ship;
    SailorManager sm;
    Sailor s1;
    Sailor s2;
    Sailor s3;
    Layout layout;

    @BeforeEach
    void setUp() {
        this.layout = new Layout();
        oar1 = new Oar(0, 0);
        oar3 = new Oar(0, 2);
        rudder = new Rudder(3, 1);
        equipement = List.of(oar1, oar3,
                new Oar(1, 0), new Oar(1, 2),
                new Oar(2, 0), new Oar(2, 2),
                rudder);
        ship = new Ship("Le Royal Kihm", 500, new Position(0, 0),
                new Rectangle(2, 3), new Deck(3, 4),
                equipement);
        s1 = new Sailor(1, "1", new Point(0, 0));
        s2 = new Sailor(2, "2", new Point(0, 2));
        s3 = new Sailor(3, "3", new Point(3, 1));
        sm = new SailorManager(List.of(s1, s2, s3), ship);
    }

    @AfterEach
    void emptyLayout() {
        this.layout = new Layout();
    }

    @Test
    void assignRudder() {
        layout.setNumberOarLeft(0);
        layout.setNumberOarRight(0);
        layout.setAngleRudder(0.5);
        assertEquals(List.of(new Turn(3, 0.5)), sm.assignEquipment(layout));
    }

    @Test
    void assignRudderOccupied() {
        rudder.setOccupied(true);
        layout.setNumberOarLeft(0);
        layout.setNumberOarRight(0);
        layout.setAngleRudder(0.5);
        assertEquals(new ArrayList<>(), sm.assignEquipment(layout));
    }

    @Test
    void assignRudderClosest() {
        layout.setNumberOarLeft(0);
        layout.setNumberOarRight(0);
        layout.setAngleRudder(0.5);
        assertEquals(List.of(new Turn(3, 0.5)), sm.assignEquipment(layout));
    }

    @Test
    void assignNoOneRudder() {
        layout.setNumberOarLeft(0);
        layout.setNumberOarRight(0);
        layout.setAngleRudder(0.0);
        assertEquals(new ArrayList<>(), sm.assignEquipment(layout));
    }

    @Test
    void assignOarLeft() {
        layout.setNumberOarLeft(0);
        layout.setNumberOarRight(1);
        assertEquals(List.of(new Action(2, OAR)), sm.assignEquipment(layout));

        layout.setNumberOarLeft(1);
        layout.setNumberOarRight(0);
        assertEquals(new ArrayList<>(), sm.assignEquipment(layout).stream().filter(action -> action.getSailorId() == 2).toList());
    }

    @Test
    void assignOarRight() {
        layout.setNumberOarLeft(1);
        layout.setNumberOarRight(0);
        assertEquals(List.of(new Action(1, OAR)), sm.assignEquipment(layout).stream().filter(action -> action.getSailorId() == 1).toList());

        layout.setNumberOarLeft(0);
        layout.setNumberOarRight(1);
        assertEquals(new ArrayList<>(), sm.assignEquipment(layout).stream().filter(action -> action.getSailorId() == 1).toList());
    }

    @Test
    void assignOarOccupied() {
        oar1.setOccupied(true);
        layout.setNumberOarLeft(1);
        layout.setNumberOarRight(0);
        assertEquals(new ArrayList<>(), sm.assignEquipment(layout));
    }

    @Test
    void whoUseEquipments() {
        layout.setNumberOarLeft(1);
        layout.setNumberOarRight(1);
        layout.setAngleRudder(0.5);
        assertEquals(List.of(new Action(1, OAR), new Action(2, OAR), new Turn(3, .5)), sm.assignEquipment(layout));
    }

    @Test
    void whoUseEquipmentsAngleMax() {
        layout.setNumberOarLeft(1);
        layout.setNumberOarRight(1);
        layout.setAngleRudder(4.0);
        assertEquals(List.of(new Action(1, OAR), new Action(2, OAR), new Turn(3, PI / 4)), sm.assignEquipment(layout));
    }

    @Test
    void sailorOnRudderNoMove() {
        layout.setNumberOarLeft(1);
        layout.setNumberOarRight(1);
        layout.setAngleRudder(1.0);
        assertEquals(new ArrayList<>(), sm.moveSailors(this.layout));
    }

    @Test
    void sailorOnRudderMove() {
        layout.setNumberOarLeft(1);
        layout.setNumberOarRight(1);
        layout.setAngleRudder(1.0);
        s3.setPosition(new Point(3, 0));
        assertEquals(List.of(new Moving(3, 0, 1)), sm.moveSailors(layout));
    }

    @Test
    void moveToSail() {
        layout.setNumberSail(1);
        List<Equipment> equipments = new ArrayList<>(ship.getEquipments());
        equipments.add(new Sail(2, 1));
        ship.setEquipments(equipments);
        assertEquals(List.of(new Moving(1, 2, 1)), sm.moveSailors(layout));
    }

    @Test
    void moveToWatch() {
        layout.setAssignedToWatch(true);
        List<Equipment> equipments = new ArrayList<>(ship.getEquipments());
        equipments.add(new Watch(2, 1));
        ship.setEquipments(equipments);
        assertEquals(List.of(new Moving(1, 2, 1)), sm.moveSailors(layout));
    }

    @Test
    void dontMoveSailorsTest() {
        layout.setNumberOarLeft(1);
        layout.setNumberOarRight(1);
        this.s3.setPosition(new Point(1, 2));
        assertEquals(new ArrayList<>(), sm.moveSailors(layout));
    }

    @Test
    void maxMovementTest() {
        this.sm.getShip().setDeck(new Deck(5, 10));
        Deck deck = this.ship.getDeck();
        Point before = this.s1.getPosition();
        assertEquals(new Moving(this.s1.getId(), 5, 0), this.sm.move(this.s1, deck.getLength() - 1, deck.getWidth() - 1));
        assertEquals(new Point((int) (before.getX() + 5), (int) before.getY()), this.s1.getPosition());

        before = this.s2.getPosition();
        assertEquals(new Moving(this.s2.getId(), 5, 0), this.sm.move(this.s2, deck.getLength() - 1, deck.getWidth() - 1));
        assertEquals(new Point((int) (before.getX() + 5), (int) before.getY()), this.s2.getPosition());

        before = this.s3.getPosition();
        assertEquals(new Moving(this.s3.getId(), 5, 0), this.sm.move(this.s3, deck.getLength() - 1, deck.getWidth() - 1));
        assertEquals(new Point((int) (before.getX() + 5), (int) before.getY()), this.s3.getPosition());

        Sailor s4 = new Sailor(4, "sailor4", new Point(4, 0));
        before = s4.getPosition();
        assertEquals(new Moving(s4.getId(), -4, 1), this.sm.move(s4, -4, 2));
        assertEquals(new Point((int) (before.getX() - 4), (int) before.getY() + 1), s4.getPosition());
    }

    @Test
    void leftOverSailorsTest() {
        Sailor s4 = new Sailor(4, "4", new Point(1, 2));
        Sailor s5 = new Sailor(5, "5", new Point(2, 3));
        assertEquals(new ArrayList<>(), new SailorManager(List.of(s1, s2, s3, s4, s5), this.ship).moveSailors(this.layout));
    }

    @Test
    void DontMoveToOpenedSailsTest() {
        List<Equipment> equipments = new ArrayList<>();
        Sail sail1 = new Sail(3, 0);
        Sail sail2 = new Sail(3, 2);
        sail1.openSail();
        sail2.openSail();
        equipments.add(sail1);
        equipments.add(sail2);
        equipments.addAll(this.ship.getEquipments());
        this.ship.setEquipments(equipments);
        layout.setNumberSail(2);

        assertEquals(new ArrayList<>(), new SailorManager(List.of(s1, s2, s3), this.ship).moveSailors(layout));
    }

    @Test
    void createCopieInstanceTest() {
        SailorManager sailorManager = new SailorManager(this.sm);
        assertEquals(this.sm, sailorManager);
    }

    @Test
    void setShipTest() {
        SailorManager sailorManagerNullShip = this.sm;
        assertNotNull(sailorManagerNullShip.getShip());
        sailorManagerNullShip.setShip(null);
        assertNull(sailorManagerNullShip.getShip());
    }

    @Test
    void testEquals() {
        assertNotEquals(new SailorManager(null, null), this.sm);
        assertEquals(new SailorManager(List.of(s1, s2, s3), this.ship), this.sm);
        assertEquals(this.sm, this.sm);
        assertNotEquals(new SailorManager(List.of(s1, s3), this.ship), this.sm);
    }

    @Test
    void testHashCode() {
        assertNotEquals(new Position().hashCode(), this.sm.hashCode());
        assertEquals(new SailorManager(List.of(s1, s2, s3), this.ship).hashCode(), this.sm.hashCode());
        assertEquals(this.sm.hashCode(), this.sm.hashCode());
        assertNotEquals(new SailorManager(List.of(s1, s3), this.ship).hashCode(), this.sm.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("{\"sailors\": " + List.of(s1, s2, s3) + ", \"ship\": " + this.ship + '}', this.sm.toString());
    }
}