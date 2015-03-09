package edu.gatech.cs2340.trydent.math;

import java.lang.reflect.Array;

import javafx.scene.transform.Transform;

/**
 * Library of advanced math tools, primarily linear algebra.
 * <p>
 * To be used by advanced developers only.
 *
 * @author Garrett Malmquist
 */
public class MathTools {

    // We don't want people instantiating this class.
    private MathTools() {
    }

    /**
     * Takes an angle in degrees and wraps it to the range 0 - 360.
     *
     * @param theta
     *            angle in degrees to wrap if necessary
     * @return the wrapped angle in degrees (between 0 and 360)
     */
    public static double wrapAngle(double theta) {
        if (theta < 0)
            return 360.0 + (theta % 360.0);
        return theta % 360.0;
    }

    /**
     * Performs a linear interpolation from the first angle to the second at
     * time t, going either clockwise or counter-clockwise depending on which
     * distance is shorter.
     *
     * @param thetaA
     *            starting angle (at t=0)
     * @param thetaB
     *            ending angle (at t=1)
     * @param t
     *            interpolation parameter, where t=0 returns thetaA, and t=1
     *            returns thetaB.
     * @return the interpolated angle, wrapped to be between 0 and 360.
     */
    public static double degreeLerp(double thetaA, double thetaB, double t) {
        thetaA = wrapAngle(thetaA);
        thetaB = wrapAngle(thetaB);

        double small = thetaA;
        double large = thetaB;
        if (thetaA > thetaB) {
            small = thetaB;
            large = thetaA;
            t = 1.0 - t;
        }

        if (large - small > 180) {
            // Quicker to go over the 0 = 360 degree barrier.
            large -= 360;
        }
        return wrapAngle((1.0 - t) * small + t * large);
    }

    /**
     * Performs an (n-1) dimensional Bezier interpolation over n points.
     * (Advanced functionality).
     *
     * @param t
     *            interpolation parameter - t=0.0 will return the 1st point,
     *            t=1.0 will return the last point.
     * @param points
     *            One or more points to interpolate between.
     * @param <T>
     *            type of point
     * @return the interpolated point
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseVector<?>> T bezier(double t, T... points) {
        if (points.length == 0)
            throw new IllegalArgumentException("Must input at least one point!");

        if (points.length == 1)
            return (T) points[0].copy();

        // Strictly speaking this base case could be omitted without loss of
        // functionality,
        // but it makes things a bit more efficient.
        if (points.length == 2)
            return (T) points[0].copy().lerp(t, points[1]);

        T[] left = (T[]) Array.newInstance(points[0].getClass(), points.length - 1);
        T[] right = (T[]) Array.newInstance(points[0].getClass(), points.length - 1);

        for (int i = 0; i < points.length - 1; i++) {
            left[i] = points[i];
            right[i] = points[i + 1];
        }

        return (T) bezier(t, left).lerp(t, bezier(t, right));
    }

    /**
     * Modified lerp that passes through point 'start' at time t=a and point
     * 'end' at time t=b. (Advanced functionality).
     *
     * @param <T>
     *            the type of the point to interpolate (inferred from other
     *            arguments)
     * @param a
     *            starting time
     * @param start
     *            starting point
     * @param b
     *            ending time
     * @param end
     *            ending point
     * @param t
     *            interpolation parameter time
     * @return interpolated point at time t
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseVector<?>> T lerp(double a, T start, double b, T end, double t) {
        return (T) start.copy().lerp((t - a) / (b - a), end);
    }

    /**
     * Linear interpolation from start to end at time t. (Advanced
     * functionality).
     *
     * @param <T>
     *            the type of the point to interpolate (inferred from the other
     *            arguments)
     * @param start
     *            start point at t=0
     * @param end
     *            end point at t=1
     * @param t
     *            interpolation parameter
     * @return interpolated point
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseVector<?>> T lerp(T start, T end, double t) {
        return (T) start.copy().lerp(t, end);
    }

    /**
     * Scalar hermite interpolation. (Advanced functionality).
     * <p>
     * {@code P(t) = (2t^3-3t^2+1)P0+(t^3-2t^2+t)T0+(-2t^3+3t^2)P1+(t^3-t^2)T1 }
     *
     * @param p0
     *            first point
     * @param t0
     *            first tangent
     * @param p1
     *            second point
     * @param t1
     *            second tangent
     * @param t
     *            interpolation parameter (P(t=0) = p0, P(t=1) = p1)
     * @return interpolated floating point value
     */
    public static double hermite(double p0, double t0, double p1, double t1, double t) {
        double t2 = t * t;
        double t3 = t * t * t;
        return (2 * t3 - 3 * t2 + 1) * p0 + (t3 - 2 * t2 + t) * t0 + (-2 * t3 + 3 * t2) * p1 + (t3 - t2) * t1;
    }

