package com.github.scottswolfe.kathyscleaning.general.controller;

import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.utility.FormLauncher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.github.scottswolfe.kathyscleaning.general.controller.GeneralController.TEMP_SAVE_FILE;

public class SubmitFormListener<View, Model> implements ActionListener {

    private final FormLauncher formLauncher;
    private final Controller<View, Model> controller;

    public static <View, Model> SubmitFormListener<View, Model> from(final Controller<View, Model> controller) {
        return new SubmitFormListener<View, Model>(controller);
    }

	private SubmitFormListener(final Controller<View, Model> controller) {
        this.formLauncher = FormLauncher.from();
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        controller.readInputAndWriteToFile(TEMP_SAVE_FILE);
        controller.eliminateWindow();
        formLauncher.launchNextForm(controller.getFormType());
	}
}
