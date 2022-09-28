package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.controller.PopUpFormWindowListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NoteData;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_NotePanel;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

public class NW_NoteListener implements ActionListener {

	NW_DayPanel day_panel;
	WorkerList dwd;
	public NoteData note_data;

	public NW_NoteListener(NW_DayPanel day_panel, WorkerList dwd, NoteData note_data) {
		this.day_panel = day_panel;
		this.dwd = dwd;
		this.note_data = note_data;
	}

	public void actionPerformed ( ActionEvent e ) {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);

		frame.addWindowListener(new PopUpFormWindowListener());

		NW_NotePanel np = new NW_NotePanel(frame, day_panel, dwd, this);

		frame.add(np);
		frame.pack();
		StaticMethods.findSetLocation(frame);
		frame.setVisible(true);
	}
}
