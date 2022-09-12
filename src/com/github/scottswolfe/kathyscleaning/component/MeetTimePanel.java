package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.Collections;
import java.util.List;

public class MeetTimePanel extends JPanel implements FocusableCollection {

    private final JTextField meetTimeTextField;

    public static MeetTimePanel from() {
        return new MeetTimePanel();
    }

    private MeetTimePanel() {

        final JLabel meetTimeLabel = new JLabel("Meet Time:");
        meetTimeLabel.setFont(meetTimeLabel.getFont().deriveFont(Settings.FONT_SIZE));
        meetTimeLabel.setForeground(Settings.MAIN_COLOR);

        meetTimeTextField = KcTextField.from("", 7, TimeKeyListener::new);

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);
        add(meetTimeLabel);
        add(meetTimeTextField);

        connectFocusableComponents();
    }

    public String getMeetTime() {
        return meetTimeTextField.getText();
    }

    public void setMeetTime(final String meetTime) {
        meetTimeTextField.setText(meetTime);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(Collections.singletonList(meetTimeTextField));
    }
}
