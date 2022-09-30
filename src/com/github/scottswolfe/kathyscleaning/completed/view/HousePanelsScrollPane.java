package com.github.scottswolfe.kathyscleaning.completed.view;

import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HousePanelsScrollPane extends JScrollPane implements FocusableCollection {

    public final static int PANEL_PADDING = 2;

    private final JPanel scrollPanePanel;

    private final Consumer<HousePanel> moveHousePanelUp;
    private final Consumer<HousePanel> moveHousePanelDown;
    private final Consumer<HousePanel> addHousePanel;
    private final Consumer<HousePanel> deleteHousePanel;

    public static HousePanelsScrollPane from(
        final Consumer<HousePanel> moveHousePanelUp,
        final Consumer<HousePanel> moveHousePanelDown,
        final Consumer<HousePanel> addHousePanel,
        final Consumer<HousePanel> deleteHousePanel
    ) {
        return new HousePanelsScrollPane(
            moveHousePanelUp,
            moveHousePanelDown,
            addHousePanel,
            deleteHousePanel
        );
    }

    private HousePanelsScrollPane(
        final Consumer<HousePanel> moveHousePanelUp,
        final Consumer<HousePanel> moveHousePanelDown,
        final Consumer<HousePanel> addHousePanel,
        final Consumer<HousePanel> deleteHousePanel
    ) {
        super();

        this.moveHousePanelUp = moveHousePanelUp;
        this.moveHousePanelDown = moveHousePanelDown;
        this.addHousePanel = addHousePanel;
        this.deleteHousePanel = deleteHousePanel;

        scrollPanePanel = createScrollPanePanel(
            moveHousePanelUp,
            moveHousePanelDown,
            addHousePanel,
            deleteHousePanel
        );

        setViewportView(scrollPanePanel);
        setBackground(Settings.BACKGROUND_COLOR);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        connectFocusableComponents();
    }

    private static JPanel createScrollPanePanel(
        final Consumer<HousePanel> moveHousePanelUp,
        final Consumer<HousePanel> moveHousePanelDown,
        final Consumer<HousePanel> addHousePanel,
        final Consumer<HousePanel> deleteHousePanel
    ) {
        final JPanel scrollPanePanel = new JPanel();
        scrollPanePanel.setLayout(new MigLayout("insets 4 0 0 0"));
        scrollPanePanel.setBackground(Settings.BACKGROUND_COLOR);

        final List<HousePanel> housePanels = new ArrayList<>(DayPanel.DEFAULT_HOUSE_PANEL_COUNT);
        for (int i = 0; i < DayPanel.DEFAULT_HOUSE_PANEL_COUNT; i++) {
            housePanels.add(buildNewHousePanel(
                moveHousePanelUp,
                moveHousePanelDown,
                addHousePanel,
                deleteHousePanel
            ));
        }

        for (int index = 0; index < housePanels.size(); index++) {
            final HousePanel housePanel = housePanels.get(index);
            scrollPanePanel.add(housePanel, createHousePanelConstraints(index, housePanels.size()));
        }

        return scrollPanePanel;
    }

    public List<HousePanel> getHousePanels() {
        synchronized (scrollPanePanel.getTreeLock()) {
            return Arrays.stream(scrollPanePanel.getComponents())
                .filter(component -> component instanceof HousePanel)
                .map(component -> (HousePanel) component)
                .collect(Collectors.toList());
        }
    }

    public int getScrollPanePanelHeight() {
        return scrollPanePanel.getHeight();
    }

    public void addHousePanel(final int newHouseIndex) {
        final HousePanel newHousePanel = buildNewHousePanel();
        scrollPanePanel.add(newHousePanel, newHouseIndex);
        updateHousePanelConstraints();
        connectFocusableComponents();
        scrollPanePanel.invalidate();
        ApplicationCoordinator.getInstance().refreshWindow();
    }

    public void deleteHousePanel(final int houseIndex) {
        scrollPanePanel.remove(houseIndex);
        updateHousePanelConstraints();
        connectFocusableComponents();
        scrollPanePanel.invalidate();
        ApplicationCoordinator.getInstance().refreshWindow();
    }

    public void swapHousePanels(final int index1, final int index2) {
        final int lowerIndex = Math.min(index1, index2);
        final int higherIndex = Math.max(index1, index2);

        final HousePanel housePanel1 = (HousePanel) scrollPanePanel.getComponent(lowerIndex);
        final HousePanel housePanel2 = (HousePanel) scrollPanePanel.getComponent(higherIndex);

        scrollPanePanel.remove(higherIndex);
        scrollPanePanel.remove(lowerIndex);

        scrollPanePanel.add(housePanel2, lowerIndex);
        scrollPanePanel.add(housePanel1, higherIndex);

        updateHousePanelConstraints();
        connectFocusableComponents();
        scrollPanePanel.invalidate();
        ApplicationCoordinator.getInstance().refreshWindow();
    }

    private void updateHousePanelConstraints() {
        final List<HousePanel> housePanels = getHousePanels();
        housePanels.forEach(scrollPanePanel::remove);
        for (int index = 0; index < housePanels.size(); index++) {
            scrollPanePanel.add(housePanels.get(index), createHousePanelConstraints(index, housePanels.size()), index);
        }
    }

    private HousePanel buildNewHousePanel() {
        return buildNewHousePanel(
            moveHousePanelUp,
            moveHousePanelDown,
            addHousePanel,
            deleteHousePanel
        );
    }

    private static HousePanel buildNewHousePanel(
        final Consumer<HousePanel> moveHousePanelUp,
        final Consumer<HousePanel> moveHousePanelDown,
        final Consumer<HousePanel> addHousePanel,
        final Consumer<HousePanel> deleteHousePanel
    ) {
        return HousePanel.from(
            (housePanel) -> ((event) -> moveHousePanelUp.accept(housePanel)),
            (housePanel) -> ((event) -> moveHousePanelDown.accept(housePanel)),
            (housePanel) -> ((event) -> addHousePanel.accept(housePanel)),
            (housePanel) -> ((event) -> deleteHousePanel.accept(housePanel))
        );
    }

    private static String createHousePanelConstraints(final int index, final int numberOfHousePanels) {
        final int wrapPadding = index < numberOfHousePanels - 1 ? PANEL_PADDING : 0;
        return "growx, wrap " + wrapPadding;
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return getHousePanels().stream()
            .map(Collections::singletonList)
            .collect(Collectors.toList());
    }
}
