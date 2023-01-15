package fr.unice.polytech.si3.qgl.kihm.equipments;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;

import java.util.Objects;

/**
 * Class that defines the sails of the ship.
 */
public class Sail extends Equipment {
    /**
     * Is the sail is open.
     */
    private boolean opened = false;

    /**
     * Constructor of a sail.
     *
     * @param x The abscissa coordinate.
     * @param y The ordinate coordinate.
     */
    public Sail(int x, int y) {
        super(equipmentTypeEnum.SAIL, x, y);
    }

    /**
     * Constructor of a sail to be opened.
     *
     * @param x      The abscissa coordinate.
     * @param y      The ordinate coordinate.
     * @param opened Is the sail open to be used.
     */
    public Sail(int x, int y, boolean opened) {
        this(x, y);
        this.opened = opened;
    }

    /**
     * Checks if the sail is opened.
     */
    public boolean isOpened() {
        return opened;
    }

    /**
     * Change the attribute "opened" to true
     */
    public void openSail() {
        this.opened = true;
    }

    /**
     * Change the attribute "opened" to false
     */
    public void closeSail() {
        this.opened = false;
    }

    @Override
    public Action action(Layout layout, Sailor sailor) {
        if (layout.getNumberSail() > 0 && !this.opened) {
            layout.reduceNumberSails();
            return new Action(sailor.getId(), Action.actionTypeEnum.LIFT_SAIL);
        }
        if (layout.getNumberSail() == 0 && this.opened) {
            return new Action(sailor.getId(), Action.actionTypeEnum.LOWER_SAIL);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sail sail = (Sail) o;
        return opened == sail.opened && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), opened);
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + this.getType().toString().toLowerCase() + "\", \"x\": " + this.getX() + ", \"y\": " + this.getY() + ", \"opened\": " + this.isOpened() + "}";
    }
}
