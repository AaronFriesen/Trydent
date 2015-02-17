package edu.gatech.cs2340.trydent.gui;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * A basic radio button group
 * @author sparky
 *
 */
public class TrydentRadioGroup extends TrydentGUIWrapper<ToggleGroup> {
    protected ToggleGroup toggles;
    protected List<String> choices;

    /**
     * Creates a radio button group with a set of choices
     * @param choices the text for the radio buttons
     */
    public TrydentRadioGroup(List<String> choices){
        toggles = new ToggleGroup();
        this.choices = choices;
        for(String choice : choices){
            RadioButton button = new RadioButton(choice);
            button.setToggleGroup(toggles);
        }
    }

    /**
     * Sets a listener for toggle events
     * @param listener the listener to process toggle events
     */
    public void setSelectedListener(ChangeListener<Toggle> listener){
        toggles.selectedToggleProperty().addListener(listener);
    }

    /**
     * Selects a radio button
     * @param index the index of the newly selected radio button
     */
    public void setSelectedIndex(int index){
        toggles.selectToggle(toggles.getToggles().get(index));
    }

    /**
     * Gets the index of the currently selected radio button
     * @return the index of the currently selected button, -1 for none
     */
    public int getSelectedIndex(){
        return toggles.getToggles().indexOf(toggles.getSelectedToggle());
    }

    /**
     * @see edu.gatech.cs2340.trydent.gui.TydentGUIWrapper#getJavaFXElement()
     */
    @Override
    public ToggleGroup getJavaFXElement() {
        return toggles;
    }

    /**
     * Gets the choices of the radio buttons
     * @return the initial list of choices for this group
     */
    public List<String> getChoices(){
        return choices;
    }
}
