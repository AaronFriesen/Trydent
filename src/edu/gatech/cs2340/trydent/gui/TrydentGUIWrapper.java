package edu.gatech.cs2340.trydent.gui;

/**
 * A basic wrapper for JavaFX elements, primarily for TrydentEngine initialization functionality
 * @author sparky
 *
 * @param <T> the type of the underlying JavaFx control element
 */
public abstract class TrydentGUIWrapper<T> {

    /**
     * This method exists to access JavaFX control methods not exposed by Trydent
     * @return the JavaFX control underlying this wrapper object
     */    
    public abstract T getJavaFXElement();
}
