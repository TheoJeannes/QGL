package fr.unice.polytech.si3.qgl.kihm.actions;

import java.util.Objects;

import static fr.unice.polytech.si3.qgl.kihm.ship.SailorManager.MOVEMENT;

/**
 * Action Moving, allows a sailor to move on the deck.
 */
public class Moving extends Action {
    /**
     * Distance according to the abscissa axe.
     */
    private final int xdistance;

    /**
     * Distance according to the ordinate axe.
     */
    private final int ydistance;

    /**
     * Constructor that initializes Moving Action.
     * If the absolute distance of x and y is above 5, which is the number maximum of movement possible for a sailor,
     * you take the minimum between the absolute value of x and the maximum movement possible. If you can still move,
     * you move according to the ordinate axe.
     *
     * @param sailorId id of the sailor to move
     * @param x        x-displacement
     * @param y        y-displacement
     */
    public Moving(int sailorId, int x, int y) {
        super(sailorId, actionTypeEnum.MOVING);
        int distance = Math.abs(x) + Math.abs(y);
        if (distance > MOVEMENT) {
            boolean xNegatif = x < 0;
            boolean yNegatif = y < 0;
            x = Math.min(Math.abs(x), MOVEMENT);
            y = MOVEMENT - x;
            x = xNegatif ? -x : x;
            y = yNegatif ? -y : y;
        }
        this.xdistance = x;
        this.ydistance = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Moving moving = (Moving) o;
        return xdistance == moving.xdistance && ydistance == moving.ydistance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), xdistance, ydistance);
    }

    public int getXdistance() {
        return xdistance;
    }

    public int getYdistance() {
        return ydistance;
    }

    @Override
    public String toString() {
        return "{\"sailorId\": " + this.getSailorId() + ", \"type\": \"MOVING\", \"xdistance\": " + this.xdistance + ", \"ydistance\": " + this.ydistance + '}';
    }
}
