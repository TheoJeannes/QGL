package fr.unice.polytech.si3.qgl.kihm.tooling.GUI;

import java.awt.*;

public enum WorldColors {
    ocean(new Color(92, 255, 255)),
    ship(new Color(201, 151, 107)),
    checkpoint(new Color(255, 255, 50)),
    reef(new Color(91, 250, 0)),
    stream(new Color(0, 30, 255)),
    line(new Color(255, 0, 0));

    private final Color color;

    WorldColors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}