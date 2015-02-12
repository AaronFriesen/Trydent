package edu.gatech.cs2340.trydent.math.curve;

import java.util.Arrays;

import edu.gatech.cs2340.trydent.internal.TrydentInternalException;

/**
 * TimingHandler which permits arbitrary durations for each span. (Advanced
 * functionality).
 */
public class ArbitraryTiming implements SplineTimingHandler {
    private double[] durations;
    private double[] times;
    private double totalDuration = 0;

    /**
     * Creates a new ArbitraryTiming object with the given array of durations,
     * such that the duration of the ith span is the ith value of the array.
     * (Advanced functionality).
     *
     * @param durations
     */
    public ArbitraryTiming(double[] durations) {
        this.durations = durations;
        this.times = new double[durations.length];
        int i = 0;
        for (double d : durations) {
            this.times[i++] = this.totalDuration;
            this.totalDuration += d;
        }
    }

    @Override
    public SpanTime transformTime(double time) {
        if (time < 0)
            time = 0;
        if (time > totalDuration)
            time = totalDuration;
        int left = 0;
        int right = times.length - 1;
        int index = (int)(time * (times.length - 1) / totalDuration);
        int iterations = 0;
        // While time is not between (times[index]) and (times[index] +
        // durations[index])
        while (time > times[index] + durations[index] || time < times[index]) {
            if (left == right && index == left) {
                throw new TrydentInternalException(
                        "Impossible timing occurred. This may indicate an internal logical programming error."
                                + "\nTime = " + time + "\nTimes = " + Arrays.toString(times) + "\nDurations = "
                                + Arrays.toString(durations));
            }
            // Time at index is too big
            if (times[index] > time) {
                right = index - 1;
            }
            // Time at index is too small
            if (times[index] + durations[index] < time) {
                left = index + 1;
            }
            if (left > right)
                throw new TrydentInternalException(
                        "Impossible timing occurred. This may indicate an internal logical programming error.");
            index = (left + right) / 2;

            iterations++;
            if (iterations > times.length + 1) {
                throw new TrydentInternalException(
                        "BinarySearch is taking longer than O(n+1), something is seriously wrong.\n" + "time=" + time
                                + ", left=" + left + ", right=" + right + ", index=" + index);
            }
        }
        return new SpanTime(index, (time - times[index]) / durations[index]);
    }
}