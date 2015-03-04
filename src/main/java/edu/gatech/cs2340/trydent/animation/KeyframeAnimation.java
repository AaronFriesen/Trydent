package edu.gatech.cs2340.trydent.animation;

import edu.gatech.cs2340.trydent.math.Orientation;
import edu.gatech.cs2340.trydent.math.curve.Curve;
import edu.gatech.cs2340.trydent.math.curve.IndexWrapMode;
import edu.gatech.cs2340.trydent.math.curve.SplineCurve;
import edu.gatech.cs2340.trydent.math.curve.TimeWrapMode;

/**
 * Represents a animation created by a sequence of "frames", where each frame
 * consists of a position, rotation, and scaling of the object this animation
 * can be applied to.
 *
 */
public class KeyframeAnimation extends Curve<Orientation> implements Animation {

    double duration = 0;
    SplineCurve<Keyframe> frameInterpolator;

    /**
     * Use the static 'create()' method to create new instances of the
     * KeyframeAnimation class.
     */
    KeyframeAnimation() {
    }

    /**
     * Returns a builder to create a new KeyframeAnimation.
     *
     * @return
     */
    public static KeyframeAnimationBuilder create() {
        return new KeyframeAnimationBuilder();
    }

    /**
     * Returns the duration of this animation in seconds.
     *
     * @return
     */
    @Override
    public double getDuration() {
        return duration;
    }

    /**
     * Returns the Orientation of this animation at the given moment in time.
     *
     * @param t
     *            The time, between 0 and the duration of this animation.
     */
    @Override
    public Orientation sample(double t) {
        return frameInterpolator.sample(t / duration).copy();
    }

    @Override
    public void setIndexWrap(IndexWrapMode mode) {
        this.frameInterpolator.setIndexWrap(mode);
    }

    @Override
    public void setTimeWrap(TimeWrapMode mode) {
        this.frameInterpolator.setTimeWrap(mode);
    }
}
