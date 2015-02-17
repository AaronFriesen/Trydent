package edu.gatech.cs2340.trydent.test.gui;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.gatech.cs2340.trydent.gui.TButton;

public class TButtonTest extends TJavaFXGUITest{

    @Test
    public void testButtonCreate() {
        String buttonText = "Hello";
        TButton button = new TButton(buttonText);
        assertEquals(buttonText, button.getJavaFXElement().getText());
    }

    @Test
    public void testButtonAction() {
        List<String> events = new LinkedList<>();

        String buttonText = "Hello";
        String eventText = "Button Fired";
        TButton button = new TButton(buttonText);

        button.setActionHandler(event -> events.add(eventText));
        button.fire();

        assertEquals(1, events.size());
    }

}
