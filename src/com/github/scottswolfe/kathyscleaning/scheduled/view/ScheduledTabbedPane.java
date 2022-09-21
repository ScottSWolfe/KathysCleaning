package com.github.scottswolfe.kathyscleaning.scheduled.view;

import com.github.scottswolfe.kathyscleaning.general.controller.FormController;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;

import javax.swing.JTabbedPane;
import java.util.Calendar;

public class ScheduledTabbedPane extends JTabbedPane {

    public NW_DayPanel[] nw_day_panel;

    public static ScheduledTabbedPane from(
        final MainFrame<ScheduledTabbedPane, NW_Data> parentFrame,
        final FormController<ScheduledTabbedPane, NW_Data> controller
    ) {
        return new ScheduledTabbedPane(parentFrame, controller);
    }

    private ScheduledTabbedPane(
        final MainFrame<ScheduledTabbedPane, NW_Data> parentFrame,
        final FormController<ScheduledTabbedPane, NW_Data> controller
    ) {
        super();

        setFont(getFont().deriveFont(Settings.TAB_FONT_SIZE));

        // creating array of dates
        Calendar[] day = new Calendar[5];
        Calendar date;
        if (SessionModel.getScheduledStartDay() == null) {
            date = SessionModel.getCompletedStartDay();
            date.add(Calendar.DATE, 7);
            SessionModel.setScheduledStartDay(date);
        } else {
            date = SessionModel.getScheduledStartDay();
        }
        for(int i = 0; i < day.length; i++) {
            day[i] = Calendar.getInstance();
            day[i].set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
            date.add(Calendar.DATE, 1);
        }

        final WorkerList workers = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());

        NW_DayPanel[] day_panel = new NW_DayPanel[5];
        for(int i = 0; i < 5; i++){
            day_panel[i] = new NW_DayPanel(controller, this, workers, day[i], parentFrame);
        }
        nw_day_panel = day_panel;

        addTab("Monday", day_panel[0]);
        addTab("Tuesday", day_panel[1]);
        addTab("Wednesday", day_panel[2]);
        addTab("Thursday", day_panel[3]);
        addTab("Friday", day_panel[4]);
    }
}
