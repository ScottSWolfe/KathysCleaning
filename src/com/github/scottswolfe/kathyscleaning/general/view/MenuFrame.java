package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.controller.MenuBarController;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;

public class MenuFrame extends JFrame {    
    
    // COMPONENTS
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem saveMenuItem;
    JMenuItem loadMenuItem;
    
    
    // CONSTRUCTOR
    public MenuFrame(Controller controller) {
        super();
        createAndAddMenu(controller);
        addWindowListener();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }
    
    
    // PRIVATE METHODS
    private void createAndAddMenu(Controller controller) {
        
        

        // Add the menu bar to the frame
        this.setJMenuBar(menuBar);
    }
    
    private void addWindowListener() {
        WindowListener listener = new MainWindowListener();
        this.addWindowListener(listener);
    }

}
