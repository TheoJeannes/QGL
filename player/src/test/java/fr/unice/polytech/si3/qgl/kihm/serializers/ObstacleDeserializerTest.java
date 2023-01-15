package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ObstacleDeserializerTest {

    String jsonString;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = this.setObjectMapper();
        this.jsonString = """
                {
                  "goal": {
                    "mode": "REGATTA",
                    "checkpoints": [
                      {
                        "type": "checkpoint",
                        "position": {
                          "x": 4036.4115963579497,
                          "y": 264.956362612613
                        },
                        "shape": {
                          "type": "circle",
                          "radius": 100
                        }
                      },
                      {
                        "type": "checkpoint",
                        "position": {
                          "x": 6866.105430167673,
                          "y": 277.9420045045067
                        },
                        "shape": {
                          "type": "circle",
                          "radius": 100
                        }
                      },
                      {
                        "type": "checkpoint",
                        "position": {
                          "x": 8019.417976524843,
                          "y": 1210.6559684684694
                        },
                        "shape": {
                          "type": "circle",
                          "radius": 100
                        }
                      },
                      {
                        "type": "checkpoint",
                        "position": {
                          "x": 8100.5085279370105,
                          "y": 2245.8826013513526
                        },
                        "shape": {
                          "type": "circle",
                          "radius": 100
                        }
                      },
                      {
                        "type": "checkpoint",
                        "position": {
                          "x": 4652.274679791037,
                          "y": 2802.9983108108113
                        },
                        "shape": {
                          "type": "circle",
                          "radius": 100
                        }
                      }
                    ]
                  },
                  "ship": {
                    "name": "Licorne",
                    "deck": {
                      "length": 7,
                      "width": 3
                    },
                    "entities": [
                      {
                        "type": "rudder",
                        "x": 6,
                        "y": 1
                      },
                      {
                        "type": "sail",
                        "x": 3,
                        "y": 1
                      },
                      {
                        "type": "oar",
                        "x": 1,
                        "y": 2
                      },
                      {
                        "type": "oar",
                        "x": 2,
                        "y": 2
                      },
                      {
                        "type": "oar",
                        "x": 3,
                        "y": 2
                      },
                      {
                        "type": "oar",
                        "x": 5,
                        "y": 2
                      },
                      {
                        "type": "oar",
                        "x": 4,
                        "y": 2
                      },
                      {
                        "type": "oar",
                        "x": 1,
                        "y": 0
                      },
                      {
                        "type": "oar",
                        "x": 2,
                        "y": 0
                      },
                      {
                        "type": "oar",
                        "x": 4,
                        "y": 0
                      },
                      {
                        "type": "oar",
                        "x": 3,
                        "y": 0
                      },
                      {
                        "type": "oar",
                        "x": 5,
                        "y": 0
                      }
                    ],
                    "life": 1050
                  },
                  "wind": {
                    "orientation": 0,
                    "strength": 50
                  },
                  "seaEntities": [
                    {
                      "position": {
                        "x": 8153.389030097254,
                        "y": 351.1753941441404,
                        "orientation": 0.4537856055185257
                      },
                      "type": "reef",
                      "shape": {
                        "type": "rectangle",
                        "width": "400",
                        "height": "1350"
                      }
                    },
                    {
                      "position": {
                        "x": 6186.903137789905,
                        "y": 2006.7567567567567,
                        "orientation": -0.17453292519943295
                      },
                      "type": "reef",
                      "shape": {
                        "type": "rectangle",
                        "width": 500,
                        "height": "1750"
                      }
                    },
                    {
                      "position": {
                        "x": 6630.286493860851,
                        "y": 3209.4594594594596,
                        "orientation": -0.24434609527920614
                      },
                      "type": "reef",
                      "shape": {
                        "type": "rectangle",
                        "width": 500,
                        "height": "1750"
                      }
                    },
                    {
                      "position": {
                        "x": 5342.527,
                        "y": 2542.45654,
                        "orientation": -0.51645
                      },
                      "type": "ship",
                      "shape": {
                        "type": "rectangle",
                        "width": 500,
                        "height": "1750"
                      },
                      "life": 450
                    },
                    {
                      "position": {
                        "x": 7174.22818123444,
                        "y": 1102.9701576576585,
                        "orientation": 0.6981317007977318
                      },
                      "type": "reef",
                      "shape": {
                        "type": "rectangle",
                        "width": "250",
                        "height": "1200"
                      }
                    },
                    {
                      "position": {
                        "x": 7409.664698840839,
                        "y": 701.646959459459,
                        "orientation": 0.7330382858376184
                      },
                      "type": "stream",
                      "shape": {
                        "type": "rectangle",
                        "width": "400",
                        "height": "1000"
                      },
                      "strength": 10
                    },
                    {
                      "position": {
                        "x": 6384.720327421553,
                        "y": 2581.0810810810804,
                        "orientation": 3.0019663134302466
                      },
                      "type": "stream",
                      "shape": {
                        "type": "rectangle",
                        "width": 200,
                        "height": 1000
                      },
                      "strength": 10
                    }
                  ],
                  "maxRound": 500,
                  "minumumCrewSize": 12,
                  "maximumCrewSize": 12,
                  "startingPositions": [
                    {
                      "x": 2852.173913043478,
                      "y": 1978.827361563518,
                      "orientation": -1.0297442586766543
                    }
                  ]
                }
                """;
    }

    private ObjectMapper setObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().addDeserializer(Obstacle.class, new ObstacleDeserializer()));
        return objectMapper;
    }

    @Test
    void testJsonNode() {
        try {
            JsonNode node = this.objectMapper.readTree(this.jsonString);
            assertEquals(this.jsonString.replace(" ", "").replace("\n", ""), node.toString());
        } catch (Exception ignored) {
        }
    }

    @Test
    void testObstacles() {
        try {
            JsonNode node = this.objectMapper.readTree(this.jsonString);
            List<Obstacle> obstacles = objectMapper.readValue(node.get("seaEntities").toString(), new TypeReference<>() {
            });
            assertEquals(7, obstacles.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testCheckpoint() {
        try {
            JsonNode node = this.objectMapper.readTree(this.jsonString);
            List<Obstacle> checkpoints = objectMapper.readValue(node.get("goal").get("checkpoints").toString(), new TypeReference<>() {
            });
            assertEquals(5, checkpoints.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}