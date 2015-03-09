package edu.gatech.cs2340.trydent.math.curve;

import edu.gatech.cs2340.trydent.internal.Adapter;

/**
 * PointStream which converts a PointStream&lt;A&gt; to a PointStream&lt;B&gt;
 * using an adapter strategy. (Advanced functionality).
 *
 * @param <A>
 *            type of points in the stream to draw from
 * @param <B>
 *            type that the points should be converted to
 */
public class AdaptedPointStream<A, B> extends PointStream<B> {

    private PointStream<A> stream;
    private Adapter<A, B> adapter;

    /**
     * Creates a new PointStream&lt;B&gt; using the data from the given stream
     * adapted by the given adapter strategy.
     *
     * @param stream
     *            - PointStream&lt;A&gt; to convert
     * @param adapter
     *            - strategy to convert instances of A to instances of B.
     */
    public AdaptedPointStream(PointStream<A> stream, Adapter<A, B> adapter) {
        setAdapter(adapter);
        setStream(stream);
    }

    @Override
    public B getAbsolute(int index) {
        return adapter.convert(stream.getAbsolute(index));
    }

    /**
     * Sets the point-stream this stream is drawing instances of A from.
     *
     * @param stream
     *            point stream to draw from
     */
    public void setStream(PointStream<A> stream) {
        this.stream = stream;
        if (stream != null) {
            this.seek(stream.getIndex());
        }
    }

    /**
     * Sets the strategy used to convert instances of A to instances of B.
     *
     * @param adapter
     *            adapter
     */
    public void setAdapter(Adapter<A, B> adapter) {
        this.adapter = adapter;
    }

}
