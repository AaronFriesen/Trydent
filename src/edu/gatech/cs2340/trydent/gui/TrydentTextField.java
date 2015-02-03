package edu.gatech.cs2340.trydent.gui;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * A basic text field
 * @author sparky
 *
 */
public class TrydentTextField extends TrydentGUIWrapper<TextField> {
    protected TextField textField;
    
    /**
     * Creates a text field with a prompt
     * @param prompt the prompt for the text field
     */
    public TrydentTextField(String prompt){
        this.textField = new TextField();
        textField.setPromptText(prompt);
    }
    
    /**
     * Adds a handler for key press events 
     * @param handler the handler to process key press events
     */
    public void setKeyPressedHandler(EventHandler<KeyEvent> handler){
        textField.setOnKeyPressed(handler);
    }
    
    /**
     * Adds a handler for key release events 
     * @param handler the handler to process key release events
     */
    public void setKeyReleasedHandler(EventHandler<KeyEvent> handler){
        textField.setOnKeyReleased(handler);
    }
    
    /**
     * Adds a handler for key typed (press then release) events 
     * @param handler the handler to process key typed events
     */
    public void setKeyTypedHandler(EventHandler<KeyEvent> handler){
        textField.setOnKeyTyped(handler);
    }

    /**
     * Sets the text field to have specific text
     * @param text the text to set the text field to
     */
    public void setText(String text){
        textField.setText(text);
    }
    
    /**
     * Gets the text in the text field
     * @return the current text in the text field
     */
    public String getText(){
        return textField.getText();
    }

    /**
     * @see edu.gatech.cs2340.trydent.gui.TydentGUIWrapper#getJavaFXElement()
     */
    @Override
    public TextField getJavaFXElement() {
        return textField;
    }
}
