package edu.gatech.cs2340.trydent.animation;

import edu.gatech.cs2340.trydent.internal.Adapter;
import edu.gatech.cs2340.trydent.math.BaseVector;
import edu.gatech.cs2340.trydent.math.MathTools;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Scale;
import edu.gatech.cs2340.trydent.math.curve.AdaptedPointStream;
import edu.gatech.cs2340.trydent.math.curve.Interpolation;
import edu.gatech.cs2340.trydent.math.curve.PointStream;

/**
 * Interpolation class used internally be KeyframeAnimation. Most users
 * shouldn't need to use this class directly.
 *
 */
public class KeyframeInterpolation implements Interpolation<Keyframe> {

    private AdaptedPointStream<Keyframe, BaseVector<?>> positionStream;
    private AdaptedPointStream<Keyframe, BaseVector<?>> scaleStream;
    private Keyframe slave;

    /**
     * Creates a new KeyframeInterpolation.
     */
    KeyframeInterpolation() {
        slave = new Keyframe();

        positionStream = new AdaptedPointStream<Keyframe, BaseVector<?>>(null, new Adapter<Keyframe, BaseVector<?>>() {
            @Override
            public BaseVector<?> convert(Keyframe keyframe) {
                return keyframe.getPosition();
            }
        });

        scaleStream = new AdaptedPointStream<Keyframe, BaseVector<?>>(null, new Adapter<Keyframe, BaseVector<?>>() {
            @Override
            public BaseVector<?> convert(Keyframe keyframe) {
                return keyframe.getScale();
            }
        });

    }

    @Override
    public Keyframe interpolate(double t, PointStream<Keyframe> points) {
        Interpolation<BaseVector<?>> interpolation = points.current().interpolation;
        positionStream.setStream(points);
        scaleStream.setStream(points);

        slave.setPosition((Position)interpolation.interpolate(t, positionStream));
        slave.setScale((Scale)interpolation.interpolate(t, scaleStream));
        slave.setRotation(MathTools.degreeLerp(points.current().getRotation(), points.next().getRotation(), t));
        return slave;
    }

}