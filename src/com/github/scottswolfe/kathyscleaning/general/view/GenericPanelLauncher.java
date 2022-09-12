package com.github.scottswolfe.kathyscleaning.general.view;

import javax.swing.JPanel;
import java.awt.event.WindowListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GenericPanelLauncher {

    private final PopUpFormLauncher popUpFormLauncher = PopUpFormLauncher.from();

    public static GenericPanelLauncher from() {
        return new GenericPanelLauncher();
    }

    private GenericPanelLauncher() {}

    public <E, T extends JPanel & Supplier<E>> void launchPanel(
        final Supplier<T> panelSupplier,
        final Runnable onCancel,
        final Consumer<E> onSubmit,
        final WindowListener popUpWindowListener
    ) {
        final T panel = panelSupplier.get();
        popUpFormLauncher.launchPanel(
            panel,
            onCancel,
            () -> onSubmit.accept(panel.get()),
            popUpWindowListener
        );
    }
}
