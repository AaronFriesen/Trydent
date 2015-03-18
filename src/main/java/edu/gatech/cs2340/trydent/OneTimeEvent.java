package edu.gatech.cs2340.trydent;


/**
 * Creates an event that starts, updates, and stops exactly once.
 *
 */
public abstract class OneTimeEvent {
    private ContinuousEvent event;

    public OneTimeEvent() {
        event = new ContinuousEvent(){
            @Override
            public void onStart(){
                OneTimeEvent.this.onStart();
            }

            @Override
            public void onUpdate() {
                OneTimeEvent.this.onUpdate();
                this.stop();
            }

            @Override
            public void onStop(){
                OneTimeEvent.this.onStop();
            }
        };
        TrydentEngine.addContinuousEvent(event);
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
     * An interface to allow easy creation of one time only events.
     * The exact syntax of this should be hidden by Java 8 lambdas.
     *
     */
    public interface OneTimeUpdateEvent {

        /**
         * The update function to use.
         */
        void onUpdate();
    }

    /**
     * Allows for an easy way to create one time events.
     * @param func the update function to call
     */
    public static void simplified(OneTimeUpdateEvent func) {
        new OneTimeEvent() {
            @Override
            public void onUpdate() {
                func.onUpdate();
            }
        };
    }

}
