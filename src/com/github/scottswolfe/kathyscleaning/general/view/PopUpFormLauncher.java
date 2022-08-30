package com.github.scottswolfe.kathyscleaning.general.view;

import com.github.scottswolfe.kathyscleaning.component.PopUpFormSubmissionPanel;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.WindowListener;

public class PopUpFormLauncher {

    private JFrame popUpFrame;

    public static PopUpFormLauncher from() {
        return new PopUpFormLauncher();
    }

    private PopUpFormLauncher() {}

    public void launchPanel(
        final JPanel formPanel,
        final Runnable onCancel,
        final Runnable onSubmit,
        final WindowListener popUpWindowListener
    ) {
        final PopUpFormSubmissionPanel popUpFormSubmissionPanel = PopUpFormSubmissionPanel.from(
            formPanel, () -> onCancelInternal(onCancel), () -> onSubmitInternal(onSubmit)
        );
        popUpFrame = createFrame(popUpFormSubmissionPanel, popUpWindowListener);
        launchFrame(popUpFrame);
    }

    private JFrame createFrame(
        final PopUpFormSubmissionPanel popUpFormSubmissionPanel,
        final WindowListener popUpWindowListener
    ) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        frame.add(popUpFormSubmissionPanel);
        frame.addWindowListener(popUpWindowListener);

        return frame;
    }

    private void launchFrame(final JFrame frame) {
        frame.pack();
        StaticMethods.findSetLocation(frame);
        frame.setVisible(true);
    }

    private void onCancelInternal(final Runnable onCancel) {
        try {
            onCancel.run();
        } catch (ButtonListenerException e) {
            return;
        }
        popUpFrame.setVisible(false);
        popUpFrame.dispose();
    }

    private <T> void onSubmitInternal(final Runnable onSubmit) {
        try {
            onSubmit.run();
        } catch (ButtonListenerException e) {
            return;
        }
        popUpFrame.setVisible(false);
        popUpFrame.dispose();
    }

    public static class ButtonListenerException extends RuntimeException {}
}
