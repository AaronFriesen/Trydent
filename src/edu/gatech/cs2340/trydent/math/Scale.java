package edu.gatech.cs2340.trydent.math;

/**
 * Represents a scaling of a 2D vector.
 * @author Garrett Malmquist
 */
public class Scale extends BaseVector<Scale> {

	/**
	 * Creates a new scaling with the given scales for
	 * the x and y components.
	 * @param scaleX - x scale factor (1.0 is no scaling)
	 * @param scaleY - y scale factor (1.0 is no scaling)
	 */
	public Scale(double scaleX, double scaleY) {
		super(2);
		set(scaleX, scaleY);
	}
	
	/**
	 * Creates a new scaling that scales x and y
	 * by the same amount.
	 * @param scale - scale factor (1.0 is no scaling)
	 */
	public Scale(double scale) {
		this(scale, scale);
	}
	
	/**
	 * Creates a new scaling from the input vector
	 * (which can be another scaling).
	 * @param other
	 */
	public Scale(BaseVector<?> other) {
		this(other.getX(), other.getY());
	}
	
	@Override
	public Scale copy() {
		return new Scale(getX(), getY());
	}
	
	/**
	 * Returns a copy of the input vector scaled by this scaling. E.g.:
	 * <pre>{@code
	 * Position pos = new Position(1,2);
	 * Scale scale = new Scale(0.5);
	 * Position scaledPos = scale.applyTo(pos); // scaledPos will now be (0.5, 1.0)
	 * }</pre>
	 * @param vector
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseVector<?>> T applyTo(T vector) {
		return (T) vector.copy().scale(this);
	}
	
}
