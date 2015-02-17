package edu.gatech.cs2340.trydent.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A basic label
 * @author sparky
 *
 */
public class TrydentLabel extends TrydentGUIWrapper<Label> {
    protected Label label;

    /**
     * Creates a label with an image and text
     * @param labelText the text for the label
     * @param imageUri the image for the label
     */
    public TrydentLabel(String labelText, String imageUri){
        if(imageUri == null || imageUri.equals("")){
            this.label = new Label(labelText);
        } else {
            this.label = new Label(labelText);
            // TODO: caching images based on uri
            Image image = new Image(imageUri, this.label.getWidth(), this.label.getHeight(), true, true);
            this.label.setGraphic(new ImageView(image));
        }
    }

    /**
     * Creates a label with text
     * @param labelText the text for the label
     */
    public TrydentLabel(String labelText){
        this(labelText, null);
    }

    /**
     * @see edu.gatech.cs2340.trydent.gui.TydentGUIWrapper#getJavaFXElement()
     */
    @Override
    public Label getJavaFXElement(){
        return label;
    }
}
