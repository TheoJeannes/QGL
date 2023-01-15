package fr.unice.polytech.si3.qgl.kihm.equipments;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.actions.Turn;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentTest {
    Oar o, of;
    Rudder r;
    Sail s;
    Watch w;
    Layout layout = new Layout();
    Sailor sailor = new Sailor(1, "Test", new Point());

    @BeforeEach
    void setUp() {
        o = new Oar(0, 0);
        of = new Oar(0, 0);
        of.setOccupied(true);
        r = new Rudder(0, 0);
        s = new Sail(0, 0);
        w = new Watch(0, 0);
    }

    @Test
    void compareOarOccupied() {
        assertNotEquals(o, of);
    }

    @Test
    void compareNull() {
        assertNotEquals(null, o);
    }

    @Test
    void compareRudderOccupied() {
        Rudder rf = new Rudder(0, 0);
        rf.setOccupied(true);
        assertNotEquals(r, rf);
    }

    @Test
    void compareOar() {
        of.setOccupied(false);
        assertEquals(o, of);
    }

    @Test
    void compareSail() {
        Sail so = new Sail(0, 0, true);
        assertNotEquals(s, so);
    }

    @Test
    void compareSailPosition() {
        Sail so = new Sail(1, 0);
        assertNotEquals(s, so);
    }

    @Test
    void compareSailOar() {
        Equipment e1 = s;
        Equipment e2 = o;
        assertNotEquals(e1, e2);
    }

    @Test
    void getterX() {
        assertEquals(o.getX(), o.getPosition().getX());
    }

    @Test
    void getterY() {
        assertEquals(o.getY(), o.getPosition().getY());
    }

    @Test
    void actionNullTest() {
        assertNull(this.o.action(layout, sailor));
        assertNull(this.r.action(layout, sailor));
        assertNull(this.s.action(layout, sailor));
        assertNull(this.w.action(layout, sailor));
    }

    @Test
    void actionUseRudderTest() {
        layout.setAngleRudder(1.0);
        assertEquals(new Turn(1, 1), this.r.action(layout, sailor));
    }

    @Test
    void actionNoUseRudderTest() {
        layout.setNumberSail(0);
        assertNull(this.r.action(layout, sailor));
    }

    @Test
    void actionUseWatchTest() {
        layout.setAssignedToWatch(true);
        assertEquals(new Action(1, Action.actionTypeEnum.USE_WATCH), this.w.action(layout, sailor));
        assertFalse(layout.isAssignedToWatch());
    }

    @Test
    void testHashCode() {
        assertNotEquals(0, this.o.hashCode());
        assertNotEquals(this.of.hashCode(), this.o.hashCode());
        assertNotEquals(this.r.hashCode(), this.o.hashCode());
        assertNotEquals(new Rudder(4, 6).hashCode(), new Oar(4, 6).hashCode());
    }

    @Test
    void testType() {
        assertEquals(Equipment.equipmentTypeEnum.OAR, this.o.getType());
        assertEquals(Equipment.equipmentTypeEnum.RUDDER, this.r.getType());
    }

    @Test
    void testToString() {
        assertEquals("{\"type\": \"" + Equipment.equipmentTypeEnum.OAR.toString().toLowerCase() + "\", \"x\": " + this.o.getPosition().x + ", \"y\": " + this.o.getPosition().y + "}", this.o.toString());
    }
}