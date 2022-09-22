package com.github.scottswolfe.kathyscleaning.completed.view;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.FormController;
import com.github.scottswolfe.kathyscleaning.general.controller.NextDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.PreviousDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.SubmitFormListener;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.swing.JTabbedPane;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompletedTabbedPane extends JTabbedPane {

    public DayPanel[] day_panel;

    public static final List<DayOfWeek> COMPLETED_FORM_DAYS = ImmutableList.of(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    );

    private static final Map<DayOfWeek, Integer> DAY_TO_CALENDAR_OFFSET = ImmutableMap.of(
        DayOfWeek.MONDAY, 0,
        DayOfWeek.TUESDAY, 1,
        DayOfWeek.WEDNESDAY, 2,
        DayOfWeek.THURSDAY, 3,
        DayOfWeek.FRIDAY, 4
    );

    public static CompletedTabbedPane from(
        final MainFrame<CompletedTabbedPane, CompletedModel> parentFrame,
        final FormController<CompletedTabbedPane, CompletedModel> controller
    ) {
        return new CompletedTabbedPane(parentFrame, controller);
    }

    private CompletedTabbedPane(
        final MainFrame<CompletedTabbedPane, CompletedModel> parentFrame,
        final FormController<CompletedTabbedPane, CompletedModel> controller
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
    }

    private List<DayPanel> createDayPanels(
        final MainFrame<CompletedTabbedPane, CompletedModel> parentFrame,
        final FormController<CompletedTabbedPane, CompletedModel> controller
    ) {
        final WorkerList workers = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());
        return COMPLETED_FORM_DAYS.stream()
            .map(dayOfWeek -> {
                final Calendar date = SessionModel.getCompletedStartDay();
                date.add(Calendar.DATE, DAY_TO_CALENDAR_OFFSET.get(dayOfWeek));
                return DayPanel.from(
                    date,
                    workers,
                    new PreviousDayListener(this),
                    new NextDayListener(this),
                    SubmitFormListener.from(controller),
                    new FrameCloseListener(parentFrame)
                );
            })
            .collect(Collectors.toList());
    }
}
