package com.github.scottswolfe.kathyscleaning.general.view;

import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.function.Consumer;

public class TemporaryPanelLauncher {

    public static void launchPanel(
        final JPanel panel,
        final Consumer<JFrame> getCreatedFrame,
        final FrameCloseListener parentFrameCloseListener
    ) {
        final JFrame temporaryFrame = createFrame(parentFrameCloseListener);
        getCreatedFrame.accept(temporaryFrame);
        temporaryFrame.add(panel);
        temporaryFrame.pack();
        StaticMethods.findSetLocation(temporaryFrame);
        temporaryFrame.setVisible(true);
    }

    private TemporaryPanelLauncher() {}

    private static JFrame createFrame(final FrameCloseListener frameCloseListener) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(frameCloseListener);
        return frame;
    }
}
