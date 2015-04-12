package edu.gatech.cs2340.trydent.animation;

import java.util.LinkedList;
import java.util.List;


/**
 * This class is a container for animation listeners. It effectively
 * behaves as a single animation listener that forwards all events
 * to its children.
 * <p>
 * This class does <i>not</i> check for cycles, to take care not to
 * create any circular dependencies, as they will cause stack overflows.
 */
public class DispatchAnimationListener implements AnimationListener {

    private List<AnimationListener> listeners;

    /**
     * Instantiates a new DispatchAnimationListener with no children.
     */
    public DispatchAnimationListener() {
        listeners = new LinkedList<>();
    }

    /**
     * Adds the animation listener.
     *
     * @param listener
     *            AnimationListener object to receive AnimationEvents.
     */
    public void addAnimationListener(AnimationListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the animation listener.
     *
     * @param listener
     *            AnimationListener to remove.
     */
    public void removeAnimationListener(AnimationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Removes all animation listeners.
     */
    public void clearAnimationListeners() {
        listeners.clear();
    }

    @Override
    public void animationEnded(AnimationEvent event) {
        for (AnimationListener l : listeners)
            l.animationEnded(event);
    }

    @Override
    public void animationInterrupted(AnimationEvent event) {
        for (AnimationListener l : listeners)
            l.animationInterrupted(event);
    }

    @Override
    public void animationLooped(AnimationEvent event) {
        for (AnimationListener l : listeners)
            l.animationLooped(event);
    }

    @Override
    public void animationPaused(AnimationEvent event) {
        for (AnimationListener l : listeners)
            l.animationPaused(event);
    }

    @Override
    public void animationStarted(AnimationEvent event) {
        for (AnimationListener l : listeners)
            l.animationStarted(event);
    }

    @Override
    public void animationStopped(AnimationEvent event) {
        for (AnimationListener l : listeners)
            l.animationStopped(event);
    }

    @Override
    public void animationUnpaused(AnimationEvent event) {
        for (AnimationListener l : listeners)
            l.animationUnpaused(event);
    }

}
