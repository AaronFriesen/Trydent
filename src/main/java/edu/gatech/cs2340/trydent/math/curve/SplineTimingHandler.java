package edu.gatech.cs2340.trydent.math.curve;

/**
 * Interface to handle the conversion from a spline's global time space to the
 * space of one of its spans. (Advanced functionality).
 *
 */
public interface SplineTimingHandler {
    public abstract SpanTime transformTime(double time);
}