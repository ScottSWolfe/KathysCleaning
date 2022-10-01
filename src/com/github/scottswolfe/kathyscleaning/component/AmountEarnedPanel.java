package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.controller.DecimalNumberDocFilter;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import java.awt.Component;
import java.util.Collections;
import java.util.List;

public class AmountEarnedPanel extends JPanel implements FocusableCollection {

    private final JTextField amountEarnedTextField;

    public static AmountEarnedPanel from() {
        return new AmountEarnedPanel();
    }

    private AmountEarnedPanel() {
        super();

        setLayout(new MigLayout("insets 0, ay 50%"));
        setBackground(Settings.BACKGROUND_COLOR);

        final JLabel amountEarnedLabel = new JLabel("$ Earned");
        amountEarnedLabel.setFont(amountEarnedLabel.getFont().deriveFont(Settings.FONT_SIZE));
        add(amountEarnedLabel, "wrap, gap 3");

        amountEarnedTextField = new JTextField(7);
        amountEarnedTextField.setFont(amountEarnedTextField.getFont().deriveFont(Settings.FONT_SIZE));
        final AbstractDocument amountEarnedDocument = (AbstractDocument) amountEarnedTextField.getDocument();
        amountEarnedDocument.setDocumentFilter(DecimalNumberDocFilter.from());
        add(amountEarnedTextField);

        connectFocusableComponents();
    }

    public JComponent getComponent() {
        return amountEarnedTextField;
    }

    public String getAmountEarnedText() {
        return amountEarnedTextField.getText();
    }

    public void setAmountEarnedText(final String newText) {
        amountEarnedTextField.setText(newText);
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return Collections.singletonList(Collections.singletonList(amountEarnedTextField));
    }
}
