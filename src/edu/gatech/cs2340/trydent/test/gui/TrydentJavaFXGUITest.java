package edu.gatech.cs2340.trydent.test.gui;

import javafx.application.Application;

import org.junit.BeforeClass;

public class TrydentJavaFXGUITest {
    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        new Thread() {
            public void run() {
                Application.launch(MinimalJavaFXApplication.class, new String[0]);
            }
        }.start();
    }
}
