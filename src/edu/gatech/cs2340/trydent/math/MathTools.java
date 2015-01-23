package edu.gatech.cs2340.trydent.math;

import java.lang.reflect.Array;

/**
 * Library of advanced math tools, primarily linear algebra. 
 * <p>
 * To be used by advanced developers only.
 * @author Garrett Malmquist
 */
public class MathTools {
	
	// We don't want people instantiating this class.
	private MathTools() {}
	
	/**
	 * Performs an (n-1) dimensional Bezier interpolation over n points. (Advanced functionality).
	 * @param t - interpolation parameter - t=0.0 will return the 1st point, 
	 * 										t=1.0 will return the last point.
	 * @param points - One or more points to interpolate between.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseVector<?>> T bezierInterpolation(double t, T ... points) {
		if (points.length == 0)
			throw new IllegalArgumentException("Must input at least one point!");
		
		if (points.length == 1)
			return (T) points[0].copy();
		
		// Strictly speaking this base case could be omitted without loss of functionality,
		// but it makes things a bit more efficient.
		if (points.length == 2)
			return (T) points[0].copy().lerp(t, points[1]);

		T[] left  = (T[]) Array.newInstance(points[0].getClass(), points.length-1);
		T[] right = (T[]) Array.newInstance(points[0].getClass(), points.length-1);
		
		for (int i = 0; i < points.length-1; i++) {
			left[i] = points[i];
			right[i] = points[i+1];
		}
		
		return (T) bezierInterpolation(t, left).lerp(t, bezierInterpolation(t, right));
	}

	/**
	 * Modified lerp that passes through point 'start' at 
	 * time t=a and point 'end' at time t=b.
	 * (Advanced functionality).
	 * @param a
	 * @param start
	 * @param b
	 * @param end
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseVector<?>> T lerp(double a, T start, double b, T end, double t) {
		return (T) start.copy().lerp((t-a)/(b-a), end);
	}
	
	/**
	 * Scalar hermite interpolation. (Advanced functionality).
	 * <p>
	 * {@code P(t) = (2t^3-3t^2+1)P0+(t^3-2t^2+t)T0+(-2t^3+3t^2)P1+(t^3-t^2)T1 }
	 * @param p0 - first point
	 * @param t0 - first tangent
	 * @param p1 - second point
	 * @param t1 - second tangent
	 * @param t - interpolation parameter (P(t=0) = p0, P(t=1) = p1)
	 * @return
	 */
	public static double hermite(double p0, double t0, double p1, double t1, double t) {
		double t2 = t*t;
		double t3 = t*t*t;
		return (2*t3 - 3*t2 + 1)*p0 + (t3 - 2*t2 + t)*t0 + (-2*t3 + 3*t2)*p1 + (t3 - t2)*t1;
	}

	/**
	 * Vector hermite interpolation. (Advanced functionality).
	 * <p>
	 * {@code P(t) = (2t^3-3t^2+1)P0+(t^3-2t^2+t)T0+(-2t^3+3t^2)P1+(t^3-t^2)T1 }
	 * @param p0 - first point
	 * @param t0 - first tangent
	 * @param p1 - second point
	 * @param t1 - second tangent
	 * @param t - interpolation parameter (P(t=0) = p0, P(t=1) = p1)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseVector<?>> T hermite(T p0, T t0, T p1, T t1, double t) {
		T point = (T) p0.copy();
		for (int i = 0; i < point.getComponentCount(); i++) {
			point.setComponent(i, hermite(
				p0.getComponent(i),
				t0.getComponent(i),
				p1.getComponent(i),
				t1.getComponent(i),
				t
			));
		}
		return point;
	}
	
}
