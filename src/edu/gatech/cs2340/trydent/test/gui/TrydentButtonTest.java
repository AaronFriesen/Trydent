package edu.gatech.cs2340.trydent.test.gui;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.gatech.cs2340.trydent.gui.TrydentButton;

public class TrydentButtonTest extends TrydentJavaFXGUITest{

    @Test
    public void testButtonCreate() {
        String buttonText = "Hello";
        TrydentButton button = new TrydentButton(buttonText);
        assertEquals(buttonText, button.getJavaFXElement().getText());
    }

    @Test
    public void testButtonAction() {
        List<String> events = new LinkedList<>();
        
        String buttonText = "Hello";
        String eventText = "Button Fired";
        TrydentButton button = new TrydentButton(buttonText);
        
        button.setActionHandler(event -> events.add(eventText));
        button.fire();
        
        assertEquals(1, events.size());
    }
    
}
