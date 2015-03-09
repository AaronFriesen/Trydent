package edu.gatech.cs2340.trydent.math.curve;

/**
 * Information holder for storing a time parameter along a particular span of a
 * spline. Used internally to handle spline timings. (Advanced functionality).
 */
public class SpanTime {
    private int index;
    private double time;

    /**
     * Creates a new span time.
     *
     * @param index
     *            - the index of the span in question
     * @param time
     *            - value between 0 and 1 indicating the position on the span.
     */
    public SpanTime(int index, double time) {
        this.index = index;
        this.time = time;
    }

    /**
     * Returns the index of the span.
     *
     * @return the span index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the position along the span, with 0 being the start of the span,
     * and 1 being the end.
     *
     * @return the "time" (aka distance along the curve)
     */
    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(32);
        sb.append("(");
        sb.append(index);
        sb.append(", ");
        sb.append(time);
        sb.append(")");
        return sb.toString();
    }

}
