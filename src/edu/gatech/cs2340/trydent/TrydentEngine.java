package edu.gatech.cs2340.trydent;

import java.util.HashSet;
import java.util.Set;

/**
 * Main class that kicks off the TrydentEngine.
 * 
 * As a developer using the TrydentEngine, all you have to do is call
 * TrydentEngine.start() to get started.
 * 
 * @author Garrett Malmquist
 * */
public class TrydentEngine {

    private static TrydentEngine instance;

    private Thread mainThread;
    private final Object mainLock = new Object();

    private volatile boolean doQuit = false;

    private Set<ContinuousEvent> continuousEvents, continuousEventsToAdd,
            continuousEventsToRemove;

    private TrydentEngine() {
        continuousEvents = new HashSet<>();
        continuousEventsToAdd = new HashSet<>();
        continuousEventsToRemove = new HashSet<>();
    }

    private void mainLoop() {
        Time.startTheDawnOfTime();

        while (!doQuit) {
            updateContinuousEvents();
            Time.updateTime();
        }

        cleanup();
    }

    private void updateContinuousEvents() {
        continuousEvents.addAll(continuousEventsToAdd);
        continuousEventsToAdd.clear();

        // Removing events is uglier than creating events, because
        // we need to make sure we handle the case where users
        // remove additional events in an events onStop() method.
        while (!continuousEventsToRemove.isEmpty()) {
            ContinuousEvent next = null;
            for (ContinuousEvent event : continuousEventsToRemove) {
                next = event;
                break;
            }
            next.doStop();
            continuousEvents.remove(next);
            continuousEventsToRemove.remove(next);
        }

        for (ContinuousEvent event : continuousEvents) {
            event.doUpdate();
        }
    }

    private void cleanup() {
        // Stop any remaining events.
        ContinuousEvent[] toStop = new ContinuousEvent[continuousEvents.size()];
        continuousEvents.toArray(toStop);
        for (ContinuousEvent event : toStop) {
            event.doStop();
        }

        continuousEvents.clear();
        continuousEventsToRemove.clear();
        continuousEventsToAdd.clear();

        synchronized (mainLock) {
            // We're done here.
            mainThread = null;
        }
    }

    private static TrydentEngine getInstance() {
        if (instance == null)
            instance = new TrydentEngine();
        return instance;
    }

    /**
     * Adds a continuous event -- an event that the engine will invoke once
     * every frame.
     * 
     * @param event
     *            - A ContinuousEvent whose onUpdate() method will be invoked
     *            every frame.
     */
    protected static void addContinuousEvent(ContinuousEvent event) {
        getInstance().continuousEventsToAdd.add(event);
    }

    /**
     * Stops running the given continuous event.
     * 
     * @param event
     */
    protected static void removeContinuousEvent(ContinuousEvent event) {
        getInstance().continuousEventsToRemove.add(event);
    }

    public static void start() {
        final TrydentEngine engine = getInstance();
        synchronized (engine.mainLock) {
            if (engine.mainThread != null) {
                // Should move this to a formal logging system in the future.
                System.err.println("Warning: engine has already been started.");
                return; // Nothing to do.
            }
            engine.doQuit = false;
            engine.mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    engine.mainLoop();
                }
            });
            engine.mainThread.start();
        }
    }

    public static void quit() {
        getInstance().doQuit = true;
    }

    /**
     * Hangs the current thread until the TrydentEngine stops running.
     */
    public static void waitUntilEngineStops() throws InterruptedException {
        TrydentEngine engine = getInstance();
        Thread waitOn = null;
        synchronized (engine.mainLock) {
            if (engine.mainThread == null || !engine.mainThread.isAlive())
                return;
            if (Thread.currentThread().equals(engine.mainThread))
                return; // TODO: should probably throw an error.
            waitOn = engine.mainThread;
        }
        waitOn.join();
    }
}
