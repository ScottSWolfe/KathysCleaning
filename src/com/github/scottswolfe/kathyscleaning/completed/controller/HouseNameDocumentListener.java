package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;

public class HouseNameDocumentListener implements DocumentListener {

    HousePanel hp;

    public final static File HOUSE_PAY_FILE = new File( System.getProperty("user.dir") + "\\save\\HousePay");

    public HouseNameDocumentListener(HousePanel hp) {
        this.hp = hp;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        checkHouseMatch();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkHouseMatch();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkHouseMatch();
    }

    private void checkHouseMatch() {

        try {
            Scanner input;
            String line;

            input = new Scanner(HOUSE_PAY_FILE);

            while(input.hasNextLine()) {
                line = input.nextLine();
                if (line.equalsIgnoreCase(hp.getHouseNameText())) {
                    hp.setAmountEarnedText(input.nextLine());
                    break;
                }
            }

            input.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
