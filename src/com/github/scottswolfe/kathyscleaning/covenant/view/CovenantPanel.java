package com.github.scottswolfe.kathyscleaning.covenant.view;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.general.controller.DecimalNumberDocFilter;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantListeners;
import com.github.scottswolfe.kathyscleaning.general.controller.KeyboardFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocumentFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;

/**
 * This is the panel where the user enters beginning and ending times for
 * workers at Covenant.
 */
public class CovenantPanel extends JPanel {

    /**
     * The controller for this panel.
     */
    CovenantListeners controller;

    public final static int ROWS = 14;
    int rows = ROWS;
    public final static int COLS = 5;

    public static String[] day = {"Monday","Tuesday","Wednesday","Thursday","Friday"};

    /**
     * Names of the workers listed vertically.
     */
    JLabel[] nameLabels;

    /**
     * Days of the week listed horizontally.
     */
    JLabel[] dayLabels;

    /**
     * Two-dimensional array of text fields for beginning times.
     */
    JTextField[][] beginTimeTextfield;
    AbstractDocument[][] beginDocs;
    TimeDocumentFilter[][] beginTDFs;

    /**
     * Two-dimensional array of text fields for ending times.
     */
    JTextField[][] endTimeTextfield;
    AbstractDocument[][] endDocs;
    TimeDocumentFilter[][] endTDFs;

    /**
     * Button to edit listed workers.
     */
    JButton editButton;

    /**
     * Button to submit entered data and continue program.
     */
    JButton submitButton;

    /**
     * Label for amount of money earned for each day.
     */
    JLabel earnedLabel;

    /**
     * Text fields to enter amount earned each day.
     */
    JTextField[] earnedTextfields;

    public CovenantPanel(CovenantListeners listeners) {

        controller = listeners;

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);

        // getting number of rows based on number of workers
        dayLabels = new JLabel[day.length];
        nameLabels = new JLabel[rows];
        beginTimeTextfield = new JTextField[rows][day.length];
        beginTDFs = new TimeDocumentFilter[rows][day.length];
        beginDocs = new AbstractDocument[rows][day.length];
        endTimeTextfield = new JTextField[rows][day.length];
        endTDFs = new TimeDocumentFilter[rows][day.length];
        endDocs = new AbstractDocument[rows][day.length];
        earnedTextfields = new JTextField[day.length];

        String layout_format;

        WorkerList workers = new WorkerList(SharedDataManager.getInstance().getCovenantWorkerNames());
        //worker labels
        for(int i=0; i<rows; i++){

            nameLabels[i] = new JLabel();


            if (i < workers.size()) {
                nameLabels[i].setText(workers.getName(i));
            }
            else {
                nameLabels[i].setText("");
            }
            nameLabels[i].setFont( nameLabels[i].getFont().deriveFont( Settings.HEADER_FONT_SIZE ));
            nameLabels[i].setBackground( Settings.BACKGROUND_COLOR );

            if ( i > 0 ) {
                layout_format = new String("cell "+ (0) + " " + (i+1) + ", align right, gapy 10, wrap" );
            }
            else {
                layout_format = new String("cell "+ (0) + " " + (i+1) + ", align right, gapy 1" );
            }

            add( nameLabels[i], layout_format);
        }

        earnedLabel = new JLabel();
        earnedLabel.setText("Earned: ");
        earnedLabel.setFont( earnedLabel.getFont().deriveFont(Settings.HEADER_FONT_SIZE) );

        add(earnedLabel, new String("cell "+ (0) + " " + (rows+1) + ", align right, gapy 10, wrap"));


        //time text fields
        int num_v_sep = 0;

        /*
         * TODO: WARNING: I screwed up the i and j here. i is columns and j is rows
         */

