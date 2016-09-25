package src.java.submit.controller;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


public class HousePayDocFilter extends DocumentFilter {

	
	public HousePayDocFilter() {
		
	}
	
	
	
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
		      AttributeSet attrs) throws BadLocationException {
		
		int count = 0;
		char[] text1 = text.toCharArray();
		char[] text2 = new char[ text1.length ];
		
		for (int i=0; i<text1.length; i++) {
			Character k = new Character( text1[i] );
			if(!Character.isDigit(k) && k!='.'){
				count++;
			}
			else{
				text2[i-count] = text1[i];
			}
		}
		
		char[] text3 = new char[ text1.length - count ];
		for(int i=0; i<text3.length; i++){
			text3[i] = text2[i];
		}
		
		text = new String(text3);
		
		if ( text.length() == 0) {
			length = 0;
		}
		
		super.replace(fb, offset, length, text, attrs);
		
		
		
		
	}
	

}
