package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class MenuEditWorkersPanel extends JPanel implements Supplier<List<List<String>>>, FocusableCollection {

    private final WorkerComboBoxGridPanel workerComboBoxGridPanel;
    private final Button loadFromExcelButton;

    public static MenuEditWorkersPanel from(final int rowCount, final int columnCount) {
        return new MenuEditWorkersPanel(rowCount, columnCount);
    }

    private MenuEditWorkersPanel(final int rowCount, final int columnCount) {
        workerComboBoxGridPanel = WorkerComboBoxGridPanel.from(
            Collections.emptyList(),
            GlobalData.getInstance().getDefaultWorkerNames(),
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
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Arrays.asList(
            Collections.singletonList(workerComboBoxGridPanel),
            Collections.singletonList(loadFromExcelButton)
        );
    }

    @Override
    public List<List<String>> get() {
        return workerComboBoxGridPanel.getSelectedWorkers();
    }
}
