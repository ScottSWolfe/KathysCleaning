package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;

@SuppressWarnings("serial")
public class MainFrame<ViewObject, ModelObject> extends JFrame {    
    
/* COMPONENTS =============================================================== */
    
    MenuBar<ViewObject, ModelObject> menuBar;
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public MainFrame(Controller<ViewObject, ModelObject> controller) {
        super();
        setTitleText(controller);
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
    
    private void setTitleText(Controller<ViewObject, ModelObject> controller) {
        String text = "";
        if (controller.getFormType() == Form.COMPLETED) {
            text = "Houses";
        } else if (controller.getFormType() == Form.COVENANT) {
            text = "Covenant";
        } else if (controller.getFormType() == Form.WEEKEND) {
            text = "Weekend";
        } else if (controller.getFormType() == Form.SCHEDULED) {
            text = "Next Week";
        }
        setTitle(text);
    }
    
    private void addMenuBar(Controller<ViewObject, ModelObject> controller) {
        menuBar = new MenuBar<>(controller);
        this.setJMenuBar(menuBar);
    }
    
    private void addWindowListener(Controller<ViewObject, ModelObject> controller) {
        WindowListener listener = new MainWindowListener(controller);
        this.addWindowListener(listener);
    }

}
