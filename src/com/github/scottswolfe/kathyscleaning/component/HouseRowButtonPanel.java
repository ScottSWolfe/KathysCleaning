package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class HouseRowButtonPanel extends JPanel implements FocusableCollection {

    private final KcButton moveUpButton;
    private final KcButton moveDownButton;
    private final KcButton addHouseButton;
    private final KcButton deleteHouseButton;

    public static HouseRowButtonPanel from(
        final ActionListener onMoveUpButtonPress,
        final ActionListener onMoveDownButtonPress,
        final ActionListener onAddHouseButtonPress,
        final ActionListener onDeleteHouseButtonPress
    ) {
        return new HouseRowButtonPanel(
            onMoveUpButtonPress,
            onMoveDownButtonPress,
            onAddHouseButtonPress,
            onDeleteHouseButtonPress
        );
    }

    private HouseRowButtonPanel(
        final ActionListener onMoveUpButtonPress,
        final ActionListener onMoveDownButtonPress,
        final ActionListener onAddHouseButtonPress,
        final ActionListener onDeleteHouseButtonPress
    ) {
        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);

        moveUpButton = new KcButton("Up", onMoveUpButtonPress);
        moveDownButton = new KcButton("Down", onMoveDownButtonPress);
        addHouseButton = new KcButton("Add", onAddHouseButtonPress);
        deleteHouseButton = new KcButton("Delete", onDeleteHouseButtonPress);

        add(moveUpButton, "growx");
        add(addHouseButton,"wrap, growx");
        add(moveDownButton);
        add(deleteHouseButton);

        connectFocusableComponents();
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return Arrays.asList(
            Arrays.asList(moveUpButton, addHouseButton),
            Arrays.asList(moveDownButton, deleteHouseButton)
        );
    }
}
