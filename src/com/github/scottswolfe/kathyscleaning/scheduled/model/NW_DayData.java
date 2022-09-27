package com.github.scottswolfe.kathyscleaning.scheduled.model;

import java.util.ArrayList;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

import javax.annotation.Nonnull;

public class NW_DayData {

    NW_HeaderData headerData;
    private ScheduledLBCData lbcData;
    public NW_HouseData[] houseData;
    public List<BeginExceptionEntry> beginExceptionList;
    public String meet_location;
    public String meet_time;
    public WorkerList cov_worker;
    public NoteData covNoteData;

    List<WorkerSchedule> ws;

    public NW_DayData() {
        headerData = new NW_HeaderData();
        lbcData = ScheduledLBCData.from();
        houseData = new NW_HouseData[3];
        for (int i = 0; i < 3; i++) {
            houseData[i] = new NW_HouseData();
        }
        beginExceptionList = new ArrayList<>();
        meet_location = "";
        meet_time = "";
        cov_worker = new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames());
        covNoteData = new NoteData();
        ws = new ArrayList<>();
    }

    public void setWorkerSchedule(List<WorkerSchedule> ws) {
        this.ws = ws;
    }

    public List<WorkerSchedule> getWorkerSchedule() {
        return ws;
    }

    public void setHeader(NW_HeaderData headerData) {
        this.headerData = headerData;
    }

    public ScheduledLBCData getLbcData() {
        return lbcData;
    }

    public void setLbcData(final ScheduledLBCData lbcData) {
        this.lbcData = lbcData;
    }

    public void setHouseData(NW_HouseData[] houseData) {
        this.houseData = houseData;
    }

    public NW_HeaderData getHeaderData() {
        return headerData;
    }

    public NW_HouseData[] getHouseData() {
        return houseData;
    }

    public void updateWorkersAndKeepSelections(@Nonnull final List<String> workerNames) {
        headerData.setDWD(WorkerList.from(workerNames, headerData.getDWD().getSelectedWorkerNames()));
        lbcData.updateWorkersAndKeepSelections(workerNames);
        for (NW_HouseData house : houseData) {
            house.setWorkerList(WorkerList.from(workerNames, house.getWorkerList().getSelectedWorkerNames()));
        }
        cov_worker = WorkerList.from(workerNames, cov_worker.getSelectedWorkerNames());
    }
}