    /**
     * Vector hermite interpolation. (Advanced functionality).
     * <p>
     * {@code P(t) = (2t^3-3t^2+1)P0+(t^3-2t^2+t)T0+(-2t^3+3t^2)P1+(t^3-t^2)T1 }
     *
     * @param <T>
     *            the type of the point to interpolate (inferred from the other
     *            arguments)
     * @param p0
     *            first point
     * @param t0
     *            first tangent
     * @param p1
     *            second point
     * @param t1
     *            second tangent
     * @param t
     *            interpolation parameter (P(t=0) = p0, P(t=1) = p1)
     * @return interpolated point
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseVector<?>> T hermite(T p0, BaseVector<?> t0, T p1, BaseVector<?> t1, double t) {
        T point = (T) p0.copy();
        for (int i = 0; i < point.getComponentCount(); i++) {
            point.setComponent(i,
                    hermite(p0.getComponent(i), t0.getComponent(i), p1.getComponent(i), t1.getComponent(i), t));
        }
        return point;
    }

    /**
     * Returns the 2D rotation of the matrix in degrees.
     *
     * @param matrix
     *            rotation matrix from which to extract the rotation about the
     *            z-axis
     * @return the rotation about the Z axis in degrees
     */
    public static double getRotation(Transform matrix) {
        // [ xx xy xz tx ] = [ Sx*c -Sy*s ... tx ]
        // [ yx yy yz ty ] [ Sx*s Sy*c ... ty ]
        // [ zx zy zz tz ] [ ... ... ... tz ]
        // [ 0 0 0 1 ] [ 0 0 0 1 ]
        // yx = scaleX * sin(theta)
        // xx = scaleX * cos(theta)
        return Math.toDegrees(Math.atan2(matrix.getMyx(), matrix.getMxx()));
    }

    /**
     * Returns the X scale of the matrix.
     *
     * @param matrix
     *            matrix to extract the x scale from
     * @return the x scaling of the transformation matrix
     */
    public static double getScaleX(Transform matrix) {
        // [ xx xy xz tx ] = [ Sx*c -Sy*s ... tx ]
        // [ yx yy yz ty ] [ Sx*s Sy*c ... ty ]
        // [ zx zy zz tz ] [ ... ... ... tz ]
        // [ 0 0 0 1 ] [ 0 0 0 1 ]
        double theta = Math.toRadians(getRotation(matrix));
        return matrix.getMxx() / Math.cos(theta);
    }

    /**
     * Returns the Y scale of the matrix.
     *
     * @param matrix
     *            matrix to extract the y scale from
     * @return the y-scale of the matrix
     */
    public static double getScaleY(Transform matrix) {
        // [ xx xy xz tx ] = [ Sx*c -Sy*s ... tx ]
        // [ yx yy yz ty ] [ Sx*s Sy*c ... ty ]
        // [ zx zy zz tz ] [ ... ... ... tz ]
        // [ 0 0 0 1 ] [ 0 0 0 1 ]
        double theta = Math.toRadians(getRotation(matrix));
        return matrix.getMyy() / Math.cos(theta);
    }

    /**
     * Returns the x, y scale of the transform.
     *
     * @param matrix
     *            the matrix from which to extract the x,y scaling
     * @return the (x,y) scale of the matrix as a Scale object
     */
    public static Scale getScale(Transform matrix) {
        return new Scale(getScaleX(matrix), getScaleY(matrix));
    }

    /**
     * Returns the translation of the matrix.
     *
     * @param matrix
     *            the matrix to extract the translation from
     * @return a position representation the translation of the matrix
     */
    public static Position getTranslation(Transform matrix) {
        return new Position(matrix.getTx(), matrix.getTy());
    }

}
