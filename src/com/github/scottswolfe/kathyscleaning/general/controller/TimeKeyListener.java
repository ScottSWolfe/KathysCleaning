package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TimeKeyListener implements KeyListener {


//  FIELDS

    TimeDocumentFilter tdf;



//  CONSTRUCTOR

    public TimeKeyListener( TimeDocumentFilter tdf ){
        this.tdf = tdf;
    }


//  LISTENER

    public void keyPressed(KeyEvent pressed){
        tdf.lastkey = pressed.getKeyCode();
    }


    public void keyReleased(KeyEvent released){

    }


    public void keyTyped(KeyEvent typed){

    }

}
