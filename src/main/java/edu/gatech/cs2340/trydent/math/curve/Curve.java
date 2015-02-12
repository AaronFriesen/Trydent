package edu.gatech.cs2340.trydent.math.curve;

/**
 * Abstract class for sampling generic parametric curves. (Advanced
 * functionality).
 *
 * @param <T>
 */
public abstract class Curve<T> {
    /**
     * Samples a point along this curve.
     *
     * @param t
     *            curve parameter, where t=0 returns the first point, and t=1
     *            returns the last point.
     * @return
     */
    public abstract T sample(double t);

}
