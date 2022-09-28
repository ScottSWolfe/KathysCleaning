package com.github.scottswolfe.kathyscleaning.utility;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class FormLauncher {

    private static final FormLauncher formLauncher = new FormLauncher();

    private static final Map<Form, Form> currentToNextForm = ImmutableMap.of(
        Form.COMPLETED, Form.COVENANT,
        Form.COVENANT, Form.LBC,
        Form.LBC, Form.WEEKEND,
        Form.WEEKEND, Form.SCHEDULED
    );

    public static FormLauncher from() {
        return formLauncher;
    }

    private FormLauncher() {}

    public void launchNextForm(final Form currentForm) {
        final Form nextForm = currentToNextForm.get(currentForm);

        if (nextForm == null) {
            throw new IllegalArgumentException("Unexpected form: " + currentForm);
        }

        ApplicationCoordinator.getInstance().navigateToForm(nextForm);
    }

    public void launchFirstForm() {
        ApplicationCoordinator.getInstance().navigateToForm(Form.COMPLETED);
    }
}
