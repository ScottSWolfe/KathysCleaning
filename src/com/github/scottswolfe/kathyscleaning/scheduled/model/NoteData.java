package com.github.scottswolfe.kathyscleaning.scheduled.model;

import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_NotePanel;

public class NoteData {

    public String[] name_box_data;
    public String[] note_field_data;

    public NoteData() {
        name_box_data = new String[NW_NotePanel.ROWS];
        note_field_data = new String[NW_NotePanel.ROWS];
        for (int i = 0; i < NW_NotePanel.ROWS; i++) {
            name_box_data[i] = "";
            note_field_data[i] = "";
        }
    }

    public NoteData(String[] name_box_data, String[] note_field_data) {
        this.name_box_data = name_box_data;
        this.note_field_data = note_field_data;
    }

    public boolean isBlank() {
        for (String name : name_box_data) {
            if (name != null && !name.equals("")) {
                return false;
            }
        }
        for (String note : note_field_data) {
            if (note != null && !note.equals("")) {
                return false;
            }
        }
        return true;
    }

    public String[] getNameBoxData() {
        return name_box_data;
    }

    public String[] getNoteFieldData() {
        return note_field_data;
    }
}
