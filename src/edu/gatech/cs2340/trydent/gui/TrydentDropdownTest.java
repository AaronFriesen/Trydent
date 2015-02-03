package edu.gatech.cs2340.trydent.gui;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.junit.Test;

import edu.gatech.cs2340.trydent.test.gui.TrydentJavaFXGUITest;

@SuppressWarnings("rawtypes")
public class TrydentDropdownTest extends TrydentJavaFXGUITest {

    @Test
    public void testDropdownCreate() {
        ObservableList items = FXCollections.observableArrayList("A", "B", "C");
        TrydentDropdown dropdown = new TrydentDropdown(items);
        assertEquals(items.size(), dropdown.getJavaFXElement().getItems().size());
    }

    @Test
    public void testDropdownpSelect() {
        ObservableList items = FXCollections.observableArrayList("A", "B", "C");
        TrydentDropdown dropdown = new TrydentDropdown(items);
        assertEquals(-1, dropdown.getSelectedIndex());
        dropdown.setSelectedIndex(2);
        assertEquals(2, dropdown.getSelectedIndex());
    }

    @Test
    public void testDropdownSelected() {
        List<String> events = new LinkedList<>();
        String eventText = "Selection Changed";

        ObservableList items = FXCollections.observableArrayList("A", "B", "C");
        TrydentDropdown dropdown = new TrydentDropdown(items);
        dropdown.setSelectedListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                events.add(eventText);
            }
        });
        dropdown.setSelectedIndex(2);
        assertEquals(1, events.size());
    }
}
