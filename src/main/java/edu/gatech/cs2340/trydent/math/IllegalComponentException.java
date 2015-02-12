package edu.gatech.cs2340.trydent.math;

import edu.gatech.cs2340.trydent.TrydentException;

/**
 * Exception for illegal component access of multidimensional vectors.
 *
 * @author Garrett Malmquist
 */
public class IllegalComponentException extends TrydentException {

    // Generated
    private static final long serialVersionUID = -8075401594353367094L;

    public IllegalComponentException(int component) {
        super("Vector does not have component " + component + ".");
    }

}
