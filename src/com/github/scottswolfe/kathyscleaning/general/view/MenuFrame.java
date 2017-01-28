package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.github.scottswolfe.kathyscleaning.general.controller.MenuBarController;
import com.github.scottswolfe.kathyscleaning.persistance.Savable;

public class MenuFrame extends JFrame {

    // INSTANCE VARIABLES
    Savable savable;
    
    
    // COMPONENTS
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem saveMenuItem;
    JMenuItem loadMenuItem;
    
    
    // CONSTRUCTOR
    public MenuFrame(Savable fm) {
        super();
        savable = fm;
        createAndAddMenu();
    }
    
    
    // PRIVATE METHODS
    private void createAndAddMenu() {
        
        // Create the menu bar controller
        MenuBarController controller = new MenuBarController(savable);
        
        // Create the menu bar
        menuBar = new JMenuBar();
        
        // Build the file menu
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
     
        // Build the save menu item
        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenuItem.addActionListener(controller.new SaveMenuItemListener());
        fileMenu.add(saveMenuItem);
        
        // Build the load menu item
        loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setMnemonic(KeyEvent.VK_L);
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        loadMenuItem.addActionListener(controller.new LoadMenuItemListener());
        fileMenu.add(loadMenuItem);

        // Add the menu bar to the frame
        this.setJMenuBar(menuBar);
    }

}
