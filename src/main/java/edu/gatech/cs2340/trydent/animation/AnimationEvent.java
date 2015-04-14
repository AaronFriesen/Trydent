package edu.gatech.cs2340.trydent.animation;

import edu.gatech.cs2340.trydent.GameObject;

/**
 * Stores information about an animation event.
 */
public class AnimationEvent {

    /** The GameObject associated with this event. */
    public final GameObject gameObject;

    /** The Animation associated with this event. */
    public final Animation animation;

    public AnimationEvent(GameObject gameObject, Animation animation) {
        this.gameObject = gameObject;
        this.animation = animation;
    }

}
