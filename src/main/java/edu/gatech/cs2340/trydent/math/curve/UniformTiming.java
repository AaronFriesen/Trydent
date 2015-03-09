package edu.gatech.cs2340.trydent.math.curve;

/**
 * TimingHandler which treats all spans in a spline as having the same duration.
 * (Advanced functionality).
 */
public class UniformTiming implements SplineTimingHandler {
    private int pointCount;

    /**
     * Creates a new uniform timing for the given number of points (or frames).
     *
     * @param pointCount
     *            the number of points (aka frames)
     */
    public UniformTiming(int pointCount) {
        this.pointCount = pointCount;
    }

    @Override
    public SpanTime transformTime(double t) {
        double spans = (pointCount - 1.0);
        int t0 = (int) (t * spans);
        int t1 = (int) ((t + 1) * spans);
        t = ((t * spans - t0) / (1.0 * t1 - t0)) * spans;
        return new SpanTime(t0, t);
    }
}
