package edu.gatech.cs2340.trydent.math;

/**
 * Custom exception for operations on vectors not of the same length.
 * @author Garrett Malmquist
 */
public class VectorMismatchException extends RuntimeException {

    public VectorMismatchException(String message) {
        super(message);
    }
}
