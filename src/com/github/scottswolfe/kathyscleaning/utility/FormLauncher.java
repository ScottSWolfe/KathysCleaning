package com.github.scottswolfe.kathyscleaning.utility;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;

import java.util.HashMap;
import java.util.Map;

public class FormLauncher {

    private static final FormLauncher formLauncher = new FormLauncher();

    private final Map<Form, Form> currentToNextForm;

    public static FormLauncher from() {
        return formLauncher;
    }

    private FormLauncher() {
        currentToNextForm = new HashMap<>();
        currentToNextForm.put(Form.COMPLETED, Form.COVENANT);
        currentToNextForm.put(Form.COVENANT, Form.WEEKEND);
        currentToNextForm.put(Form.WEEKEND, Form.SCHEDULED);
    }

    public void launchNextForm(final Form currentForm) {
        new GeneralController<>(currentToNextForm.get(currentForm)).initializeForm();
    }
}
