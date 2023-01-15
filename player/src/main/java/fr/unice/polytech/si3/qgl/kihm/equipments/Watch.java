package fr.unice.polytech.si3.qgl.kihm.equipments;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;

/**
 * Defines the watch of the ship
 */
public class Watch extends Equipment {

    /**
     * Size of Vision
     */
    public static final double VISION_SIZE = 1000;

    /**
     * Constructor of the watch.
     *
     * @param x The abscissa coordinate.
     * @param y The ordinate coordinate.
     */
    public Watch(int x, int y) {
        super(equipmentTypeEnum.WATCH, x, y);
    }

    @Override
    public Action action(Layout layout, Sailor sailor) {
        if (layout.isAssignedToWatch()) {
            layout.removeUsageOfWatch();
            return new Action(sailor.getId(), Action.actionTypeEnum.USE_WATCH);
        }
        return null;
    }
}
