package fr.unice.polytech.si3.qgl.kihm.structures;

import fr.unice.polytech.si3.qgl.kihm.Cockpit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class GoalTest {
    private Cockpit cockpit;


    @BeforeEach
    void setUp() {
        cockpit = new Cockpit();
    }

    @Test
    void goalBattleDefault() {
        cockpit.initGame("{\"goal\": {\"mode\": \"run\",\"checkpoints\":[{\"position\":{\"x\": 1000,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}}]}}");
        assertNull(cockpit.getGame().getGoalName());
    }

    @Test
    void goalNoMode() {
        cockpit.initGame("{\"goal\": {\"checkpoints\":[{\"position\":{\"z\" : 12 },\"shape\": {\"type\": \"circle\",\"radius\": 35}}]}}");
        assertNull(cockpit.getGame().getGoalName());
    }

}