package fr.unice.polytech.si3.qgl.kihm.ship;

import fr.unice.polytech.si3.qgl.kihm.equipments.Equipment;
import fr.unice.polytech.si3.qgl.kihm.equipments.Oar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SailorTest {

    Sailor sailorNull;
    Sailor sailorDefault;
    Sailor sailorPosition;
    Equipment equipment;


    @BeforeEach
    void setUp() {
        this.sailorNull = new Sailor();
        this.sailorDefault = new Sailor(1, "Bob");
        this.sailorPosition = new Sailor(2, "Robert", new Point(1, 1));

        this.equipment = new Oar(0, 0);
    }

    @Test
    void testPositionSailor() {
        assertEquals(new Point(0, 0), sailorNull.getPosition());
        assertEquals(new Point(0, 0), sailorDefault.getPosition());
        assertEquals(new Point(1, 1), sailorPosition.getPosition());
    }

    @Test
    void testMoveTo() {
        assertArrayEquals(new int[]{0, 0}, sailorDefault.moveTo(equipment));
        assertArrayEquals(new int[]{-1, -1}, sailorPosition.moveTo(equipment));
    }

    @Test
    void testNotMoveTo() {
        assertFalse(Arrays.equals(new int[]{0, 0}, sailorPosition.moveTo(equipment)));
    }

    @Test
    void compareNull() {
        assertNotEquals(null, new Sailor(1, "new"));
    }

    @Test
    void compareId() {
        assertNotEquals(new Sailor(1, "run"), new Sailor(1, "new"));
    }

    @Test
    void compareName() {
        assertNotEquals(sailorDefault, new Sailor(1, "Jack"));
        assertNotEquals("Jack", sailorDefault.getName());
    }

    @Test
    void comparePosition() {
        assertNotEquals(sailorDefault, new Sailor(1, "Bob", new Point(0, 1)));
    }

    @Test
    void createCopieInstanceTest() {
        Sailor sailorCopie = new Sailor(this.sailorPosition);
        assertEquals(this.sailorPosition, sailorCopie);
    }

    @Test
    void compareEquals() {
        assertEquals(sailorDefault, new Sailor(1, "Bob", new Point(0, 0)));
    }

    @Test
    void testHashCode() {
        assertEquals(this.sailorPosition.hashCode(), this.sailorPosition.hashCode());
        assertNotEquals(this.sailorPosition.hashCode(), this.sailorDefault.hashCode());
        assertNotEquals(new Sailor().hashCode(), this.sailorDefault.hashCode());
        assertNotEquals(0, this.sailorDefault.hashCode());
        assertNotEquals(0, this.sailorPosition.hashCode());
    }
}
