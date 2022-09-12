package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.model.ButtonColors;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.Collections;
import java.util.List;

public class ButtonPanel extends JPanel implements FocusableCollection {

    private final Button button;

    public static ButtonPanel from(final String label, final Runnable onPress) {
        return new ButtonPanel(label, onPress);
    }

    private ButtonPanel(final String label, final Runnable onPress) {

        button = Button.from(label, Settings.QUIET_BUTTON_COLORS, onPress);

        setLayout(new MigLayout("fill"));
        setBackground(Settings.BACKGROUND_COLOR);
        add(button);

        connectFocusableComponents();
    }

    public void setButtonColors(final ButtonColors colors) {
        button.setColors(colors);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(Collections.singletonList(button));
    }
}
