package edu.gatech.cs2340.trydent.math.geom;

import edu.gatech.cs2340.trydent.JavaFxConvertable;
import edu.gatech.cs2340.trydent.math.BaseVector;
import edu.gatech.cs2340.trydent.math.MathTools;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Vector;

/**
 * Represents an axis-aligned rectangle.
 */
public class Rectangle implements JavaFxConvertable {
    private Position topLeft;
    private Vector dimensions;

    /**
     * Creates a new rectangle with the given parameters.
     *
     * @param x
     *            Left x-coordinate.
     * @param y
     *            Top y-coordinate.
     * @param width
     *            Width of the rectangle.
     * @param height
     *            Height of the rectangle.
     */
    public Rectangle(double x, double y, double width, double height) {
        this.topLeft = new Position(x, y);
        this.dimensions = new Vector(width, height);
    }

    /**
     * Creates a new rectangle with the given parameters.
     *
     * @param topLeft
     *            Top-left corner.
     * @param dimensions
     *            Width and height.
     */
    public Rectangle(Position topLeft, Vector dimensions) {
        this.topLeft = topLeft.copy();
        this.dimensions = dimensions.copy();
    }

    /**
     * Copy-constructor: creates a new rectangle identical to the parameter.
     *
     * @param rect
     *            Rectangle to copy.
     */
    public Rectangle(Rectangle rect) {
        this(rect.topLeft, rect.dimensions);
    }

    /**
     * Translates this rectangle.
     * @param vector vector to translate by.
     */
    public void moveBy(BaseVector<?> vector) {
        this.topLeft.add(vector);
    }

    /**
     * Sets where the top-left corner of this rectangle is.
     * @param vector position of the new top-left corner.
     */
    public void setTopLeft(BaseVector<?> vector) {
        this.topLeft.set(vector.getX(), vector.getY());
    }

    /**
     * Sets where the center of this rectangle is.
     * @param vector position of the new center.
     */
    public void setCenter(BaseVector<?> vector) {
        this.setTopLeft(vector);
        topLeft.subtract(0.5, dimensions);
    }

    /**
     * Changes the width of this rectangle.
     * @param width the new width.
     */
    public void setWidth(double width) {
        this.dimensions.setX(width);
    }

    /**
     * Changes the height of this rectangle.
     * @param height the new height.
     */
    public void setHeight(double height) {
        this.dimensions.setY(height);
    }

    /**
     * Sets the width and height of this rectangle.
     * @param dimensions the new width and height.
     */
    public void setDimensions(BaseVector<?> dimensions) {
        this.dimensions.set(dimensions.getX(), dimensions.getY());
    }

    /**
     * Gets left side of the rectangle.
     *
     * @return the x-coordinate of the left side.
     */
    public double getLeft() {
        return topLeft.getX();
    }

    /**
     * Gets the top of the rectangle.
     *
     * @return the y-coordinate of the top side.
     */
    public double getTop() {
        return topLeft.getY();
    }

    /**
     * Gets the right of the rectangle.
     *
     * @return the x-coordinate of the right side.
     */
    public double getRight() {
        return topLeft.getX() + dimensions.getX();
    }

    /**
     * Gets the bottom of the rectangle.
     *
     * @return the y-coordinate of the bottom side.
     */
    public double getBottom() {
        return topLeft.getY() + dimensions.getY();
    }

    /**
     * Gets the width.
     *
     * @return the width (horizontal size) of the rectangle.
     */
    public double getWidth() {
        return dimensions.getX();
    }

    /**
     * Gets the height.
     *
     * @return the height (vertical size) of the rectangle.
     */
    public double getHeight() {
        return dimensions.getY();
    }

    /**
     * Gets the top left position.
     *
     * @return the coordinates of the top-left corner.
     */
    public Position getTopLeft() {
        return topLeft.copy();
    }

    /**
     * Gets the top right position.
     *
     * @return the coordinates of the top-right corner.
     */
    public Position getTopRight() {
        return topLeft.copy().add(getWidth(), 0.0);
    }

    /**
     * Gets the bottom left position.
     *
     * @return the coordinates of the bottom-left corner.
     */
    public Position getBottomLeft() {
        return topLeft.copy().add(0.0, getHeight());
    }

    /**
     * Gets the bottom right position.
     *
     * @return the coordinates of the bottom-right corner.
     */
    public Position getBottomRight() {
        return topLeft.copy().add(dimensions);
    }

    /**
     * Gets the position of the center.
     *
     * @return the coordinates of the center of the rectangle.
     */
    public Position getCenter() {
        return topLeft.copy().add(0.5, dimensions);
    }

    /**
     * Gets the dimensions of the rectangle.
     *
     * @return A vector whose x-component is the width and whose y-component is
     *         the height of this rectangle.
     */
    public Vector getDimensions() {
        return dimensions.copy();
    }

    /**
     * Performs rectangular intersection testing.
     *
     * @see edu.gatech.cs2340.trydent.math.MathTools#doRectanglesIntersect
     *
     * @param other
     *            the other rectangle.
     * @return true if the rectangles intersect, false otherwise.
     */
    public boolean intersects(Rectangle other) {
        return MathTools.doRectanglesIntersect(this, other);
    }

    /**
     * Creates a new Rectangle identical to this one.
     * @return
     */
    public Rectangle copy() {
        return new Rectangle(this);
    }

    @Override
    public javafx.scene.shape.Rectangle toJavaFxNode() {
        return new javafx.scene.shape.Rectangle(getLeft(), getTop(), getWidth(), getHeight());
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(128);
        sb.append("[ ");
        sb.append(getLeft());
        sb.append(", ");
        sb.append(getTop());
        sb.append(", ");
        sb.append(getWidth());
        sb.append(", ");
        sb.append(getHeight());
        sb.append(" ]");
        return sb.toString();
    }

}
