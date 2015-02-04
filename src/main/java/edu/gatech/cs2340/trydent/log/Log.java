package edu.gatech.cs2340.trydent.log;

public class Log {

    public static final String TYPE_DEBUG = "Debug";
    public static final String TYPE_WARNING = "Warning";
    public static final String TYPE_ERROR = "Error";
    
    public static void log(String logType, String message) {
        System.out.printf("[%s] %s\n", logType, message);
    }
    
    public static void debug(String message) {
        log(TYPE_DEBUG, message);
    }
    
    public static void warn(String message) {
        log(TYPE_WARNING, message);
    }
    
    public static void error(String message) {
        log(TYPE_ERROR, message);
    }
    
}
