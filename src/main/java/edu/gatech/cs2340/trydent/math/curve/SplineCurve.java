package edu.gatech.cs2340.trydent.math.curve;

import edu.gatech.cs2340.trydent.TrydentException;

/**
 * Represents a parametric spline of points. (Advanced functionality).
 *
 * @param <P>
 */
public class SplineCurve<P> extends Curve<P> {

    private ArrayPointStream<P> pointStream;
    private Interpolation<P> interpolation;
    private SplineTimingHandler timing;
    private TimeWrapMode timeWrap;

    /**
     * Creates a new spline from the input points where the input points are
     * spaced evenly apart in /time/, in which the duration is set to 1.0.
     *
     * @param interpolation
     *            interpolation strategy to use for constructing the spans of
     *            the spline.
     * @param indexWrap
     *            bounds-handling strategy to use for managing indices outside
     *            the range 0 to # points-1.
     * @param points
     *            array of points to interpolate, such that given points (P0,
     *            P1, P2, ..., Pn-1), the spline will pass through each point Pi
     *            at time i/(n-1).
     */
    public SplineCurve(Interpolation<P> interpolation, IndexWrapMode indexWrap, P[] points) {
        this.pointStream = new ArrayPointStream<P>(points, indexWrap);
        this.interpolation = interpolation;
        this.timing = new UniformTiming(points.length);
        this.timeWrap = TimeWrapMode.WRAP;
    }

    /**
     * Creates a new spline from the input points where the points are spaced
     * apart arbitrarily far apart in time by the durations given in the
     * durations array.
     *
     * @param interpolation
     *            interpolation strategy to use for constructing the spans of
     *            the spline.
     * @param bounds
     *            bounds-handling strategy to use for managing times outside the
     *            range 0.0 to 1.0.
     * @param points
     *            array of points to interpolate.
     * @param durations
     *            array of scalar durations, such that it takes duration[i] time
     *            to move from points[i] to points[i+1]. The durations are
     *            expected to sum to 1.0; if they do not, the array will be
     *            normalize to force the duration to be 1.0.
     */
    public SplineCurve(Interpolation<P> interpolation, IndexWrapMode bounds, P[] points, double[] durations) {
        double sum = 0;
        for (double d : durations) {
            sum += d;
        }
        if (sum != 1.0) {
            if (sum <= 0)
                throw new TrydentException("Durations must sum to a positive number.");
            double[] normalized = new double[durations.length];
            for (int i = 0; i < normalized.length; i++) {
                normalized[i] = durations[i] * 1.0 / sum;
            }
            durations = normalized;
        }

        this.pointStream = new ArrayPointStream<>(points, bounds);
        this.interpolation = interpolation;
        this.timing = new ArbitraryTiming(durations);
        this.timeWrap = TimeWrapMode.WRAP;
    }

    /**
     * Sets the interpolation strategy used by this spline.
     *
     * @param interpolation
     */
    public void setInterpolation(Interpolation<P> interpolation) {
        this.interpolation = interpolation;
    }

    /**
     * Sets the index wrapping used by this spline.
     *
     * @param handler
     */
    public void setIndexWrap(IndexWrapMode handler) {
        this.pointStream.setBoundsHandler(handler);
    }

    /**
     * Sets the time wrapping used by this spline.
     *
     * @param mode
     */
    public void setTimeWrap(TimeWrapMode mode) {
        this.timeWrap = mode;
    }

    @Override
    public P sample(double t) {
        // There is some subtle wrapping behavior occurring in this function.
        // Our first step is to wrap the floating-point time value, so that
        // our TimingHandler can correctly translate the time value to a span
        // and a sub-time of that span. The TimingHandler requires its input
        // to be between 0 and 1.
        t = this.timeWrap.handle(t, 1.0);

        SpanTime time = timing.transformTime(t);
        pointStream.seek(time.getIndex());

        // Next, the actual interpolation uses its IndexWrapMode to find the
        // adjacent keyframe points for use in interpolation. This is important
        // because an interpolation near the boundary of the spline may need
        // to get extra points before or after it that are outside the normal
        // bounds of the stream.
        // This behavior is handled by the pointStream, which wraps according to
        // its IndexWrapMode.
        return interpolation.interpolate(time.getTime(), pointStream);
    }

}
