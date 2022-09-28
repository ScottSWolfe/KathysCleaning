package com.github.scottswolfe.kathyscleaning.lbc.view;

import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.google.common.collect.ImmutableList;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LBCDaysPanel extends JPanel implements FocusableCollection {

    public static final List<DayOfWeek> LBC_DAYS = ImmutableList.of(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    );

    public final static int PANEL_PADDING = 4;
    private static final String DAY_PANEL_CONSTRAINTS = "grow";
    private static final String DAY_PANEL_CONSTRAINTS_WITH_BOTTOM_PADDING =
        DAY_PANEL_CONSTRAINTS + ", wrap " + PANEL_PADDING;

    public static LBCDaysPanel from() {
        return new LBCDaysPanel();
    }

    private LBCDaysPanel() {

        final List<LBCDayPanel> daysCompleted = LBC_DAYS.stream()
            .map(dayOfWeek -> LBCDayPanel.from(dayOfWeek))
            .collect(Collectors.toList());

        setLayout(new MigLayout("fill, insets 0"));
        setBackground(Settings.BACKGROUND_COLOR);
        for (int index = 0; index < daysCompleted.size() - 1; index++) {
            final LBCDayPanel lbcDayPanel = daysCompleted.get(index);
            add(lbcDayPanel, DAY_PANEL_CONSTRAINTS_WITH_BOTTOM_PADDING);
        }
        add(daysCompleted.get(daysCompleted.size() - 1), DAY_PANEL_CONSTRAINTS);

        connectFocusableComponents();
    }

    public LBCDayPanel getLBCDayPanel(final int index) {
        final List<LBCDayPanel> lbcDayPanels = getLBCDayPanels();
        if (index < 0 || index >= lbcDayPanels.size()) {
            throw new IllegalArgumentException("index is out of bounds. index: " + index);
        }
        return getLBCDayPanels().get(index);
    }

    public List<LBCDayPanel> getLBCDayPanels() {
        synchronized (getTreeLock()) {
            return Arrays.stream(getComponents())
                .map(component -> (LBCDayPanel) component)
                .collect(Collectors.toList());
        }
    }

    public void setWorkers(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        getLBCDayPanels().forEach(lbcDayPanel -> lbcDayPanel.setWorkers(workerSelectionGrid));
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return getLBCDayPanels().stream()
            .map(Collections::singletonList)
            .collect(Collectors.toList());
    }
}
