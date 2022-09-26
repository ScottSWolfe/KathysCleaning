package com.github.scottswolfe.kathyscleaning.general.controller;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.view.CompletedTabbedPane;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.view.ScheduledTabbedPane;
import com.github.scottswolfe.kathyscleaning.utility.SaveFileManager;
import com.github.scottswolfe.kathyscleaning.utility.ScheduledToCompletedModelConverter;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.swing.SwingUtilities;
import java.io.File;
import java.util.Calendar;
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
    private final ScheduledToCompletedModelConverter modelConverter;

    private Map<Form, FormController<?, ?>> formControllers;
    private Form currentForm;

    public static ApplicationCoordinator getInstance() {
        return applicationCoordinatorInstance;
    }

    private ApplicationCoordinator() {
        saveFileManager = SaveFileManager.from();
        modelConverter = ScheduledToCompletedModelConverter.from();
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
            formControllers = FORMS.stream().collect(Collectors.toMap(form -> form, FormController::from));
        } catch (Exception e) {
            StaticMethods.shareErrorMessage("Error while initializing form controllers:", e);
            System.exit(1);
        }

        SwingUtilities.invokeLater(MenuPanelController::initializeMenuPanelFrame);
    }

    public void endApplication() {
        System.exit(0);
    }

    public void endApplicationDueToException(@Nonnull final String message, @Nonnull final Exception e) {
        e.printStackTrace();
        StaticMethods.shareErrorMessage(message, e);
        System.exit(1);
    }

    public void hideCurrentWindow() {
        if (currentForm != null) {
            final FormController<?, ?> controller = formControllers.get(currentForm);
            controller.hideWindow();
        }
    }

    public void navigateToForm(final Form targetForm) {
        writeCurrentStateToTemporarySaveFile();
        hideCurrentWindow();
        currentForm = targetForm;
        final FormController<?, ?> targetController = formControllers.get(targetForm);
        SwingUtilities.invokeLater(targetController::launchForm);
    }

    public void refreshWindow() {
        if (currentForm != null) {
            formControllers.get(currentForm).refreshWindow();
        }
    }

    public void updateWorkers(final List<List<String>> workerNames) {
        writeCurrentStateToTemporarySaveFile();
        formControllers.values().forEach(controller -> controller.updateWorkers(workerNames));
    }

    public void setStartDate(@Nonnull final Calendar date) {
        writeCurrentStateToTemporarySaveFile();
        SessionModel.setCompletedStartDay(date);
        formControllers.values().forEach(FormController::updateDate);
    }

    public void save() {
        writeCurrentStateToTemporarySaveFile();
        saveFileManager.save();
        updateWindowTitle();
    }

    public void saveAs() {
        writeCurrentStateToTemporarySaveFile();
        saveFileManager.saveAs();
        updateWindowTitle();
    }

    public boolean saveAsNew() {
        writeCurrentStateToTemporarySaveFile();
        final boolean shouldCompleteAction = saveFileManager.saveAsNew();
        updateWindowTitle();
        return shouldCompleteAction;
    }

    public boolean askIfSaveBeforeClose() {
        writeCurrentStateToTemporarySaveFile();

        final boolean shouldCompleteAction =
            saveFileManager.askUserIfSaveBeforeAction(SaveFileManager.Action.CLOSE_PROGRAM);

        // Set the window title since there are some cases where the program does not close
        updateWindowTitle();

        return shouldCompleteAction;
    }

    public boolean open() {
        writeCurrentStateToTemporarySaveFile();
        final boolean shouldCompleteAction = saveFileManager.open();
        if (shouldCompleteAction) {
            loadCurrentStateFromTemporarySaveFile();
        }
        updateWindowTitle();
        return shouldCompleteAction;
    }

    public void saveAndOpen() {
        writeCurrentStateToTemporarySaveFile();
        final boolean shouldCompleteAction = saveFileManager.saveAndOpen();
        if (shouldCompleteAction) {
            loadCurrentStateFromTemporarySaveFile();
        }
        updateWindowTitle();
    }

    public void loadSchedule() {
        writeCurrentStateToTemporarySaveFile();

        final File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR, FileChooserHelper.KC);
        if (file == null) {
            return;
        }

        final Calendar ScheduledStartDay = SessionModel.readScheduledStartDayFromFile(file);
        setStartDate(ScheduledStartDay);

        final FormController<ScheduledTabbedPane, NW_Data> scheduledFormController =
            (FormController<ScheduledTabbedPane, NW_Data>) formControllers.get(Form.SCHEDULED);

        final FormController<CompletedTabbedPane, CompletedModel> completedFormController =
            (FormController<CompletedTabbedPane, CompletedModel>) formControllers.get(Form.COMPLETED);

        final NW_Data scheduledData = scheduledFormController.readFile(file);
        final CompletedModel completedModel = modelConverter.convert(scheduledData);
        completedFormController.writeModelToView(completedModel);

        writeCurrentStateToTemporarySaveFile();
    }

    private void writeCurrentStateToTemporarySaveFile() {
        SessionModel.writeToTemporarySaveFile();
        formControllers.values().forEach(FormController::writeViewToTemporarySaveFile);
    }

    private void loadCurrentStateFromTemporarySaveFile() {
        SessionModel.load();
        formControllers.values().forEach(FormController::readTemporaryFileAndWriteToView);
    }

    private void updateWindowTitle() {
        if (currentForm != null) {
            formControllers.get(currentForm).setTitleText();
        }
    }
}
