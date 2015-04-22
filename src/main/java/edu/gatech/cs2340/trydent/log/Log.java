package edu.gatech.cs2340.trydent.log;

/**
 * A basic class for logging to standard output.
 */
public class Log {

    public static final String TYPE_DEBUG = "Debug";
    public static final String TYPE_WARNING = "Warning";
    public static final String TYPE_ERROR = "Error";

    /**
     * Logs a message with a tag.
     * @param logType the tag associated with the log
     * @param message the message to log
     */
    public static void log(String logType, String message) {
        System.out.printf("[%s] %s\n", logType, message);
    }

    /**
     * Logs a message with a debug tag.
     * @param message the message to log
     */
    public static void debug(String message) {
        log(TYPE_DEBUG, message);
    }

    /**
     * Logs a message with a warning tag.
     * @param message the message to log
     */
    public static void warn(String message) {
        log(TYPE_WARNING, message);
    }

    /**
     * Logs a message with an error tag.
     * @param message the message to log
     */
    public static void error(String message) {
        log(TYPE_ERROR, message);
    }

}
