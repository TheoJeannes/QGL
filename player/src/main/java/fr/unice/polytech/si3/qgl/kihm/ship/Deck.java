package fr.unice.polytech.si3.qgl.kihm.ship;

/**
 * Class that defines the deck of our ship
 */
public class Deck {
    /**
     * The width of the deck of the ship
     */
    private int width;

    /**
     * The length of the deck of the ship
     */
    private int length;

    public Deck() {
    }

    /**
     * The constructor of the deck
     *
     * @param width  the width of the deck
     * @param length the length of the deck
     */
    public Deck(int width, int length) {
        this.width = width;
        this.length = length;
    }

    public Deck(Deck deck) {
        this(deck.getWidth(), deck.getLength());
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck deck = (Deck) o;

        if (width != deck.width) return false;
        return length == deck.length;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + length;
        return result;
    }

    @Override
    public String toString() {
        return "{\"width\": " + this.width + ", \"length\": " + this.length + '}';
    }
}
