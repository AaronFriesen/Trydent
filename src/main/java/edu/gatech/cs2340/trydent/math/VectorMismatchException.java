package edu.gatech.cs2340.trydent.math;

import edu.gatech.cs2340.trydent.TrydentException;

/**
 * Custom exception for operations on vectors not of the same length.
 * @author Garrett Malmquist
 */
public class VectorMismatchException extends TrydentException {

    // Generated
    private static final long serialVersionUID = -5720560950265797400L;

    public VectorMismatchException(String message) {
        super(message);
    }
}
