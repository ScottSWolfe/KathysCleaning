package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.controller.SubmitFormListener;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.Collections;
import java.util.List;

public class SubmitFormPanel extends JPanel implements FocusableCollection {

    private final KcButton submitFormButton;

    public static <View, Model> SubmitFormPanel from(final Controller<View, Model> controller) {
        return new SubmitFormPanel(controller);
    }

    private <View, Model> SubmitFormPanel(final Controller<View, Model> controller) {
        super();

        submitFormButton = new KcButton("Next", SubmitFormListener.from(controller));
        submitFormButton.setPreferredSize(new Dimension(100,40));
        submitFormButton.setBackground(Settings.MAIN_COLOR);
        submitFormButton.setForeground(Settings.FOREGROUND_COLOR);

        setLayout(new MigLayout("insets 2","[]","[grow]"));
        setBackground(Settings.HEADER_BACKGROUND);
        add(submitFormButton);

        connectFocusableComponents();
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(
            Collections.singletonList(submitFormButton)
        );
    }
}
