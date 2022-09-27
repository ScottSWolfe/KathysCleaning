package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComponent;

import com.github.scottswolfe.kathyscleaning.component.MenuEditWorkersPanel;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
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

    private final ApplicationCoordinator applicationCoordinator;

    public MenuBarController(FormController<ViewObject, ModelObject> controller) {
        this.controller = controller;
        applicationCoordinator = ApplicationCoordinator.getInstance();
    }

    @Override
    public void menuItemSave() {
        applicationCoordinator.save();
    }

    @Override
    public void menuItemSaveAs() {
        applicationCoordinator.saveAs();
    }

    @Override
    public void menuItemOpen() {
        applicationCoordinator.saveAndOpen();
    }

    @Override
    public void menuItemGenExcel() {
        ExcelMethods.chooseFileAndGenerateExcelDoc();
    }

    public void menuItemChangeDate() {
        ChooseWeekPanel.initializePanel(controller);
    }

    private void menuItemEditWorkers() {

        final Consumer<List<String>> dataConsumer;
        final int rowCount;
        final int columnCount;

        if (controller.getFormType() == Form.COVENANT) {
            dataConsumer = applicationCoordinator::updateCovenantWorkers;
            rowCount = CovenantPanel.ROWS;
            columnCount = 1;
        } else {
            dataConsumer = applicationCoordinator::updateWorkers;
            rowCount = WorkerSelectPanel.DEFAULT_ROW_COUNT;
            columnCount = WorkerSelectPanel.DEFAULT_COLUMN_COUNT;
        }

        GenericPanelLauncher.from().launchPanel(
            () -> MenuEditWorkersPanel.from(rowCount, columnCount),
            () -> {},
            dataConsumer,
            new FrameCloseListener(controller.getParentFrame())
        );
    }

    public void menuItemGoHouses() {
        applicationCoordinator.navigateToForm(Form.COMPLETED);
    }

    public void menuItemGoCovenant() {
        applicationCoordinator.navigateToForm(Form.COVENANT);
    }

    public void menuItemGoLBC() {
        applicationCoordinator.navigateToForm(Form.LBC);
    }

    public void menuItemGoWeekend() {
        applicationCoordinator.navigateToForm(Form.WEEKEND);
    }

    public void menuItemGoNextWeek() {
        applicationCoordinator.navigateToForm(Form.SCHEDULED);
    }

    public void menuItemLoadSchedule() {
        applicationCoordinator.loadSchedule();
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
