package edu.gatech.cs2340.trydent.test.gui;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.junit.Test;

import edu.gatech.cs2340.trydent.gui.TDropdown;

@SuppressWarnings("rawtypes")
public class TDropdownTest extends TJavaFXGUITest {

    @Test
    public void testDropdownCreate() {
        ObservableList items = FXCollections.observableArrayList("A", "B", "C");
        TDropdown dropdown = new TDropdown(items);
        assertEquals(items.size(), dropdown.getJavaFXElement().getItems().size());
    }

    @Test
    public void testDropdownpSelect() {
        ObservableList items = FXCollections.observableArrayList("A", "B", "C");
        TDropdown dropdown = new TDropdown(items);
        assertEquals(-1, dropdown.getSelectedIndex());
        dropdown.setSelectedIndex(2);
        assertEquals(2, dropdown.getSelectedIndex());
    }

    @Test
    public void testDropdownSelected() {
        List<String> events = new LinkedList<>();
        String eventText = "Selection Changed";

        ObservableList items = FXCollections.observableArrayList("A", "B", "C");
        TDropdown dropdown = new TDropdown(items);
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
