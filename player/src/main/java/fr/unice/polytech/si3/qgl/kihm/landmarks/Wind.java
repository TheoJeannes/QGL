package fr.unice.polytech.si3.qgl.kihm.landmarks;


import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Class that defines the wind of the map
 */
public class Wind {
    /**
     * The orientation of the wind.
     */
    @JsonAlias({"direction", "orientation"})
    private final double orientation;

    /**
     * The strength of the wind.
     */
    private final double strength;

    /**
     * Basic constructor of the wind with no parameters. By default, the orientation and the strength are 0.
     */
    public Wind() {
        orientation = 0;
        strength = 0;
    }

    /**
     * Constructor of a wind.
     *
     * @param orientation The orientation of the wind.
     * @param strength    The strength of the wind.
     */
    public Wind(double orientation, double strength) {
        this.orientation = orientation;
        this.strength = strength;
    }

    public Wind(Wind w) {
        orientation = w.getOrientation();
        strength = w.getStrength();
    }

    public double getOrientation() {
        return orientation;
    }

    public double getStrength() {
        return strength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wind wind = (Wind) o;
        return Double.compare(wind.orientation, orientation) == 0 && Double.compare(wind.strength, strength) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(orientation);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(strength);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "{\"orientation\": " + this.orientation + ", \"strength\": " + this.strength + "}";
    }
}
