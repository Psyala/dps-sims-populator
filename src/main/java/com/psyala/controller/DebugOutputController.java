package com.psyala.controller;

import com.psyala.model.sim.Simulation;
import com.psyala.view.MainFrame;

import javax.swing.*;

public class DebugOutputController {
    private final ConfigurationController configurationController;
    private MainFrame mainFrame;

    public DebugOutputController(ConfigurationController configurationController) {
        this.configurationController = configurationController;

        if (configurationController.showDebugOutput) {
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        }
    }

    public JTextArea registerSimcThread(Simulation simulation) {
        return mainFrame.getSimcOutputPanel().addThreadOutput(simulation);
    }
}
