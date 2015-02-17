package edu.gatech.cs2340.trydent.test.gui;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.junit.Test;

import edu.gatech.cs2340.trydent.gui.TTextField;

public class TTextFieldTest extends TJavaFXGUITest {

    @Test
    public void testTextFieldCreate() {
        String prompt = "Hello";
        TTextField textField = new TTextField(prompt);
        assertEquals(prompt, textField.getJavaFXElement().getPromptText());
    }

    @Test
    public void testTextFieldSet() {
        String prompt = "Hello";
        String textToSet = "Set";

        TTextField textField = new TTextField(prompt);
        textField.setText(textToSet);

        assertEquals(textToSet, textField.getText());
    }

    @Test
    public void testTextFieldReleased() {
        List<String> events = new LinkedList<>();
        String prompt = "Hello";
        String eventText = "Key Event";

        TTextField textField = new TTextField(prompt);

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

        TTextField textField = new TTextField(prompt);

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

        TTextField textField = new TTextField(prompt);

        textField.setKeyTypedHandler(event -> events.add(eventText));
        Event.fireEvent(textField.getJavaFXElement(), new KeyEvent(
                KeyEvent.KEY_TYPED, "a", "a", KeyCode.A, false, false, false, false
                ));
        assertEquals(1, events.size());
    }
}
