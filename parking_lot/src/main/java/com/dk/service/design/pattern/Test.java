package com.dk.service.design.pattern;

public class Test {

	public static void main(String[] args) {
		Editor editor = new TextEditor();
		editor.edit();
		
		editor = new DocumentEditor();
		editor.edit();
		
		editor = new MailEditor();
		editor.edit();
	}
}
