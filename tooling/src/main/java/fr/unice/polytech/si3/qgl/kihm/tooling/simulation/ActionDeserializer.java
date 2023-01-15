package fr.unice.polytech.si3.qgl.kihm.tooling.simulation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.actions.Moving;
import fr.unice.polytech.si3.qgl.kihm.actions.Turn;

import java.io.IOException;

import static java.util.Objects.isNull;

/**
 * Class that defines the Equipment deserializer
 */
public class ActionDeserializer extends StdDeserializer<Action> {

    public ActionDeserializer() {
        this(null);
    }

    protected ActionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Action deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String s = isNull(node.get("type")) ? "" : node.get("type").asText();
        int sailorId = isNull(node.get("sailorId")) ? 0 : node.get("sailorId").asInt();
        int xDistance = isNull(node.get("xdistance")) ? 0 : node.get("xdistance").asInt();
        int yDistance = isNull(node.get("ydistance")) ? 0 : node.get("ydistance").asInt();
        double rotation = isNull(node.get("rotation")) ? 0 : node.get("rotation").asDouble();
        return switch (s) {
            case "MOVING" -> new Moving(sailorId, xDistance, yDistance);
            case "TURN" -> new Turn(sailorId, rotation);
            default -> new Action(sailorId, Action.actionTypeEnum.valueOf(s));
        };
    }
}
