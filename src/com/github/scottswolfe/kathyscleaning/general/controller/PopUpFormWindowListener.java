package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class PopUpFormWindowListener implements WindowListener {

    public static PopUpFormWindowListener from() {
        return new PopUpFormWindowListener();
    }

	public PopUpFormWindowListener() {}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {
        ApplicationCoordinator.getInstance().unfreezeCurrentWindow();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {
        ApplicationCoordinator.getInstance().freezeCurrentWindow();
    }
}
