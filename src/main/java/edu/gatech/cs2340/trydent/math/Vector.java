package edu.gatech.cs2340.trydent.math;

/**
 * Represents a displacement or direction in 2D.
 * <p>
 * (Internally represented as a vector, for the mathematically inclined).
 * @author Garrett Malmquist
 *
 */
public class Vector extends BaseVector<Vector> {

    /**
     * Creates a new 2D vector pointing in the direction dx, dy.
     * @param dx
     * @param dy
     */
    public Vector(double dx, double dy) {
        super(2);
        set(dx, dy);
    }

    /**
     * Creates a new vector pointing nowhere <0, 0>.
     */
    public Vector() {
        super(2);
    }

    /**
     * Creates a new Vector from the input vector
     * (which can be a Vector, Position, etc).
     * @param other
     */
    public Vector(BaseVector<?> other) {
        this(other.getX(), other.getY());
    }

    /**
     * Creates a new vector point from the first position to the second position.
     * @param from - the first point (origin)
     * @param to - the second point (destination)
     */
    public Vector(Position from, Position to) {
        this(to.getX() - from.getX(), to.getY() - from.getY());
    }

    @Override
    public Vector copy() {
        return new Vector(getX(), getY());
    }

    public Position toPosition() {
        return new Position(getX(), getY());
    }

}
