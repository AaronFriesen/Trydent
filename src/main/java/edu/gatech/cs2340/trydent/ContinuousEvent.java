package edu.gatech.cs2340.trydent;

/**
 * Represents an event that is called by the engine every frame.
 *
 * @author Garrett Malmquist
 *
 */
public abstract class ContinuousEvent {

    private boolean started = false;

    public ContinuousEvent() {
        TrydentEngine.addContinuousEvent(this);
    }

    /**
     * Called by the engine on update.
     */
    final void doUpdate() {
        if (!started) {
            onStart();
            started = true;
        }
        onUpdate();
    }

    /**
     * Called by the engine on stop.
     */
    final void doStop() {
        if (started) {
            onStop();
            started = false;
        }
    }

    /**
     * Tells the TrydentEngine to stop running this event.
     */
    public final void stop() {
        TrydentEngine.removeContinuousEvent(this);
    }

    /**
     * Called when this event is run for the first time.
     */
    public void onStart() {
    }

    /**
     * Called by the engine every frame.
     */
    public abstract void onUpdate();

    /**
     * Called when this event stops.
     */
    public void onStop() {
    }

}
