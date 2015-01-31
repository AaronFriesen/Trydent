package edu.gatech.cs2340.trydent.math;

/**
 * Exception for illegal component access of multidimensional
 * vectors.
 * @author Garrett Malmquist
 */
public class IllegalComponentException extends RuntimeException {

    public IllegalComponentException(int component) {
        super("Vector does not have component " + component + ".");
    }
    
}
