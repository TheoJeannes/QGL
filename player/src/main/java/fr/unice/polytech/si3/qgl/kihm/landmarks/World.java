package fr.unice.polytech.si3.qgl.kihm.landmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.CHECKPOINT;
import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.STREAM;

/**
 * Class that defines the world.
 */
public class World {
    /**
     * The list of the checkpoints in the world.
     */
    private final List<Checkpoint> checkpoints;

    /**
     * The wind present in the world.
     */
    private Wind wind;

    /**
     * The list of the visible entities the ship can see.
     */
    private List<Obstacle> entities;

    /**
     * Constructor of the complete world.
     *
     * @param checkpoints The list of checkpoints in the world.
     * @param wind        The wind in the world.
     * @param entities    The list of the entities in the world.
     */
    public World(List<Checkpoint> checkpoints, Wind wind, List<Obstacle> entities) {
        this.checkpoints = checkpoints;
        this.wind = wind;
        entities.removeIf(e -> e.getType().equals(CHECKPOINT));
        this.entities = entities;
    }

    /**
     * The basic constructor of the world, with nothing in it.
     */
    public World() {
        wind = new Wind();
        checkpoints = new ArrayList<>();
        entities = new ArrayList<>();
    }

    /**
     * Constructor of a world with only checkpoints.
     *
     * @param checkpoints The list of the checkpoints in the world.
     */
    public World(List<Obstacle> checkpoints) {
        this.wind = new Wind();
        this.checkpoints = new ArrayList<>();
        checkpoints.stream().filter(e -> e.getType().equals(CHECKPOINT)).map(Checkpoint.class::cast).forEach(this.checkpoints::add);
        this.entities = new ArrayList<>();
    }

    public World(World w) {
        this.wind = new Wind(w.getWind());
        this.checkpoints = new ArrayList<>();
        this.checkpoints.addAll(w.getCheckpoints());
        this.entities = new ArrayList<>();
        this.entities.addAll(w.getEntities());
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public List<Stream> getStreams() {
        return this.entities.stream().filter(obstacle -> obstacle.getType() == STREAM).map(Stream.class::cast).toList();
    }

    public List<Obstacle> getEntities() {
        return this.entities;
    }

    public void setEntities(List<Obstacle> entities) {
        this.entities = entities.stream().filter(obstacle -> obstacle.getType() != CHECKPOINT).toList();
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkpoints, wind, entities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        World world = (World) o;
        return Objects.equals(checkpoints, world.checkpoints) && Objects.equals(wind, world.wind) && Objects.equals(entities, world.entities);
    }

    @Override
    public String toString() {
        return "World{" +
                "checkpoints=" + checkpoints +
                ", wind=" + wind +
                ", entities=" + entities +
                '}';
    }
}
