package fr.unice.polytech.si3.qgl.kihm.tooling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.unice.polytech.si3.qgl.kihm.Cockpit;
import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.actions.Turn;
import fr.unice.polytech.si3.qgl.kihm.equipments.Oar;
import fr.unice.polytech.si3.qgl.kihm.equipments.Sail;
import fr.unice.polytech.si3.qgl.kihm.equipments.Watch;
import fr.unice.polytech.si3.qgl.kihm.landmarks.*;
import fr.unice.polytech.si3.qgl.kihm.logger.Printer;
import fr.unice.polytech.si3.qgl.kihm.serializers.ObstacleDeserializer;
import fr.unice.polytech.si3.qgl.kihm.serializers.ShapeFunctions;
import fr.unice.polytech.si3.qgl.kihm.serializers.ShipDeserializer;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.structures.Calculator;
import fr.unice.polytech.si3.qgl.kihm.structures.Game;
import fr.unice.polytech.si3.qgl.kihm.tooling.simulation.ActionDeserializer;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Map.entry;

public class Simulator {
    private final Random random = new Random();
    private final ShapeFunctions sf = new ShapeFunctions();
    private final Calculator calculator = new Calculator();
    int maxRound;
    int maximumCrewSize;
    int minimumCrewSize;
    private ObjectMapper om;
    private int roundNumber = 0;
    private Cockpit cockpit;
    private String goalString;
    private List<Obstacle> seaEntities;
    private World world;
    private boolean watchUsed;

    private boolean simulationEnded = false;

    Simulator(String init) {
        initGame(init);
    }

