package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import com.github.scottswolfe.kathyscleaning.component.MenuEditWorkersPanel;
import com.github.scottswolfe.kathyscleaning.general.view.GenericPanelLauncher;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.helper.ExcelMethods;
import com.github.scottswolfe.kathyscleaning.interfaces.FileMenuListener;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;

public class MenuBarController <ViewObject extends JComponent, ModelObject> implements FileMenuListener {

    /**
     * The controller that this class calls on to do the reading and writing
     */
    FormController<ViewObject, ModelObject> controller;

    public MenuBarController(FormController<ViewObject, ModelObject> controller) {
        this.controller = controller;
    }

    @Override
    public void menuItemSave() {
        ApplicationCoordinator.getInstance().save();
    }

    @Override
    public void menuItemSaveAs() {
        ApplicationCoordinator.getInstance().saveAs();
    }

    @Override
    public void menuItemOpen() {
        ApplicationCoordinator.getInstance().saveAndOpen();
    }

    @Override
    public void menuItemGenExcel() {
        ExcelMethods.chooseFileAndGenerateExcelDoc();
    }

    public void menuItemChangeDate() {
        ChooseWeekPanel.initializePanel(controller);
    }

    private void menuItemEditWorkers() {
        GenericPanelLauncher.from().launchPanel(
            MenuEditWorkersPanel::from,
            () -> {},
            (workerNames) -> ApplicationCoordinator.getInstance().updateWorkers(workerNames),
            new FrameCloseListener(controller.getParentFrame())
        );
    }

    public void menuItemGoHouses() {
        ApplicationCoordinator.getInstance().navigateToForm(Form.COMPLETED);
    }

    public void menuItemGoCovenant() {
        ApplicationCoordinator.getInstance().navigateToForm(Form.COVENANT);
    }

    public void menuItemGoLBC() {
        ApplicationCoordinator.getInstance().navigateToForm(Form.LBC);
    }

    public void menuItemGoWeekend() {
        ApplicationCoordinator.getInstance().navigateToForm(Form.WEEKEND);
    }

    public void menuItemGoNextWeek() {
        ApplicationCoordinator.getInstance().navigateToForm(Form.SCHEDULED);
    }

    public void menuItemLoadSchedule() {
        ApplicationCoordinator.getInstance().loadSchedule();
    }

    public class SaveMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemSave();
        }
    }

    public class SaveAsMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemSaveAs();
        }
    }

    public class LoadMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemOpen();
        }
    }

    public class GenExcelMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGenExcel();
        }
    }

    public class ChangeDateMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemChangeDate();
        }
    }

    public class EditWorkersItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemEditWorkers();
        }
    }

    public class HousesMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGoHouses();
        }
    }

    public class CovenantMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGoCovenant();
        }
    }

    public class LBCMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGoLBC();
        }
    }

    public class WeekendMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGoWeekend();
        }
    }

    public class NextWeekMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGoNextWeek();
        }
    }

    public class LoadScheduleMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemLoadSchedule();
        }
    }
}
