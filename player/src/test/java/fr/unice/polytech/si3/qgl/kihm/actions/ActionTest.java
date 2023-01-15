package fr.unice.polytech.si3.qgl.kihm.actions;

import fr.unice.polytech.si3.qgl.kihm.structures.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ActionTest {
    Parser p;
    Action action;

    @BeforeEach
    void setUp() {
        p = new Parser();
        this.action = new Action(8, Action.actionTypeEnum.AIM);
    }

    @Test
    void constructJsonNullType() {
        assertEquals("[{\"sailorId\":1,\"type\":\"TURN\"}]", p.constructJson(List.of(new Action(1, null), new Action(1, Action.actionTypeEnum.TURN))));
    }

    @Test
    void constructJsonMoving() {
        assertEquals("[{\"sailorId\":1,\"type\":\"MOVING\",\"xdistance\":3,\"ydistance\":2},{\"sailorId\":2,\"type\":\"MOVING\",\"xdistance\":-3,\"ydistance\":2}]", p.constructJson(List.of(new Moving(1, 3, 2), new Moving(2, -3, 2))));
    }

    @Test
    void constructJsonMovingGreaterThan5() {
        assertEquals("[{\"sailorId\":1,\"type\":\"MOVING\",\"xdistance\":3,\"ydistance\":2}]", p.constructJson(List.of(new Moving(1, 3, 3))));
    }

    @Test
    void constructJsonMovingGreaterThan5Neg() {
        assertEquals("[{\"sailorId\":1,\"type\":\"MOVING\",\"xdistance\":-3,\"ydistance\":2}]", p.constructJson(List.of(new Moving(1, -3, 3))));
    }

    @Test
    void constructJson() {
        assertEquals("[{\"sailorId\":1,\"type\":\"MOVING\",\"xdistance\":-3,\"ydistance\":2},{\"sailorId\":1,\"type\":\"OAR\"}]", p.constructJson(List.of(new Moving(1, -3, 3), new Action(1, Action.actionTypeEnum.OAR), new Action(1, null))));
    }

    @Test
    void constructJsonTurnWrong() {
        assertEquals("[{\"sailorId\":1,\"type\":\"TURN\",\"rotation\":0.7853981633974483}]", p.constructJson(List.of(new Turn(1, Math.PI / 2))));
    }

    @Test
    void constructJsonNegativeTurnWrong() {
        assertEquals("[{\"sailorId\":1,\"type\":\"TURN\",\"rotation\":-0.7853981633974483}]", p.constructJson(List.of(new Turn(1, -Math.PI / 2))));
    }

    @Test
    void constructJsonNegativeTurn() {
        assertEquals("[{\"sailorId\":1,\"type\":\"TURN\",\"rotation\":-0.25}]", p.constructJson(List.of(new Turn(1, -0.25))));
    }

    @Test
    void constructJsonTurn() {
        assertEquals("[{\"sailorId\":1,\"type\":\"TURN\",\"rotation\":0.5235987755982988}]", p.constructJson(List.of(new Turn(1, Math.PI / 6))));
    }

    @Test
    void constructorTest() {
        assertEquals(new Action(0, null), new Action());
    }

    @Test
    void compareXDistance() {
        assertNotEquals(new Moving(0, 0, 0), new Moving(0, 1, 0));
    }

    @Test
    void compareYDistance() {
        assertNotEquals(new Moving(0, 0, 0), new Moving(0, 1, 1));
    }

    @Test
    void compareAllDistance() {
        assertNotEquals(new Moving(0, 0, 0), new Moving(0, 0, 1));
    }

    @Test
    void compareId() {
        assertNotEquals(new Moving(1, 0, 0), new Moving(0, 0, 0));
    }

    @Test
    void compareNullMoving() {
        assertNotEquals(null, new Moving(0, 0, 0));
    }

    @Test
    void compareNullTurn() {
        assertNotEquals(null, new Turn(0, 0));
    }

    @Test
    void compareIdTurn() {
        assertNotEquals(new Turn(10, 0.5), new Turn(10, 0));
    }

    @Test
    void compareTurn() {
        assertEquals(new Turn(0, 0.5), new Turn(0, 0.5));
    }

    @Test
    void testHashCode() {
        assertNotEquals(0, new Action().hashCode());
        assertNotEquals(0, this.action.hashCode());
        assertEquals(new Action(8, Action.actionTypeEnum.AIM).hashCode(), this.action.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("{\"sailorId\": " + this.action.getSailorId() + ", \"type\": \"" + this.action.getType() + "\"}", this.action.toString());
    }
}