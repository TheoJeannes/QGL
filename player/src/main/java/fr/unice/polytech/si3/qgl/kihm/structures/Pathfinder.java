package fr.unice.polytech.si3.qgl.kihm.structures;

import fr.unice.polytech.si3.qgl.kihm.equipments.Sail;
import fr.unice.polytech.si3.qgl.kihm.equipments.Watch;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Checkpoint;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.util.Objects;

import static fr.unice.polytech.si3.qgl.kihm.equipments.Equipment.equipmentTypeEnum;
import static java.lang.Math.PI;
import static java.util.Objects.hash;

/**
 * Class that defines the pathfinder
 */
public class Pathfinder {

    private static final double MINIUM_RUDDER_ANGLE = 1;
    private final World world;
    private Ship ship;

    /**
     * Constructor of the pathfinder
     *
     * @param ship the current ship
     *             By default, a new world is created
     */
    public Pathfinder(Ship ship) {
        this.ship = ship;
        this.world = new World();
    }

    /**
     * Constructor of the pathfinder
     *
     * @param ship  the current ship
     * @param world the current world
     */
    public Pathfinder(Ship ship, World world) {
        this.ship = ship;
        this.world = world;
    }

    /**
     * Give the layout of equipments to be used
     *
     * @param numberOfSailors the number of sailor on the boat
     * @return the layout of equipments needed
     */
    public Layout nextRoundLayout(int numberOfSailors) {
        Checkpoint nextCheckpoint = this.world.getCheckpoints().get(0);
        Layout layout = this.getSimpleAvoidingLayout(this.calculateAngle(nextCheckpoint), numberOfSailors, Watch.VISION_SIZE);
        // Methode when there is more than 1 checkpoint remaining and that its in our line of sight
        if (world.getCheckpoints().size() > 1 && world.getCheckpoints().get(1).distance(this.ship.getPosition()) < Watch.VISION_SIZE) {
            Checkpoint preShotCheckpoint = this.world.getCheckpoints().get(1);
            Layout preShotLayout = this.getSimpleAvoidingLayout(this.calculateAngle(preShotCheckpoint), numberOfSailors, Watch.VISION_SIZE);

            // Checks if pre-shooting the checkpoint we still get the next checkpoint
            if (nextCheckpoint.distance(this.nextRoundPosition(preShotLayout)) <= nextCheckpoint.getShape().getBounds().getHeight() * 0.5) {
                layout = preShotLayout;
            }
        }

        // Slows the ship down if needed to get the checkpoint
        if (numberOfSailors > 0) this.slowDown(layout, nextCheckpoint, this.calculateAngle(nextCheckpoint));
        return layout;
    }

    /**
     * Compute number of oar on each side and the angle for the rudder, in order to move the boat with the better angle
     *
     * @return [Number of left oar needed, Number of right oar needed, Angle of the rudder]
     */
    public Layout getLayout(double angle, int numberOfSailors) {
        Layout layout = new Layout();
        double angleRudder = 0.0;
        int nbLeft = this.ship.getOarLeft().size();
        int nbRight = this.ship.getOarRight().size();
        double delta = PI / (nbRight + nbLeft);
        boolean hasRudder = this.ship.hasEquipment(equipmentTypeEnum.RUDDER) && numberOfSailors > 2;

        if ((!hasRudder && Math.abs(angle) < delta) || (hasRudder && Math.abs(angle) <= PI / 4)) {
            angleRudder = this.angleOnCondition(hasRudder && (numberOfSailors >= this.ship.getEquipments().size() || Math.abs(angle) >= Math.toRadians(MINIUM_RUDDER_ANGLE)), angle, angleRudder);
            nbRight = nbLeft = Math.min(this.ship.getOarLeft().size(), this.ship.getOarRight().size());
        } else {
            int diffRL = (int) (angle / delta);
            if (hasRudder) {
                double alpha = angle - PI / 4;
                diffRL = (int) (alpha / delta) + 1;
            }
            int[] oars = this.balanceOars(nbLeft, nbRight, diffRL);
            nbLeft = oars[0];
            nbRight = oars[1];
            angleRudder = this.angleOnCondition(hasRudder, angle - (nbRight - nbLeft) * delta, angleRudder);
        }

        layout.setEquipment(nbLeft, nbRight, angleRudder, this.sailsManagement(angleRudder, nbLeft, nbRight, delta), false);

        this.prioritizingLayout(numberOfSailors, layout);
        layout.setAssignedToWatch(this.useWatch(numberOfSailors, layout));
        return layout;
    }

