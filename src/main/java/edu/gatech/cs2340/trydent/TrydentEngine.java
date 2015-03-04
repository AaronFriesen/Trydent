package edu.gatech.cs2340.trydent;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import edu.gatech.cs2340.trydent.internal.JavaFXManager;
import edu.gatech.cs2340.trydent.internal.SwingManager;
import edu.gatech.cs2340.trydent.log.Log;

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

    private final Object mainLock = new Object();

    private volatile boolean doQuit = false;

    private volatile long frameNumber;

    private Set<ContinuousEvent> continuousEvents, continuousEventsToAdd, continuousEventsToRemove;

    private JavaFXManager fxManager;

    private TrydentEngine() {
        continuousEvents = new HashSet<>();
        continuousEventsToAdd = new HashSet<>();
        continuousEventsToRemove = new HashSet<>();

        fxManager = new SwingManager();
    }

    private void mainUpdate() {
        if (frameNumber == 0)
            Time.startTheDawnOfTime();

        if (!doQuit) {
            try {
                updateContinuousEvents();
            } catch (Exception ex) {
                Log.error(String.valueOf(ex));
                ex.printStackTrace();
                quit();
            }
            Time.updateTime();
        } else {
            cleanup();
        }

        frameNumber++;
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

        fxManager.stopJavaFX();
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
    static void addContinuousEvent(ContinuousEvent event) {
        getInstance().continuousEventsToAdd.add(event);
    }

    /**
     * Stops running the given continuous event.
     *
     * @param event
     */
    static void removeContinuousEvent(ContinuousEvent event) {
        getInstance().continuousEventsToRemove.add(event);
    }

    static Group getRootNode() {
        return getInstance().fxManager.getBackground();
    }

    /**
     * Replaces the current foreground layout node
     *
     * @param resource
     *            the url of the new JavaFX node
     * @return the previous foreground JavaFX node, if none, returns null
     */
    public static Node setForeground(URL resource) {
        Node previous = null;
        try {
            FXMLLoader myLoader = new FXMLLoader(resource);
            Node node = myLoader.load();
            previous = getInstance().fxManager.setForeground(node);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return previous;
    }

    /**
     * Starts the TrydentEngine and JavaFX.
     */
    public static void start() {
        final TrydentEngine engine = getInstance();
        synchronized (engine.mainLock) {
            if (engine.fxManager.isRunning()) {
                Log.warn("Engine has already been started.");
                return; // Nothing to do.
            }
            engine.doQuit = false;
            engine.frameNumber = 0;
            engine.fxManager.setUpdateAction(new Runnable() {
                @Override
                public void run() {
                    engine.mainUpdate();
                }
            });
            // Could pass command-line args here maybe
            // JavaFXFacade.main(args);
            engine.fxManager.startJavaFX();
        }
    }

    public static void quit() {
        getInstance().doQuit = true;
    }

    /**
     * Hangs the current thread until the TrydentEngine stops running.
     *
     * @throws InterruptedException
     *             if this thread is interrupted while waiting
     */
    public static void waitUntilEngineStops() throws InterruptedException {
        // We don't have our own thread, because we rely on JavaFX
        // to invoke the engine's update method, so we have to busy-wait.
        while (getInstance().fxManager.isRunning()) {
            try {
                // Wait 100 ms and try again.
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }
        }
    }

    /**
     * Returns true if the TrydentEngine has been started, and hasn't been
     * stopped.
     *
     * @return whether the engine is running
     */
    public static boolean isRunning() {
        return getInstance().fxManager.isRunning();
    }

    /**
     * Tells the TrydentEngine to run the given runnable on the next frame.
     *
     * @param runnable
     *            action to run
     */
    public static void runLater(final Runnable runnable) {
        new ContinuousEvent() {
            @Override
            public void onStart() {
                runnable.run();
                this.stop();
            }

            @Override
            public void onUpdate() {
            }
        };
    }

    public static void setWindowTitle(String title) {
        getInstance().fxManager.setWindowTitle(title);
    }

    public static void setWindowSize(int width, int height) {
        getInstance().fxManager.setWindowSize(width, height);
    }

}
