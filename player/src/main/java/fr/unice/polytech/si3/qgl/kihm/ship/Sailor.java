package fr.unice.polytech.si3.qgl.kihm.ship;

import fr.unice.polytech.si3.qgl.kihm.equipments.Equipment;

import java.awt.*;
import java.util.Objects;

/**
 * Class that defines the sailors
 */
public class Sailor {

    /**
     * The id of the sailor
     */
    private final int id;

    /**
     * The name of the sailor
     */
    private final String name;

    /**
     * The position of the sailor
     */
    private Point position;

    /**
     * The constructor of a sailor
     * By default, the id is 0, the name is Default and the position is in (0,0)
     */
    public Sailor() {
        id = 0;
        name = "Default";
        position = new Point(0, 0);
    }

    /**
     * The constructor of a sailor
     *
     * @param id   the id of the sailor
     * @param name the name of the sailor
     *             By default, the position is in (0,0)
     */
    public Sailor(int id, String name) {
        this.id = id;
        this.name = name;
        position = new Point(0, 0);
    }

    /**
     * The constructor of a sailor
     *
     * @param id       the id of the sailor
     * @param name     the name of the sailor
     * @param position the position of the sailor
     */
    public Sailor(int id, String name, Point position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public Sailor(Sailor s) {
        this(s.getId(), s.getName(), new Point(s.getPosition()));
    }

    /**
     * Move the sailor to an equipment
     *
     * @param equipment the equipment the sailor has to move to
     * @return the movement of the sailor
     */
    public int[] moveTo(Equipment equipment) {
        return this.moveTo(equipment.getPosition());
    }

    /**
     * Move the sailor to a position
     *
     * @param pos the position the sailor has to move to
     * @return the movement of the sailor
     */
    public int[] moveTo(Point pos) {
        return new int[]{pos.x - this.position.x, pos.y - this.position.y};
    }

    /**
     * Calculate the distance between a sailor and an equipment
     *
     * @param equipment the equipment you want to calculate the distance with
     * @return the distance
     */
    public int distanceTo(Equipment equipment) {
        return Math.abs(equipment.getX() - this.position.x) + Math.abs(equipment.getY() - this.position.y);
    }

    public int getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sailor sailor = (Sailor) o;
        return id == sailor.id && Objects.equals(name, sailor.name) && Objects.equals(position, sailor.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position);
    }

    @Override
    public String toString() {
        return "{\"id\": " + this.id + ", \"x\": " + this.getPosition().x + ", \"y\": " + this.getPosition().y + ", \"name\": " + this.name + "}";
    }

    public String getName() {
        return name;
    }
}
