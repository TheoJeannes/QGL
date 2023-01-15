package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.unice.polytech.si3.qgl.kihm.equipments.*;

import java.io.IOException;

import static java.util.Objects.isNull;

/**
 * Class that defines the Equipment deserializer
 */
public class EquipmentDeserializer extends StdDeserializer<Equipment> {

    public EquipmentDeserializer() {
        this(null);
    }

    protected EquipmentDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Equipment deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String s = isNull(node.get("type")) ? "" : node.get("type").asText();
        int x = isNull(node.get("x")) ? 0 : node.get("x").asInt();
        int y = isNull(node.get("y")) ? 0 : node.get("y").asInt();
        @SuppressWarnings("all")
        boolean opened = (!isNull(node.get("openned")) && node.get("openned").asBoolean()) || (!isNull(node.get("opened")) && node.get("opened").asBoolean());
        return switch (s) {
            case "oar" -> new Oar(x, y);
            case "sail" -> new Sail(x, y, opened);
            case "rudder" -> new Rudder(x, y);
            case "watch" -> new Watch(x, y);
            default -> null;
        };
    }
}
