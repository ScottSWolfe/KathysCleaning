package com.github.scottswolfe.kathyscleaning.scheduled.model;

import java.util.ArrayList;
import java.util.List;

public class WorkerSchedule {


    // FIELDS
    String name;
    private boolean isLBCSelected;
    private String lbcTime;
    private String lbcNote;
    String location;
    public String time;
    public List<String> houseList;
    public List<String> noteList;
    String schedule;

    public String ex_note;
    public boolean ex_note_written = false;

    public boolean working_covenant = false;

    String covenant; // still needed??




    // CONSTRUCTOR
    public WorkerSchedule() {
        name = "";
        isLBCSelected = false;
        lbcTime = "";
        lbcNote = "";
        location = "";
        time = "";
        houseList = new ArrayList<>();
        noteList = new ArrayList<>();
        schedule = "";
        ex_note = "";
    }


    // METHDOS


    // isEquivalent checks a string character by character to see if it matches the name of the worker schedule
    public boolean isEquivalent( String s ) {

        // putting new string into Character array
        Character[] c = null;
        if ( s != null) {
            c = new Character[s.length()];
            for ( int i=0; i<s.length(); i++) {
                c[i] = s.charAt(i);
            }
        }
        else {
            return false;
        }

        // putting name into Character array
        Character[] k = null;
        if ( name != null) {
            k = new Character[name.length()];
            for ( int i=0; i<name.length(); i++) {
                k[i] = name.charAt(i);
            }
        }
        else {
            return false;
        }


        // comparing the two arrays
        boolean done = false;
        int i=0;
        while ( !done ) {

            // if
            if (k.length <= i) {
                done = true;
            }
            else if ( c.length > i && c[i].equals(k[i]) ) {
                i++;
            }
            else {
                return false;
            }

        }

        return true;

    }


    // addHouse adds a house string onto the array of house strings that may or may not already exist
    public void addHouse(String newHouse) {
        houseList.add(newHouse);
    }


    // addNote adds a note string onto the array of not strings that may or may not already exist
    public void addNote(String newNote) {
        noteList.add(newNote);
    }

    public void setLBCSelected(final boolean LBCSelected) {
        isLBCSelected = LBCSelected;
    }

    public boolean isLBCSelected() {
        return isLBCSelected;
    }

    public void setLbcTime(final String lbcTime) {
        this.lbcTime = lbcTime;
    }

    public String getLbcTime() {
        return lbcTime;
    }

    public void setLbcNote(final String lbcNote) {
        this.lbcNote = lbcNote;
    }

    public String getLbcNote() {
        return lbcNote;
    }

    public void setSchedule( String s ) {
        schedule = s;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMeetLocation() {
        return location;
    }

    public void setMeetLocation(String location) {
        this.location = location;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public List<String> getHouseList() {
        return houseList;
    }

    public void setHouse(List<String> houseList) {
        this.houseList = houseList;
    }


    public String getCovenant() {
        return covenant;
    }

    public void setCovenant(String covenant) {
        this.covenant = covenant;
    }


    public List<String> getNote() {
        return noteList;
    }

    public void setNote(List<String> noteList) {
        this.noteList = noteList;
    }
}
