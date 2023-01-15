package fr.unice.polytech.si3.qgl.kihm.equipments;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;

import java.awt.*;
import java.util.Objects;

/**
 * The equipment class for each equipment on the ship.
 */
public abstract class Equipment {

    /**
     * The position of each equipment.
     */
    private final Point position;

    /**
     * The enumType of the equipment.
     */
    private final equipmentTypeEnum type;

    /**
     * Is the equipment occupied by a sailor.
     */
    protected boolean isOccupied = false;

    /**
     * The constructor of the equipment.
     *
     * @param type The type of the equipment.
     * @param x    The abscissa coordinate.
     * @param y    The ordinate coordinate.
     */
    protected Equipment(equipmentTypeEnum type, int x, int y) {
        position = new Point(x, y);
        this.type = type;
    }

    public Point getPosition() {
        return position;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public equipmentTypeEnum getType() {
        return this.type;
    }

    public boolean isAvailable() {
        return !isOccupied;
    }

    public void setOccupied(boolean b) {
        this.isOccupied = b;
    }

    public abstract Action action(Layout layout, Sailor sailor);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return isOccupied == equipment.isOccupied && Objects.equals(position, equipment.position) && type == equipment.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, type, isOccupied);
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + this.type.toString().toLowerCase() + "\", \"x\": " + this.getX() + ", \"y\": " + this.getY() + "}";
    }

    /**
     * Enumeration of all the equipments possible on the ship.
     */
    public enum equipmentTypeEnum {
        OAR,
        RUDDER,
        SAIL,
        WATCH
    }
}
