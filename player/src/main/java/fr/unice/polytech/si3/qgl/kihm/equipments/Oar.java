package fr.unice.polytech.si3.qgl.kihm.equipments;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.ship.Layout;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;

/**
 * Class that defines oars of the ship.
 */
public class Oar extends Equipment {
    private boolean left = false;
    private boolean right = false;

    /**
     * Constructor of the oar.
     *
     * @param x The abscissa coordinate.
     * @param y The ordinate coordinate.
     */
    public Oar(int x, int y) {
        super(equipmentTypeEnum.OAR, x, y);
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft() {
        this.left = true;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight() {
        this.right = true;
    }

    @Override
    public Action action(Layout layout, Sailor sailor) {
        boolean used = false;
        if (this.left && layout.getNumberOarLeft() > 0) {
            layout.reduceNumberOarLeft();
            used = true;
        }
        if (!used && this.right && layout.getNumberOarRight() > 0) {
            layout.reduceNumberOarRight();
            used = true;
        }
        return used ? new Action(sailor.getId(), Action.actionTypeEnum.OAR) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Oar oar)) return false;
        if (!super.equals(o)) return false;

        if (left != oar.left) return false;
        return right == oar.right;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (left ? 1 : 0);
        result = 31 * result + (right ? 1 : 0);
        return result;
    }
}
