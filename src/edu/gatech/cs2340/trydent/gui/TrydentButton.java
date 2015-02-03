package edu.gatech.cs2340.trydent.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * A basic button
 * @author sparky
 *
 */
public class TrydentButton extends TrydentGUIWrapper<Button> {
    protected Button button;

    /**
     * Creates a button with an image and text
     * @param buttonText the text for the button
     * @param imageUri the image for the button
     */
    public TrydentButton(String buttonText, String imageUri){
        if(imageUri == null || imageUri.equals("")){
            this.button = new Button(buttonText);
        } else {
            this.button = new Button(buttonText);
            // TODO: caching images based on uri
            Image image = new Image(imageUri, this.button.getWidth(), this.button.getHeight(), true, true);
            this.button.setGraphic(new ImageView(image));
        }
    }
    
    /**
     * Creates a button with text
     * @param buttonText the text for the button
     */
    public TrydentButton(String buttonText){
        this(buttonText, null);
    }
    
    /**
     * Adds a handler for button events
     * @param handler the handler to process button events
     */
    public void setActionHandler(EventHandler<ActionEvent> handler){
        button.setOnAction(handler);
    }

    /**
     * Triggers the event for the button
     */
    public void fire(){
        button.fire();
    }
    
    /**
     * @see edu.gatech.cs2340.trydent.gui.TydentGUIWrapper#getJavaFXElement()
     */
    @Override
    public Button getJavaFXElement() {
        return button;
    }

}
