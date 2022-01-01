package com.psyala.view;

import com.psyala.model.sim.Simulation;

import javax.swing.*;
import java.awt.*;

public class SimcOutputPanel extends JPanel {
    private final JTabbedPane tabbedPane;

    public SimcOutputPanel() {
        super(new GridLayout(1, 1));
        setMinimumSize(new Dimension(1000, 800));
        setPreferredSize(new Dimension(1000, 800));
        tabbedPane = new JTabbedPane();
        add(tabbedPane);
    }

    public JTextArea addThreadOutput(Simulation simulation) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        tabbedPane.addTab(simulation.getName(), new JScrollPane(textArea));
        return textArea;
    }
}