    private boolean useWatch(int numberOfSailors, Layout layout) {
        return this.ship.hasEquipment(equipmentTypeEnum.WATCH) && this.sailorsNeeded(layout) < numberOfSailors;
    }

    private double angleOnCondition(boolean condition, double angle1, double angle2) {
        return condition ? angle1 : angle2;
    }

    private int[] balanceOars(int left, int right, int difference) {
        int max = Math.max(left, right);
        int min = max - Math.abs(difference);
        if (difference > 0) {
            left = min;
            right = max;
        } else {
            left = max;
            right = min;
        }
        return new int[]{Math.max(left, 0), Math.max(right, 0)};
    }

    /**
     * Manage the sails
     *
     * @param angleRudder the angle of the rudder
     * @param nbLeft      the number of oar in the left
     * @param nbRight     the number of oar in the right
     * @param delta       the minimum angle possible with an oar
     * @return the number of sail to be used, if none return 0
     */
    private int sailsManagement(double angleRudder, int nbLeft, int nbRight, double delta) {
        //si le bateau et le vent sont favorables au debut et a la fin du tour
        if (this.ship.hasEquipment(equipmentTypeEnum.SAIL) && this.world.getWind().getStrength() > 0 && isWindFavorable(this.world.getWind().getOrientation(), this.ship.getPosition().getOrientation()) && isWindFavorable(this.world.getWind().getOrientation(), this.ship.getPosition().getOrientation() + ((nbRight - nbLeft) * delta + angleRudder))) {
            return this.ship.getSails().size();
        }
        return 0;
    }

    private void prioritizingLayout(int numberOfSailors, Layout layout) {
        if (Math.abs(layout.getAngleRudder()) <= Math.toRadians(MINIUM_RUDDER_ANGLE)) {
            layout.removeUsageOfRudder();
        }

        while (this.sailorsNeeded(layout) > numberOfSailors && (layout.getNumberOarLeft() > 0 || layout.getNumberOarRight() > 0)) {
            layout.reduceNumberOarLeft();
            layout.reduceNumberOarRight();
        }
    }

    /**
     * Calculate the number of sailors needed
     *
     * @param layout the layout of the equipments on the ship
     * @return the number of sailors needed
     */
    private int sailorsNeeded(Layout layout) {
        int assignedToSails = (int) Math.abs(layout.getNumberSail() - this.ship.getSails().stream().filter(Sail::isOpened).count());
        return layout.getNumberOarLeft() + layout.getNumberOarRight() + (layout.getAngleRudder() != 0.0 ? 1 : 0) + Math.max(assignedToSails, 0) + (layout.isAssignedToWatch() ? 1 : 0);
    }

    /**
     * Change the layout if we need to slow down
     *
     * @param layout     the layout of the equipments
     * @param checkPoint the aimed checkpoint
     * @param angle      the angle between the boat and the ship
     */
    private void slowDown(Layout layout, Obstacle checkPoint, double angle) {
        double radius = checkPoint.getShape().getBounds().height * 0.5;
        boolean early;

        // Decides when it gets to the checkpoint if it's better to stop early in the checkpoint or not
        if (this.world.getCheckpoints().size() > 1) {
            early = Math.toDegrees(this.ship.getPosition().angleBetweenTwoPositions(this.world.getCheckpoints().get(0).getPosition(), this.world.getCheckpoints().get(1).getPosition())) <= 100;
        } else {
            early = false;
        }

        // Slow down to not miss the checkpoint
        while (layout.getNumberOarLeft() >= 0 && layout.getNumberOarRight() >= 0 && checkPoint.distance(this.ship.getPosition()) + radius < this.travelDistance(layout) && checkPoint.distance(this.nextRoundPosition(layout)) > radius) {
            layout.reduceNumberOarLeft();
            layout.reduceNumberOarRight();
        }

        // Slow down to stop earlier in the checkpoint
        while (early && layout.getNumberOarLeft() >= 0 && layout.getNumberOarRight() >= 0 && checkPoint.distance(this.ship.getPosition()) - radius / 2 < this.travelDistance(layout) && checkPoint.distance(this.nextRoundPosition(layout)) <= radius) {
            layout.reduceNumberOarLeft();
            layout.reduceNumberOarRight();
        }
        if (layout.getNumberOarLeft() <= 0 && layout.getNumberOarRight() <= 0) {
            if (angle < 0) {
                layout.setNumberOarLeft(1);
                layout.setNumberOarRight(0);
            }
            if (angle > 0) {
                layout.setNumberOarLeft(0);
                layout.setNumberOarRight(1);
            }
        }
    }

