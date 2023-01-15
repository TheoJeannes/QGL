package fr.unice.polytech.si3.qgl.kihm.tooling.GUI;

import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


//TODO Definir le zoom en fonction de mid

public class MapPanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    private final List<Obstacle> worldEntities;
    private final MainApp mainApp;
    private final List<List<Obstacle>> visibleEntities;
    private final List<Ship> ships;
    private final List<World> worlds;
    private double zoomFactor = 1;
    private double prevZoomFactor = 1;
    private boolean released;
    private boolean dragger;
    private Point startPoint;
    private double xOffset = 0;
    private double yOffset = 0;
    private int xDiff;
    private int yDiff;
    private double width;
    private boolean zoomer = true;
    private int currentTurn;
    private int maxTurn;
    private double height;
    private boolean init = false;

    public MapPanel(World world, Ship ship, MainApp mainApp) {
        this.mainApp = mainApp;
        this.ships = new ArrayList<>(List.of(ship));
        this.worldEntities = new ArrayList<>();
        worldEntities.addAll(world.getEntities());
        worldEntities.addAll(world.getCheckpoints());
        visibleEntities = new ArrayList<>();
        worlds = new ArrayList<>(List.of(world));
        initComponent();
        currentTurn = 0;
        maxTurn = 0;
    }

    private void initComponent() {
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void addTurn(Ship ship, World entities) {
        ships.add(ship);
        worlds.add(entities);
        visibleEntities.add(entities.getEntities());
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //Definir le fond + Marge ajoutée
        g2d.setColor(WorldColors.ocean.getColor());
        g2d.fill(new Rectangle2D.Double(0, 0, this.getWidth() * 4, this.getHeight() * 4));
        transformScale(g2d);
        //Definir le centre des obstacles, pour tout afficher en coords positives
        drawWorld(g2d);
    }

    private void transformScale(Graphics2D g2d) {
        if (zoomer) {
            AffineTransform at = new AffineTransform();

            double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
            double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

            double zoomDiv = zoomFactor / prevZoomFactor;

            xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
            yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

            at.translate(xOffset, yOffset);
            at.scale(zoomFactor, zoomFactor);
            prevZoomFactor = zoomFactor;
            g2d.transform(at);
            zoomer = false;
        }
        if (dragger) {
            AffineTransform at = new AffineTransform();
            at.translate(xOffset + xDiff, yOffset + yDiff);
            at.scale(zoomFactor, zoomFactor);
            g2d.transform(at);

            if (released) {
                xOffset += xDiff;
                yOffset += yDiff;
                dragger = false;
            }
        }
    }

    private void drawWorld(Graphics2D g2d) {
        if (!worldEntities.isEmpty()) {
            translateCenter(g2d);
            drawObstacles(g2d, worldEntities, false);
            if (currentTurn >= 1) {
                int c = currentTurn - 1;
                drawObstacles(g2d, visibleEntities.get(c), true);
//                drawGrid(g2d,ships.get(c),worlds.get(c));
//                drawPath(g2d, ships.get(c), worlds.get(c));
            }
            drawShips(g2d);
        }
    }


    private void translateCenter(Graphics2D g2d) {
        double maxX, minX = maxX = ships.get(currentTurn).getPosition().getX();
        double maxY, minY = maxY = ships.get(currentTurn).getPosition().getY();
        for (Obstacle entity : worldEntities) {
            minX = Math.min(minX, entity.getX());
            minY = Math.min(minY, entity.getY());
            maxX = Math.max(maxX, entity.getX());
            maxY = Math.max(maxY, entity.getY());
        }
        width = (this.getWidth() / 2.) - ((minX + maxX) / 2) + 50;
        height = (this.getHeight() / 2.) - ((minY + maxY) / 2) + 50;

        double inGameDiagonalDistance = Math.sqrt(Math.pow(maxX - minY, 2) + Math.pow(maxY - minY, 2));
        double gameSizeDiagonalDistance = Math.sqrt(Math.pow(this.mainApp.getSize().getWidth(), 2) + Math.pow(this.mainApp.getSize().getHeight(), 2));

        if (!init) {
            init = true;
            this.zoomFactor = gameSizeDiagonalDistance / inGameDiagonalDistance;
            this.prevZoomFactor = this.zoomFactor;
            // TODO Update on reset and Start
        }

        //Dessiner tout les obstacles et le bateau
        g2d.translate(width, height);
    }

    private void drawShips(Graphics2D g2d) {
        g2d.setColor(WorldColors.ship.getColor());
        Ship s = ships.get(currentTurn);
        g2d.fill(s.getShape());
        int i = 0;
        while (i < currentTurn) {
            g2d.setColor(WorldColors.line.getColor());
            g2d.setStroke(new BasicStroke());
            Position prev = ships.get(i).getPosition();
            Position next = ships.get(++i).getPosition();
            g2d.drawLine((int) prev.getX(), (int) prev.getY(), (int) next.getX(), (int) next.getY());
            g2d.setColor(Color.BLACK);
            g2d.draw(ships.get(i).getShape());
        }
    }

    private void drawObstacles(Graphics2D g2d, List<Obstacle> entities, boolean draw) {
        for (Obstacle e : entities) {
            Color color = switch (e.getType()) {
                case CHECKPOINT -> WorldColors.checkpoint.getColor();
                case STREAM -> WorldColors.stream.getColor();
                case REEF -> WorldColors.reef.getColor();
                case SHIP -> WorldColors.ship.getColor();
            };

            if (e.getType().equals(Obstacle.obstacleTypeEnum.CHECKPOINT)) {
                g2d.setColor(color);
                g2d.fill(e.getShape());
            } else if (draw) {
                g2d.setColor(color.brighter());
                g2d.fill(e.getShape());
            } else {
                g2d.setStroke(new BasicStroke(5));
                g2d.setColor(color.darker());
                g2d.draw(e.getShape());
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        zoomer = true;

        //Zoom in
        if (e.getWheelRotation() < 0) {
            zoomFactor *= 1.1;
            repaint();
        }
        //Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor /= 1.1;
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point curPoint = e.getLocationOnScreen();
        xDiff = curPoint.x - startPoint.x;
        yDiff = curPoint.y - startPoint.y;

        dragger = true;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        double xCoords = Math.round(((e.getX() - xOffset) / zoomFactor - width) * 1000.) / 1000.;
        double yCoords = Math.round(((e.getY() - yOffset) / zoomFactor - height) * 1000.) / 1000.;

        mainApp.coordsText.setText("| X : " + String.format("%.2f", xCoords) + ", Y : " + String.format("%.2f", yCoords));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        released = false;
        startPoint = MouseInfo.getPointerInfo().getLocation();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        released = true;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int getMaxTurn() {
        return maxTurn;
    }

    public void updateTurnCount(int increment) {
        currentTurn += increment;
        if (maxTurn < currentTurn) maxTurn = currentTurn;
        zoomer = true;
    }
}
