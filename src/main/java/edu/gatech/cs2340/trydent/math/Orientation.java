package edu.gatech.cs2340.trydent.math;

/**
 * Information holder class to store the position, rotation, and scale of an
 * object. This is mostly a convenience class to avoid polluting method calls
 * with the same three parameters in many places.
 *
 */
public class Orientation {
    private Position position;
    private double rotation;
    private Scale scale;

    /**
     * Creates a new orientation with the given position, rotation, and scale.
     *
     * @param position
     * @param rotation
     * @param scale
     */
    public Orientation(Position position, double rotation, Scale scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     * Gets the position.
     *
     * @return
     */
    public Position getPosition() {
        return position.copy();
    }

    /**
     * Gets the rotation.
     *
     * @return The rotation in degrees.
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Gets the scale.
     *
     * @return The scale.
     */
    public Scale getScale() {
        return scale.copy();
    }

    /**
     * Sets the position.
     *
     * @param p
     */
    public void setPosition(Position p) {
        this.position = p.copy();
    }

    /**
     * Sets the rotation.
     *
     * @param r
     *            - rotation in degrees
     */
    public void setRotation(double r) {
        this.rotation = r;
    }

    /**
     * Sets the scale.
     *
     * @param s
     */
    public void setScale(Scale s) {
        this.scale = s.copy();
    }

    /**
     * Sets the position, rotation, and scale of this orientation to that of the
     * other orientation.
     *
     * @param other
     */
    public void set(Orientation other) {
        setPosition(other.position);
        setRotation(other.rotation);
        setScale(other.scale);
    }

    /**
     * Returns a copy of this Orientation.
     *
     * @return
     */
    public Orientation copy() {
        return new Orientation(position, rotation, scale);
    }

    /**
     * Linearly interpolates between this orientation and the other orientation.
     * This method modifies the Orientation it is called on, and returns itself.
     * (Advanced functionality).
     *
     * @param t
     *            - interpolation parameter between 0 and 1 to interpolate.
     * @param other
     * @return
     */
    public Orientation lerp(double t, Orientation other) {
        position.lerp(t, other.position);
        rotation = (1.0 - t) * rotation + t * other.rotation;
        scale.lerp(t, other.scale);
        return this;
    }

}
