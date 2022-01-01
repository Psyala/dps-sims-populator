package com.psyala.view;

import com.psyala.DpsSimPopulator;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final SimcOutputPanel simcOutputPanel;

    public MainFrame() {
        super(DpsSimPopulator.class.getSimpleName());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(1400, 800);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel base = new JPanel(new BorderLayout());
        this.setContentPane(base);

        simcOutputPanel = new SimcOutputPanel();
        base.add(simcOutputPanel, BorderLayout.CENTER);

        //TODO: add panel to monitor status of Simulations
    }

    public SimcOutputPanel getSimcOutputPanel() {
        return simcOutputPanel;
    }
}
