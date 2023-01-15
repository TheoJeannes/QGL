package fr.unice.polytech.si3.qgl.kihm.ship;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.actions.Moving;
import fr.unice.polytech.si3.qgl.kihm.equipments.Equipment;
import fr.unice.polytech.si3.qgl.kihm.equipments.Oar;
import fr.unice.polytech.si3.qgl.kihm.equipments.Sail;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static fr.unice.polytech.si3.qgl.kihm.equipments.Equipment.equipmentTypeEnum;

/**
 * Defines the movement of the sailors
 */
public class SailorManager {
    /**
     * Number of movement a sailor can do in a turn
     */
    public static final int MOVEMENT = 5;

    /**
     * The list of sailors on the ship
     */
    private final List<Sailor> sailors;

    /**
     * The current ship
     */
    private Ship ship;

    /**
     * The constructor of the sailor manager
     *
     * @param sailors the sailors that are on the ship
     * @param ship    the current ship
     */
    public SailorManager(List<Sailor> sailors, Ship ship) {
        this.sailors = sailors;
        this.ship = ship;
    }

    public SailorManager(SailorManager sm) {
        sailors = new ArrayList<>();
        sm.getSailors().forEach(s -> sailors.add(new Sailor(s)));
        this.ship = new Ship(sm.getShip());
    }

    /**
     * Move the sailor to a certain position and send the information to the JSon
     *
     * @param sailor the sailor that moves
     * @param x      the x position
     * @param y      the y position
     * @return the action of moving that we send to the JSon
     */
    public Action move(Sailor sailor, int x, int y) {
        if (x == 0 && y == 0) {
            return null;
        }
        int distance = Math.abs(x) + Math.abs(y);

        if (distance > MOVEMENT) {
            boolean xNegative = x < 0;
            boolean yNegative = y < 0;
            x = Math.min(Math.abs(x), MOVEMENT);
            y = MOVEMENT - x;
            x = xNegative ? -x : x;
            y = yNegative ? -y : y;
        }
        sailor.setPosition(new Point(sailor.getPosition().x + x, sailor.getPosition().y + y));
        return new Moving(sailor.getId(), x, y);
    }

    private Action move(Sailor sailor, Equipment equipment) {
        int[] movement = sailor.moveTo(equipment);
        return move(sailor, movement[0], movement[1]);
    }

    /**
     * Remove all sailors on equipments needed, then move remaining sailors to used equipments without a sailor
     *
     * @param layout equipment to use
     * @return actions to do
     */
    public List<Action> moveSailors(Layout layout) {
        Layout layoutCopy = new Layout(layout);
        List<Action> actions = new ArrayList<>();
        List<Equipment> equipments = this.initialiseEquipmentNeeded(layoutCopy);
        this.removeUnnecessarySails(layoutCopy, equipments);
        boolean needClosedSails = layoutCopy.getNumberSail() == 0;

        // On classe les sailors suivant s'ils sont utilisables ou pas.
        List<Sailor> sailorsToUse = new ArrayList<>(this.sailors.stream().filter(sailor -> !this.inRange(sailor, equipments).isEmpty()).toList());

        // Parcourir en ordre de priorité les sailors qui doivent aller sur un équipement spécifique
        int i = 1;
        while (i <= equipments.size()) {
            int tmpI = i;
            List<Sailor> sailorsInRangeI = new ArrayList<>(sailorsToUse.stream().filter(sailor -> this.inRange(sailor, equipments).size() == tmpI).toList());
            for (Sailor sailor : sailorsInRangeI) {
                List<Equipment> inRange = this.inRange(sailor, equipments);
                if (!inRange.isEmpty()) {
                    Equipment chosen = inRange.get(0);
                    actions.add(this.move(sailor, chosen));
                    equipments.remove(chosen);
                    sailorsToUse.remove(sailor);
                    this.countEquipmentAsUsed(layoutCopy, chosen);
                    i = Math.max(1, i - this.removeSatisfiedEquipments(layoutCopy, equipments, needClosedSails) - 1);
                }
            }
            i++;
        }

        actions.removeIf(Objects::isNull);
        return actions;
    }

    private void countEquipmentAsUsed(Layout layout, Equipment chosen) {
        switch (chosen.getType()) {
            case OAR -> {
                if (((Oar) chosen).isLeft()) layout.reduceNumberOarLeft();
                if (((Oar) chosen).isRight()) layout.reduceNumberOarRight();
            }
            case RUDDER -> layout.removeUsageOfRudder();
            case SAIL -> layout.reduceNumberSails();
            case WATCH -> layout.removeUsageOfWatch();
        }
    }

