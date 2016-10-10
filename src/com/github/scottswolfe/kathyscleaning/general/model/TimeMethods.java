package com.github.scottswolfe.kathyscleaning.general.model;

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

    
    
    
    
/* TIME CONVERSIONS ========================================================= */
    
    /**
     * Returns the number of hours worked between the given beginning time and
     * ending time.
     */
    public static double getHours(String time_begin, String time_end){
        
        double hours;

        int minutes = convertToMinutes(time_end) - convertToMinutes(time_begin);
        hours = convertToHours(minutes);
        
        return hours;
    }
    
    
    /**
     * Converts the given string into number of minutes.
     */
    public static int convertToMinutes(String time){
        
        char[] temp_ch = time.toCharArray();
        char[] ch = new char[ temp_ch.length - 1 ];
        int minutes;
        
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
        
        // TODO: **ERROR occurs for work times beginning before 10am and ending after 10am**
        // converting from hhmm to minutes
        if ( ch.length == 4) {
            minutes = (Character.getNumericValue(ch[0]) * 600 + Character.getNumericValue(ch[1]) * 60 + Character.getNumericValue(ch[2]) * 10 + Character.getNumericValue(ch[3]) );
        }
        // TODO: I would like to make this time calculation work more generally (eg if she started covenant before 9am or 
        //        finished after 9pm)
        else if ( ch.length == 3) {
            if (Character.getNumericValue(ch[0]) <= 9) { 
                minutes =  (Character.getNumericValue(ch[0]) + 12) * 60 + Character.getNumericValue(ch[1]) * 10 + Character.getNumericValue(ch[2]);
            }
            else {
                minutes =  ((Character.getNumericValue(ch[0]) - 7)%12 + 7) * 60 + Character.getNumericValue(ch[1]) * 10 + Character.getNumericValue(ch[2]);
            }
        }
        else {
            minutes = 0;  // TODO throw some type of error message here??
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
