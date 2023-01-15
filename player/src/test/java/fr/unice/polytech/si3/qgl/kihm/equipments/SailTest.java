package fr.unice.polytech.si3.qgl.kihm.equipments;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class SailTest {

    Sail sail;
    Point position;
    Layout layout = new Layout();
    Sailor sailor = new Sailor(1, "Test", new Point());

    @BeforeEach
    void setUp() {
        this.position = new Point(6, 2);
        this.sail = new Sail(this.position.x, this.position.y);
    }

    @Test
    void testOpenAndCloseSail() {
        this.sail.closeSail();
        assertFalse(this.sail.isOpened());
        this.sail.openSail();
        this.sail.closeSail();
        this.sail.closeSail();
        this.sail.openSail();
        assertTrue(this.sail.isOpened());
        this.sail.closeSail();
    }

    @Test
    void actionNullTest() {
        assertNull(this.sail.action(layout, sailor));
    }

    @Test
    void actionOpenTest() {
        layout.setNumberSail(1);
        assertEquals(new Action(1, Action.actionTypeEnum.LIFT_SAIL), this.sail.action(layout, sailor));
    }

    @Test
    void actionCloseTest() {
        this.sail.openSail();
        layout.setNumberSail(0);
        assertEquals(new Action(1, Action.actionTypeEnum.LOWER_SAIL), this.sail.action(layout, sailor));
    }

    @Test
    void testEquals() {
        assertEquals(this.sail, this.sail);
        assertEquals(new Sail(this.sail.getX(), this.sail.getY()), this.sail);
        assertNotEquals(new Sail(5, 7), this.sail);
        assertNotEquals(null, this.sail);
    }

    @Test
    void testHashCode() {
        assertEquals(this.sail.hashCode(), this.sail.hashCode());
        assertEquals(new Sail(this.sail.getX(), this.sail.getY()).hashCode(), this.sail.hashCode());
        assertNotEquals(new Sail(9, 2).hashCode(), this.sail.hashCode());
        assertNotEquals(0, this.sail.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("{\"type\": \"" + Equipment.equipmentTypeEnum.SAIL.toString().toLowerCase() + "\", \"x\": " + this.position.x + ", \"y\": " + this.position.y + ", \"opened\": " + this.sail.isOpened() + "}", this.sail.toString());
    }
}