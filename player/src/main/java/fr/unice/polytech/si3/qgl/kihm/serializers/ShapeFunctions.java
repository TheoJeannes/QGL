package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.utilities.PointDouble;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Functions to deal with transforming nodes into shapes
 */
public class ShapeFunctions {
    private static final String OR = "orientation";
    private final ObjectMapper om;

    public ShapeFunctions() {
        om = new ObjectMapper();
    }

    public Shape nodeToShape(Position p, JsonNode node) throws JsonProcessingException {
        Shape shape;
        String s = isNull(node.get("type")) ? "" : node.get("type").asText();
        shape = switch (s) {
            case "polygon" -> nodeToPolygon(p, node);
            case "circle" -> nodeToCircle(p, node);
            case "rectangle" -> nodeToPolygon(p, rectangleToPolygon(node));
            default -> new Area();
        };
        return shape;
    }

    /**
     * Transform a polygon-typed node into a Path2D
     *
     * @param position center and orientation of the shape
     * @param s        the node
     * @return a closed path representing the shape
     * @throws JsonProcessingException if readValue fails
     */
    private Path2D.Double nodeToPolygon(Position position, JsonNode s) throws JsonProcessingException {
        double orientation = position.getOrientation() + (isNull(s.get(OR)) ? 0 : s.get(OR).asDouble());
        double rounded = Math.pow(10, 5);
        double cos = Math.round(Math.cos(orientation) * rounded) / rounded;
        double sin = Math.round(Math.sin(orientation) * rounded) / rounded;
        //Calcul de la matrice de rotation
        double[][] matOrientation = {{cos, -sin}, {sin, cos}};
        List<PointDouble> vertices = om.readValue(s.get("vertices").toString(), new TypeReference<>() {
        });
        //On modifie chaque point, d'abord en l'orientant
        for (PointDouble p : vertices) {
            p.multiplyMatrix(matOrientation);
            //Puis en le translatant depuis l'origine
            p.move(position.getX(), position.getY());
        }
        //Construction du chemin
        Path2D.Double path = new Path2D.Double();
        path.moveTo(vertices.get(0).getX(), vertices.get(0).getY());
        if (!vertices.isEmpty()) vertices.remove(0);
        vertices.forEach(e -> path.lineTo(e.getX(), e.getY()));
        path.closePath();
        return path;
    }

    /**
     * Transform a shape into a list of points
     *
     * @param shape any class implementing Shape
     * @return list of points
     */
    public String shapeToString(Shape shape) {
        if (shape.getClass().equals(Ellipse2D.Double.class))
            return circleToString((Ellipse2D.Double) shape);
        return polygonToString(shape);
    }

    private String circleToString(Ellipse2D.Double shape) {
        double radius = shape.getHeight() / 2;
        return (shape.getX() + radius) + "," + (shape.getY() + radius) + "\nRadius :" + radius;
    }

    /**
     * @param shape a polygon
     * @return list of points
     */
    private String polygonToString(Shape shape) {
        StringBuilder res = new StringBuilder();
        PathIterator p = shape.getPathIterator(new AffineTransform());
        while (!p.isDone()) {
            double[] tmp = new double[6];
            p.currentSegment(tmp);
            double x = Math.round(tmp[0] * 100000.) / 100000.;
            double y = Math.round(tmp[1] * 100000.) / 100000.;
            res.append(x).append(",").append(y).append("\n");
            p.next();
        }
        if (!res.isEmpty()) {//Enlever le dernier retour a la ligne
            res.deleteCharAt(res.length() - 1);
        }
        return res.substring(0, Math.max(0, res.lastIndexOf("\n")));//Enlever la derniere ligne (0 0)
    }

    /**
     * Transform a rectangle-typed node into a polygon-typed node
     *
     * @param s rectangle
     * @return polygon
     * @throws JsonProcessingException if readTree fails
     */
    private JsonNode rectangleToPolygon(JsonNode s) throws JsonProcessingException {
        double orientation = (isNull(s.get(OR)) ? 0 : s.get(OR).asDouble());
        double height = (isNull(s.get("width")) ? 0 : s.get("width").asDouble());
        double width = (isNull(s.get("height")) ? 0 : s.get("height").asDouble());
        String x = "\"x\":";
        String y = ",\"y\":";
        //Verification que les valeurs existent, 0 sinon
        return om.readTree("{\"type\":\"polygon\",\"vertices\":" + "[{" + x + (width / 2) + y + (height / 2) + "}," + "{" + x + (width / 2) + y + -(height / 2) + "}," + "{" + x + -(width / 2) + y + -(height / 2) + "}," + "{" + x + -(width / 2) + y + (height / 2) + "}]," + "\"orientation\":" + orientation + "}");
    }

