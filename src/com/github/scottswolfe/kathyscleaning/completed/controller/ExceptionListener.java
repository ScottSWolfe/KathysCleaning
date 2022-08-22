package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.view.ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

public class ExceptionListener implements ActionListener {

    WorkerList dwd;
    JFrame frame;
    JFrame local_frame;
    Supplier<ExceptionData> exceptionDataSupplier;
    Consumer<ExceptionData> exceptionDataConsumer;

    public ExceptionListener(
        WorkerList dwd,
        JFrame frame,
        Supplier<ExceptionData> exceptionDataSupplier,
        Consumer<ExceptionData> exceptionDataConsumer
    ) {
        this.dwd = dwd;
        this.frame = frame;
        this.exceptionDataSupplier = exceptionDataSupplier;
        this.exceptionDataConsumer = exceptionDataConsumer;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        local_frame = new JFrame();
        local_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        local_frame.addWindowListener( new FrameCloseListener(frame) );

        local_frame.add(new ExceptionPanel(local_frame, dwd, exceptionDataSupplier, exceptionDataConsumer));
        local_frame.pack();

        StaticMethods.findSetLocation(local_frame);

        frame.setEnabled(false);
        local_frame.setVisible(true);
    }
}
