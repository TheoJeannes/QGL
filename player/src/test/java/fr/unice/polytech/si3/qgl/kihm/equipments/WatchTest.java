package fr.unice.polytech.si3.qgl.kihm.equipments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WatchTest {

    Watch watch;
    Point position;

    @BeforeEach
    void setUp() {
        this.position = new Point(4, 6);
        this.watch = new Watch(this.position.x, this.position.y);
    }

    @Test
    void testType() {
        assertEquals(Equipment.equipmentTypeEnum.WATCH, this.watch.getType());
    }

    @Test
    void testToString() {
        assertEquals("{\"type\": \"" + Equipment.equipmentTypeEnum.WATCH.toString().toLowerCase() + "\", \"x\": " + this.position.x + ", \"y\": " + this.position.y + "}", this.watch.toString());
    }
}