        for(int i=0; i<day.length; i++){

            dayLabels[i] = new JLabel();
            dayLabels[i].setText( day[i] );
            dayLabels[i].setFont( dayLabels[i].getFont().deriveFont( Settings.HEADER_FONT_SIZE ));
            dayLabels[i].setBackground( Settings.BACKGROUND_COLOR );


            layout_format = new String("cell "+ (i+1+num_v_sep) + " " + (0) + ", center, wrap");
            add( dayLabels[i], layout_format );

            for(int j=0; j<rows; j++) {

                beginTimeTextfield[j][i] = new JTextField();
                beginTimeTextfield[j][i].setColumns( 5 );
                beginTimeTextfield[j][i].setFont( beginTimeTextfield[j][i].getFont().deriveFont(Settings.FONT_SIZE) );
                beginTimeTextfield[j][i].setBackground( Settings.BACKGROUND_COLOR );
                beginDocs[j][i] = (AbstractDocument) beginTimeTextfield[j][i].getDocument();
                beginTDFs[j][i] = new TimeDocumentFilter( beginTimeTextfield[j][i] );
                beginDocs[j][i].setDocumentFilter( beginTDFs[j][i] );
                beginTimeTextfield[j][i].addKeyListener( new TimeKeyListener( beginTDFs[j][i] ) );


                endTimeTextfield[j][i] = new JTextField();
                endTimeTextfield[j][i].setColumns( 5 );
                endTimeTextfield[j][i].setFont( endTimeTextfield[j][i].getFont().deriveFont(Settings.FONT_SIZE) );
                endTimeTextfield[j][i].setBackground( Settings.BACKGROUND_COLOR );
                endDocs[j][i] = (AbstractDocument) endTimeTextfield[j][i].getDocument();
                endTDFs[j][i] = new TimeDocumentFilter( endTimeTextfield[j][i] );
                endDocs[j][i].setDocumentFilter( endTDFs[j][i] );
                endTimeTextfield[j][i].addKeyListener( new TimeKeyListener( endTDFs[j][i] ) );

                layout_format = new String("cell " + (i+1+num_v_sep) + " " + (j+1) + ", gapx 10, split 2" );
                add(beginTimeTextfield[j][i], layout_format);
                add(endTimeTextfield[j][i], layout_format);

            }

            earnedTextfields[i] = new JTextField();
            earnedTextfields[i].setFont( earnedTextfields[i].getFont().deriveFont(Settings.FONT_SIZE) );
            earnedTextfields[i].setColumns(7);
            AbstractDocument amount_earned_doc = (AbstractDocument) earnedTextfields[i].getDocument();
            amount_earned_doc.setDocumentFilter(DecimalNumberDocFilter.from());

            layout_format = new String("cell " + (i+1+num_v_sep) + " " + (rows+1) + ", center" );
            add(earnedTextfields[i], layout_format);

            try {
                FileInputStream inp = new FileInputStream(Settings.COVENANT_EARNED_SAVE_FILE);
                Scanner scanner = new Scanner(inp);

                String amount = "";

                for (int m=0; m<i+1; m++) {
                    amount = scanner.nextLine();
                }

                earnedTextfields[i].setText( amount );

                scanner.close();
                inp.close();
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }


            if ( i < day.length - 1) {
                add( new JSeparator(SwingConstants.VERTICAL), "cell " + (i+2+num_v_sep) + " " + 1 + ", span 1 " + (rows+1) + ", growy, gapx 10" );
                num_v_sep++;
            }

        }
        addFlexibleFocusListeners();

