package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.HeaderData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.completed.view.CompletedTabbedPane;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HeaderPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledControllerHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import org.apache.commons.lang3.NotImplementedException;

public class CompletedControllerHelper implements ControllerHelper<CompletedTabbedPane, CompletedModel> {

    @Override
    public CompletedModel initializeModel() {
        return new CompletedModel();
    }

    @Override
    public CompletedTabbedPane initializeView(
        final GeneralController<CompletedTabbedPane, CompletedModel> controller,
        final MainFrame<CompletedTabbedPane, CompletedModel> parentFrame
    ) {
        return CompletedTabbedPane.from(
            parentFrame,
            controller
        );
    }

    @Override
    public void readViewAndWriteToFileHook(final CompletedTabbedPane view) {
        saveHousePay(view);
    }

    @Override
    public CompletedModel readViewIntoModel(final CompletedTabbedPane tp) {
        CompletedModel completedModel = new CompletedModel();
        DayData[] dayData = new DayData[5];

        // for each day
        for (int d = 0; d < dayData.length; d++) {

            // Header for day
            HeaderData headerData = new HeaderData();
            headerData.setDate(tp.day_panel[d].headerPanel.getDate());
            headerData.setWorkers(tp.day_panel[d].headerPanel.getWorkers());

            // Houses in day
            HouseData[] houseData = new HouseData[tp.day_panel[d].getHousePanelCount()];

            // for each house panel in the day
            for (int h = 0; h < houseData.length; h++) {
                houseData[h] = new HouseData();
                houseData[h].setHouseName(tp.day_panel[d].getHousePanel(h).getHouseNameText());
                houseData[h].setHousePay(tp.day_panel[d].getHousePanel(h).getAmountEarnedText());
                houseData[h].setTimeBegin(tp.day_panel[d].getHousePanel(h).getBeginTimeText());
                houseData[h].setTimeEnd(tp.day_panel[d].getHousePanel(h).getEndTimeText());
                houseData[h].setSelectedWorkers(tp.day_panel[d].getHousePanel(h).getSelectedWorkerNames());
                houseData[h].setWorkerList(tp.day_panel[d].getHousePanel(h).getWorkerList());
                houseData[h].setExceptionData(tp.day_panel[d].getHousePanel(h).getExceptionData());
            }

            dayData[d] = new DayData();
            dayData[d].setHouseData(houseData);
            dayData[d].setHeader(headerData);

        }  // end day panels

        completedModel.setDayData(dayData);
        return completedModel;
    }

    @Override
    public void writeModelToView(final CompletedModel completedModel, final CompletedTabbedPane tp) {

        DayPanel day_panel;
        DayData day_data;
        HousePanel house_panel;
        HouseData house_data;
        int num_house_panels;
        int num_house_datas;

        // iterate through each day
        for (int d = 0; d < 5; d++) {

            day_panel = tp.day_panel[d];
            day_data = completedModel.dayData[d];

            // set header panel
            HeaderPanel headerPanel = day_panel.headerPanel;
            HeaderData headerData = day_data.getHeaderData();
            headerPanel.setDate(headerData.getDate());
            headerPanel.setWorkers(headerData.getWorkers());

            num_house_panels = day_panel.getHousePanelCount();
            num_house_datas = day_data.houseData.length;

            // iterate through each house
            for (int h = 0; h < num_house_datas; h++) {

                if (h == DayPanel.MAX_HOUSE_PANEL_COUNT) {
                    // todo: log or notify user about this situation
                    break;
                }

                house_panel = day_panel.getHousePanel(h);
                house_data = day_data.houseData[h];

                house_panel.setHouseNameText(house_data.getHouseName());
                if (house_data.getHousePay() != 0) {
                    house_panel.setAmountEarnedText(
                            String.valueOf(house_data.getHousePay()));
                }
                house_panel.setBeginTimeText(house_data.getTimeBegin());
                house_panel.setEndTimeText(house_data.getTimeEnd());
                house_panel.setWorkers(house_data.getWorkerSelectionGrid());
                house_panel.setExceptionData(house_data.getExceptionData());

                // if there are more houses to fill in
                // and there are more empty house panels
                if (h < num_house_datas - 1 && h < num_house_panels - 1) {
                    // do nothing
                }
                // if there are more houses to fill in
                // but there are no more empty house panels
                else if (h < num_house_datas - 1 && h >= num_house_panels - 1) {
                    day_panel.addNewHousePanel();
                }
                // if there are no more houses to fill in
                // and there are more empty house panels
                else if (h >= num_house_datas - 1 && h < num_house_panels - 1) {
                    int numrepeat = num_house_panels - h - 1;
                    for (int k = h; k < numrepeat + h; k++) {
                        day_panel.deleteHousePanel();
                    }
                }

            }
        }
    }

