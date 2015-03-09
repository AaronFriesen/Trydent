package edu.gatech.cs2340.trydent.math.curve;

/**
 * Abstract representation of a relative-indexable stream of generic points.
 * (Advanced functionality).
 *
 * @param <P>
 *            the type of the points in this point-stream
 */
public abstract class PointStream<P> {

    private int index;

    /**
     * Returns the point at the given index.
     *
     * @param index
     *            index into the underlying datastructure of this stream.
     *            out-of-bounds indices will be handled according to the
     *            IndexWrapMode.
     * @return the point at the index
     */
    public abstract P getAbsolute(int index);

    /**
     * Sets the current point index.
     *
     * @param index
     *            absolute index in the underlying datastructure
     */
    public void seek(int index) {
        this.index = index;
    }

    /**
     * Returns the current index of this stream.
     *
     * @return index the "read head" of this stream is at
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the point at the given offset from the current point.
     *
     * @param offset
     *            offset from the current "read head"
     * @return the point at the given offset
     */
    public P get(int offset) {
        return getAbsolute(index + offset);
    }

    /**
     * Returns the point at the current point.
     *
     * @return the current point
     */
    public P current() {
        return get(0);
    }

    /**
     * Returns the point before the current point.
     *
     * @return the previous point
     */
    public P previous() {
        return get(-1);
    }

    /**
     * Returns the point after the current point.
     *
     * @return the next point
     */
    public P next() {
        return get(1);
    }

}
