package com.github.scottswolfe.kathyscleaning.general.controller;

import com.github.scottswolfe.kathyscleaning.utility.FormLauncher;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubmitFormListener<View extends JComponent, Model> implements ActionListener {

    private final FormLauncher formLauncher;
    private final FormController<View, Model> controller;

    public static <View extends JComponent, Model> SubmitFormListener<View, Model> from(
        final FormController<View, Model> controller
    ) {
        return new SubmitFormListener<>(controller);
    }

	private SubmitFormListener(final FormController<View, Model> controller) {
        this.formLauncher = FormLauncher.from();
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        formLauncher.launchNextForm(controller.getFormType());
	}
}