    @Override
    public void saveModelToFile(final CompletedModel model, final File file) {
        JsonMethods.saveToFileJSON(model, CompletedModel.class, file, Form.COMPLETED.getNum());
    }

    @Override
    public CompletedModel loadFromFile(File file) {
        return (CompletedModel) JsonMethods.loadFromFileJSON(CompletedModel.class, file, Form.COMPLETED.getNum());
    }

    @Override
    public void updateDate(
        final Controller<CompletedTabbedPane, CompletedModel> controller,
        final CompletedTabbedPane tp
    ) {
        ChooseWeekPanel.initializePanel(controller, false);
    }

    @Override
    public void updateWorkersOnModel(final CompletedModel completedModel, final List<List<String>> workerNames) {
        completedModel.setWorkers(workerNames);
    }

    /**
     * Saves the amount of money earned at a house as the house's default
     * amount earned.
     *
     * todo: rewrite so can be saved as key-value pairs in json
     */
    public void saveHousePay(final CompletedTabbedPane view) {

        BufferedWriter bw = null;
        try {

            Scanner input = new Scanner( HouseNameDocumentListener.HOUSE_PAY_FILE );
            Scanner input2 = new Scanner( HouseNameDocumentListener.HOUSE_PAY_FILE );

            int i=0;
            while (input2.hasNextLine()) {
                input2.nextLine();
                i++;
            }

            String[] s = new String[i];

            for( int j=0; j<i; j++) {
                s[j] = input.nextLine();
            }

            input.close();
            input2.close();

            // for each day
            for (int d=0; d<5; d++) {

                DayPanel dp = view.day_panel[d];

                // for each house
                for (int h = 0; h<dp.getHousePanelCount(); h++) {

                    boolean match = false;

                    // for length of array
                    for (int k=0; k<s.length; k++) {

                        if (s[k].equalsIgnoreCase( dp.getHousePanel(h).getHouseNameText() )) {

                            s[k+1] = dp.getHousePanel(h).getAmountEarnedText();
                            match = true;
                            break;
                        }

                    }
                    if (match == false) {

                        String[] r = new String[s.length+2];

                        for (int l=0; l<s.length; l++) {
                            r[l] = s[l];
                        }

                        r[r.length-2] = dp.getHousePanel(h).getHouseNameText();
                        r[r.length-1] = dp.getHousePanel(h).getAmountEarnedText();

                        s = r;
                    }
                }
            }

            FileWriter fw = new FileWriter( HouseNameDocumentListener.HOUSE_PAY_FILE );
            bw = new BufferedWriter( fw );
            for (int m=0; m<s.length; m++) {
                bw.write(s[m]);
                bw.newLine();
            }
            bw.close();
        }
        catch(Exception e1){
            e1.printStackTrace();
        }
    }

    /**
     * Imports a scheduled form into completed form
     *
     * @param file the file that has previously been completed
     * @param tp the view into which to import the schedule
     */
    public static void importSchedule(File file, CompletedTabbedPane tp) {
        // todo: rewrite this logic
        throw new NotImplementedException("Behavior not implemented.");
    }



/* PRIVATE METHODS ========================================================== */

    private static CompletedModel scheduledToCompletedModel(NW_Data scheduledModel) {
        // todo: rewrite this logic
        throw new NotImplementedException("Behavior not implemented.");
    }
}
