package fr.unice.polytech.si3.qgl.kihm.structures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Wind;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;
import fr.unice.polytech.si3.qgl.kihm.logger.Printer;
import fr.unice.polytech.si3.qgl.kihm.serializers.ObstacleDeserializer;
import fr.unice.polytech.si3.qgl.kihm.serializers.SailorDeserializer;
import fr.unice.polytech.si3.qgl.kihm.serializers.ShipDeserializer;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import fr.unice.polytech.si3.qgl.kihm.ship.SailorManager;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.isNull;


/**
 * The type Parser, read Json and instantiates classes
 */
public class Parser {
    /**
     * The Object mapper.
     */
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Parser.
     */
    public Parser() {
        this.objectMapper = new ObjectMapper();
        SimpleModule shipModule = new SimpleModule();
        SimpleModule sailorsModule = new SimpleModule();
        SimpleModule entitiesModule = new SimpleModule();
        SimpleModule checkpointsModule = new SimpleModule();
        //Create all modules, sailors, ship and goal
        sailorsModule.addDeserializer(Sailor.class, new SailorDeserializer());
        shipModule.addDeserializer(Ship.class, new ShipDeserializer());
        checkpointsModule.addDeserializer(Obstacle.class, new ObstacleDeserializer());
        //Add all module to the mapper
        this.objectMapper.registerModule(shipModule);
        this.objectMapper.registerModule(sailorsModule);
        this.objectMapper.registerModule(entitiesModule);
        this.objectMapper.registerModule(checkpointsModule);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Take the path of a file, and return a string of the content
     *
     * @param path path of the file
     * @return content of the file
     */
    public static String fileToString(String path) {
        try {
            return fileToString(path, 0, Integer.MAX_VALUE);
        } catch (Exception e) {
            Printer.get().severe("Le fichier n'est pas au bon endroit i.e. chemin depuis la racine du projet nécessaire");
            return null;
        }
    }

    @SuppressWarnings("all")
    private static String fileToString(String path, int firstLine, int lastLine) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (Stream<String> in = Files.lines(Paths.get(path))) {
            final int[] i = {1};
            in.forEach(e -> {
                if (i[0] >= firstLine && i[0] <= lastLine) sb.append(e);
                i[0]++;
            });
        }
        return sb.toString();
    }

    /**
     * Read init game, and update the attribut game.
     *
     * @param initGame the init game
     */
    public Game readInitGame(String initGame) {
        Game g;
        List<Obstacle> checkpoints = new ArrayList<>();
        try {
            g = objectMapper.readValue(initGame, Game.class);
            JsonNode node = objectMapper.readTree(initGame);
            JsonNode goal = node.get("goal");
            String s = "";
            if (!isNull(goal) && !isNull(goal.get("mode")))
                s = goal.get("mode").asText();
            JsonNode ship = isNull(goal) ? null : goal.get("checkpoints");
            if (!isNull(ship) && s.equals("REGATTA"))
                checkpoints = objectMapper.readValue(ship.toString(), new TypeReference<>() {
                });
            if (s.equals("REGATTA") || s.equals("BATTLE"))
                g.setGoalName(s);
            g.setWorld(new World(checkpoints));
        } catch (JsonProcessingException e) {
            g = new Game();
        }
        g.init();
        return g;
    }

    /**
     * Read next round.
     * <p>
     * Modify the ship of the game
     *
     * @param nextRound the next round
     */
    public Game readNextRound(String nextRound, Game game) {
        try {
            JsonNode node = objectMapper.readTree(nextRound);
            JsonNode ship = node.get("ship");
            JsonNode visibleEntities = node.get("visibleEntities");
            JsonNode wind = node.get("wind");
            List<Obstacle> entities = new ArrayList<>();
            Wind w = game.getWorld().getWind();
            if (!isNull(ship)) {
                game.setShip(objectMapper.readValue(ship.toString(), Ship.class));
            }
            if (!isNull(wind)) {
                w = objectMapper.readValue(node.get("wind").toString(), Wind.class);
            }
            if (!isNull(visibleEntities)) {
                entities = objectMapper.readValue(node.get("visibleEntities").toString(), new TypeReference<>() {
                });
            }
            game.setSailorManager(new SailorManager(game.getSailors(), game.getShip()));
            game.update(w, entities);
        } catch (JsonProcessingException e) {
            Printer.get().severe("Erreur Creation d'un Vaisseau");
            return null;
        }
        return game;
    }

    /**
     * Convert actions list into a String with the json format
     *
     * @param actions list
     * @return json string
     */
    public String constructJson(List<Action> actions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getType() != null) {
                sb.append(actions.get(i).toString().replace(" ", ""));
            }
            if (i + 1 < actions.size()) {
                sb.append(",");
            }
        }
        if (!sb.isEmpty() && sb.charAt(0) == ',') {
            sb.deleteCharAt(0);
        }
        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return "[" + sb + "]";
    }
}
