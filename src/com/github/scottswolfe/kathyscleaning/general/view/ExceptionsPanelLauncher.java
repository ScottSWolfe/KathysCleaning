package com.github.scottswolfe.kathyscleaning.general.view;

import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.view.CompletedExceptionsPanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

import java.awt.event.WindowListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *  This is used to launch a panel used to add exceptions for workers' worked hours for houses.
 */
public class ExceptionsPanelLauncher {

    private final PopUpFormLauncher popUpFormLauncher = PopUpFormLauncher.from();

    public static ExceptionsPanelLauncher from() {
        return new ExceptionsPanelLauncher();
    }

    private ExceptionsPanelLauncher() {}

    public void launchPanel(
        final WorkerList dwd,
        final Supplier<ExceptionData> exceptionDataSupplier,
        final Runnable onCancel,
        final Consumer<ExceptionData> onSubmit,
        final WindowListener popUpWindowListener
    ) {
        final CompletedExceptionsPanel exceptionsPanel = new CompletedExceptionsPanel(
            dwd, exceptionDataSupplier
        );

        popUpFormLauncher.launchPanel(
            exceptionsPanel,
            onCancel,
            () -> onSubmit.accept(exceptionsPanel.getExceptionData()),
            popUpWindowListener
        );
    }
}
