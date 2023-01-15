package fr.unice.polytech.si3.qgl.kihm.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TurnTest {

    Turn turn;
    double angle;
    int id;

    @BeforeEach
    void setUp() {
        this.angle = 0.752;
        this.id = 8;
        this.turn = new Turn(this.id, this.angle);
    }

    @Test
    void testEquals() {
        assertNotEquals(null, this.turn);
        assertEquals(new Turn(this.turn.getSailorId(), this.angle), this.turn);
        assertNotEquals(new Turn(56, 0.98), this.turn);
        assertEquals(this.turn, this.turn);
    }

    @Test
    void testHashCode() {
        assertNotEquals(0, this.turn.hashCode());
        assertEquals(new Turn(this.turn.getSailorId(), this.angle).hashCode(), this.turn.hashCode());
        assertNotEquals(new Turn(56, 0.98).hashCode(), this.turn.hashCode());
        assertEquals(this.turn.hashCode(), this.turn.hashCode());
    }

    @Test
    void getRotation() {
        assertEquals(this.angle, this.turn.getRotation());
        assertNotEquals(78, this.turn.getRotation());
    }

    @Test
    void testToString() {
        assertEquals("{\"sailorId\": " + this.id + ", \"type\": \"" + this.turn.getType() + "\", \"rotation\": " + this.angle + '}', this.turn.toString());
    }
}