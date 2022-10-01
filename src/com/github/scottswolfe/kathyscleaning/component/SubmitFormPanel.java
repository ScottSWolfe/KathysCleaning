package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

public class SubmitFormPanel extends JPanel implements FocusableCollection {

    private static final String DEFAULT_LABEL = "Next";

    private final KcButton submitFormButton;

    public static SubmitFormPanel from(final ActionListener submitFormListener) {
        return new SubmitFormPanel(DEFAULT_LABEL, submitFormListener);
    }

    public static SubmitFormPanel from(final String label, final ActionListener submitFormListener) {
        return new SubmitFormPanel(label, submitFormListener);
    }

    private SubmitFormPanel(
        final String label,
        final ActionListener submitFormListener
    ) {
        super();

        submitFormButton = new KcButton(label, submitFormListener);
        submitFormButton.setPreferredSize(new Dimension(100,40));
        submitFormButton.setBackground(Settings.MAIN_COLOR);
        submitFormButton.setForeground(Settings.FOREGROUND_COLOR);

        setLayout(new MigLayout("insets 2","[]","[grow]"));
        setBackground(Settings.HEADER_BACKGROUND);
        add(submitFormButton);

        connectFocusableComponents();
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return Collections.singletonList(
            Collections.singletonList(submitFormButton)
        );
    }
}
