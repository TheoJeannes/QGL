package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.REEF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class WorldTest {
    World w;
    List<Obstacle> obstacles;
    Stream s;
    Obstacle reef;
    OtherShip os;

    @BeforeEach
    void setUp() {
        s = new Stream(new Position(), new Area(), 0);
        reef = new Obstacle(REEF, new Position(), new Area());
        os = new OtherShip(new Position(), new Area(), 0);
        obstacles = new ArrayList<>();
        w = new World(new ArrayList<>(), new Wind(), obstacles);
    }

    @Test
    void emptyObstacles() {
        assertEquals(new ArrayList<>(), w.getStreams());
    }

    @Test
    void onlyStreams() {
        obstacles.add(s);
        assertEquals(obstacles, w.getStreams());
    }

    @Test
    void mixedUpGetStreams() {
        obstacles.add(s);
        obstacles.add(reef);
        obstacles.add(os);
        assertEquals(List.of(s), w.getStreams());
    }

    @Test
    void compareSelf() {
        assertEquals(new World(new ArrayList<>(), new Wind(), obstacles), w);
    }

    @Test
    void compareNull() {
        assertNotEquals(null, w);
    }

    @Test
    void compareCheckPoints() {
        assertNotEquals(new World(List.of(new Checkpoint(new Position())), new Wind(), obstacles), w);
    }

    @Test
    void compareObstacles() {
        List<Obstacle> obstaclesBis = new ArrayList<>(List.of(s, reef, os));
        assertNotEquals(new World(new ArrayList<>(), new Wind(), obstaclesBis), w);
    }

    @Test
    void getterTest() {
        assertEquals(0, this.w.getEntities().size());
        this.w.setEntities(List.of(this.os, this.s));
        assertEquals(2, this.w.getEntities().size());
    }

    @Test
    void createCopieInstanceTest() {
        World worldCopie = new World(this.w);
        assertEquals(this.w, worldCopie);
    }

    @Test
    void compareWind() {
        assertNotEquals(new World(new ArrayList<>(), new Wind(3.14, 50), obstacles), w);
    }

    @Test
    void hashCodeTest() {
        assertEquals(w.hashCode(), w.hashCode());
        this.w.setWind(new Wind(Math.PI, 100));
        assertNotEquals(new World().hashCode(), w.hashCode());
        World w2 = this.w;
        assertEquals(w2.hashCode(), w.hashCode());
        assertEquals(new World().hashCode(), new World().hashCode());
    }
}