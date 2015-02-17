package edu.gatech.cs2340.trydent.gui;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

/**
 * A basic dropdown box
 * @author sparky
 *
 */
@SuppressWarnings("rawtypes")
public class TDropdown extends TGUIWrapper<ChoiceBox> {
    protected ChoiceBox box;
    protected ObservableList items;

    /**
     * Creates a dropdown box
     * @param items the items in the drop down
     */
    @SuppressWarnings("unchecked")
    public TDropdown(ObservableList items){
        this.box = new ChoiceBox(items);
        this.items = items;
    }

    /**
     * Sets the listener for selection change events
     * @param listener the listener for selection events
     */
    public void setSelectedListener(ChangeListener<? super Number> listener){
        box.getSelectionModel().selectedIndexProperty().addListener(listener);
    }

    /**
     * Gets the index of the currently selected item
     * @return the index of the currently selected item, -1 for none
     */
    public int getSelectedIndex(){
        return box.getItems().indexOf(box.getValue());
    }

    /**
     * Selects a item by index
     * @param index the index of the newly selected item
     */
    @SuppressWarnings("unchecked")
    public void setSelectedIndex(int index){
        box.setValue(box.getItems().get(index));
    }

    /**
     * @see edu.gatech.cs2340.trydent.gui.TydentGUIWrapper#getJavaFXElement()
     */
    @Override
    public ChoiceBox getJavaFXElement() {
        return box;
    }

    /**
     * Gets the items from the dropdown box
     * @return the list of items for the dropdown box
     */
    public ObservableList getItems(){
        return items;
    }
}
