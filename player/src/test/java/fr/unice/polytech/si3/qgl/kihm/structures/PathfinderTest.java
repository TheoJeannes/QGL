package fr.unice.polytech.si3.qgl.kihm.structures;

import fr.unice.polytech.si3.qgl.kihm.equipments.Equipment;
import fr.unice.polytech.si3.qgl.kihm.equipments.Oar;
import fr.unice.polytech.si3.qgl.kihm.equipments.Rudder;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Checkpoint;
import fr.unice.polytech.si3.qgl.kihm.ship.Deck;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathfinderTest {
    List<Equipment> equipment;
    Pathfinder pf;
    Ship sh;
    Checkpoint c, backward, left, right, forward, massive;
    ArrayList<Checkpoint> checkpoints;

    @BeforeEach
    void setUp() {
        equipment = new ArrayList<>();
        Oar oar1 = new Oar(0,0);
        Oar oar2 = new Oar(0,2);
        Oar oar3 = new Oar(1,0);
        Oar oar4 = new Oar(1,2);
        Rudder rud = new Rudder(2,3);
        equipment.add(oar1);
        equipment.add(oar2);
        equipment.add(oar3);
        equipment.add(oar4);
        equipment.add(rud);
        sh = new Ship("s", 10, new Position(0, 0), new Rectangle2D.Double(10, 10, 20, 20), new Deck(3, 4), equipment);
        pf = new Pathfinder(sh);
        c = new Checkpoint(new Position(300, 20, 0.5), new Rectangle2D.Double(10, 10, 20, 20));
        right = new Checkpoint(new Position(100, -200), new Rectangle2D.Double(6, -24, 8, 8));
        backward = new Checkpoint(new Position(-800, 0, 0), new Rectangle2D.Double(-10, -4, 8, 8));
        left = new Checkpoint(new Position(0, 200, 0), new Rectangle2D.Double(-4, 16, 8, 8));
        forward = new Checkpoint(new Position(200, -20), new Rectangle2D.Double(196, -24, 8, 8));
        sh.setEquipments(List.of(new Oar(0, 0), new Oar(0, 2), new Oar(1, 0), new Oar(1, 2)));
        checkpoints = new ArrayList<>();
        checkpoints.add(c);
        checkpoints.add(backward);
        checkpoints.add(left);
        checkpoints.add(right);
        checkpoints.add(forward);
        this.massive = new Checkpoint(new Position(10000, 10000), new Ellipse2D.Double(10000, 10000, 5000, 5000));
    }

    @Test
    void calculAngle() {
        assertEquals(0.06656816377582381, pf.calculateAngle(c));
    }

    @Test
    void calculAngleBack() {
        assertEquals(3.141592653589793, pf.calculateAngle(backward));
    }

    @Test
    void calculAngleLeft() {
        assertEquals(1.5707963267948966, pf.calculateAngle(left));
    }

    @Test
    void equipmentNoSailors() {
        sh.setEquipments(List.of(new Oar(0, 0), new Oar(0, 2)));
        Layout d = pf.getLayout(pf.calculateAngle(c), 0);
        assertEquals(0, d.getNumberOarLeft());
        assertEquals(0, d.getNumberOarRight());
    }

    @Test
    void angleBehind() {
        Layout d = pf.getLayout(pf.calculateAngle(backward), 5);
        assertEquals(0, d.getNumberOarLeft());
        assertEquals(2, d.getNumberOarRight());
    }

    @Test
    void equipmentLeft() {
        Layout d = pf.getLayout(pf.calculateAngle(left), 5);
        assertEquals(0, d.getNumberOarLeft());
        assertEquals(2, d.getNumberOarRight());
    }

    @Test
    void angleRight() {
        Layout d = pf.getLayout(pf.calculateAngle(right), 5);
        assertEquals(2, d.getNumberOarLeft());
        assertEquals(1, d.getNumberOarRight());
    }

    @Test
    void forwardAngle() {
        Layout d = pf.getLayout(pf.calculateAngle(forward), 5);
        assertEquals(2, d.getNumberOarLeft());
        assertEquals(2, d.getNumberOarRight());
        assertEquals(0.0, d.getAngleRudder());
    }

    @Test
    void newPositionTest(){
        Layout layout = pf.getLayout(pf.calculateAngle(forward), 5);
        assertEquals(new Position(165.0000000000003, 0.0, 0.0), this.pf.nextRoundPosition(layout));
    }

    @Test
    void getShip(){
        assertEquals(sh, pf.getShip());
    }

    @Test
    void distanceTravelTest(){
        Layout layout = pf.getLayout(pf.calculateAngle(forward), 5);
        assertEquals(165.0000000000003, this.pf.travelDistance(layout));
    }

    @Test
    void favorableWind(){
        assertTrue(this.pf.isWindFavorable(0.0, 45.0));
    }

    @Test
    void notFavorableWind(){
        assertTrue(this.pf.isWindFavorable(0.0, 100.0));
    }

}
