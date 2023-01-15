package fr.unice.polytech.si3.qgl.kihm.structures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.kihm.equipments.Sail;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Stream;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Wind;
import fr.unice.polytech.si3.qgl.kihm.logger.Printer;
import fr.unice.polytech.si3.qgl.kihm.serializers.ShapeFunctions;
import fr.unice.polytech.si3.qgl.kihm.ship.Deck;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.List;

/**
 * Class that calculates if we are in collision with an entity.
 */
public class Calculator {

    /**
     * Strength of oars.
     */
    public static final double OAR_STRENGTH = 165;
    /**
     * Number of iteration we divide one round in.
     */
    private static final int ITERATION = 100;
    /**
     * At what angle do we consider a stream as an obstacle.
     */
    private static final double MAX_ANGLE_STREAM = 135;

    /**
     * Calculates the position for the next round. We divide our turn in 100 iterations.
     *
     * @param position        The current position of the ship.
     * @param shape           The shape of the ship.
     * @param oarSpeed        The oar speed.
     * @param turningAngle    The turning angle.
     * @param windStrength    The strength of the wind.
     * @param windOrientation The orientation of the wind.
     * @param streams         The list of streams in the world.
     * @return The position calculated for the next round.
     */
    public Position calculatingNextRoundPosition(Position position, Shape shape, double oarSpeed, double turningAngle, double windStrength, double windOrientation, List<Stream> streams) {
        Position pos = new Position(position.getX(), position.getY(), position.getOrientation());
        for (int i = 0; i < ITERATION; i++) {
            double windSpeed = windStrength * Math.cos(windOrientation - pos.getOrientation());
            double xMovement = ((oarSpeed + windSpeed) / ITERATION) * Math.cos(pos.getOrientation());
            double yMovement = ((oarSpeed + windSpeed) / ITERATION) * Math.sin(pos.getOrientation());

            oarAndWindEffect(pos, xMovement, yMovement);
            rotationEffect(pos, turningAngle);
            if (!streams.isEmpty()) streamEffect(shape, pos, streams);
        }
        return pos;
    }

    /**
     * Checks if the ship is in collision with an entity.
     *
     * @param position         The position of the ship.
     * @param visibleObstacles The list of the visible obstacles.
     * @return True if we are in collision with a visible entity, false otherwise.
     */
    public boolean collisionWithEntity(double antennaLength, Position position, double angle, Deck deck, List<Obstacle> visibleObstacles) {
        Position posTmp = new Position(position.getX(), position.getY(), position.getOrientation());
        List<Obstacle> obstructions = visibleObstacles.stream().filter(obstacle -> this.isObstacleToAvoid(obstacle, position.getOrientation())).toList();

        posTmp.rotate(angle);
        double width = deck.getWidth() + 20.;
        Shape newShape = null;
        try {
            JsonNode node = new ObjectMapper().readTree("{\"type\": \"rectangle\",\"width\": " + width + ",\"height\": " + antennaLength + "}");
            newShape = new ShapeFunctions().nodeToShape(posTmp, node);
        } catch (Exception e) {
            Printer.get().severe(e.getMessage());
            e.printStackTrace();
        }
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToTranslation(Math.cos(posTmp.getOrientation()) * 0.5 * (antennaLength - deck.getLength()), Math.sin(posTmp.getOrientation()) * 0.5 * (antennaLength - deck.getLength()));
        newShape = affineTransform.createTransformedShape(newShape);

        Shape finalNewShape = newShape;
        return obstructions.stream().anyMatch(obstacle -> this.collision(finalNewShape, obstacle.getShape()));
    }

    /**
     * The effect of a rotation on a position.
     *
     * @param pos          The position.
     * @param turningAngle The angle it needs to turn.
     */
    private void rotationEffect(Position pos, double turningAngle) {
        pos.rotate(turningAngle / ITERATION);
    }

    /**
     * Apply the travel distance of the wind and oars to the position
     *
     * @param pos       Position to be changed
     * @param xMovement Travel on X-axis
     * @param yMovement Travel on Y-axis
     */
    private void oarAndWindEffect(Position pos, double xMovement, double yMovement) {
        pos.move(xMovement, yMovement);
    }

    /**
     * Apply the travel distance of the stream to the position.
     *
     * @param s       The shape of the ship.
     * @param pos     The position of the ship.
     * @param streams The list of streams.
     */
    private void streamEffect(Shape s, Position pos, List<Stream> streams) {
        double streamSpeedX;
        double streamSpeedY;
        for (Stream stream : streams.stream().filter(stream -> this.collision(s, stream.getShape())).toList()) {
            streamSpeedX = (stream.getStrength() / ITERATION) * Math.cos(stream.getPosition().getOrientation());
            streamSpeedY = (stream.getStrength() / ITERATION) * Math.sin(stream.getPosition().getOrientation());
            pos.moveX(streamSpeedX);
            pos.moveY(streamSpeedY);
        }
    }

    /**
     * Calculates the speed of the oars with a given layout.
     *
     * @param layout    The given layout.
     * @param totalOars The total number of oars.
     * @return The oar speed.
     */
    public double calculateOarSpeed(Layout layout, int totalOars) {
        return OAR_STRENGTH * (layout.getNumberOarRight() + layout.getNumberOarLeft()) / totalOars;
    }

    /**
     * Calculates the angle that the ship will do with the given layout.
     *
     * @param layout    The given layout.
     * @param totalOars The total number of oars.
     * @return The turning angle.
     */
    public double calculateTurningAngle(Layout layout, int totalOars) {
        return (layout.getNumberOarRight() - layout.getNumberOarLeft()) * Math.PI / totalOars + layout.getAngleRudder();
    }

    /**
     * Calculates the strength of the wind with the use of sails.
     *
     * @param wind  The wind in the game.
     * @param sails The list of the sails.
     * @return The strength that the wind gives us.
     */
    public double calculateWindStrength(Wind wind, List<Sail> sails) {
        return wind.getStrength() * (sails.isEmpty() ? 0 : sails.stream().filter(Sail::isOpened).count() / (double) sails.size());
    }

    /**
     * Finds if the obstacle is in our way and needs to be avoided.
     *
     * @param obstacle    The obstacle we need to avoid.
     * @param orientation The orientation of the ship.
     * @return True if the obstacle needs to be avoided, false otherwise.
     */
    public boolean isObstacleToAvoid(Obstacle obstacle, double orientation) {
        if (obstacle.getType() == Obstacle.obstacleTypeEnum.STREAM) {
            double angleBetween = Math.abs(Math.toDegrees(orientation) - Math.toDegrees(obstacle.getPosition().getOrientation()));
            return angleBetween >= MAX_ANGLE_STREAM && angleBetween <= 360 - MAX_ANGLE_STREAM;
        } else {
            return obstacle.getType() != Obstacle.obstacleTypeEnum.CHECKPOINT;
        }
    }

    /**
     * Determines if two shapes intersect and are in collision.
     *
     * @param a The first shape.
     * @param b The second shape.
     * @return True if the shapes are in collision.
     */
    public boolean collision(Shape a, Shape b) {
        Area zone = new Area(a);
        zone.intersect(new Area(b));
        return !zone.isEmpty();
    }
}
