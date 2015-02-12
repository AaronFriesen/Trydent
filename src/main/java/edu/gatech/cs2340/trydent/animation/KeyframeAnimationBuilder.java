package edu.gatech.cs2340.trydent.animation;

import java.util.LinkedList;
import java.util.List;

import edu.gatech.cs2340.trydent.TrydentException;
import edu.gatech.cs2340.trydent.math.BaseVector;
import edu.gatech.cs2340.trydent.math.Orientation;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Scale;
import edu.gatech.cs2340.trydent.math.curve.IndexWrapMode;
import edu.gatech.cs2340.trydent.math.curve.Interpolation;
import edu.gatech.cs2340.trydent.math.curve.SplineCurve;

/**
 * Builder class used to construct KeyframeAnimations. This class should not be
 * instantiated directly; use KeyframeAnimation.create().
 */
public class KeyframeAnimationBuilder {
    private List<Keyframe> keyframes;
    private Keyframe buildingFrame;
    private double enforcedTotal;
    private boolean circular;

    KeyframeAnimationBuilder() {
        // LinkedList because we will be adding lots of keyframes.
        keyframes = new LinkedList<>();
        buildingFrame = new Keyframe();
        enforcedTotal = 0;
    }

    /**
     * Sets the position of the current keyframe.
     *
     * @param position
     * @return
     */
    public KeyframeAnimationBuilder setPosition(Position position) {
        buildingFrame.setPosition(position);
        return this;
    }

    /**
     * Shifts the current keyframe by the given translation.
     *
     * @param translation
     * @return
     */
    public KeyframeAnimationBuilder moveBy(Position translation) {
        buildingFrame.setPosition(buildingFrame.getPosition().add(translation));
        return this;
    }

    /**
     * Sets the rotation of the current keyframe.
     *
     * @param rotation
     *            - rotation in degrees.
     * @return
     */
    public KeyframeAnimationBuilder setRotation(double rotation) {
        buildingFrame.setRotation(rotation);
        return this;
    }

    /**
     * Rotates the current keyframe.
     *
     * @param rotation
     *            - amount to rotate in degrees.
     * @return
     */
    public KeyframeAnimationBuilder rotateBy(double rotation) {
        buildingFrame.setRotation(buildingFrame.getRotation() + rotation);
        return this;
    }

    /**
     * Sets the scale of the current keyframe, with (1,1) being no scale.
     *
     * @param scale
     * @return
     */
    public KeyframeAnimationBuilder setScale(Scale scale) {
        buildingFrame.setScale(scale);
        return this;
    }

    /**
     * Scales the current keyframe.
     *
     * @param scale
     * @return
     */
    public KeyframeAnimationBuilder scaleBy(Scale scale) {
        buildingFrame.setScale(buildingFrame.getScale().scale(scale));
        return this;
    }

    /**
     * Sets the position, rotation, and scale of the current keyframe.
     *
     * @param orientation
     * @return
     */
    public KeyframeAnimationBuilder setOrientation(Orientation orientation) {
        buildingFrame.setPosition(orientation.getPosition());
        buildingFrame.setRotation(orientation.getRotation());
        buildingFrame.setScale(orientation.getScale());
        return this;
    }

    /**
     * Sets the interpolation strategy used to generate in-between frames. This
     * defaults to Interpolation.SMOOTH and for most uses will not need to be
     * changed.
     *
     * @param interpolation
     * @return
     */
    public KeyframeAnimationBuilder setInterpolation(Interpolation<BaseVector<?>> interpolation) {
        buildingFrame.interpolation = interpolation;
        return this;
    }

    /**
     * Adds a new keyframe at the current position, rotation, and scale.
     *
     * @param duration
     *            - Time in seconds it should take for the animation to get from
     *            this keyframe to the one after it. For the final keyframe,
     *            this value is ignored, as the final keyframe has no duration.
     * @return
     */
    public KeyframeAnimationBuilder addKeyframe(double duration) {
        buildingFrame.duration = duration;
        keyframes.add(buildingFrame);
        Keyframe next = new Keyframe();
        next.interpolation = buildingFrame.interpolation;
        next.setPosition(buildingFrame.getPosition());
        next.setRotation(buildingFrame.getRotation());
        next.setScale(buildingFrame.getScale());
        buildingFrame = next;
        return this;
    }