    private List<Equipment> initialiseEquipmentNeeded(Layout layout) {
        List<Equipment> equipments = new ArrayList<>();
        if (layout.getNumberOarLeft() > 0) {
            equipments.addAll(this.ship.getOarLeft());
        }
        if (layout.getNumberOarRight() > 0) {
            equipments.addAll(this.ship.getOarRight());
        }
        if (layout.getAngleRudder() != 0.0) {
            equipments.addAll(this.ship.getRudders());
        }
        if (layout.getNumberSail() >= 0) {
            equipments.addAll(this.ship.getSails());
        }

        if (layout.isAssignedToWatch()) {
            equipments.addAll(this.ship.getWatches());
        }
        return equipments;
    }

    /**
     * Removes the sails that are not needed to change from the needed equipments.
     *
     * @param layout     The current layout of equipment needed.
     * @param equipments The List of equipments needed during the turn.
     */
    private void removeUnnecessarySails(Layout layout, List<Equipment> equipments) {
        // Calculating number of sails to open or close
        int sailsNeeded = layout.getNumberSail();
        int sailsOpened = (int) this.ship.getSails().stream().filter(Sail::isOpened).count();
        int sailsNotToChange = sailsNeeded == 0 ? this.ship.getSails().size() - sailsOpened : Math.min(sailsNeeded, sailsOpened);
        boolean sailOpenStatus = sailsNeeded > sailsOpened;
        if (sailsNeeded == sailsOpened) {
            sailOpenStatus = sailsNeeded != 0;
        }

        // Keep Necessary Sails.
        for (Equipment equipment : new ArrayList<>(equipments)) {
            if (sailsNotToChange > 0 && equipment.getType() == Equipment.equipmentTypeEnum.SAIL && ((Sail) equipment).isOpened() == sailOpenStatus) {
                equipments.remove(equipment);
                sailsNotToChange--;
            }
        }
    }

    private int removeSatisfiedEquipments(Layout layout, List<Equipment> equipments, boolean needClosedSails) {
        List<Equipment> equipmentsToRemove = new ArrayList<>();
        if (layout.getNumberOarLeft() == 0) {
            equipmentsToRemove.addAll(equipments.stream().filter(equipment -> equipment.getType() == equipmentTypeEnum.OAR).map(Oar.class::cast).filter(Oar::isLeft).toList());
        }
        if (layout.getNumberOarRight() == 0) {
            equipmentsToRemove.addAll(equipments.stream().filter(equipment -> equipment.getType() == equipmentTypeEnum.OAR).map(Oar.class::cast).filter(Oar::isRight).toList());
        }
        if (layout.getAngleRudder() == 0.0) {
            equipmentsToRemove.addAll(equipments.stream().filter(equipment -> equipment.getType() == equipmentTypeEnum.RUDDER).toList());
        }
        if (layout.getNumberSail() == 0 && !needClosedSails) {
            equipmentsToRemove.addAll(equipments.stream().filter(equipment -> equipment.getType() == equipmentTypeEnum.SAIL).toList());
        }
        if (!layout.isAssignedToWatch()) {
            equipmentsToRemove.addAll(equipments.stream().filter(equipment -> equipment.getType() == equipmentTypeEnum.WATCH).toList());
        }
        int removal = equipmentsToRemove.size();
        equipments.removeAll(equipmentsToRemove);
        return removal;
    }

    /**
     * Find the equipments a sailor can go to in one turn
     *
     * @param sailor     the sailor
     * @param equipments the list of equipments on the ship
     * @return the list of equipments the sailor can go to
     */
    private List<Equipment> inRange(Sailor sailor, List<Equipment> equipments) {
        List<Equipment> inRange = new ArrayList<>(equipments.stream().filter(e -> sailor.distanceTo(e) <= MOVEMENT).toList());
        inRange.sort(Comparator.comparingInt(sailor::distanceTo));
        return inRange;
    }

    public List<Action> assignEquipment(Layout layout) {
        List<Action> actions = new ArrayList<>();
        for (Equipment equipment : this.ship.getEquipments()) {
            for (Sailor sailor : this.sailors) {
                if (equipment.isAvailable() && sailor.getPosition().equals(equipment.getPosition())) {
                    Action action = equipment.action(layout, sailor);
                    if (action != null) {
                        actions.add(action);
                        equipment.setOccupied(true);
                    }
                }
            }
        }
        return actions;
    }

    public Ship getShip() {
        return ship;
    }


    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public List<Sailor> getSailors() {
        return sailors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SailorManager that = (SailorManager) o;
        return Objects.equals(sailors, that.sailors) && Objects.equals(ship, that.ship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sailors, ship);
    }

    @Override
    public String toString() {
        return "{\"sailors\": " + this.sailors + ", \"ship\": " + this.ship + '}';
    }
}
