package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.github.scottswolfe.kathyscleaning.general.controller.MenuBarController;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;

@SuppressWarnings("serial")
public class  MenuBar<ViewObject, ModelObject> extends JMenuBar {

/* INSTANCE VARIABLES ======================================================= */
    
    /**
     * The controller for this menu bar
     */
    MenuBarController<ViewObject, ModelObject> menuController;
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public MenuBar(Controller<ViewObject, ModelObject> controller) {
        menuController = new MenuBarController<>(controller);        
        addFileMenu();
    }
    
    
    
/* PRIVATE METHODS ========================================================== */
    
    private void addFileMenu() {
        add(createFileMenu());
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        addSaveMenuItem(fileMenu);
        addSaveAsMenuItem(fileMenu);
        addOpenMenuItem(fileMenu);
        fileMenu.addSeparator();
        addGenExcelItem(fileMenu);
        return fileMenu;
    }
    
    private void addSaveMenuItem(JMenu fileMenu) {
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenuItem.addActionListener(
                menuController.new SaveMenuItemListener());
        fileMenu.add(saveMenuItem);
    }
    
    private void addSaveAsMenuItem(JMenu fileMenu) {
        JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.addActionListener(
                menuController.new SaveAsMenuItemListener());
        fileMenu.add(saveAsMenuItem);
    }
    
    private void addOpenMenuItem(JMenu fileMenu) {
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openMenuItem.addActionListener(
                menuController.new LoadMenuItemListener());
        fileMenu.add(openMenuItem);    
    }
    
    private void addGenExcelItem(JMenu fileMenu) {
        JMenuItem genExcelMenuItem = new JMenuItem("Generate Excel Document");
        genExcelMenuItem.addActionListener(
                menuController.new GenExcelMenuItemListener());
        fileMenu.add(genExcelMenuItem);    
    }
    
}
