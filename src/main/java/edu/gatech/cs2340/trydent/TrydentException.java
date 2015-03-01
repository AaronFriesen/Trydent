package edu.gatech.cs2340.trydent;

/**
 * General TrydentEngine exception
 * @author Garrett Malmquist
 */
public class TrydentException extends RuntimeException {

    // Generated
    private static final long serialVersionUID = -5755614648691042543L;

    public TrydentException(String message) {
        super(message);
    }
    public TrydentException(String message, Exception nestedException) {
        super(message, nestedException);
    }

}
