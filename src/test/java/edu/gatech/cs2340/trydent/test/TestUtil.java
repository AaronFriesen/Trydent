package edu.gatech.cs2340.trydent.test;

import static org.junit.Assert.assertTrue;

/**
 * Utility class for unit tests, with methods that provide more verbose output
 * when test-cases fail.
 */
public class TestUtil {

    /**
     * Asserts that two strings are equal.
     *
     * @param expected
     * @param actualObj
     */
    public static void stringEquals(String expected, Object actualObj) {
        String actual = String.valueOf(actualObj);
        assertTrue("Expected string \"" + expected + "\", got \"" + actual + "\"", actual.equals(expected));
    }

    /**
     * Asserts that two objects are equal.
     *
     * @param expected
     * @param actual
     */
    public static void objectEquals(Object expected, Object actual) {
        assertTrue("Expected value \"" + expected + "\", got \"" + actual + "\"", actual.equals(expected));
    }

}
