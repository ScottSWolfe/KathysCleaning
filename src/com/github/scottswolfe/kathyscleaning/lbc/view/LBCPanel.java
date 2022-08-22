package com.github.scottswolfe.kathyscleaning.lbc.view;

import com.github.scottswolfe.kathyscleaning.general.controller.DecimalNumberDocFilter;
import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.lbc.controller.LBCListeners;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This is the panel where the user enters beginning and ending times for workers at LBC.
 */
public class LBCPanel extends JPanel {

    public final static int ROWS = 12;
    public final static int COLS = 6;

    private static final List<String> DAYS = LBCModel.DAYS.stream()
        .map(DayOfWeek::getName)
        .collect(Collectors.toList());

    /**
     * The controller for this panel.
     */
    LBCListeners controller;

    /**
     * The frame containing this panel.
     */
    MainFrame<LBCPanel, LBCModel> frame;

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
    TimeDocFilter[][] beginTDFs;

    /**
     * Two-dimensional array of text fields for ending times.
     */
    JTextField[][] endTimeTextfield;
    AbstractDocument[][] endDocs;
    TimeDocFilter[][] endTDFs;

    /**
     * Text fields for any additional input data.
     */
    JTextField[] otherTextfields;

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

    public LBCPanel(LBCListeners listeners) {

        controller = listeners;

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);

        // getting number of rows based on number of workers
        dayLabels = new JLabel[DAYS.size()];
        nameLabels = new JLabel[ROWS];
        beginTimeTextfield = new JTextField[ROWS][DAYS.size()];
        beginTDFs = new TimeDocFilter[ROWS][DAYS.size()];
        beginDocs = new AbstractDocument[ROWS][DAYS.size()];
        endTimeTextfield = new JTextField[ROWS][DAYS.size()];
        endTDFs = new TimeDocFilter[ROWS][DAYS.size()];
        endDocs = new AbstractDocument[ROWS][DAYS.size()];
        earnedTextfields = new JTextField[DAYS.size()];

        String layout_format;

