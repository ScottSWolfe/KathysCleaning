package com.github.scottswolfe.kathyscleaning.completed.view;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class DayPanel extends JPanel implements FocusableCollection {

    public final static int MAX_HOUSE_PANEL_COUNT = 5;
    public final static int DEFAULT_HOUSE_PANEL_COUNT = 3;
    public final static int PANEL_PADDING = 6;

    public final HeaderPanel headerPanel;
    public final HousePanelsScrollPane scrollPane;

    public static DayPanel from(
        final WorkerList workerList,
        final ActionListener previousDayButtonListener,
        final ActionListener nextDayButtonListener,
        final ActionListener submitFormListener,
        final WindowListener popUpWindowListener
    ) {
        return new DayPanel(
             workerList,
             previousDayButtonListener,
             nextDayButtonListener,
             submitFormListener,
             popUpWindowListener
        );
    }

    private DayPanel(
        final WorkerList workerList,
        final ActionListener previousDayButtonListener,
        final ActionListener nextDayButtonListener,
        final ActionListener submitFormListener,
        final WindowListener popUpWindowListener
    ) {
        headerPanel = HeaderPanel.from(
            workerList,
            this::setWorkerSelectionsForAllHouses,
            previousDayButtonListener,
            nextDayButtonListener,
            popUpWindowListener,
            submitFormListener
        );

        scrollPane = HousePanelsScrollPane.from(
            popUpWindowListener,
            this::moveHousePanelUp,
            this::moveHousePanelDown,
            this::addHousePanel,
            this::deleteHousePanel
        );

        setLayout(new MigLayout("fillx, insets 0"));
        setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));
        setBackground(Settings.BACKGROUND_COLOR);
        add(headerPanel, "growx, wrap 0");
        add(scrollPane, "growx");

        connectFocusableComponents();
    }

    public int getHousePanelCount() {
        return scrollPane.getHousePanels().size();
    }

    public HousePanel getHousePanel(final int index) {
        return scrollPane.getHousePanels().get(index);
    }

    public int getScrollPanePanelHeight() {
        return scrollPane.getScrollPanePanelHeight();
    }

    public void addNewHousePanel() {
        addHousePanel(getHousePanelCount());
    }

    public void deleteHousePanel() {
        deleteHousePanel(getHousePanelCount());
    }

    private void setWorkerSelectionsForAllHouses(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        for (int index = 0; index < getHousePanelCount(); index++) {
            getHousePanel(index).setWorkers(workerSelectionGrid);
        }
    }

    private void moveHousePanelUp(final HousePanel housePanel) {
        final int houseIndex = findHousePanelIndex(housePanel);
        if (houseIndex > 0) {
            swapHousePanels(houseIndex, houseIndex - 1);
        }
    }

    private void moveHousePanelDown(final HousePanel housePanel) {
        final int houseIndex = findHousePanelIndex(housePanel);
        if (houseIndex < getHousePanelCount() - 1) {
            swapHousePanels(houseIndex, houseIndex + 1);
        }
    }

    private void addHousePanel(final HousePanel existingHousePanel) {
        final int existingHouseIndex = findHousePanelIndex(existingHousePanel);
        addHousePanel(existingHouseIndex + 1);
    }

    private void addHousePanel(final int newHouseIndex) {
        if (getHousePanelCount() == MAX_HOUSE_PANEL_COUNT) {
            return;
        }
        validateIndexOnAdd(newHouseIndex);
        scrollPane.addHousePanel(newHouseIndex);
    }

    private void deleteHousePanel(final HousePanel housePanel) {
        final int houseIndex = findHousePanelIndex(housePanel);
        deleteHousePanel(houseIndex);
    }

    private void deleteHousePanel(final int houseIndex) {
        if (getHousePanelCount() == 1) {
            return;
        }
        scrollPane.deleteHousePanel(houseIndex);
    }

    private int findHousePanelIndex(final HousePanel housePanel) {
        for (int houseIndex = 0; houseIndex < getHousePanelCount(); houseIndex++) {
            if (getHousePanel(houseIndex) == housePanel) {
                return houseIndex;
            }
        }
        throw new RuntimeException("Could not find house panel in day panel's list of house panels.");
    }

    private void swapHousePanels(final int index1, final int index2) {
        validateIndexOnRead(index1);
        validateIndexOnRead(index2);
        scrollPane.swapHousePanels(index1, index2);
    }

    private void validateIndexOnRead(final int index) {
        if (index < 0 || index >= getHousePanelCount()) {
            throwIndexOutOfBoundsException(index);
        }
    }

    private void validateIndexOnAdd(final int index) {
        if (index < 0 || index > getHousePanelCount()) {
            throwIndexOutOfBoundsException(index);
        }
    }

    private void throwIndexOutOfBoundsException(final int index) {
        throw new IllegalArgumentException(
            String.format("Index out of bounds. House Panel length: %s. Index: %s.", getHousePanelCount(), index)
        );
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Arrays.asList(
            Collections.singletonList(headerPanel),
            Collections.singletonList(scrollPane)
        );
    }
}
