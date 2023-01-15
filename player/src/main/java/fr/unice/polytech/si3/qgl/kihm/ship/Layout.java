package fr.unice.polytech.si3.qgl.kihm.ship;

/**
 * The class that defines the layout of the required equipments for the ship to move
 */
public class Layout {

    /**
     * The number of oar needed at the left
     */
    private int numberOarLeft = 0;

    /**
     * The number of oar needed at the right
     */
    private int numberOarRight = 0;

    /**
     * The angle needed for the rudder
     */
    private double angleRudder = 0.0;

    /**
     * The number of sails needed
     */
    private int numberSail = 0;

    /**
     * The number of watch needed
     */
    private boolean assignedToWatch = false;

    /**
     * The constructor of the class with all the equipments
     *
     * @param numberOarLeft   the number of left oars needed
     * @param numberOarRight  the number of right oars needed
     * @param angleRudder     the angle of the rudder needed
     * @param numberSail      the number of sails needed
     * @param assignedToWatch the number of watch needed
     */
    public Layout(int numberOarLeft, int numberOarRight, double angleRudder, int numberSail, boolean assignedToWatch) {
        this.numberOarLeft = numberOarLeft;
        this.numberOarRight = numberOarRight;
        this.angleRudder = angleRudder;
        this.numberSail = numberSail;
        this.assignedToWatch = assignedToWatch;
    }

    /**
     * The constructor of the class with all of the equipments
     * If the ship has no watch, the number needed is 0.0
     *
     * @param numberOarLeft  the number of left oars needed
     * @param numberOarRight the number of right oars needed
     * @param angleRudder    the angle of the rudder needed
     * @param numberSail     the number of sails needed
     */
    public Layout(int numberOarLeft, int numberOarRight, double angleRudder, int numberSail) {
        this.numberOarLeft = numberOarLeft;
        this.numberOarRight = numberOarRight;
        this.angleRudder = angleRudder;
        this.numberSail = numberSail;
    }

    /**
     * The constructor of the class with all of the equipments
     * If the ship has no sails, the number needed is 0.0
     * If the ship has no watch, the number needed is 0.0
     *
     * @param numberOarLeft  the number of left oars needed
     * @param numberOarRight the number of right oars needed
     * @param angleRudder    the angle of the rudder needed
     */
    public Layout(int numberOarLeft, int numberOarRight, double angleRudder) {
        this.numberOarLeft = numberOarLeft;
        this.numberOarRight = numberOarRight;
        this.angleRudder = angleRudder;
    }

    /**
     * The constructor of the class with all of the equipments
     * If the ship has no rudder, the number needed is 0.0
     * If the ship has no sails, the number needed is 0.0
     * If the ship has no watch, the number needed is 0.0
     *
     * @param numberOarLeft  the number of left oars needed
     * @param numberOarRight the number of right oars needed
     */
    public Layout(int numberOarLeft, int numberOarRight) {
        this.numberOarLeft = numberOarLeft;
        this.numberOarRight = numberOarRight;
    }


    /**
     * The constructor of the class with all of the equipments
     * If the ship has no rudder, the number needed is 0.0
     * If the ship has no sails, the number needed is 0.0
     * If the ship has no watch, the number needed is 0.0
     * If the ship has no left oars, the number needed is 0.0
     * If the ship has no right oars, the number needed is 0.0
     */
    public Layout() {
    }

    public Layout(Layout layout) {
        this.numberOarLeft = layout.numberOarLeft;
        this.numberOarRight = layout.numberOarRight;
        this.angleRudder = layout.angleRudder;
        this.numberSail = layout.numberSail;
        this.assignedToWatch = layout.assignedToWatch;
    }

    public int getNumberOarLeft() {
        return numberOarLeft;
    }

    public void setNumberOarLeft(int numberOarLeft) {
        this.numberOarLeft = numberOarLeft;
    }

    public int getNumberOarRight() {
        return numberOarRight;
    }

    public void setNumberOarRight(int numberOarRight) {
        this.numberOarRight = numberOarRight;
    }

    public double getAngleRudder() {
        return angleRudder;
    }

    public void setAngleRudder(double angleRudder) {
        this.angleRudder = angleRudder;
    }

    public int getNumberSail() {
        return numberSail;
    }

    public void setNumberSail(int numberSail) {
        this.numberSail = numberSail;
    }

    public boolean isAssignedToWatch() {
        return assignedToWatch;
    }

    public void setAssignedToWatch(boolean assignedToWatch) {
        this.assignedToWatch = assignedToWatch;
    }

    /**
     * Reduce the number of left oars by 1
     */
    public void reduceNumberOarLeft() {
        this.numberOarLeft = Math.max(this.numberOarLeft - 1, 0);
    }

    /**
     * Reduce the number of right oars by 1
     */
    public void reduceNumberOarRight() {
        this.numberOarRight = Math.max(this.numberOarRight - 1, 0);
    }

    public void reduceNumberSails() {
        this.numberSail = Math.max(this.numberSail - 1, 0);
    }

    public void removeUsageOfRudder() {
        this.angleRudder = 0.0;
    }

    public void removeUsageOfWatch() {
        this.assignedToWatch = false;
    }

    public void setEquipment(int nbLeft, int nbRight, double angleRudder, int nbSail, boolean watch) {
        this.numberOarLeft = nbLeft;
        this.numberOarRight = nbRight;
        this.angleRudder = angleRudder;
        this.numberSail = nbSail;
        this.assignedToWatch = watch;
    }

    @Override
    public String toString() {
        return "Layout{" +
                "numberOarLeft=" + numberOarLeft +
                ", numberOarRight=" + numberOarRight +
                ", angleRudder=" + angleRudder +
                ", numberSail=" + numberSail +
                ", assignedToWatch=" + assignedToWatch +
                '}';
    }
}
