package edu.gatech.cs2340.trydent.math;

/**
 * A position in 2D.
 * <p>
 * (Aka a 2D point, represented internally as a vector displacement from the origin).
 * @author Garrett Malmquist
 *
 */
public class Position extends BaseVector<Position> {

    /**
     * Creates a new 2D position.
     * @param x
     * @param y
     */
    public Position(double x, double y) {
        super(2);
        set(x, y);

        // We want positions to be represented in the
        // form (x, y) rather than <x, y>.
        setStringOpen("(");
        setStringClose(")");
    }

    /**
     * Creates a new 2D position initialized to (0,0).
     */
    public Position() {
        this(0, 0);
    }

    /**
     * Creates a new position from the input vector
     * (which can be another Position, Vector, etc).
     * @param other
     */
    public Position(BaseVector<?> other) {
        this(other.getX(), other.getY());
    }

    @Override
    public Position copy() {
        return new Position(getX(), getY());
    }

    public Vector toVector() {
        return new Vector(getX(), getY());
    }

}
