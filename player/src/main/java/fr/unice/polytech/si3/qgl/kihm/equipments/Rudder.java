package fr.unice.polytech.si3.qgl.kihm.equipments;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.actions.Turn;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;

/**
 * Defines the Rudder of the ship.
 */
public class Rudder extends Equipment {

    /**
     * Constructor of the rudder.
     *
     * @param x The abscissa coordinate.
     * @param y The ordinate coordinate.
     */
    public Rudder(int x, int y) {
        super(equipmentTypeEnum.RUDDER, x, y);
    }

    @Override
    public Action action(Layout layout, Sailor sailor) {
        if (layout.getAngleRudder() != 0.0) {
            return new Turn(sailor.getId(), layout.getAngleRudder());
        }
        return null;
    }
}
