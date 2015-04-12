package edu.gatech.cs2340.trydent;

/**
 * Represents an event that is called by the engine every frame.
 * <p>
 * ContinousEvents start being executed automatically by
 * {@link edu.gatech.cs2340.trydent.TrydentEngine} the frame after they are
 * created.
 * <p>
 * Users seeking to create ContinousEvents should subclass this class, and
 * override at least the {@link #onUpdate} method, which is called once per
 * frame.
 *
 * @author Garrett Malmquist
 *
 */
public abstract class ContinuousEvent {

    private boolean started = false;

    /**
     * Creates a new ContinuousEvent, and tells the engine to start running it
     * on the next frame.
     */
    public ContinuousEvent() {
        TrydentEngine.addContinuousEvent(this);
    }

    /**
     * <strong>Do not call this method! It is for internal use only.</strong>
     * <p>
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
     * <strong>Do not call this method! It is for internal use only.</strong>
     * <p>
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

    /**
     * Called right before onUpdate(). Subclasses overriding this method MUST
     * invoke the super-method, or an exception will be thrown.
     */
    public void onPreUpdate() {
        TrydentEngine.setSuperCalledFlag();
    }
}
