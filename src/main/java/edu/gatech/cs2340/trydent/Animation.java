package edu.gatech.cs2340.trydent;

import edu.gatech.cs2340.trydent.math.Orientation;
import edu.gatech.cs2340.trydent.math.curve.IndexWrapMode;
import edu.gatech.cs2340.trydent.math.curve.TimeWrapMode;

/**
 * Interface representing an abstract animation, which defines the Orientation
 * of an object over time.
 *
 */
public interface Animation {

    /** Returns the duration in seconds. */
    public double getDuration();

    /**
     * Samples the Orientation of this animation at the given time between 0 and
     * duration.
     */
    public Orientation sample(double time);

    /**
     * Sets the strategy used to handle keyframe index values that are outside
     * of the range 0 ... # frames.
     *
     * @param mode
     */
    public void setIndexWrap(IndexWrapMode mode);

    /**
     * Sets the strategy used to handle time values that are outside the range 0
     * ... duration.
     *
     * @param mode
     */
    public void setTimeWrap(TimeWrapMode mode);

}
