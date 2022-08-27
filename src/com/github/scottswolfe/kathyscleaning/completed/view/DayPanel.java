package com.github.scottswolfe.kathyscleaning.completed.view;

import java.util.Calendar;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class DayPanel extends JPanel {

    // todo: make it so we don't have to store house panel references in both house panel array and on scroll pane

    public final static int MAX_HOUSE_PANEL_COUNT = 6;
    public final static int DEFAULT_HOUSE_PANEL_COUNT = 3;
    public final static int PANEL_PADDING = 6;
    private static final String HOUSE_PANEL_CONSTRAINTS = "wrap " + PANEL_PADDING + ", grow";

    GeneralController<TabbedPane, CompletedModel> controller;
    TabbedPane tp;
    public JScrollPane jsp;
    public JPanel jsp_panel;
    Calendar date;
    MainFrame<TabbedPane, CompletedModel> frame;

    int wk;

    public HeaderPanel header_panel;
    public HousePanel[] house_panel;

    WorkerList dwd;

    public int mode;

    public DayPanel(
        GeneralController<TabbedPane, CompletedModel> controller,
        TabbedPane tp,
        WorkerList dwd,
        Calendar date,
        MainFrame<TabbedPane, CompletedModel> frame,
        int mode,
        int wk
    ) {
        this.controller = controller;
        this.tp = tp;
        this.dwd = dwd;
        this.date = date;
        this.frame = frame;
        this.mode = mode;
        this.wk = wk;

        header_panel = new HeaderPanel(
            controller, tp, dwd, this, date, frame, mode, wk
        );

        jsp_panel = new JPanel();
        jsp_panel.setLayout(new MigLayout());
        jsp_panel.setBackground(Settings.BACKGROUND_COLOR);

        house_panel = new HousePanel[DayPanel.DEFAULT_HOUSE_PANEL_COUNT];
        for (int i = 0; i < DayPanel.DEFAULT_HOUSE_PANEL_COUNT; i++) {
            final int houseIndex = i;
            house_panel[i] = HousePanel.from(
                frame,
                tp,
                this,
                (housePanel) -> ((event) -> moveHousePanelUp(housePanel)),
                (housePanel) -> ((event) -> moveHousePanelDown(housePanel)),
                (housePanel) -> ((event) -> addHousePanel(housePanel)),
                (housePanel) -> ((event) -> deleteHousePanel(housePanel))
            );
        }

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);

        add(header_panel, "dock north");

        for (HousePanel housePanel : house_panel) {
            jsp_panel.add(housePanel, HOUSE_PANEL_CONSTRAINTS);
        }

        jsp = new JScrollPane(
            jsp_panel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        jsp.setBackground(Settings.BACKGROUND_COLOR);

        add(jsp, "grow");

        // todo: use FocusableConnector to connect components for focus transfer
    }

    public void addNewHousePanel() {
        addHousePanel(house_panel.length);
    }

    public void deleteHousePanel() {
        deleteHousePanel(house_panel.length);
    }

    private HousePanel buildNewHousePanel() {
        return HousePanel.from(
            frame,
            tp,
            this,
            (housePanel) -> ((event) -> moveHousePanelUp(housePanel)),
            (housePanel) -> ((event) -> moveHousePanelDown(housePanel)),
            (housePanel) -> ((event) -> addHousePanel(housePanel)),
            (housePanel) -> ((event) -> deleteHousePanel(housePanel))
        );
    }

    // todo: set to private when possible
    public void setWorkerSelectionsForAllHouses(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        for (int index = 0; index < house_panel.length; index++) {
            house_panel[index].setWorkers(workerSelectionGrid);
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
        if (houseIndex < house_panel.length - 1) {
            swapHousePanels(houseIndex, houseIndex + 1);
        }
    }

    private void addHousePanel(final HousePanel existingHousePanel) {
        final int existingHouseIndex = findHousePanelIndex(existingHousePanel);
        addHousePanel(existingHouseIndex + 1);
    }

    private void addHousePanel(final int newHouseIndex) {
        if (house_panel.length == MAX_HOUSE_PANEL_COUNT) {
            return;
        }

        final HousePanel newHousePanel = buildNewHousePanel();

        addHousePanelToHousePanelArray(newHousePanel, newHouseIndex);
        jsp_panel.add(newHousePanel, HOUSE_PANEL_CONSTRAINTS, newHouseIndex);

        jsp_panel.revalidate();
        jsp_panel.repaint();
    }

    private void deleteHousePanel(final HousePanel housePanel) {
        final int houseIndex = findHousePanelIndex(housePanel);
        deleteHousePanel(houseIndex);
    }

    private void deleteHousePanel(final int houseIndex) {
        if (house_panel.length == 1) {
            return;
        }

        removeHousePanelFromHousePanelArray(houseIndex);
        jsp_panel.remove(houseIndex);

        jsp_panel.revalidate();
        jsp_panel.repaint();
    }

    private int findHousePanelIndex(final HousePanel housePanel) {
        for (int houseIndex = 0; houseIndex < house_panel.length; houseIndex++) {
            if (house_panel[houseIndex] == housePanel) {
                return houseIndex;
            }
        }
        throw new RuntimeException("Could not find house panel in day panel's list of house panels.");
    }

    private void swapHousePanels(final int index1, final int index2) {
        validateIndexOnRead(index1);
        validateIndexOnRead(index2);

        final int lowerIndex = Math.min(index1, index2);
        final int higherIndex = Math.max(index1, index2);

        final HousePanel housePanel1 = house_panel[lowerIndex];
        final HousePanel housePanel2 = house_panel[higherIndex];

        house_panel[lowerIndex] = housePanel2;
        house_panel[higherIndex] = housePanel1;

        jsp_panel.remove(higherIndex);
        jsp_panel.remove(lowerIndex);

        jsp_panel.add(housePanel2, HOUSE_PANEL_CONSTRAINTS, lowerIndex);
        jsp_panel.add(housePanel1, HOUSE_PANEL_CONSTRAINTS, higherIndex);

        jsp_panel.revalidate();
        jsp_panel.repaint();
    }

    private void addHousePanelToHousePanelArray(final HousePanel housePanel, final int index) {
        validateIndexOnAdd(index);

        final HousePanel[] newHousePanelArray = new HousePanel[house_panel.length + 1];
        System.arraycopy(house_panel, 0, newHousePanelArray, 0, index);
        newHousePanelArray[index] = housePanel;
        System.arraycopy(house_panel, index, newHousePanelArray, index + 1, house_panel.length - index);

        this.house_panel = newHousePanelArray;
    }

    private void removeHousePanelFromHousePanelArray(final int houseIndex) {
        final HousePanel[] newHousePanelArray = new HousePanel[house_panel.length - 1];
        System.arraycopy(house_panel, 0, newHousePanelArray, 0, houseIndex);
        System.arraycopy(
            house_panel,
            houseIndex + 1,
            newHousePanelArray,
            houseIndex,
            newHousePanelArray.length - houseIndex
        );
        house_panel = newHousePanelArray;
    }

    private void validateIndexOnRead(final int index) {
        if (index < 0 || index >= house_panel.length) {
            throwIndexOutOfBoundsException(index);
        }
    }

    private void validateIndexOnAdd(final int index) {
        if (index < 0 || index > house_panel.length) {
            throwIndexOutOfBoundsException(index);
        }
    }

    private void throwIndexOutOfBoundsException(final int index) {
        throw new IllegalArgumentException(
            String.format("Index out of bounds. House Panel length: %s. Index: %s.", house_panel.length, index)
        );
    }
}
