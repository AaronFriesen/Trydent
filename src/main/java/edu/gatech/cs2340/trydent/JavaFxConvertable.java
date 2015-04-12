package edu.gatech.cs2340.trydent;

import javafx.scene.Node;

/**
 * Represents an object which can create/be converted to a javafx node.
 *
 */
public interface JavaFxConvertable {

    /**
     * Creates a javafx node from the data in this class.
     * @return the new javafx node.
     */
    Node toJavaFxNode();

}
