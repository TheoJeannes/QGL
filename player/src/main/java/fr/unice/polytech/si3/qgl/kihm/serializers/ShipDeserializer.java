package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.unice.polytech.si3.qgl.kihm.equipments.Equipment;
import fr.unice.polytech.si3.qgl.kihm.ship.Deck;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * The type Ship deserializer.
 */
public class ShipDeserializer extends StdDeserializer<Ship> {
    private final ObjectMapper om;
    private final transient ShapeFunctions sf;

    /**
     * Instantiates a new Ship deserializer.
     */
    public ShipDeserializer() {
        this(null);
    }

    /**
     * Instantiates a new Ship deserializer.
     *
     * @param vc the vc
     */
    protected ShipDeserializer(Class<?> vc) {
        super(vc);
        om = new ObjectMapper();
        sf = new ShapeFunctions();
        SimpleModule equipmentModule = new SimpleModule();
        equipmentModule.addDeserializer(Equipment.class, new EquipmentDeserializer());
        om.registerModule(equipmentModule);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Define each field then create a Ship with that
     */
    @Override
    public Ship deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Ship ship = om.readValue(node.toString(), Ship.class);
        int w;
        int l;
        if (isNull(node.get("deck"))) {
            w = 0;
            l = 0;
        } else {
            w = isNull(node.get("deck").get("width")) ? 0 : node.get("deck").get("width").asInt();
            l = isNull(node.get("deck").get("length")) ? 0 : node.get("deck").get("length").asInt();
        }
        List<Equipment> list = new ArrayList<>();
        if (!isNull(node.get("entities"))) //Ajoute les Equipment a la liste
            list = om.readValue(node.get("entities").toString(), new TypeReference<>() {
            });
        list.removeIf(Objects::isNull); //Retire les equipment null
        ship.setEquipments(list); //Affecte les list au bateau
        ship.setDeck(new Deck(w, l));
        //Ajoute la forme au bateau, si elle existe
        if (!isNull(node.get("shape")))
            ship.setShape(sf.nodeToShape(ship.getPosition(), node.get("shape")));
        return ship;
    }
}
