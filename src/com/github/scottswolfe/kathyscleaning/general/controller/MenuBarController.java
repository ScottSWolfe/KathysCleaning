package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.persistance.FileManager;

public class MenuBarController {
    
    // FIELDS
    FileManager fileManager;
    
    
    // CONSTRUCTOR
    public MenuBarController(FileManager fm) {
        fileManager = fm;
    }
    
    
    // LISTENERS
    public class SaveMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            fileManager.saveToFile();
        }
    }
    
    public class LoadMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            fileManager.loadFromFile();
        }
    }
    
}
