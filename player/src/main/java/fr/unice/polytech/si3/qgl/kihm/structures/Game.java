package fr.unice.polytech.si3.qgl.kihm.structures;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Wind;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;
import fr.unice.polytech.si3.qgl.kihm.logger.Printer;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import fr.unice.polytech.si3.qgl.kihm.ship.SailorManager;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * Class with all information of the game
 */
public class Game {
    /**
     * The name of the goal, regatta or battle.
     */
    private String goalName;
    /**
     * The world the ship is in.
     */
    private World world;
    /**
     * The number of ships in the game.
     */
    private int shipCount;
    /**
     * Our ship we are using in the game.
     */
    private Ship ship;
    /**
     * The list of sailors on the ship.
     */
    private List<Sailor> sailors;
    /**
     * Our pathfinder to avoid obstacles.
     */
    private Pathfinder pathfinder;
    /**
     * The sailor manager that places the sailors on the correct equipment.
     */
    private SailorManager sailorManager;

    public Game(String goalName, World world, int shipCount, Ship ship, List<Sailor> sailors) {
        this.goalName = goalName;
        this.world = world;
        this.shipCount = shipCount;
        this.ship = ship;
        this.sailors = sailors;
        init();
    }

    public Game() {
        init();
    }

    /**
     * Initializes the pathfinder for the ship and the sailor manager.
     */
    public void init() {
        if (isNull(world)) this.world = new World();
        this.pathfinder = new Pathfinder(this.ship);
        this.sailorManager = new SailorManager(this.sailors, this.ship);
    }

    /**
     * Main method of the game
     * Decide the game plan
     *
     * @return the list of actions needed
     */
    public List<Action> play() {
        if (this.checkpointReached()) {
            Printer.get().fine("*** Checkpoint was Reached ***");
            this.world.getCheckpoints().remove(0);
        }

        List<Action> actions = new ArrayList<>();
        if (!world.getCheckpoints().isEmpty()) {
            Layout layout = this.pathfinder.nextRoundLayout(this.sailors.size());
            actions.addAll(sailorManager.moveSailors(layout));
            actions.addAll(sailorManager.assignEquipment(layout));
        }
        return actions;
    }

    public boolean checkpointReached() {
        return new Calculator().collision(world.getCheckpoints().get(0).getShape(), this.ship.getShape());
    }

    public int getShipCount() {
        return shipCount;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public List<Sailor> getSailors() {
        return sailors;
    }

    public void setSailors(List<Sailor> sailors) {
        this.sailors = sailors;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return shipCount == game.shipCount && Objects.equals(goalName, game.goalName) && Objects.equals(ship, game.ship) && Objects.equals(sailors, game.sailors) && Objects.equals(pathfinder, game.pathfinder) && Objects.equals(sailorManager, game.sailorManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalName, shipCount, ship, sailors, pathfinder, sailorManager);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public void setSailorManager(SailorManager sailorManager) {
        this.sailorManager = sailorManager;
    }

    /**
     * Updates the game with the visible entities and the wind.
     * With it we have a pathfinder to not collide with the visible entities.
     *
     * @param w        The wind in the game.
     * @param entities The visible entities for the ship.
     */
    public void update(Wind w, List<Obstacle> entities) {
        this.world.setEntities(entities);
        this.world.setWind(w);
        this.pathfinder = new Pathfinder(this.ship, this.world);
    }


    @Override
    public String toString() {
        return "Game{" + "goal=" + goalName + ", shipCount=" + shipCount + ", ship=" + ship + ", sailors=" + sailors + ", pathfinder=" + pathfinder + ", sailorManager=" + sailorManager + '}';
    }
}