    /**
     * Find the position of the ship in the next round
     *
     * @param layout the layout of the equipments
     * @return the position of the ship in the next round
     */
    public Position nextRoundPosition(Layout layout) {
        Calculator calculator = new Calculator();

        int totalOars = this.ship.getOarLeft().size() + this.ship.getOarRight().size();
        double oarSpeed = calculator.calculateOarSpeed(layout, totalOars);
        double angle = calculator.calculateTurningAngle(layout, totalOars);
        double windStrength = calculator.calculateWindStrength(this.world.getWind(), this.ship.getSails());

        return calculator.calculatingNextRoundPosition(this.ship.getPosition(), this.ship.getShape(), oarSpeed, angle, windStrength, this.world.getWind().getOrientation(), this.world.getStreams());
    }

    /**
     * Find the distance of the ship between before and after a movement
     *
     * @param layout the layout of the equipments
     * @return the distance between before and after a movement
     */
    protected double travelDistance(Layout layout) {
        Position before = this.ship.getPosition();
        Position after = this.nextRoundPosition(layout);
        return after.distance(before);
    }

    /**
     * Calculate the angle between the checkpoint and the ship
     *
     * @param c the aimed checkpoint
     * @return the angle between a checkpoint and the ship
     */
    double calculateAngle(Obstacle c) {
        boolean hasRudder = this.ship.hasEquipment(equipmentTypeEnum.RUDDER);
        return this.angleOnCondition(c.distance(this.ship.getPosition()) > c.getShape().getBounds().getHeight() && hasRudder, this.ship.getPosition().getAngleBetween(this.ship.getPosition().getClosestPoint(c.getShape())), this.ship.getPosition().getAngleBetween(c.getPosition()));
    }

    /**
     * Find if the wind will be useful or not
     *
     * @param windOrientation the orientation of the wind
     * @param shipOrientation the orientation of the ship
     * @return true if the wind is favorable for the ship
     */
    protected boolean isWindFavorable(double windOrientation, double shipOrientation) {
        return Math.cos(shipOrientation - windOrientation) > 0;
    }

    /**
     * Find the new layout to avoid obstacles
     *
     * @param angle           the angle to turn
     * @param numberOfSailors the number of sailors
     * @return the new layout
     */
    public Layout getSimpleAvoidingLayout(double angle, int numberOfSailors, double antennaLength) {
        Calculator calculator = new Calculator();
        int incrementation = 1;
        int maxIteration = 360;
        double avoidAngle = angle;

        while (incrementation <= maxIteration && (calculator.collisionWithEntity(antennaLength, this.ship.getPosition(), avoidAngle, this.ship.getDeck(), this.world.getEntities()) || Math.abs(avoidAngle) > Math.toRadians(135))) {
            avoidAngle += Math.pow(-1, incrementation % 2) * Math.toRadians(incrementation++);
        }
        if (antennaLength > Watch.VISION_SIZE / 2 && Math.abs(avoidAngle) > Math.toRadians(90)) {
            return this.getSimpleAvoidingLayout(angle, numberOfSailors, antennaLength / 2);
        }
        angle = this.angleOnCondition(incrementation == 1, angle, avoidAngle);
        return this.getLayout(angle, numberOfSailors);
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    @Override
    public int hashCode() {
        return hash(ship, world);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pathfinder that = (Pathfinder) o;
        return Objects.equals(ship, that.ship) && Objects.equals(world, that.world);
    }

    @Override
    public String toString() {
        return "Pathfinder{" + "ship=" + ship + ", world=" + world + '}';
    }
}
