package edu.gatech.cs2340.trydent.animation;

/**
 * Listens to the animations on a GameObject.
 */
public interface AnimationListener {

    /**
     * Called when the animation ends and the GameObject stops playing it.
     *
     * @param event
     *            the animation event
     */
    void animationEnded(AnimationEvent event);

    /**
     * Called when the GameObject interrupts the current animation to play a new
     * one.
     *
     * @param event
     *            the animation event
     */
    void animationInterrupted(AnimationEvent event);

    /**
     * Called when the GameObject loops an animation.
     *
     * @param event
     *            the animation event
     */
    void animationLooped(AnimationEvent event);

    /**
     * Called when the GameObject pauses an animation.
     *
     * @param event
     *            the animation event
     */
    void animationPaused(AnimationEvent event);

    /**
     * Called when the GameObject starts playing an animation.
     *
     * @param event
     *            the animation event
     */
    void animationStarted(AnimationEvent event);

    /**
     * Called when the GameObject stops playing an animation before it actually
     * ends.
     *
     * @param event
     *            the animation event
     */
    void animationStopped(AnimationEvent event);

    /**
     * Called when the GameObject unpauses an animation.
     *
     * @param event
     *            the animation event
     */
    void animationUnpaused(AnimationEvent event);

}
