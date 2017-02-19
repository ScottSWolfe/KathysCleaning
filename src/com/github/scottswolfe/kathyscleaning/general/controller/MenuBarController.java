package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.interfaces.Controller;

public class MenuBarController {
    
    // FIELDS
    Controller controller;
    
    
    // CONSTRUCTOR
    public MenuBarController(Controller controller) {
        this.controller = controller;
    }
    
    
    // LISTENERS
    public class SaveMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO add interface for user to choose where to save new file
            controller.readInputAndWriteToFile();
        }
    }
    
    public class LoadMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO add interface for user to choose which file to load
            controller.readFileAndWriteToView();
        }
    }
    
}
