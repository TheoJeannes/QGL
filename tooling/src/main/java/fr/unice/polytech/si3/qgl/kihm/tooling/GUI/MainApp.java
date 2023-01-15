package fr.unice.polytech.si3.qgl.kihm.tooling.GUI;

import fr.unice.polytech.si3.qgl.kihm.Cockpit;
import fr.unice.polytech.si3.qgl.kihm.equipments.Equipment;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;
import fr.unice.polytech.si3.qgl.kihm.logger.CustomFormatter;
import fr.unice.polytech.si3.qgl.kihm.ship.Deck;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.tooling.Simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainApp extends JFrame implements ActionListener {
    MapPanel mapPanel;
    JPanel header;
    JLabel turnText = new JLabel("Tour 0");
    JLabel coordsText = new JLabel("| X : 0, Y :0");
    JButton nextStep = new JButton("Next");
    JButton play = new JButton("Play");
    JButton reset = new JButton("Reset");
    JButton previousStep = new JButton("Previous");
    JButton showLayout = new JButton("Layout");
    Simulator simulator;

    public MainApp(Simulator simulator) throws HeadlessException {
        this.simulator = simulator;
        Cockpit c = simulator.getCockpit();
        mapPanel = new MapPanel(simulator.getWorld(), c.getGame().getShip(), this);
        header = new JPanel();
        nextStep.addActionListener(this);
        previousStep.addActionListener(this);
        this.showLayout.addActionListener(this);
        play.addActionListener(this);
        reset.addActionListener(this);
        previousStep.setEnabled(false);
        header.add(play);
        header.add(reset);
        header.add(previousStep);
        header.add(nextStep);
        header.add(showLayout);
        header.add(turnText);
        header.add(coordsText);
        header.setBackground(WorldColors.ocean.getColor());
        this.setTitle("KIHM | La Famille Royal de l'IHM");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenDimension = this.getToolkit().getScreenSize();
        this.setSize(new Dimension((int) (0.75 * screenDimension.getWidth()), (int) (0.75 * screenDimension.getHeight())));
        this.setLocation((int) (screenDimension.getWidth() / 2 - this.getSize().getWidth() / 2), (int) (screenDimension.getHeight() / 2 - this.getSize().getHeight() / 2));
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(mapPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (play.equals(source)) {
            updateAll(true);
        } else if (reset.equals(source)) {
            updateAll(false);
        } else if (nextStep.equals(source) || previousStep.equals(source)) {
            updateTurns(e.getSource().equals(nextStep));
        } else if (showLayout.equals(source)) {
            this.printLayout();
            return;
        } else {
            throw new IllegalStateException("Unexpected value: " + e.getSource());
        }
        repaint();
    }

    private void printLayout() {
        Ship ship = this.simulator.getCockpit().getGame().getShip();
        Deck deck = ship.getDeck();

        for (int x = 0; x < deck.getLength(); x++) {
            for (int y = 0; y < deck.getWidth(); y++) {
                int finalX = x;
                int finalY = y;
                Equipment equipment = ship.getEquipments().stream().filter(e -> e.getX() == finalX && e.getY() == finalY).findFirst().orElse(null);
                System.out.print(this.isOccupied(equipment == null ? "." : String.valueOf(equipment.getType().toString().charAt(0)), finalX, finalY));
                if (y + 1 < deck.getWidth()) System.out.print(" ");
            }
            System.out.print('\n');
        }
    }

    private String isOccupied(String text, int x, int y) {
        List<Sailor> sailors = this.simulator.getCockpit().getGame().getSailors();
        if (sailors.stream().map(Sailor::getPosition).anyMatch(point -> point.getX() == x && point.getY() == y))
            text = CustomFormatter.RED_COLOR + text + CustomFormatter.RESET;
        return text;
    }

    private void updateAll(boolean forward) {
        while (((simulator.notFinished() || mapPanel.getCurrentTurn() < mapPanel.getMaxTurn() - 1) && forward) || (mapPanel.getCurrentTurn() > 0 && !forward)) {
            updateTurns(forward);
        }
    }

    private void updateTurns(boolean forward) {
        nextStep.setEnabled(false);
        previousStep.setEnabled(false);
        mapPanel.updateTurnCount(forward ? 1 : -1);
        int turn = mapPanel.getCurrentTurn();
        if (turn >= mapPanel.getMaxTurn()) {
            if (simulator.notFinished()) {
                List<Object> res = simulator.nextStep();
                mapPanel.addTurn((Ship) res.get(0), (World) res.get(1));
            } else
                mapPanel.updateTurnCount(-1);
        }
        if (turn < mapPanel.getMaxTurn() || simulator.notFinished()) nextStep.setEnabled(true);
        if (turn > 0) previousStep.setEnabled(true);
        turnText.setText("Tour " + turn);
    }
}