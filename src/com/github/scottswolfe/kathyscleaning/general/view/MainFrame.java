package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

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
        setBackground(Settings.BACKGROUND_COLOR);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }



/* PUBLIC METHODS =========================================================== */

    public void eliminate() {
        this.setVisible(false);
        this.dispose();
    }

    public void setTitleText(Controller<ViewObject, ModelObject> controller) {
        String text = "";

        if (controller.getFormType() == Form.COMPLETED) {
            text = "Houses";
        } else if (controller.getFormType() == Form.COVENANT) {
            text = "Covenant";
        } else if (controller.getFormType() == Form.LBC) {
            text = "LBC";
        } else if (controller.getFormType() == Form.WEEKEND) {
            text = "Weekend";
        } else if (controller.getFormType() == Form.SCHEDULED) {
            text = "Next Week";
        }

        if (SessionModel.isSaveFileChosen()) {
            text += " - " + SessionModel.getSaveFile().getName();
        }

        setTitle(text);
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
