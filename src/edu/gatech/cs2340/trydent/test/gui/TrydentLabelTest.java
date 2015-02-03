package edu.gatech.cs2340.trydent.test.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.gatech.cs2340.trydent.gui.TrydentLabel;

public class TrydentLabelTest extends TrydentJavaFXGUITest{
    
    @Test
    public void testLabelCreate() {
        String labelText = "Hello";
        TrydentLabel label = new TrydentLabel(labelText);
        assertEquals(labelText, label.getJavaFXElement().getText());
    }
}
