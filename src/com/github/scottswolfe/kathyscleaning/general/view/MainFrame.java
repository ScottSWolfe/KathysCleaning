package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;

@SuppressWarnings("serial")
public class MainFrame<ViewObject, ModelObject> extends JFrame {    
    
/* COMPONENTS =============================================================== */
    
    MenuBar<ViewObject, ModelObject> menuBar;
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public MainFrame(Controller<ViewObject, ModelObject> controller) {
        super();
        addMenuBar(controller);
        addWindowListener(controller);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
    }

    
    
/* PUBLIC METHODS =========================================================== */
    
    public void eliminate() {
        this.setVisible(false);
        this.dispose();
    }
    
    
    
/* PRIVATE METHODS ========================================================== */
    
    private void addMenuBar(Controller<ViewObject, ModelObject> controller) {
        menuBar = new MenuBar<>(controller);
        this.setJMenuBar(menuBar);
    }
    
    private void addWindowListener(Controller<ViewObject, ModelObject> controller) {
        WindowListener listener = new MainWindowListener(controller);
        this.addWindowListener(listener);
    }

}
