package edu.gatech.cs2340.trydent.animation;

import edu.gatech.cs2340.trydent.math.BaseVector;
import edu.gatech.cs2340.trydent.math.Orientation;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Scale;
import edu.gatech.cs2340.trydent.math.curve.Interpolation;

/**
 * Information holder class for a single keyframe. Here as an inner class
 * for encapsulation.
 */
public class Keyframe extends Orientation {
    double duration;
    Interpolation<BaseVector<?>> interpolation;

    Keyframe() {
        super(new Position(), 0, new Scale());
        duration = 1;
        interpolation = Interpolation.SMOOTH;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(512);
        sb.append("Keyframe(");
        sb.append("\n  position: ");
        sb.append(getPosition());
        sb.append(", ");
        sb.append("\n  rotation: ");
        sb.append(getRotation());
        sb.append(",");
        sb.append("\n  scale: ");
        sb.append(getScale());
        sb.append(",");
        sb.append("\n  duration: " + duration);
        sb.append(",");
        sb.append("\n  interpolation: " + interpolation.getClass().getSimpleName());
        sb.append(",");
        sb.append("\n)");
        return sb.toString();
    }
}