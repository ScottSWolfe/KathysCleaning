package com.github.scottswolfe.kathyscleaning.scheduled.view;

import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;

import javax.annotation.Nonnull;
import javax.swing.JTabbedPane;
import java.util.Calendar;

public class ScheduledTabbedPane extends JTabbedPane {

    public NW_DayPanel[] nw_day_panel;

    public static ScheduledTabbedPane from(@Nonnull final Calendar scheduledStartDay) {
        return new ScheduledTabbedPane(scheduledStartDay);
    }

    private ScheduledTabbedPane(@Nonnull final Calendar scheduledStartDay) {
        super();

        setFont(getFont().deriveFont(Settings.TAB_FONT_SIZE));

        // creating array of dates
        Calendar[] day = new Calendar[5];
        for (int i = 0; i < day.length; i++) {
            day[i] = CalendarMethods.copy(scheduledStartDay);
            day[i].add(Calendar.DATE, i);
        }

        final WorkerList workers = new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames());

        NW_DayPanel[] day_panel = new NW_DayPanel[5];
        for(int i = 0; i < 5; i++){
            day_panel[i] = new NW_DayPanel(this, workers, day[i]);
        }
        nw_day_panel = day_panel;

        addTab("Monday", day_panel[0]);
        addTab("Tuesday", day_panel[1]);
        addTab("Wednesday", day_panel[2]);
        addTab("Thursday", day_panel[3]);
        addTab("Friday", day_panel[4]);
    }
}