    /**
     * Adds a new keyframe of zero duration at the current position, rotation,
     * and scale.
     * <p>
     * Unless this is the final keyframe in the animation, this call should be
     * followed by a call to setTotalDuration(duration), which will
     * automatically change the duration of this frame and other zero-duration
     * frames to appropriately fill in the remaining time.
     *
     * @return
     */
    public KeyframeAnimationBuilder addKeyframe() {
        return addKeyframe(0);
    }

    /**
     * Sets the duration for the /TOTAL/ animation, changing the duration of all
     * individual keyframes that have been set so far to accomplish this.
     *
     * If any keyframes have a negative or 0 duration, they will be used to fill
     * in the gaps in time, and the other frames will be left alone.
     *
     * @param duration
     * @return
     */
    public KeyframeAnimationBuilder setTotalDuration(double duration) {
        if (duration <= 0) {
            throw new TrydentException("The duration of the animation must be a positive number!");
        }
        enforcedTotal = duration;
        return this;
    }

    /**
     * If true, this animation will be constructed so that it loops back to the
     * beginning when it ends.
     *
     * @param circular
     * @return
     */
    public KeyframeAnimationBuilder setAnimationCircular(boolean circular) {
        this.circular = circular;
        return this;
    }

    /**
     * Builds and returns a KeyframeAnimation with the given keyframes.
     *
     * @return
     */
    public KeyframeAnimation build() {
        if (keyframes.isEmpty())
            throw new TrydentException("Cannot build a keyframe animation without any frames!"
                    + " Use addKeyframe(duration) to add a keyframe.");
        enforceTotal();
        double duration = 0;
        int index = 0;
        for (Keyframe frame : keyframes) {
            if (++index == keyframes.size())
                break;
            duration += frame.duration;
        }

        if (keyframes.size() > 1) {
            Keyframe last = keyframes.get(keyframes.size() - 1);
            if (circular) {
                last.duration = 1.0 / duration;
            } else {
                last.duration = 0;
            }
            duration += last.duration;
        }

        if (duration <= 0) {
            throw new TrydentException("Total duration of animation must be positive!");
        }

        KeyframeAnimation animation = new KeyframeAnimation();
        animation.duration = duration;

        // Piecewise interpolation.
        Interpolation<Keyframe> interpolation = new KeyframeInterpolation();
        IndexWrapMode bounds = circular ? IndexWrapMode.WRAP : IndexWrapMode.CLAMP;
        Keyframe[] points = keyframes.toArray(new Keyframe[keyframes.size()]);
        double[] durations = new double[keyframes.size()];
        for (int i = 0; i < points.length; i++) {
            points[i].duration /= animation.duration;
            durations[i] = points[i].duration;
        }
        animation.frameInterpolator = new SplineCurve<>(interpolation, bounds, points, durations);

        return animation;
    }

    private void enforceTotal() {
        if (enforcedTotal <= 0) {
            int index = 0;
            for (Keyframe frame : keyframes) {
                if (++index == keyframes.size())
                    break;
                if (frame.duration < 0) {
                    throw new TrydentException("Animation contains frames of negative duration,"
                            + " and never called setTotalDuration() with a positive number to fix it!");
                }
            }
            return;
        }
        if (keyframes.size() < 2) {
            keyframes.get(0).duration = enforcedTotal;
            return;
        }
        double total = 0;
        int timeless = 0;
        int index = 0;
        for (Keyframe frame : keyframes) {
            if (++index == keyframes.size())
                break;
            if (frame.duration > 0) {
                total += frame.duration;
            } else {
                timeless++;
            }
        }
        if (timeless == 0) {
            // Rescale all duration to match total time.
            for (Keyframe frame : keyframes) {
                frame.duration *= enforcedTotal / total;
            }
        } else {
            if (enforcedTotal > total) {
                // Fill in the gaps.
                index = 0;
                for (Keyframe frame : keyframes) {
                    if (++index == keyframes.size())
                        break;
                    if (frame.duration <= 0) {
                        frame.duration = (enforcedTotal - total) / timeless;
                    }
                }
            } else {
                // Rescale and fill in gaps reasonably.
                double gapAmount = enforcedTotal / (keyframes.size() - 1);
                double frameFactor = (enforcedTotal - gapAmount * timeless) / total;
                index = 0;
                for (Keyframe frame : keyframes) {
                    if (++index == keyframes.size())
                        break;
                    if (frame.duration <= 0) {
                        frame.duration = gapAmount;
                    } else {
                        frame.duration *= frameFactor;
                    }
                }
            }
        }
    }

}
