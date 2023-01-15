package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Checkpoint;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.landmarks.OtherShip;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Stream;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;
import java.awt.geom.Area;
import java.io.IOException;

import static java.util.Objects.isNull;

/**
 * Class that defines the obstacle deserializer
 */
public class ObstacleDeserializer extends StdDeserializer<Obstacle> {
    private final transient ShapeFunctions sf;
    private final ObjectMapper om;

    public ObstacleDeserializer() {
        this(null);
    }

    protected ObstacleDeserializer(Class<?> vc) {
        super(vc);
        sf = new ShapeFunctions();
        om = new ObjectMapper();
    }

    @Override
    public Obstacle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String type = isNull(node.get("type")) ? "" : node.get("type").asText();
        Position position = isNull(node.get("position")) ? new Position() : om.readValue(node.get("position").toString(), Position.class);
        Shape shape = isNull(node.get("shape")) ? new Area() : sf.nodeToShape(position, node.get("shape"));
        int life = isNull(node.get("life")) ? 0 : node.get("life").asInt();
        int strength = isNull(node.get("strength")) ? 0 : node.get("strength").asInt();
        return switch (type) {
            case "reef" -> new Obstacle(position, shape);
            case "ship" -> new OtherShip(position, shape, life);
            case "stream" -> new Stream(position, shape, strength);
            default -> new Checkpoint(position, shape);
        };
    }
}