        WorkerList workers = new WorkerList(WorkerList.COVENANT_WORKERS);
        //worker labels
        for(int i=0; i<ROWS; i++){

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

        add(earnedLabel, new String("cell "+ (0) + " " + (ROWS+1) + ", align right, gapy 10, wrap"));


        //time text fields
        int num_v_sep = 0;

        /*
         * TODO: WARNING: I screwed up the i and j here. i is columns and j is rows
         */

        for(int i = 0; i< DAYS.size(); i++){

            dayLabels[i] = new JLabel();
            dayLabels[i].setText( DAYS.get(i) );
            dayLabels[i].setFont( dayLabels[i].getFont().deriveFont( Settings.HEADER_FONT_SIZE ));
            dayLabels[i].setBackground( Settings.BACKGROUND_COLOR );


            layout_format = new String("cell "+ (i+1+num_v_sep) + " " + (0) + ", center, wrap");
            add( dayLabels[i], layout_format );

            for(int j=0; j<ROWS; j++) {

                beginTimeTextfield[j][i] = new JTextField();
                beginTimeTextfield[j][i].setColumns( 5 );
                beginTimeTextfield[j][i].setFont( beginTimeTextfield[j][i].getFont().deriveFont(Settings.FONT_SIZE) );
                beginTimeTextfield[j][i].setBackground( Settings.BACKGROUND_COLOR );
                beginDocs[j][i] = (AbstractDocument) beginTimeTextfield[j][i].getDocument();
                beginTDFs[j][i] = new TimeDocFilter( beginTimeTextfield[j][i] );
                beginDocs[j][i].setDocumentFilter( beginTDFs[j][i] );
                beginTimeTextfield[j][i].addKeyListener( new TimeKeyListener( beginTDFs[j][i] ) );


                endTimeTextfield[j][i] = new JTextField();
                endTimeTextfield[j][i].setColumns( 5 );
                endTimeTextfield[j][i].setFont( endTimeTextfield[j][i].getFont().deriveFont(Settings.FONT_SIZE) );
                endTimeTextfield[j][i].setBackground( Settings.BACKGROUND_COLOR );
                endDocs[j][i] = (AbstractDocument) endTimeTextfield[j][i].getDocument();
                endTDFs[j][i] = new TimeDocFilter( endTimeTextfield[j][i] );
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

            layout_format = new String("cell " + (i+1+num_v_sep) + " " + (ROWS+1) + ", center" );
            add(earnedTextfields[i], layout_format);

            try {
                FileInputStream inp = new FileInputStream(Settings.LBC_EARNED_SAVE_FILE);
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


            if ( i < DAYS.size() - 1) {
                add( new JSeparator(SwingConstants.VERTICAL), "cell " + (i+2+num_v_sep) + " " + 1 + ", span 1 " + (ROWS+1) + ", growy, gapx 10" );
                num_v_sep++;
            }

        }
        addFlexibleFocusListeners();

        JPanel panel = buttonPanel();
        layout_format = new String("cell 3 " + (ROWS+2) + ", span " + (4 + num_v_sep) + ", growx" );
        add(panel, layout_format);

    }



    // CONSTRUCTION METHODS

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
        for (int i=0; i<ROWS; i++) {
            for (int j = 0; j< DAYS.size(); j++) {

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
                if ( i<ROWS-1 ) {
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
                if ( j< DAYS.size()-1 ) {
                    right_end = beginTimeTextfield[i][j+1];
                }
                else {

                }

                // enter components
                enter_begin = endTimeTextfield[i][j];
                if ( i<ROWS-1 ) {
                    enter_end = beginTimeTextfield[i+1][j];
                }
                else {
                    enter_end = earnedTextfields[j];
                }


                beginTimeTextfield[i][j].addFocusListener( new FlexibleFocusListener(
                        beginTimeTextfield[i][j],
                        FlexibleFocusListener.TEXTFIELD,
                        left_begin, right_begin,
                        up_begin, down_begin,
                        enter_begin ) );

                endTimeTextfield[i][j].addFocusListener( new FlexibleFocusListener(
                        endTimeTextfield[i][j],
                        FlexibleFocusListener.TEXTFIELD,
                        left_end, right_end,
                        up_end, down_end,
                        enter_end ) );

            }
        }


        // adding amount earned focus listeners
        for (int i = 0; i< DAYS.size(); i++) {

            Component up = beginTimeTextfield[ROWS-1][i];
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
            if ( i< DAYS.size()-1 ) {
                right = earnedTextfields[i+1];
            }
            else{
                right = null;
            }
            if ( i< DAYS.size()-1 ) {
                enter = beginTimeTextfield[0][i+1];
            }
            else {
                enter = null;
            }

            earnedTextfields[i].addFocusListener( new FlexibleFocusListener(
                    earnedTextfields[i],
                    FlexibleFocusListener.TEXTFIELD,
                    left, right,
                    up, down,
                    enter ) );

        }

    }





    private class TimeFocusListener implements FocusListener {

        // FIELDS
        JTextField tf;
        TempKeyListener tkl;

        public static final int BEGIN = 0;
        public static final int END = 1;


        // LISTENERS
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

        // FIELDS
        JTextField[][] beginTimeTextfield;
        JTextField[][] endTimeTextfield;
        int row;
        int column;
        int type;


        // LISTENERS
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
        public void keyReleased(KeyEvent arg0) {

        }

        @Override
        public void keyTyped(KeyEvent arg0) {

        }

    }



/* GETTERS/SETTERS ========================================================== */

    /**
     * @return the controller
     */
    public LBCListeners getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(LBCListeners controller) {
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
     * @param nameLabels the nameLabels to set
     */
    public void setNameLabels(JLabel[] nameLabels) {
        this.nameLabels = nameLabels;
    }



    /**
     * @return the beginTimeTextfield
     */
    public JTextField[][] getBeginTimeTextfield() {
        return beginTimeTextfield;
    }



    /**
     * @param beginTimeTextfield the beginTimeTextfield to set
     */
    public void setBeginTimeTextfield(JTextField[][] beginTimeTextfield) {
        this.beginTimeTextfield = beginTimeTextfield;
    }



    /**
     * @return the endTimeTextfield
     */
    public JTextField[][] getEndTimeTextfield() {
        return endTimeTextfield;
    }



    /**
     * @param endTimeTextfield the endTimeTextfield to set
     */
    public void setEndTimeTextfield(JTextField[][] endTimeTextfield) {
        this.endTimeTextfield = endTimeTextfield;
    }



    /**
     * @return the otherTextfields
     */
    public JTextField[] getOtherTextfields() {
        return otherTextfields;
    }



    /**
     * @param otherTextfields the otherTextfields to set
     */
    public void setOtherTextfields(JTextField[] otherTextfields) {
        this.otherTextfields = otherTextfields;
    }



    /**
     * @return the editButton
     */
    public JButton getEditButton() {
        return editButton;
    }



    /**
     * @param editButton the editButton to set
     */
    public void setEditButton(JButton editButton) {
        this.editButton = editButton;
    }



    /**
     * @return the submitButton
     */
    public JButton getSubmitButton() {
        return submitButton;
    }



    /**
     * @param submitButton the submitButton to set
     */
    public void setSubmitButton(JButton submitButton) {
        this.submitButton = submitButton;
    }



    /**
     * @return the earnedTextfields
     */
    public JTextField[] getEarnedTextfields() {
        return earnedTextfields;
    }



    /**
     * @param earnedTextfields the earnedTextfields to set
     */
    public void setEarnedTextfields(JTextField[] earnedTextfields) {
        this.earnedTextfields = earnedTextfields;
    }

    /**
     * Returns the frame that contains this panel
     * @return
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Sets the frame that contains this CovenantPanel
     * @return
     */
    public void setFrame(MainFrame<LBCPanel, LBCModel> frame) {
        this.frame = frame;
    }
}
