package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/*
 * 	NOTE: In order for this document filter to work correctly,
 * 		   a TimeKeyListener should also be added to the chosen textfield.
 * 		   The key listener informs this filter of the last key pressed.
 */

public class TimeDocumentFilter extends DocumentFilter {

    // FIELDS
    JTextField txtfield;
    int lastkey;


    // CONSTRUCTOR
    public TimeDocumentFilter(JTextField txtfield ){
        this.txtfield = txtfield;
        lastkey = 0;
    }


    // FILTER METHODS

    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
              AttributeSet attr) throws BadLocationException {

        super.insertString(fb, offset, string, attr);

    }



    public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
              throws BadLocationException {

        // adjust length, remove ':', adjust offset, do the remove, insert the ':', and set the caret position

        char[] old_txt = txtfield.getText().toCharArray();
        int index = 0;

        // 1. Adjust Delete Length (if ':' is in portion to be deleted, subtract one from length)
        if(length>1){
            for(int i=offset; i<offset+length; i++){
                Character k = old_txt[i];
                if(!Character.isDigit(k)){
                    length--;
                    // for the singular case that the ':' is the first of multiple characters to be deleted
                    if(i==offset){
                        lastkey = 0; // in order for things to work correctly, need to make lastKeyPress NOT be backspace
                    }
                    break;
                }
            }
        }


        // remove the ':'
        char[] txt = new char[old_txt.length-1];
        int shift = 0;
        for(int i=0; i<old_txt.length; i++){
            Character k = old_txt[i];
            if(!Character.isDigit(k)){
                shift++;
                index = i;
            }
            else{
                txt[i-shift]=old_txt[i];
            }
        }


        // adjust the offset
        if (lastkey == KeyEvent.VK_BACK_SPACE) {
            if(offset>=index){
                offset--;
            }
        }
        else {
            if(offset>index){
                offset--;
            }
        }

        /*
        System.out.println("** After Offset Adjustment **");
        System.out.println("New Offset: "+offset);
        System.out.println();
        */

        // remove the character
        shift = 0;
        char[] new_text = new char[txt.length-length];
        for(int i=0; i<txt.length; i++){

            if(i>=offset && i<offset+length){
                shift++;
            }
            else{
                new_text[i-shift]=txt[i];  // TODO: some issue here with an array index being out of bounds when backspaceing ':'
            }
        }

        /*
        System.out.println("** After Removing Deleted Text **");
        System.out.println("New Text: "+new String(new_text));
        System.out.println();
        */

        //  insert a ':' where appropriate and set caret position
            int caretPosition = offset;
            char[] newer_text;
            if(new_text.length>0){
                newer_text = new char[new_text.length + 1];
            }
            else{
                newer_text = new char[0];
            }
            // if length is 1
            if (new_text.length == 1) {
                newer_text[0] = new_text[0];
                newer_text[1] = ':';
                if (caretPosition >= 1) {
                    caretPosition++;
                }
            }
            // if length is 2
            else if (new_text.length == 2) {
                newer_text[0] = new_text[0];
                newer_text[1] = ':';
                newer_text[2] = new_text[1];
                if (caretPosition >= 1 ) {
                    caretPosition++;
                }
            }
            // if length is 3
            else if (new_text.length == 3) {
                newer_text[0] = new_text[0];
                newer_text[1] = ':';
                newer_text[2] = new_text[1];
                newer_text[3] = new_text[2];
                if (caretPosition >= 1 ) {
                    caretPosition++;
                }
            }
            // if length is 4
            else if (new_text.length == 4) {
                newer_text[0] = new_text[0];
                newer_text[1] = new_text[1];
                newer_text[2] = ':';
                newer_text[3] = new_text[2];
                newer_text[4] = new_text[3];
                if (caretPosition >= 2) {
                    caretPosition++;
                }
            }
            String text = new String(newer_text);

            /*
            System.out.println("** After Inserting ':' ");
            System.out.println("Newer Text: "+text);
            System.out.println("Carrot Position: "+caretPosition);
            System.out.println();
            */

            fb.remove(0, txtfield.getText().length());

            /*
            System.out.println("** After Removing Text in Textfield **");
            System.out.println("Textfield: "+hp.time_begin_txt.getText());
            System.out.println();
            */

            if(newer_text.length == 0){

            }
            else{
                fb.insertString(0, text, null);
            }

            txtfield.setCaretPosition(caretPosition);


    }




    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
              AttributeSet attrs) throws BadLocationException {

        // TODO if first digit is not '1' or '2', don't allow more than 3 characters
        // TODO when removing (delete or backspace) and next char is ':' move caret past ':' (may have done this...)



        // STEPS
        //	1. remove ':' from old_txt
        //  2. filter and add in new txt up to 4 characters
        //  3. insert a ':' where appropriate
        //  4. fb.remove everything
        //  5. fb.insertString new text
        //  6. super.replace to put curosr in correct location



        char[] pre_old_txt = txtfield.getText().toCharArray();


        // adjusting delete length if ':' is in delete selection
        for(int i=offset; i<offset+length; i++){
            Character k = pre_old_txt[i];
            if(!Character.isDigit(k)){
                length--;
                // for the singular case that the ':' is the first of multiple characters to be deleted
                if(i==offset){
                    lastkey = 0; // in order for things to work correctly, need to make lastKeyPress NOT be backspace
                }
                break;
            }
        }



        // 1. removing ':' from old_txt

        char[] old_txt;
        if (pre_old_txt.length <= 0) {
            old_txt = new char[0];
        }
        else {
            int count = 0;
            int index = 0;

            old_txt = new char[pre_old_txt.length - 1];

            for (int i=0; i<pre_old_txt.length; i++) {
                Character k = new Character( pre_old_txt[i] );
                if(!k.equals(':')){
                    old_txt[i - count] = pre_old_txt[i];

                }
                else{
                    count++;
                    index = i;
                }
            }
            if(offset > index){
                offset--;
            }

        }




        // 2. Filter new text and cut characters if too long; then put old and new text together
        //       a) delete non-digits, b) reducing entry, c) old and new text combined
        char[] ch_array = text.toCharArray();
        char[] new_ch_array = new char[ch_array.length];


        // a) deleting any character in entry that is not a digit
        int skipped = 0;
        for(int i=0; i<ch_array.length; i++){
            Character ch = ch_array[i];
            if(Character.isDigit(ch)){
                new_ch_array[i-skipped]=ch.charValue();
            }
            else{
                skipped++;
            }
        }
        char[] final_ch_array = new char[ch_array.length - skipped];
        for(int i=0; i<ch_array.length - skipped ; i++){
            final_ch_array[i] = new_ch_array[i];
        }



        // b and c
        char[] shortened_ch_array;
        char[] new_text;
        int caretPosition;

        // if length of characters to replace are more than 0
        if (length > 0) {

            // b) reducing the entry to make sure the text is 4 characters or less
            if (final_ch_array.length + old_txt.length - length > 4 ){
                shortened_ch_array = new char[4 - (old_txt.length - length)];
                if(shortened_ch_array.length <= 0){

                }
                else{
                    for(int i=0; i < 4 - (old_txt.length - length); i++){
                        shortened_ch_array[i]=final_ch_array[i];
                    }
                }
                final_ch_array = shortened_ch_array;
            }

            // for length of old text, keep what is not being replaced
            char[] keep_txt = new char[old_txt.length - length];
            int shift = 0;
            for (int i=0; i<old_txt.length; i++) {
                //Character k = old_txt[i];
                if(i >= offset && i < offset + length){
                    shift++;
                }
                else{
                    keep_txt[i-shift] = old_txt[i];
                }
            }


            // c) putting the old text and new entry together into one new char array
            new_text = new char[keep_txt.length + final_ch_array.length];
            shift = 0;
            caretPosition = offset;

            // if length of replaced characters is 0
            if (keep_txt.length <= 0) {
                for(int j=0; j<final_ch_array.length; j++){
                    new_text[j] = final_ch_array[j];
                }
                caretPosition = final_ch_array.length;
            }
            else {
                for(int i=0; i<keep_txt.length + 1; i++){
                    if (i == offset){
                        for(int j=0; j<final_ch_array.length; j++){
                            new_text[i+j] = final_ch_array[j];
                            shift++;
                        }
                        caretPosition = i+final_ch_array.length;
                    }

                    if( i+shift < keep_txt.length + final_ch_array.length){
                        new_text[i+shift]=keep_txt[i];
                    }
                }
            }
        }

        // if length of characters to replace is more than 0
        else {
            // b) reducing the entry to make sure the text is 4 characters or less
            if (final_ch_array.length + old_txt.length >4 ){
                shortened_ch_array = new char[4 - old_txt.length];
                if(shortened_ch_array.length <= 0){

                }
                else{
                    for(int i=0; i < 4 - old_txt.length; i++){
                        shortened_ch_array[i]=final_ch_array[i];
                    }
                }
                final_ch_array = shortened_ch_array;
            }

            // c) putting the old text and new entry together into one new char array
            new_text = new char[old_txt.length + final_ch_array.length];
            int shift = 0;
            caretPosition = offset;
            for(int i=0; i<old_txt.length + final_ch_array.length; i++){
                if (i == offset){
                    for(int j=0; j<final_ch_array.length; j++){
                        new_text[i+j] = final_ch_array[j];
                        shift++;
                    }
                    caretPosition = i+final_ch_array.length;
                }

                if(old_txt.length > 0 && i+shift<old_txt.length + final_ch_array.length){
                    new_text[i+shift]=old_txt[i];  // error here when offset is 0
                }
            }
        } // end if-else



        //  3. insert a ':' where appropriate and set caret position

        char[] newer_text;
        if (new_text.length <= 0) {
            newer_text = new char[0];
        }
        else {
            newer_text = new char[new_text.length + 1];
        }
        // if length is 1
        if (new_text.length == 1) {
            newer_text[0] = new_text[0];
            newer_text[1] = ':';
            if (caretPosition >= 1) {
                caretPosition++;
            }
        }
        // if length is 2
        else if (new_text.length == 2) {
            newer_text[0] = new_text[0];
            newer_text[1] = ':';
            newer_text[2] = new_text[1];
            if (caretPosition >= 1) {
                caretPosition++;
            }
        }
        // if length is 3
        else if (new_text.length == 3) {
            newer_text[0] = new_text[0];
            newer_text[1] = ':';
            newer_text[2] = new_text[1];
            newer_text[3] = new_text[2];
            if (caretPosition >= 1) {
                caretPosition++;
            }
        }
        // if length is 4
        else if (new_text.length == 4) {
            newer_text[0] = new_text[0];
            newer_text[1] = new_text[1];
            newer_text[2] = ':';
            newer_text[3] = new_text[2];
            newer_text[4] = new_text[3];
            if (caretPosition >= 2) {
                caretPosition++;
            }
        }
        text = new String(newer_text);


        //  4. use filter bypass to remove all old text
        fb.remove(0, txtfield.getText().length());



        //  5. use filter bypass to enter edited new text
        fb.insertString(0, text, attrs);



        //  6. set caret position
        txtfield.setCaretPosition(caretPosition);

    }





}
