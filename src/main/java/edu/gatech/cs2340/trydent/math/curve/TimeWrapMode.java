package edu.gatech.cs2340.trydent.math.curve;

/**
 * Interface to manage appropriate wrapping, clamping, etc behavior for
 * indexing-based behavior.
 * <p>
 * This is analogous to IndexWrapMode, but for times rather than indices; the
 * primary difference is that valid values for <i>indices</i> are
 * {@code 0 <= i <=
 * (size-1)}, however valid values for <i>times</i> are
 * {@code 0 <= i <= duration}.
 * <p>
 * (Advanced functionality).
 */
public interface TimeWrapMode {

    public double handle(double time, double duration);

    /**
     * Clamps the input time, such that negative times will return 0, and times
     * greater than the duration will return the duration.
     */
    public static final TimeWrapMode CLAMP = new TimeWrapMode() {
        @Override
        public double handle(double time, double duration) {
            if (time < 0)
                return 0;
            if (time > duration)
                return duration;
            return time;
        }
    };

    /**
     * Wraps the input time, such that negative times will wrap to the "end",
     * and times greater than duration will wrap back to 0. (See the behavior of
     * IndexWrapMode.WRAP).
     */
    public static final TimeWrapMode WRAP = new TimeWrapMode() {
        @Override
        public double handle(double time, double duration) {
            if (time >= 0)
                return time % duration;
            // In java, -a % b = -(a % b)
            // Also mod works just fine for doubles.
            return duration + (time % duration);
        }
    };

    /**
     * Reflects the input time, such that negative times are taken in absolute
     * value, and times greater than duration move back down towards 0. (See the
     * behavior of IndexWrapMode.REFLECT).
     */
    public static final TimeWrapMode REFLECT = new TimeWrapMode() {
        @Override
        public double handle(double time, double duration) {
            if (time < 0)
                time = -time;
            if ((int)(time / duration) % 2 == 0) {
                return time % duration;
            }
            return duration - (time % duration);
        }
    };

}
