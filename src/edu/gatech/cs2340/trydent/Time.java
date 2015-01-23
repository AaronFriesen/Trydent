package edu.gatech.cs2340.trydent;

/**
 * Static class to keep track of time.
 * 
 * All values returned by public methods are in seconds, unless stated
 * otherwise.
 * 
 * @author Garrett Malmquist
 */
public class Time {

    // There are 1000 milliseconds per second. This would change to
    // 1e9 if we were to use nano seconds instead.
    private static final double UNITS_PER_SECOND = 1000.0;
    
    // All private variables are stored in milliseconds for accuracy.
    private static volatile double gameTimeSinceStartup;
    private static volatile long realTimeSinceStartup;
    private static volatile long realLastFrame;
    private static volatile long realStartTime;
    private static volatile double gameTimePassed;
    private static volatile long realTimePassed;
    private static volatile double timeRate;

    /**
     * Game time in seconds since the game started.
     * 
     * @return
     */
    public static double getTime() {
        return gameTimeSinceStartup / UNITS_PER_SECOND;
    }

    /**
     * Game time in seconds  elapsed since the previous frame.
     * 
     * @return
     */
    public static double getTimePassed() {
        return gameTimePassed / UNITS_PER_SECOND;
    }

    /**
     * Real time in seconds  since the game started.
     * 
     * @return
     */
    public static double getRealTimeSinceStartup() {
        return realTimeSinceStartup / UNITS_PER_SECOND;
    }

    /**
     * Real time in seconds  elapsed since the previous frame.
     * 
     * @return
     */
    public static double getRealTimePassed() {
        return realTimePassed / UNITS_PER_SECOND;
    }

    /**
     * Rate of game time relative to real-time. I.e, a time rate of 0.5
     * indicates that the game is running at half-speed (in slow motion).
     * 
     * @return
     */
    public static double getTimeRate() {
        return timeRate;
    }

    /**
     * Sets the rate of game time relative to real-time. I.e, a time rate of 0.5
     * indicates that the game is running at half-speed (in slow motion).
     * 
     * @param rate
     */
    public static void setTimeRate(double rate) {
        timeRate = rate;
    }

    /**
     * Starts keeping track of time, called by the TrydentEngine.
     */
    static void startTheDawnOfTime() {
        realStartTime = System.currentTimeMillis();
        gameTimeSinceStartup = 0;
        realTimeSinceStartup = 0;
        timeRate = 1;

        realLastFrame = realStartTime;

        gameTimePassed = 0;
        realTimePassed = 0;
    }

    /**
     * Expected to be called by the engine at the start of every frame.
     */
    static void updateTime() {
        long now = System.currentTimeMillis();

        realTimePassed = now - realLastFrame;
        realTimeSinceStartup = now - realStartTime;
        gameTimePassed = timeRate * realTimePassed;

        gameTimeSinceStartup += gameTimePassed;

        realLastFrame = now;
    }
}
