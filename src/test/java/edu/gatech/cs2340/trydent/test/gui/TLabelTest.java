package edu.gatech.cs2340.trydent.test.gui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.gatech.cs2340.trydent.gui.TLabel;

public class TLabelTest extends TJavaFXGUITest{

    @Test
    public void testLabelCreate() {
        String labelText = "Hello";
        TLabel label = new TLabel(labelText);
        assertEquals(labelText, label.getJavaFXElement().getText());
    }
}
