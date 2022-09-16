package com.github.scottswolfe.kathyscleaning.general.controller;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.SaveFileManager;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import com.google.common.collect.ImmutableList;

import javax.swing.SwingUtilities;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationCoordinator {

    private static final List<Form> FORMS = ImmutableList.of(
        Form.COMPLETED,
        Form.COVENANT,
        Form.LBC,
        Form.WEEKEND,
        Form.SCHEDULED
    );

    private static final ApplicationCoordinator applicationCoordinatorInstance = new ApplicationCoordinator();

    private final SaveFileManager saveFileManager;
    private Map<Form, GeneralController<?, ?>> formControllers;

    public static ApplicationCoordinator getInstance() {
        return applicationCoordinatorInstance;
    }

    private ApplicationCoordinator() {
        saveFileManager = SaveFileManager.from();
    }

    public void startApplication() {

        Settings.changeLookAndFeelProgram();

        try {
            saveFileManager.initializeSaveFiles();
        } catch (Exception e) {
            StaticMethods.shareErrorMessage("Error while initializing save files:", e);
            System.exit(1);
        }

        try {
            formControllers = FORMS.stream().collect(Collectors.toMap(form -> form, GeneralController::from));
        } catch (Exception e) {
            StaticMethods.shareErrorMessage("Error while initializing form controllers:", e);
            System.exit(1);
        }

        SwingUtilities.invokeLater(MenuPanelController::initializeMenuPanelFrame);
    }

    public void navigateToForm(final Form sourceForm, final Form targetForm) {
        final Controller<?, ?> sourceController = formControllers.get(sourceForm);
        formControllers.values().forEach(
            controller -> controller.readInputAndWriteToFile()
        );
        sourceController.hideWindow();

        navigateToForm(targetForm);
    }

    public void navigateToForm(final Form targetForm) {
        final Controller<?, ?> targetController = formControllers.get(targetForm);
        SwingUtilities.invokeLater(targetController::launchForm);
    }

    public void loadFile(final File file) {
        saveFileManager.loadFile(file);
        formControllers.values().forEach(controller -> controller.readFileAndWriteToView(file));
    }

    public void updateWorkers(final List<List<String>> workerNames) {
        formControllers.values().forEach(controller -> controller.updateWorkers(workerNames));
    }
}
