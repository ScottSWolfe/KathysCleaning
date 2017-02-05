package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.persistance.Savable;

public class MenuBarController {
    
    // FIELDS
    Savable savable;
    
    
    // CONSTRUCTOR
    public MenuBarController(Savable savable) {
        this.savable = savable;
    }
    
    
    // LISTENERS
    public class SaveMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            savable.saveToFile();
        }
    }
    
    public class LoadMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            savable.loadFromFile();
        }
    }
    
}