    private void initGame(String init) {
        om = initObjectMapper();
        try {
            JsonNode node = om.readTree(init);
            Ship ship = om.readValue(node.get("ship").toString(), Ship.class);
            List<Position> positions = om.readValue(node.get("startingPositions").toString(), new TypeReference<>() {
            });
            if (!positions.isEmpty()) ship.setPosition(positions.get(0));
            String newShapeJson = "{\"type\":\"rectangle\",\"width\":" + ship.getDeck().getWidth() + ",\"height\":" + ship.getDeck().getLength() + ",\"orientation\":" + ship.getPosition().getOrientation() + "}";
            ship.setShape(new ShapeFunctions().nodeToShape(ship.getPosition(), om.readTree(newShapeJson)));
            Wind wind = om.readValue(node.get("wind").toString(), Wind.class);
            //Utilisation de la partie goal
            List<Checkpoint> checkpoints = goalParsing(om, node.get("goal"));
            initSimulator(node, checkpoints, wind);
            cockpit = new Cockpit(new Game(goalString, new World(checkpoints, wind, new ArrayList<>()), positions.size(), ship, generateSailors(ship)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("You fool");
        }
    }

    private void initSimulator(JsonNode node, List<Checkpoint> checkpoints, Wind w) throws JsonProcessingException {
        seaEntities = om.readValue(node.get("seaEntities").toString(), new TypeReference<>() {
        });
        maximumCrewSize = node.get("maximumCrewSize").asInt();
        minimumCrewSize = node.get("minumumCrewSize").asInt();
        maxRound = node.get("maxRound").asInt();
        world = new World(checkpoints, w, seaEntities);

    }

    private List<Sailor> generateSailors(Ship ship) {
        List<Sailor> sailors = new ArrayList<>();
        int x = 0, y = 0;
        for (int i = 0; i < maximumCrewSize; i++) {
            if (y >= ship.getDeck().getWidth()) {
                x++;
                y = 0;
            }
            if (y >= ship.getDeck().getLength()) x = 0;
            sailors.add(new Sailor(i, "sailor", new Point(x, y++)));
        }
        return sailors;
    }

    private List<Checkpoint> goalParsing(ObjectMapper om, JsonNode goal) throws JsonProcessingException {
        goalString = goal.get("mode").asText();
        ArrayList<Obstacle> checkpointsTmp = new ArrayList<>();
        ArrayList<Checkpoint> checkpoints = new ArrayList<>();
        if (goalString.equals("REGATTA"))
            checkpointsTmp = om.readValue(goal.get("checkpoints").toString(), new TypeReference<>() {
            });
        checkpointsTmp.stream().filter(e -> e.getType().equals(Obstacle.obstacleTypeEnum.CHECKPOINT)).map(e -> (Checkpoint) e).forEach(checkpoints::add);
        return checkpoints;
    }

    private ObjectMapper initObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        SimpleModule entitiesModule = new SimpleModule();
        SimpleModule shipModule = new SimpleModule();
        SimpleModule actionsModule = new SimpleModule();
        shipModule.addDeserializer(Ship.class, new ShipDeserializer());
        entitiesModule.addDeserializer(Obstacle.class, new ObstacleDeserializer());
        actionsModule.addDeserializer(Action.class, new ActionDeserializer());
        om.registerModule(entitiesModule);
        om.registerModule(shipModule);
        om.registerModule(actionsModule);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    public void getActionsString(String actionsString) throws JsonProcessingException {
        List<Action> actions = om.readValue(actionsString, new TypeReference<>() {
        });
        Ship ship = moveShip(actions);
        Checkpoint nextCheckpoint = this.cockpit.getGame().getWorld().getCheckpoints().get(0);
        if (this.calculator.collision(nextCheckpoint.getShape(), ship.getShape()) || nextCheckpoint.distance(ship.getPosition()) <= nextCheckpoint.getShape().getBounds().getHeight() * 0.5) {
            this.cockpit.getGame().getWorld().getCheckpoints().remove(nextCheckpoint);
        }
    }

    private Ship moveShip(List<Action> actions) throws JsonProcessingException {
        Ship ship = cockpit.getGame().getShip();
        Map<String, Integer> oarsCount = oarMovement(ship, actions.stream().filter(e -> e.getType().equals(Action.actionTypeEnum.OAR)).toList());

        for (Sailor sailor : cockpit.getGame().getSailors()) {
            sailsOpened(ship, actions.stream().filter(e -> e.getType().equals(Action.actionTypeEnum.LIFT_SAIL)).toList(), sailor);
            sailsClosed(ship, actions.stream().filter(e -> e.getType().equals(Action.actionTypeEnum.LOWER_SAIL)).toList(), sailor);
            if (useWatch(ship, actions.stream().filter(e -> e.getType().equals(Action.actionTypeEnum.USE_WATCH)).toList(), sailor)) {
                watchUsed = true;
            }
        }
        Wind gameWind = cockpit.getGame().getWorld().getWind();
        List<Turn> turning = actions.stream().filter(e -> e.getType().equals(Action.actionTypeEnum.TURN)).map(Turn.class::cast).toList();
        // Const speeds
        int oarsLeft = oarsCount.get("left");
        int oarsRight = oarsCount.get("right");
        double openSails = (!ship.getSails().isEmpty() ? ((double) ship.getSails().stream().filter(Sail::isOpened).count() / ship.getSails().size()) : 0.0);
        double totalOars = ship.getOarLeft().size() + ship.getOarRight().size();
        double oarSpeed = 165 * (double) (oarsRight + oarsLeft) / totalOars;
        double windStrength = world.getWind().getStrength() * openSails;
        double angle = (oarsRight - oarsLeft) * Math.PI / totalOars;
        if (!turning.isEmpty()) angle += turning.get(0).getRotation();

        List<Stream> streams = seaEntities.stream().filter(obstacle -> obstacle.getType() == Obstacle.obstacleTypeEnum.STREAM).map(Stream.class::cast).toList();
        // Updating position
        ship.setPosition(calculator.calculatingNextRoundPosition(ship.getPosition(), ship.getShape(), oarSpeed, angle, windStrength, gameWind.getOrientation(), streams));

        // Updating Shape
        String newShapeJson = "{\"type\":\"rectangle\",\"width\":" + ship.getDeck().getWidth() + ",\"height\":" + ship.getDeck().getLength() + ",\"orientation\":" + ship.getPosition().getOrientation() + "}";
        cockpit.getGame().getShip().setShape(sf.nodeToShape(ship.getPosition(), om.readTree(newShapeJson)));

        // Check if Collision
        if (this.seaEntities.stream().filter(obstacle -> obstacle.getType() != Obstacle.obstacleTypeEnum.STREAM).anyMatch(obstacle -> this.calculator.collision(ship.getShape(), obstacle.getShape()))) {
            this.getCockpit().getGame().getShip().setLife(0);
            Printer.get().severe("Mission Failed - Collision with: " + this.seaEntities.stream().filter(obstacle -> obstacle.getType() != Obstacle.obstacleTypeEnum.STREAM).filter(obstacle -> this.calculator.collision(ship.getShape(), obstacle.getShape())).toList());
            Printer.get().severe("Ship location: " + ship.getPosition());
            Printer.get().flush();
        }
        return ship;
    }

    private boolean useWatch(Ship ship, List<Action> useWatch, Sailor sailor) {
        for (Action a : useWatch) {
            if (sailor.getId() == a.getSailorId()) {
                for (Watch w : ship.getWatches()) {
                    if (sailor.getPosition().equals(w.getPosition())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void sailsClosed(Ship ship, List<Action> lowerSail, Sailor sailor) {
        for (Action a : lowerSail) {
            if (sailor.getId() == a.getSailorId()) {
                for (Sail s : ship.getSails()) {
                    if (sailor.getPosition().equals(s.getPosition())) {
                        s.closeSail();
                    }
                }
            }
        }
    }

    private void sailsOpened(Ship ship, List<Action> liftSail, Sailor sailor) {
        for (Action a : liftSail) {
            if (sailor.getId() == a.getSailorId()) {
                for (Sail s : ship.getSails()) {
                    if (sailor.getPosition().equals(s.getPosition())) {
                        s.openSail();
                    }
                }

            }
        }
    }

    private Map<String, Integer> oarMovement(Ship ship, List<Action> paddle) {
        int left = 0;
        int right = 0;
        for (Sailor s : cockpit.getGame().getSailors()) {
            for (Action a : paddle) {
                if (s.getId() == a.getSailorId()) {
                    for (Oar oar : ship.getOarLeft()) {
                        if (s.getPosition().equals(oar.getPosition())) {
                            left++;
                        }
                    }
                    for (Oar oar : ship.getOarRight()) {
                        if (s.getPosition().equals(oar.getPosition())) {
                            right++;
                        }
                    }
                }
            }
        }
        return Map.ofEntries(entry("left", left), entry("right", right));
    }

    public List<Obstacle> getVisibleObstacles(Position position, double range) {
        List<Obstacle> seen = new ArrayList<>();
        Shape shown = new Ellipse2D.Double(position.getX() - range, position.getY() - range, 2 * range, 2 * range);
        for (Obstacle s : seaEntities) {
            try {
                if (this.calculator.collision(shown, s.getShape())) {
                    seen.add(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return seen;
    }

    public boolean notFinished() {
        boolean status = cockpit.getGame().getWorld().getCheckpoints().isEmpty() || this.roundNumber > this.maxRound || cockpit.getGame().getShip().getLife() <= 0;
        if (status && !this.simulationEnded) {
            Printer.get().info("=== Game has finished in " + this.roundNumber + " tours ===");
            Printer.get().flush();
            this.simulationEnded = true;
        }
        return !status;
    }

    public void run() {
        startGame();
    }

    public void run(String init) {
        initGame(init);
        startGame();
    }

    private void startGame() {
        while (notFinished() && roundNumber < maxRound) {
            nextStep();
        }
    }

    public List<Object> nextStep() {
        Ship ship = cockpit.getGame().getShip();
        World world = new World();
        try {
            String s = createNextRoundString(ship, getVisibleObstacles(ship.getPosition(), watchUsed ? 5000 : 1000));
            roundNumber++;
            getActionsString(cockpit.nextRound(s));
            world = cockpit.getGame().getWorld();
            ship = cockpit.getGame().getShip();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Printer.get().flush();
        return List.of(ship, world);
    }

    private String createNextRoundString(Ship s, List<Obstacle> visibleEntities) {
        String ship = "\"ship\":" + s.toString();
        String wind = "\"wind\":" + world.getWind().toString();
        String visibleEntitiesString = "\"visibleEntities\":" + entitiesToString(visibleEntities);
        return "{" + ship + "," + wind + "," + visibleEntitiesString + "}";
    }

    private String entitiesToString(List<Obstacle> visibleObstacles) {
        StringBuilder sb = new StringBuilder();
        for (Obstacle o : visibleObstacles) {
            sb.append(o).append(",");
        }
        String res = sb.toString();
        if (sb.length() > 0) res = sb.substring(0, sb.length() - 1);
        return "[" + res + "]";
    }


    public int getMaxRound() {
        return maxRound;
    }

    public int getMaximumCrewSize() {
        return maximumCrewSize;
    }

    public Cockpit getCockpit() {
        return cockpit;
    }

    public List<Obstacle> getSeaEntities() {
        return seaEntities;
    }

    public World getWorld() {
        return world;
    }
}
