package com.github.scottswolfe.kathyscleaning.utility;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TimeMethods {

/* CONSTANTS ================================================================ */

    /**
     * ...
     */
    public static final int HOUSE_TIME = 0;
    public static final int COVENANT_TIME = 1;
    public static final int LBC_TIME = 2;





/* TIME CONVERSIONS ========================================================= */

    /**
     * Returns the number of hours worked between the given beginning time and
     * ending time.
     */
    public static double getHours(String time_begin, String time_end){
        int minutes = 0;
        int begin_minutes = convertToMinutes(time_begin);
        int end_minutes = convertToMinutes(time_end);
        if (end_minutes >= begin_minutes) {
            minutes = end_minutes - begin_minutes;
        }
        else {
            minutes = end_minutes + (720 - begin_minutes);
        }
        return convertToHours(minutes);
    }


    /**
     * Converts the given string into number of minutes past 12:00.
     */
    public static int convertToMinutes(String time) {

        // doctoring the input
        time = time.replace(":","");
        StringBuilder builder = new StringBuilder();
        builder.append(time);
        builder.reverse();
        time = builder.toString();

        int minutes = 0;

        int n = time.length();
        for (int i = 0; i < n; i++) {
            double digit_factor = 1;
            if (i == 1) {
                digit_factor = 10;
            }
            else if (i > 1) {
                digit_factor = Math.pow(10, i - 1) * 6;
            }
            minutes += Character.getNumericValue(time.charAt(i)) * digit_factor;
        }

        if (time.length() == 4 && time.charAt(3) == '1' && time.charAt(2) == '2') {
            minutes -= 720;
        }

        return minutes;
    }

    /**
     * Converts the given number of minutes into number of hours
     */
    public static double convertToHours(int minutes) {

        return ((double) minutes) / 60;

    }

    /**
     * Converts ...
     */
    public static String convertFormat (JTextField field, int type) {

        if ( field.getText() == null ) {
            return "00:00";
        }
        else if ( field.getText().isEmpty() ) {
            return "00:00";
        }
        else {


            int time_break = 7;
            if (type == HOUSE_TIME) {
                time_break = 7;
            }
            else if (type == COVENANT_TIME) {
                time_break = 9;
            }
            else if (type == LBC_TIME) {
                time_break = 5;
            }
            else {
                throw new RuntimeException("Unexpected time type: " + type);
            }

            char[] temp_ch = field.getText().toCharArray();
            char[] ch = new char[ temp_ch.length - 1 ];
            //int minutes;

            // remove the ':'
            int shift = 0;
            for(int i=0; i<temp_ch.length; i++){
                Character k = temp_ch[i];
                if(!Character.isDigit(k)){
                    shift++;
                }
                else{
                    ch[i-shift]=temp_ch[i];
                }
            }

            // converting from 12:00 to 24:00
            if ( ch.length == 4) {
                return field.getText();
            }


            else if ( ch.length == 3) {

                // if time comes before the time_break: add 12 hours
                if (Character.getNumericValue(ch[0]) < time_break) {

                    int time = Integer.parseInt( String.valueOf(ch) );
                    time = time + 1200;

                    String s1 = String.valueOf( time );

                    char[] c = new char[5];
                    c[0] = s1.charAt(0);
                    c[1] = s1.charAt(1);
                    c[2] = ':';
                    c[3] = s1.charAt(2);
                    c[4] = s1.charAt(3);

                    return String.valueOf(c);

                }
                // if time comes after the time break: do nothing
                else {
                    return field.getText();
                }
            }
            else {
                JOptionPane.showMessageDialog(new JFrame(), "Error: Time conversion error.", null, JOptionPane.ERROR_MESSAGE);
                return "00:00";
            }


        }

    }


    /**
     * Converts ...
     */
    public static String convertFormat (String s, int type) {

        if ( s == null ) {
            return "00:00";
        }
        else if ( s.isEmpty() ) {
            return "00:00";
        }
        else {

            int time_break = 7;
            if (type == HOUSE_TIME) {
                time_break = 7;
            }
            else if (type == COVENANT_TIME) {
                time_break = 9;
            }
            else if (type == LBC_TIME) {
                time_break = 5;
            }
            else {
                throw new RuntimeException("Unexpected time type: " + type);
            }

            char[] temp_ch = s.toCharArray();
            char[] ch = new char[ temp_ch.length - 1 ];
            //int minutes;

            // remove the ':'
            int shift = 0;
            for(int i=0; i<temp_ch.length; i++){
                Character k = temp_ch[i];
                if(!Character.isDigit(k)){
                    shift++;
                }
                else{
                    ch[i-shift]=temp_ch[i];
                }
            }

            // converting from 12:00 to 24:00
            if ( ch.length == 4) {
                return s;
            }


            else if ( ch.length == 3) {

                // if time comes before the time_break: add 12 hours
                if (Character.getNumericValue(ch[0]) < time_break) {

                    int time = Integer.parseInt( String.valueOf(ch) );
                    time = time + 1200;

                    String s1 = String.valueOf( time );

                    char[] c = new char[5];
                    c[0] = s1.charAt(0);
                    c[1] = s1.charAt(1);
                    c[2] = ':';
                    c[3] = s1.charAt(2);
                    c[4] = s1.charAt(3);

                    return String.valueOf(c);

                }
                // if time comes after the time break: do nothing
                else {
                    return s;
                }
            }
            else {
                JOptionPane.showMessageDialog(new JFrame(), "Error: Time conversion error.", null, JOptionPane.ERROR_MESSAGE);
                return "00:00";
            }

        }

    }

}
