package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;

import java.awt.*;
import java.io.IOException;

import static java.util.Objects.isNull;

/**
 * The type Sailor deserializer.
 */
public class SailorDeserializer extends StdDeserializer<Sailor> {
    private final ObjectMapper om;

    /**
     * Instantiates a new Sailor deserializer.
     */
    public SailorDeserializer() {
        this(null);
    }

    /**
     * Instantiates a new Sailor deserializer.
     *
     * @param vc the vc
     */
    protected SailorDeserializer(Class<?> vc) {
        super(vc);
        om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Sailor deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Sailor s = om.readValue(node.toString(), Sailor.class);
        //Instantiates a sailor
        int x = isNull(node.get("x")) ? 0 : node.get("x").asInt();
        int y = isNull(node.get("y")) ? 0 : node.get("y").asInt();
        s.setPosition(new Point(x, y));
        //Set the position of the sailor
        return s;
    }
}
