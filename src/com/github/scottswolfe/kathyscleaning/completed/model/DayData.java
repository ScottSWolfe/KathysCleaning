package com.github.scottswolfe.kathyscleaning.completed.model;

import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class DayData {

	HeaderData headerData;
	public HouseData[] houseData;

	public DayData() {
	    headerData = new HeaderData();
	    houseData = new HouseData[3];// TODO change to dynamic List
	    for (int i = 0; i < houseData.length; i++) {
	        houseData[i] = new HouseData();
	    }
	}

	public void setHeader(HeaderData headerData) {
		this.headerData = headerData;
	}

	public HeaderData getHeaderData() {
		return headerData;
	}

	public void setHouseData(HouseData[] houseData) {
		this.houseData = houseData;
	}

	public HouseData[] getHouseData() {
		return houseData;
	}

    public void addHouseData(final HouseData newHouseData) {
        houseData = ArrayUtils.add(houseData, newHouseData);
    }

    public void updateWorkersAndKeepExistingSelections(@Nonnull final List<String> workerNames) {
        headerData.updateWorkersAndKeepExistingSelections(workerNames);
        for (HouseData house : houseData) {
            house.updateWorkersAndKeepExistingSelections(workerNames);
        }
    }
}
