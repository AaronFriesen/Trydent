package edu.gatech.cs2340.trydent.math.curve;

/**
 * Point-stream backed by an array for quick indexing. (Advanced functionality).
 *
 * @param <P>
 *            the type of points in this stream
 */
public class ArrayPointStream<P> extends PointStream<P> {

    private P[] stream;
    private IndexWrapMode handler;

    /**
     * Creates a new ArrayPointStream.
     *
     * @param stream
     *            - array of points in the stream.
     * @param boundsHandler
     *            - strategy to handle requests for points outside the bounds of
     *            the array.
     */
    public ArrayPointStream(P[] stream, IndexWrapMode boundsHandler) {
        this.stream = stream;
        this.handler = boundsHandler;
    }

    @Override
    public P getAbsolute(int index) {
        return stream[handler.handle(index, stream.length)];
    }

    /**
     * Sets the strategy used to handle requests for points outside the bounds
     * of the array.
     *
     * @param handler
     *            the IndexWrapMode
     */
    public void setBoundsHandler(IndexWrapMode handler) {
        this.handler = handler;
    }

    /**
     * Returns the startegy used to handle requests for points outside the
     * bounds of the array.
     *
     * @return the IndexWrapMode
     */
    public IndexWrapMode getBoundsHandler() {
        return handler;
    }

    /**
     * Returns the number of points in this stream.
     *
     * @return as described
     */
    public int size() {
        return stream.length;
    }

}
