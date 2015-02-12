package edu.gatech.cs2340.trydent.math.curve;

/**
 * Abstract representation of a relative-indexable stream of generic points.
 * (Advanced functionality).
 *
 * @param <P>
 */
public abstract class PointStream<P> {

    private int index;

    /**
     * Returns the point at the given index.
     *
     * @param index
     * @return
     */
    public abstract P getAbsolute(int index);

    /**
     * Sets the current point index.
     *
     * @param index
     * @return
     */
    public void seek(int index) {
        this.index = index;
    }

    /**
     * Returns the current index of this stream.
     *
     * @return
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the point at the given offset from the current point.
     *
     * @param offset
     * @return
     */
    public P get(int offset) {
        return getAbsolute(index + offset);
    }

    /** Returns the point at the current point. */
    public P current() {
        return get(0);
    }

    /** Returns the point before the current point. */
    public P previous() {
        return get(-1);
    }

    /** Returns the point after the current point. */
    public P next() {
        return get(1);
    }

}
