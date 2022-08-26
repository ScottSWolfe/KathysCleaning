package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import java.util.Collections;
import java.util.List;

public class HouseNamePanel extends JPanel implements FocusableCollection {

    private final JLabel houseNameLabel;
    private final JTextField houseNameTextField;

    public static HouseNamePanel from(final DocumentListener houseNameDocumentListener) {
        return new HouseNamePanel(houseNameDocumentListener);
    }

    private HouseNamePanel(final DocumentListener houseNameDocumentListener) {
        setLayout(new MigLayout("insets 0, ay 50%"));
        setBackground( Settings.BACKGROUND_COLOR );

        houseNameLabel = new JLabel("House Name");
        houseNameLabel.setFont(houseNameLabel.getFont().deriveFont(Settings.FONT_SIZE));

        houseNameTextField = new JTextField(10);
        houseNameTextField.setFont(houseNameTextField.getFont().deriveFont( Settings.FONT_SIZE));
        houseNameTextField.getDocument().addDocumentListener(houseNameDocumentListener);

        add(houseNameLabel,"wrap, gap 3");
        add(houseNameTextField);

        connectFocusableComponents();
    }

    public String getHouseNameText() {
        return houseNameTextField.getText();
    }

    public void setHouseNameText(final String houseName) {
        houseNameTextField.setText(houseName);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(Collections.singletonList(houseNameTextField));
    }
}
