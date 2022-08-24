package com.github.scottswolfe.kathyscleaning.completed.view;

import java.util.Calendar;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.general.controller.KeyboardFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;

public class DayPanel extends JPanel {

// FIELDS ------------------------------------------------------------------- */

    GeneralController<TabbedPane, CompletedModel> controller;
    TabbedPane tp;
    public JScrollPane jsp;
    public JPanel jsp_panel;
    DayData day_data;
    Calendar date;
    MainFrame<TabbedPane, CompletedModel> frame;

    int wk;

    public HeaderPanel header_panel;
    public HousePanel[] house_panel;

    WorkerList dwd;

    public int mode;

    public final static int PANEL_PADDING = 6;
    public final static int TOP_INSET = 5;

    public final static int NUM_HOUSE_PANELS = 3;





    public DayPanel(GeneralController<TabbedPane, CompletedModel> controller, TabbedPane tp,
            WorkerList dwd, Calendar date, MainFrame<TabbedPane, CompletedModel> frame, int mode, int wk) {

        this.controller = controller;
        this.tp = tp;
        this.dwd = dwd;
        this.date = date;
        this.frame = frame;
        this.mode = mode;
        this.wk = wk;

        header_panel = new HeaderPanel(controller, tp, dwd, this,
                                        date, frame, mode, wk);

        // System.out.println

        jsp_panel = new JPanel();
        jsp_panel.setLayout( new MigLayout() );
        jsp_panel.setBackground(Settings.BACKGROUND_COLOR);

        house_panel = new HousePanel[DayPanel.NUM_HOUSE_PANELS];
        for(int i=0; i<DayPanel.NUM_HOUSE_PANELS; i++) {
            String temp = new String("House " + (i+1) );
            house_panel[i] = new HousePanel(temp,dwd,this,frame,tp);
        }

        addFlexibleFocusListeners();

        setLayout( new MigLayout( /*new String("insets " + TOP_INSET + " 5 0 5"),"",""*/) );
        setBackground(Settings.BACKGROUND_COLOR);

        add(header_panel, "dock north"); //new String("wrap " + PANEL_PADDING + ", growx") );

        for(int i=0; i<house_panel.length - 1; i++) {
            jsp_panel.add(house_panel[i], new String("wrap " + PANEL_PADDING + ", grow") );
        }
        jsp_panel.add(house_panel[house_panel.length-1], new String("wrap " + PANEL_PADDING + ", grow") );

        jsp = new JScrollPane( jsp_panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        jsp.setBackground(Settings.BACKGROUND_COLOR);

        add(jsp, "grow");

    }


    public DayPanel() {

    }



    // PRIVATE METHODS

    public void addFlexibleFocusListeners(){


        // adding focus listeners for textfields and buttons
        for (int i = 0; i < house_panel.length; i++) {

            HousePanel hp = house_panel[i];

            Optional<HousePanel> hp_up;
            if (i > 0) {
                hp_up = Optional.of(house_panel[i - 1]);
            } else {
                hp_up = Optional.empty();
            }

            Optional<HousePanel> hp_down;
            if (i < house_panel.length - 1) {
                hp_down = Optional.of(house_panel[i+1]);
            } else {
                hp_down = Optional.empty();
            }

            hp.house_name_txt.addFocusListener(KeyboardFocusListener.from(
                hp.house_name_txt,
                null,
                hp.getAmountEarnedComponent(),
                hp_up.map(housePanelUp -> housePanelUp.house_name_txt).orElse(null),
                hp_down.map(housePanelDown -> housePanelDown.house_name_txt).orElse(null),
                null
            ));

            hp.getAmountEarnedComponent().addFocusListener(KeyboardFocusListener.from(
                hp.getAmountEarnedComponent(),
                hp.house_name_txt,
                hp.time_begin_txt,
                hp_up.map(HousePanel::getAmountEarnedComponent).orElse(null),
                hp_down.map(HousePanel::getAmountEarnedComponent).orElse(null),
                null
            ));

            hp.time_begin_txt.addFocusListener(KeyboardFocusListener.from(
                hp.time_begin_txt,
                hp.getAmountEarnedComponent(),
                hp.time_end_txt,
                hp_up.map(housePanelUp -> housePanelUp.time_begin_txt).orElse(null),
                hp_down.map(housePanelDown -> housePanelDown.time_begin_txt).orElse(null),
                null
            ));

            hp.time_end_txt.addFocusListener(KeyboardFocusListener.from(
                hp.time_end_txt,
                hp.time_begin_txt,
                hp.workerSelectPanel.getComponentToFocusFromLeft(),
                hp_up.map(housePanelUp -> housePanelUp.time_end_txt).orElse(null),
                hp_down.map(housePanelDown -> housePanelDown.time_end_txt).orElse(null),
                hp_down.map(housePanelDown -> housePanelDown.time_begin_txt).orElse(null)
            ));

            hp.exceptions.addFocusListener(KeyboardFocusListener.from(
                hp.exceptions,
                hp.workerSelectPanel.getComponentToFocusFromRight(),
                hp.move_up,
                hp_up.map(housePanelUp -> housePanelUp.exceptions).orElse(null),
                hp_down.map(housePanelDown -> housePanelDown.exceptions).orElse(null),
                null
            ));
        }
    }





//  PUBLIC METHODS

    public void changeWorkerPanels(WorkerList new_dwd){

        //getting old size
        int header_width = header_panel.getWidth();
        int house_panel_width = house_panel[0].getWidth();

        header_panel.dwp.setWorkers(new_dwd);
        for (int i = 0; i < house_panel.length; i++) {
            house_panel[i].workerSelectPanel.setWorkers(new_dwd);
        }

        // revalidate and repaint
        frame.revalidate();
        frame.repaint();

        int new_header_width = header_panel.getWidth();
        int new_house_panel_width = house_panel[0].getWidth();

        int header_change = new_header_width - header_width;
        int house_panel_change = new_house_panel_width - house_panel_width;
        int change = 0;
        if(header_change > house_panel_change) {
            change = header_change;
        }
        else {
            change = house_panel_change;
        }


        frame.setSize( frame.getWidth() + change , frame.getHeight() );

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);

    }



}
