package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.List;

public class PopUpFormSubmissionPanel extends JPanel implements FocusableCollection {

    private final JPanel formPanel;
    private final JButton cancelButton;
    private final JButton submitButton;

    public static PopUpFormSubmissionPanel from(
        final JPanel formPanel,
        final Runnable onCancel,
        final Runnable onSubmit
    ) {
        return new PopUpFormSubmissionPanel(formPanel, onCancel, onSubmit);
    }

    private PopUpFormSubmissionPanel(
        final JPanel formPanel,
        final Runnable onCancel,
        final Runnable onSubmit
    ) {
        this.formPanel = formPanel;
        cancelButton = Button.from("Cancel", onCancel);
        submitButton = Button.from("Submit", onSubmit);

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);

        add(formPanel, "wrap");
        add(cancelButton, "gapleft push, split 2");
        add(submitButton);

        connectFocusableComponents();
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Arrays.asList(
            Arrays.asList(formPanel, FocusableCollection.GAP),
            Arrays.asList(cancelButton, submitButton)
        );
    }
}
