package edu.gatech.cs2340.trydent.test.gui;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;

import org.junit.Test;

import edu.gatech.cs2340.trydent.gui.TRadioGroup;

public class TRadioGroupTest extends TJavaFXGUITest {

    @Test
    public void testRadioGroupCreate() {
        List<String> choices = new LinkedList<>();
        choices.add("A");
        choices.add("B");
        choices.add("C");
        TRadioGroup group = new TRadioGroup(choices);
        assertEquals(choices.size(), group.getJavaFXElement().getToggles().size());
    }

    @Test
    public void testRadioGroupSelect() {
        List<String> choices = new LinkedList<>();
        choices.add("A");
        choices.add("B");
        choices.add("C");
        TRadioGroup group = new TRadioGroup(choices);
        assertEquals(-1, group.getSelectedIndex());
        group.setSelectedIndex(2);
        assertEquals(2, group.getSelectedIndex());
    }

    @Test
    public void testRadioGroupSelected() {
        List<String> events = new LinkedList<>();
        String eventText = "Selection Changed";
        List<String> choices = new LinkedList<>();
        choices.add("A");
        choices.add("B");
        choices.add("C");
        TRadioGroup group = new TRadioGroup(choices);
        group.setSelectedListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> observable,
                    Toggle oldValue, Toggle newValue) {
                events.add(eventText);
            }
        });
        group.setSelectedIndex(2);
        assertEquals(1, events.size());
    }
}
