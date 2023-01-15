package fr.unice.polytech.si3.qgl.kihm;

import fr.unice.polytech.si3.qgl.kihm.equipments.Oar;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Wind;
import fr.unice.polytech.si3.qgl.kihm.structures.Game;
import fr.unice.polytech.si3.qgl.kihm.structures.Parser;
import fr.unice.polytech.si3.qgl.kihm.structures.Pathfinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class ParserTest {
    Parser p;
    Cockpit c;

    @BeforeEach
    void setUp() {
        c = new Cockpit();
        p = new Parser();
    }

    @Test
    void constructJsonEmpty() {
        assertEquals("[]", p.constructJson(new ArrayList<>()));
    }

    @Test
    void nextRoundCreationErrorTest() {
        c.initGame("{\"goal\":{\"mode\":\"REGATTA\", \"checkpoints\":[{\"position\":{\"x\":-6, \"y\":0, \"orientation\":0}, \"shape\":{\"type\":\"circle\", \"radius\":4}}]}, \"ship\":{\"type\":\"ship\", \"life\":100, \"position\":{\"x\":0, \"y\":0, \"orientation\":0}, \"name\":\"Lescopaingsd'abord!\", \"deck\":{\"width\":4, \"length\":2}, \"entities\":[{\"type\":\"oar\", \"x\":0, \"y\":0}, {\"type\":\"oar\", \"x\":1, \"y\":1}], \"shape\":{\"type\":\"polygon\", \"vertices\":[{\"x\":3, \"y\":1}, {\"x\":2, \"y\":1}, {\"x\":2, \"y\":2}], \"orientation\":0}}, \"sailors\":[{\"x\":0, \"y\":0, \"id\":0, \"name\":\"EdwardTeach\"}, {\"x\":0, \"y\":0, \"id\":1, \"name\":\"EdwardPouce\"}, {\"x\":1, \"y\":1, \"id\":2, \"name\":\"TomPouce\"}, {\"x\":1, \"y\":1, \"id\":3, \"name\":\"JackTeach\"}], \"shipCount\":1}");
        assertNull(p.readNextRound("Provoking the Error", c.getGame()));
    }

    @Test
    void nextRoundWindAndEntites() {
        Oar oar = new Oar(1, 1);
        oar.setOccupied(true);
        c.initGame("{\"goal\":{\"mode\":\"REGATTA\", \"checkpoints\":[{\"position\":{\"x\":-6, \"y\":0, \"orientation\":0}, \"shape\":{\"type\":\"circle\", \"radius\":4}}]}, \"ship\":{\"type\":\"ship\", \"life\":100, \"position\":{\"x\":0, \"y\":0, \"orientation\":0}, \"name\":\"Lescopaingsd'abord!\", \"deck\":{\"width\":4, \"length\":2}, \"entities\":[{\"type\":\"oar\", \"x\":0, \"y\":0}, {\"type\":\"oar\", \"x\":1, \"y\":1}], \"shape\":{\"type\":\"polygon\", \"vertices\":[{\"x\":3, \"y\":1}, {\"x\":2, \"y\":1}, {\"x\":2, \"y\":2}], \"orientation\":0}}, \"sailors\":[{\"x\":0, \"y\":0, \"id\":0, \"name\":\"EdwardTeach\"}, {\"x\":0, \"y\":0, \"id\":1, \"name\":\"EdwardPouce\"}, {\"x\":1, \"y\":1, \"id\":2, \"name\":\"TomPouce\"}, {\"x\":1, \"y\":1, \"id\":3, \"name\":\"JackTeach\"}], \"shipCount\":1}");
        p.readNextRound("{\"ship\":{\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": -200,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 3,\"length\": 4},\"entities\": [{\"type\": \"oar\",\"x\": 2,\"y\": 0},{\"type\": \"oar\",\"x\": 1,\"y\": 2}],\"shape\": {\"type\": \"rectangle\",\"width\": 3,\"height\": 6,\"orientation\": 0}}, \"wind\" : {\"orientation\": 0.8, \"strength\":10 }, \"visibleEntities\":[{\"type\":\"stream\", \"position\":{\"x\":2953.063885267276, \"y\":944.0104166666712, \"orientation\":2.3387411976724013}, \"shape\":{\"type\":\"polygon\", \"vertices\":[{\"x\":-1239.8085, \"y\":887.9785}, {\"x\":-844.1715, \"y\":1270.0415}, {\"x\":1239.8085, \"y\":-887.9785}, {\"x\":844.1715, \"y\":-1270.0415}], \"orientation\":-2.3387411976724013}}]}", c.getGame());
        assertEquals(1, c.getGame().getWorld().getEntities().size());
        assertEquals(new Wind(0.8, 10), c.getGame().getWorld().getWind());
    }

    @Test
    void fileToStringTest() {
        assertNotEquals(new Parser().toString(), this.p.toString());
        assertNotEquals("", this.p.toString());
        assertNull(Parser.fileToString("tooling/src/main/resources/Games/week1/initGame.json"));
        assertNotNull(Parser.fileToString("src/main/java/fr/unice/polytech/si3/qgl/kihm/structures/Parser.java"));
    }

    @Test
    void readInitGameEmpty() {
        assertEquals(new Game(), p.readInitGame(""));
    }

    @Test
    void readInitGameName() {
        c.initGame("""
                {
                  "goal": {
                    "mode": "REGATTA",
                    "checkpoints": [
                      {
                        "position": {
                          "x": -869.5652173913059,
                          "y": 2052.1172638436483
                        },
                        "type": "checkpoint",
                        "shape": {
                          "type": "circle",
                          "radius": 100
                        }
                      }
                    ]
                  }}""");

        assertEquals("REGATTA", c.getGame().getGoalName());
    }

}
