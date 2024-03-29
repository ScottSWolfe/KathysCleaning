package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.annotation.Nonnull;
import javax.swing.JPanel;
import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class MenuEditWorkersPanel extends JPanel implements Supplier<List<String>>, FocusableCollection {

    private final WorkerComboBoxGridPanel workerComboBoxGridPanel;
    private final Button loadFromExcelButton;

    public static MenuEditWorkersPanel from(
        @Nonnull final List<String> currentWorkerNames,
        @Nonnull final List<String> availableWorkerNames,
        final int rowCount,
        final int columnCount
    ) {
        return new MenuEditWorkersPanel(currentWorkerNames, availableWorkerNames, rowCount, columnCount);
    }

    private MenuEditWorkersPanel(
        @Nonnull final List<String> currentWorkerNames,
        @Nonnull final List<String> availableWorkerNames,
        final int rowCount,
        final int columnCount
    ) {
        workerComboBoxGridPanel = WorkerComboBoxGridPanel.from(
            currentWorkerNames,
            availableWorkerNames,
            rowCount,
            columnCount,
            Settings.BACKGROUND_COLOR
        );

        loadFromExcelButton = Button.from(
            "Load from Excel Template",
            Settings.ALTERNATE_BUTTON_COLORS,
            () -> workerComboBoxGridPanel.setSelectedWorkers(GlobalData.getInstance().getDefaultWorkerNames())
        );

        setLayout(new MigLayout("fill, insets 0"));
        setBackground(Settings.BACKGROUND_COLOR);
        add(workerComboBoxGridPanel, "wrap");
        add(loadFromExcelButton, "align right");

        connectFocusableComponents();
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return Arrays.asList(
            Collections.singletonList(workerComboBoxGridPanel),
            Collections.singletonList(loadFromExcelButton)
        );
    }

    @Override
    public List<String> get() {
        return workerComboBoxGridPanel.getSelectedWorkers();
    }
}
