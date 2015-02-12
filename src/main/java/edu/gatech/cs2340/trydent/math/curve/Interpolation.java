package edu.gatech.cs2340.trydent.math.curve;

import edu.gatech.cs2340.trydent.math.BaseVector;
import edu.gatech.cs2340.trydent.math.MathTools;

/**
 * Interpolation strategy interface.
 *
 * @author Garrett Malmquist
 *
 * @param <T>
 */
public interface Interpolation<T> {

    /**
     * Interpolates between the current and next points on the input stream by
     * an the interpolation parameter t.
     *
     * @param t
     *            - where t=0 returns points.current(), and t=1 returns
     *            points.next().
     * @param points
     *            - abstract polyline to interpolate along.
     * @return
     */
    T interpolate(double t, PointStream<T> points);

    /**
     * Performs a straight-line interpolation between points. (Also known as a
     * linear interpolation or "lerp".)
     */
    public static final Interpolation<BaseVector<?>> STRAIGHT = new Interpolation<BaseVector<?>>() {
        @Override
        public BaseVector<?> interpolate(double t, PointStream<BaseVector<?>> points) {
            return points.current().copy().lerp(t, points.next());
        }
    };

    /**
     * Performs a smooth (cubic B-spline) interpolation between points.
     */
    public static final Interpolation<BaseVector<?>> SMOOTH = new Interpolation<BaseVector<?>>() {
        @Override
        public BaseVector<?> interpolate(double t, PointStream<BaseVector<?>> points) {
            BaseVector<?> p0 = points.get(-1);
            BaseVector<?> p1 = points.get(0);
            BaseVector<?> p2 = points.get(1);
            BaseVector<?> p3 = points.get(2);
            return MathTools.hermite(p1, p2.copy().subtract(p0).scale(0.5), p2, p3.copy().subtract(p1).scale(0.5), t);
        }
    };

}