        JPanel panel = buttonPanel();
        layout_format = new String("cell 3 " + (rows+2) + ", span " + (4 + num_v_sep) + ", growx" );
        add(panel, layout_format);

    }

    private JPanel buttonPanel() {

        JPanel panel = new JPanel();
        panel.setLayout( new MigLayout("align right", "", "") );
        panel.setBackground( Settings.BACKGROUND_COLOR );

        editButton = new JButton();
        editButton.setText("Edit Workers");
        editButton.setFont( editButton.getFont().deriveFont(Settings.FONT_SIZE));
        editButton.setBackground(Settings.MAIN_COLOR);
        editButton.setForeground( Settings.FOREGROUND_COLOR );
        editButton.addActionListener(controller.new EditListener());

        submitButton = new JButton();
        submitButton.setText("Next");
        submitButton.setFont( submitButton.getFont().deriveFont(Settings.FONT_SIZE));
        submitButton.setBackground(Settings.MAIN_COLOR);
        submitButton.setForeground( Settings.FOREGROUND_COLOR );
        submitButton.addActionListener(controller.new SubmitListener());

        panel.add(editButton, "");
        panel.add(submitButton, "");

        return panel;
    }

    private void addFlexibleFocusListeners () {


        // adding time field focus listeners
        for (int i=0; i<rows; i++) {
            for (int j=0; j<day.length; j++) {

                Component up_begin = null;
                Component up_end = null;

                Component down_begin = null;
                Component down_end = null;

                Component right_begin = null;
                Component right_end = null;

                Component left_begin = null;
                Component left_end = null;

                Component enter_begin = null;
                Component enter_end = null;

                // up components
                if ( i>0 ) {
                    up_begin = beginTimeTextfield[i-1][j];
                    up_end = endTimeTextfield[i-1][j];
                }
                else {

                }

                // down components
                if ( i<rows-1 ) {
                    down_begin = beginTimeTextfield[i+1][j];
                    down_end = endTimeTextfield[i+1][j];
                }
                else {
                    down_begin = earnedTextfields[j];
                    down_end = earnedTextfields[j];
                }

                // left components
                left_end = beginTimeTextfield[i][j];
                if ( j>0 ) {
                    left_begin = endTimeTextfield[i][j-1];
                }
                else {

                }

                // right components
                right_begin = endTimeTextfield[i][j];
                if ( j<day.length-1 ) {
                    right_end = beginTimeTextfield[i][j+1];
                }
                else {

                }

                // enter components
                enter_begin = endTimeTextfield[i][j];
                if ( i<rows-1 ) {
                    enter_end = beginTimeTextfield[i+1][j];
                }
                else {
                    enter_end = earnedTextfields[j];
                }


                beginTimeTextfield[i][j].addFocusListener(KeyboardFocusListener.from(
                    beginTimeTextfield[i][j],
                    left_begin,
                    right_begin,
                    up_begin,
                    down_begin,
                    enter_begin
                ));

                endTimeTextfield[i][j].addFocusListener(KeyboardFocusListener.from(
                    endTimeTextfield[i][j],
                    left_end,
                    right_end,
                    up_end,
                    down_end,
                    enter_end
                ));

            }
        }


        // adding amount earned focus listeners
        for (int i=0; i<day.length; i++) {

            Component up = beginTimeTextfield[rows-1][i];
            Component down = null;
            Component left = null;
            Component right = null;
            Component enter = null;

            if ( i>0 ) {
                left = earnedTextfields[i-1];
            }
            else {
                left = null;
            }
            if ( i<day.length-1 ) {
                right = earnedTextfields[i+1];
            }
            else{
                right = null;
            }
            if ( i<day.length-1 ) {
                enter = beginTimeTextfield[0][i+1];
            }
            else {
                enter = null;
            }

            earnedTextfields[i].addFocusListener(KeyboardFocusListener.from(
                earnedTextfields[i],
                left,
                right,
                up,
                down,
                enter
            ));
        }
    }

    private class TimeFocusListener implements FocusListener {

        JTextField tf;
        TempKeyListener tkl;

        public static final int BEGIN = 0;
        public static final int END = 1;

        @Override
        public void focusGained(FocusEvent e) {
            tf.addKeyListener( tkl );
        }

        @Override
        public void focusLost(FocusEvent e) {
            tf.removeKeyListener( tkl );
        }
    }

    private class TempKeyListener implements KeyListener {

        JTextField[][] beginTimeTextfield;
        JTextField[][] endTimeTextfield;
        int row;
        int column;
        int type;

        @Override
        public void keyPressed(KeyEvent arg0) {

            if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {

                // if end time field and there is a row below
                if (type == TimeFocusListener.END && row + 1 < beginTimeTextfield.length) {
                    beginTimeTextfield[row+1][column].requestFocus();
                }
                // if end time field and there is not a row below and  there is a column to the right
                else if (type == TimeFocusListener.END && row + 1 >= beginTimeTextfield.length &&
                         column + 1 < beginTimeTextfield[0].length) {
                    beginTimeTextfield[row][column+1].requestFocus();
                }
                // if begin time field
                else if (type == TimeFocusListener.BEGIN) {
                    endTimeTextfield[row][column].requestFocus();
                }
                else {
                    // do nothing
                }

            }

            else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {

                // if begin time field
                if (type == TimeFocusListener.BEGIN) {
                    endTimeTextfield[row][column].requestFocus();
                }
                // if end time field and there is a column to right
                else if (type == TimeFocusListener.END  &&  column + 1 < beginTimeTextfield[0].length) {
                    beginTimeTextfield[row][column+1].requestFocus();
                }

            }

            else if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {

                // if begin time field and column to left
                if (type == TimeFocusListener.BEGIN && column > 0) {
                    endTimeTextfield[row][column-1].requestFocus();
                }
                // if end time field
                else if (type == TimeFocusListener.END ) {
                    beginTimeTextfield[row][column].requestFocus();
                }

            }

            else if (arg0.getKeyCode() == KeyEvent.VK_UP) {

                // if begin time field and row above
                if (type == TimeFocusListener.BEGIN && row > 0) {
                    beginTimeTextfield[row-1][column].requestFocus();
                }
                // if end time field and there is a row above
                else if (type == TimeFocusListener.END  &&  row > 0) {
                    endTimeTextfield[row-1][column].requestFocus();
                }

            }

            else if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {

                // if begin time field and row above
                if (type == TimeFocusListener.BEGIN  &&  row + 1 < beginTimeTextfield.length) {
                    beginTimeTextfield[row+1][column].requestFocus();
                }
                // if end time field and there is a row above
                else if (type == TimeFocusListener.END  &&  row + 1 < beginTimeTextfield.length) {
                    endTimeTextfield[row+1][column].requestFocus();
                }

            }

        }

        @Override
        public void keyReleased(KeyEvent arg0) {}

        @Override
        public void keyTyped(KeyEvent arg0) {}
    }

    /**
     * @return the controller
     */
    public CovenantListeners getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(CovenantListeners controller) {
        this.controller = controller;
    }

    /**
     * @return the nameLabels
     */
    public JLabel[] getNameLabels() {
        return nameLabels;
    }

    public List<String> getWorkerNames() {
        return Arrays.asList(nameLabels).stream()
            .map(JLabel::getText)
            .collect(Collectors.toList());
    }

    /**
     * @return the beginTimeTextfield
     */
    public JTextField[][] getBeginTimeTextfield() {
        return beginTimeTextfield;
    }

    /**
     * @return the endTimeTextfield
     */
    public JTextField[][] getEndTimeTextfield() {
        return endTimeTextfield;
    }

    /**
     * @return the earnedTextfields
     */
    public JTextField[] getEarnedTextfields() {
        return earnedTextfields;
    }
}
