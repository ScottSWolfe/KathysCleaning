package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.general.controller.FormController;
import com.github.scottswolfe.kathyscleaning.general.controller.MenuBarController;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

public class MenuBar<View extends JComponent, Model> extends JMenuBar {

/* INSTANCE VARIABLES ======================================================= */

    /**
     * The controller for this menu bar
     */
    MenuBarController<View, Model> menuController;

    /**
     * The type of form, this menu bar is for
     */
    Form form;



/* CONSTRUCTORS ============================================================= */

    public MenuBar(FormController<View, Model> controller) {
        menuController = new MenuBarController<>(controller);
        form = controller.getFormType();
        addFileMenu();
        addEditMenu();
        addNavMenu();
    }



/* PRIVATE METHODS ========================================================== */

    private void addFileMenu() {
        add(createFileMenu());
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");
        addSaveMenuItem(fileMenu);
        addSaveAsMenuItem(fileMenu);
        addOpenMenuItem(fileMenu);
        fileMenu.addSeparator();
        addGenExcelItem(fileMenu);
        fileMenu.setFont(fileMenu.getFont().deriveFont(Settings.FONT_SIZE));
        return fileMenu;
    }

    private void addSaveMenuItem(Menu fileMenu) {
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenuItem.addActionListener(
                menuController.new SaveMenuItemListener());
        fileMenu.add(saveMenuItem);
    }

    private void addSaveAsMenuItem(Menu fileMenu) {
        MenuItem saveAsMenuItem = new MenuItem("Save As...");
        saveAsMenuItem.addActionListener(
                menuController.new SaveAsMenuItemListener());
        fileMenu.add(saveAsMenuItem);
    }

    private void addOpenMenuItem(Menu fileMenu) {
        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openMenuItem.addActionListener(
                menuController.new LoadMenuItemListener());
        fileMenu.add(openMenuItem);
    }

    private void addGenExcelItem(Menu fileMenu) {
        MenuItem genExcelMenuItem = new MenuItem("Generate Excel Document");
        genExcelMenuItem.addActionListener(
                menuController.new GenExcelMenuItemListener());
        fileMenu.add(genExcelMenuItem);
    }

    private void addEditMenu() {
        add(createEditMenu());
    }

    private Menu createEditMenu() {
        Menu fileMenu = new Menu("Edit");
        addChangeDateMenuItem(fileMenu);
        fileMenu.addSeparator();
        addEditWorkersMenuItem(fileMenu);
        addCopyWorkersMenuItem(fileMenu);
        if (ApplicationCoordinator.getInstance().shouldDisplayLoadScheduleMenuItem(form)) {
            fileMenu.addSeparator();
            addLoadScheduleMenuItem(fileMenu);
        }
        return fileMenu;
    }

    private void addChangeDateMenuItem(Menu fileMenu) {
        MenuItem changeDateMenuItem = new MenuItem("Change the Date");
        changeDateMenuItem.addActionListener(
                menuController.new ChangeDateMenuItemListener());
        fileMenu.add(changeDateMenuItem);
    }

    private void addEditWorkersMenuItem(Menu fileMenu) {
        MenuItem editWorkersMenuItem = new MenuItem("Edit Workers");
        editWorkersMenuItem.addActionListener(menuController.new EditWorkersItemListener());
        fileMenu.add(editWorkersMenuItem);
    }

    private void addCopyWorkersMenuItem(Menu fileMenu) {
        MenuItem copyWorkersMenuItem = new MenuItem("Copy Workers from File");
        copyWorkersMenuItem.addActionListener(menuController.new CopyWorkersItemListener());
        fileMenu.add(copyWorkersMenuItem);
    }

    private void addLoadScheduleMenuItem(Menu fileMenu) {
        MenuItem loadScheduleMenuItem = new MenuItem("Load Schedule from Previous Week");
        loadScheduleMenuItem.addActionListener(
                menuController.new LoadScheduleMenuItemListener());
        fileMenu.add(loadScheduleMenuItem);
    }

    private void addNavMenu() {
        add(createNavMenu());
    }

    private Menu createNavMenu() {
        Menu navMenu = new Menu("Navigate");
        addHousesItem(navMenu);
        addCovenantItem(navMenu);
        addLBCItem(navMenu);
        addWeekendItem(navMenu);
        addNextWeekItem(navMenu);
        return navMenu;
    }

    private void addHousesItem(Menu navMenu) {
        MenuItem housesMenuItem = new MenuItem("Houses");
        housesMenuItem.addActionListener(
                menuController.new HousesMenuItemListener());
        navMenu.add(housesMenuItem);
    }

    private void addCovenantItem(Menu navMenu) {
        MenuItem covenantMenuItem = new MenuItem("Covenant");
        covenantMenuItem.addActionListener(
                menuController.new CovenantMenuItemListener());
        navMenu.add(covenantMenuItem);
    }

    private void addLBCItem(Menu navMenu) {
        MenuItem lbcMenuItem = new MenuItem("LBC");
        lbcMenuItem.addActionListener(menuController.new LBCMenuItemListener());
        navMenu.add(lbcMenuItem);
    }

    private void addWeekendItem(Menu navMenu) {
        MenuItem weekendMenuItem = new MenuItem("Weekend");
        weekendMenuItem.addActionListener(
                menuController.new WeekendMenuItemListener());
        navMenu.add(weekendMenuItem);
    }

    private void addNextWeekItem(Menu navMenu) {
        MenuItem nextWeekMenuItem = new MenuItem("Next Week");
        nextWeekMenuItem.addActionListener(
                menuController.new NextWeekMenuItemListener());
        navMenu.add(nextWeekMenuItem);
    }
}
