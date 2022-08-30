package com.github.scottswolfe.kathyscleaning.completed.view;

import com.github.scottswolfe.kathyscleaning.completed.controller.TabChangeListener;
import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import java.util.Calendar;

public class CompletedTabbedPane extends TabbedPane {

    public static CompletedTabbedPane from(
        final MainFrame<TabbedPane, CompletedModel> parentFrame,
        final Controller<TabbedPane, CompletedModel> controller
    ) {
        return new CompletedTabbedPane(parentFrame, controller);
    }

    private CompletedTabbedPane(
        final MainFrame<TabbedPane, CompletedModel> parentFrame,
        final Controller<TabbedPane, CompletedModel> controller
    ) {
        setFont(getFont().deriveFont(Settings.TAB_FONT_SIZE));

        // Creating array of dates
        final Calendar[] day = new Calendar[5];
        Calendar temp_date = SessionModel.getCompletedStartDay();
        for (int i = 0; i < day.length; i++) {
            day[i] = Calendar.getInstance();
            day[i].set(temp_date.get(Calendar.YEAR), temp_date.get(Calendar.MONTH), temp_date.get(Calendar.DATE));
            temp_date.add(Calendar.DATE, 1);
        }

        final DayPanel[] dayPanels = new DayPanel[5];
        WorkerList workers = new WorkerList(WorkerList.HOUSE_WORKERS);
        for (int i = 0; i < dayPanels.length; i++) {
            dayPanels[i] = new DayPanel(
                controller,
                this,
                workers,
                new FrameCloseListener(parentFrame)
                // TODO remove wk = 2
            );
        }
        day_panel = dayPanels;

        addTab("Monday", dayPanels[0]);
        addTab("Tuesday", dayPanels[1]);
        addTab("Wednesday", dayPanels[2]);
        addTab("Thursday", dayPanels[3]);
        addTab("Friday", dayPanels[4]);

        changePreviousTab(0);
        addChangeListener(new TabChangeListener(this, parentFrame));
    }
}