    /**
     * Transform a circle-typed node into an Ellipse2D
     *
     * @param p center and orientation of the shape
     * @param s the node
     * @return an Ellipse representing the shape
     */
    private Ellipse2D.Double nodeToCircle(Position p, JsonNode s) {
        double radius = isNull(s.get("radius")) ? 0 : s.get("radius").asDouble();
        //Verification que les valeurs existent, 0 sinon
        return new Ellipse2D.Double(p.getX() - radius, p.getY() - radius, radius * 2, radius * 2);
    }

    public Shape scaleObstacle(Obstacle o, double scale) {
        return scale(o.getShape(), o.getPosition(), scale);
    }

    public Shape scale(Shape shape, Position p, double scale) {
        if (shape.getClass().equals(Ellipse2D.Double.class))
            return scaleCircle((Ellipse2D.Double) shape, p, scale);
        AffineTransform at = new AffineTransform();
        shape = sendToOrigin(p, shape);
        at.setToScale(scale, scale);
        shape = at.createTransformedShape(shape);//Agrandi la forme
        at.setToTranslation(p.getX(), p.getY());
        shape = at.createTransformedShape(shape);//Redeplacer la forme a son point d'origine
        return shape;
    }

    private Shape sendToOrigin(Position p, Shape shape) {
        AffineTransform at = new AffineTransform();
        at.translate(-p.getX(), -p.getY());
        shape = at.createTransformedShape(shape);//Centrer la forme en 0,0
        return shape;
    }

    private Ellipse2D.Double scaleCircle(Ellipse2D.Double shape, Position p, double scale) {
        double radius = shape.getHeight() * scale / 2;
        try {
            return nodeToCircle(p, om.readTree("{\n" +
                    "          \"type\": \"circle\",\n" +
                    "          \"radius\": " + radius + "\n" +
                    "        }"));
        } catch (JsonProcessingException e) {
            return shape;
        }
    }

    public Shape move(Shape shape, Position origin, Position end) {
        if (shape.getClass().equals(Ellipse2D.Double.class)) {
            return scaleCircle((Ellipse2D.Double) shape, end, 1);
        }
        AffineTransform at = new AffineTransform();
        shape = sendToOrigin(origin, shape);
        at.setToTranslation(end.getX(), end.getY());
        return at.createTransformedShape(shape);
    }

    public String shapeToNode(Shape shape, Position origin) {
        if (shape.getClass().equals(Ellipse2D.Double.class))
            return circleToNode(((Ellipse2D.Double) shape).getWidth());
        return polygonToNode(shape, origin);
    }

    private String polygonToNode(Shape shape, Position origin) {
        StringBuilder sb = new StringBuilder();
        List<PointDouble> vertices = new ArrayList<>();
        shape = sendToOrigin(origin, shape);
        PathIterator path = shape.getPathIterator(new AffineTransform());
        while (!path.isDone()) {
            double[] tmp = new double[6];
            path.currentSegment(tmp);
            double x = Math.round(tmp[0] * 100000.) / 100000.;
            double y = Math.round(tmp[1] * 100000.) / 100000.;
            vertices.add(new PointDouble(x, y));
            path.next();
        }
        vertices = vertices.subList(0, vertices.size() - 1);
        vertices.forEach(e -> sb.append("{\"x\":").append(e.getX()).append(", \"y\":").append(e.getY()).append("},"));
        return "{\"type\":\"polygon\",\"vertices\":[" + sb.substring(0, sb.length() - 1) + "],\"orientation\":" + (-origin.getOrientation()) + "}";
    }

    private String circleToNode(double diameter) {
        return "{\"type\":\"circle\",\"radius\":" + diameter / 2 + "}";

    }
}
