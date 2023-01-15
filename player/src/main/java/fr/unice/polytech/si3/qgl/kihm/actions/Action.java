package fr.unice.polytech.si3.qgl.kihm.actions;

import java.util.Objects;

/**
 * Create Actions to be serialized.
 */
public class Action {
    /**
     * The type of actions the sailor can do.
     */
    private final actionTypeEnum type;

    /**
     * The sailor id.
     */
    private int sailorId;

    /**
     * Constructor of an empty action.
     */
    public Action() {
        this.sailorId = 0;
        this.type = null;
    }

    /**
     * Constructor of an action for a specific sailor and the type of action he will do.
     *
     * @param sailorId The sailors' id.
     * @param type     The type of action for the sailor.
     */
    public Action(int sailorId, actionTypeEnum type) {
        this.sailorId = sailorId;
        this.type = type;
    }

    /**
     * Gets the sailors' id.
     *
     * @return The sailors' id
     */
    public int getSailorId() {
        return sailorId;
    }

    public void setSailorId(int sailorId) {
        this.sailorId = sailorId;
    }

    /**
     * Gets the action that the sailor will do.
     *
     * @return The action of the sailor.
     */
    public actionTypeEnum getType() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return sailorId == action.sailorId && type == action.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sailorId, type);
    }

    @Override
    public String toString() {
        return "{\"sailorId\": " + this.sailorId + ", \"type\": \"" + this.type + "\"}";
    }


    /**
     * Enumeration of the actions possible for the sailor.
     */
    public enum actionTypeEnum {
        MOVING,
        LIFT_SAIL,
        LOWER_SAIL,
        TURN,
        OAR,
        USE_WATCH,
        AIM,
        FIRE,
        RELOAD
    }
}
