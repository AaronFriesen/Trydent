package edu.gatech.cs2340.trydent.math.curve;

import edu.gatech.cs2340.trydent.internal.Adapter;

/**
 * PointStream which converts a PointStream<A> to a PointStream<B> using an
 * adapter strategy. (Advanced functionality).
 *
 * @param <A>
 * @param <B>
 */
public class AdaptedPointStream<A, B> extends PointStream<B> {

    private PointStream<A> stream;
    private Adapter<A, B> adapter;

    /**
     * Creates a new PointStream<B> using the data from the given stream adapted
     * by the given adapter strategy.
     *
     * @param stream
     *            - PointStream<A> to convert
     * @param adapter
     *            - strategy to convert instances of A to instances of B.
     */
    public AdaptedPointStream(PointStream<A> stream, Adapter<A, B> adapter) {
        this.stream = stream;
        this.adapter = adapter;
    }

    @Override
    public B getAbsolute(int index) {
        return adapter.convert(stream.getAbsolute(index));
    }

    /**
     * Sets the point-stream this stream is drawing instances of A from.
     *
     * @param stream
     */
    public void setStream(PointStream<A> stream) {
        this.stream = stream;
        this.seek(stream.getIndex());
    }

    /**
     * Sets the strategy used to convert instances of A to instances of B.
     *
     * @param adapter
     */
    public void setAdapter(Adapter<A, B> adapter) {
        this.adapter = adapter;
    }

}
