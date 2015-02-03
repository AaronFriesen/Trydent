package edu.gatech.cs2340.trydent.test.gui;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.junit.Test;

import edu.gatech.cs2340.trydent.gui.TrydentTextField;

public class TrydentTextFieldTest extends TrydentJavaFXGUITest {

    @Test
    public void testTextFieldCreate() {
        String prompt = "Hello";
        TrydentTextField textField = new TrydentTextField(prompt);
        assertEquals(prompt, textField.getJavaFXElement().getPromptText());
    }

    @Test
    public void testTextFieldSet() {
        String prompt = "Hello";
        String textToSet = "Set";
        
        TrydentTextField textField = new TrydentTextField(prompt);
        textField.setText(textToSet);
        
        assertEquals(textToSet, textField.getText());
    }

    @Test
    public void testTextFieldReleased() {
        List<String> events = new LinkedList<>();
        String prompt = "Hello";
        String eventText = "Key Event";
        
        TrydentTextField textField = new TrydentTextField(prompt);
        
        textField.setKeyReleasedHandler(event -> events.add(eventText));
        Event.fireEvent(textField.getJavaFXElement(), new KeyEvent(
                KeyEvent.KEY_RELEASED, "a", "a", KeyCode.A, false, false, false, false
        ));
        assertEquals(1, events.size());
    }

    @Test
    public void testTextFieldPressed() {
        List<String> events = new LinkedList<>();
        String prompt = "Hello";
        String eventText = "Key Event";
        
        TrydentTextField textField = new TrydentTextField(prompt);
        
        textField.setKeyPressedHandler(event -> events.add(eventText));
        Event.fireEvent(textField.getJavaFXElement(), new KeyEvent(
                KeyEvent.KEY_PRESSED, "a", "a", KeyCode.A, false, false, false, false
        ));
        assertEquals(1, events.size());
    }

    @Test
    public void testTextFieldTyped() {
        List<String> events = new LinkedList<>();
        String prompt = "Hello";
        String eventText = "Key Event";
        
        TrydentTextField textField = new TrydentTextField(prompt);
        
        textField.setKeyTypedHandler(event -> events.add(eventText));
        Event.fireEvent(textField.getJavaFXElement(), new KeyEvent(
                KeyEvent.KEY_TYPED, "a", "a", KeyCode.A, false, false, false, false
        ));
        assertEquals(1, events.size());
    }
}
