package com.github.scottswolfe.kathyscleaning.completed.view;

import com.github.scottswolfe.kathyscleaning.completed.controller.TabChangeListener;
import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.NextDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.PreviousDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.SubmitFormListener;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

public class CompletedTabbedPane extends TabbedPane {

    public DayPanel[] day_panel;

    public static List<DayOfWeek> COMPLETED_FORM_DAYS = ImmutableList.of(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    );

    public static CompletedTabbedPane from(
        final MainFrame<CompletedTabbedPane, CompletedModel> parentFrame,
        final GeneralController<CompletedTabbedPane, CompletedModel> controller
    ) {
        return new CompletedTabbedPane(parentFrame, controller);
    }

    private CompletedTabbedPane(
        final MainFrame<CompletedTabbedPane, CompletedModel> parentFrame,
        final GeneralController<CompletedTabbedPane, CompletedModel> controller
    ) {
        final List<DayPanel> dayPanels = createDayPanels(parentFrame, controller);

        // todo: remove need for day_panel
        day_panel = dayPanels.toArray(new DayPanel[dayPanels.size()]);

        setFont(getFont().deriveFont(Settings.TAB_FONT_SIZE));
        addTab("Monday", dayPanels.get(0));
        addTab("Tuesday", dayPanels.get(1));
        addTab("Wednesday", dayPanels.get(2));
        addTab("Thursday", dayPanels.get(3));
        addTab("Friday", dayPanels.get(4));

        changePreviousTab(0);
        addChangeListener(new TabChangeListener(this, parentFrame));
    }

    private List<DayPanel> createDayPanels(
        final MainFrame<CompletedTabbedPane, CompletedModel> parentFrame,
        final GeneralController<CompletedTabbedPane, CompletedModel> controller
    ) {
        final WorkerList workers = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());
        return COMPLETED_FORM_DAYS.stream()
            .map(dayOfWeek -> DayPanel.from(
                workers,
                new PreviousDayListener(this),
                new NextDayListener(this),
                SubmitFormListener.from(controller),
                new FrameCloseListener(parentFrame)
            ))
            .collect(Collectors.toList());
    }
}
