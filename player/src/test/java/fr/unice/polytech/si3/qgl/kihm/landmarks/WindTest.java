package fr.unice.polytech.si3.qgl.kihm.landmarks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class WindTest {

    Wind wind;
    double strength;
    double orientation;

    @BeforeEach
    void setUp() {
        this.strength = 578;
        this.orientation = 0.465413;
        this.wind = new Wind(this.orientation, this.strength);
    }

    @Test
    void getOrientation() {
        assertEquals(this.orientation, this.wind.getOrientation());
    }

    @Test
    void getStrength() {
        assertEquals(this.strength, this.wind.getStrength());
    }

    @Test
    void createCopieInstanceTest() {
        Wind windCopie = new Wind(this.wind);
        assertEquals(wind, windCopie);
    }

    @Test
    void testEquals() {
        assertNotEquals(new Wind(), this.wind);
        assertNotEquals(new Wind(this.strength, this.orientation), this.wind);
        assertEquals(this.wind, this.wind);
    }

    @Test
    void testHashCode() {
        assertEquals(0, new Wind().hashCode());
        assertNotEquals(0, this.wind.hashCode());
        assertEquals(new Wind(this.orientation, this.strength).hashCode(), this.wind.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("{\"orientation\": " + this.orientation + ", \"strength\": " + this.strength + "}", this.wind.toString());
    }
}