package edu.gatech.cs2340.trydent.test;


import static edu.gatech.cs2340.trydent.test.TestUtil.objectEquals;

import org.junit.Test;

import edu.gatech.cs2340.trydent.math.curve.IndexWrapMode;
import edu.gatech.cs2340.trydent.math.curve.TimeWrapMode;

public class TimingTest {

    @Test
    public void testIndexClamp() {
        IndexWrapMode m = IndexWrapMode.CLAMP;

        objectEquals(3, m.handle(3, 5));
        objectEquals(2, m.handle(3, 3));
        objectEquals(0, m.handle(-1, 5));
        objectEquals(5, m.handle(1000, 6));
    }

    @Test
    public void testIndexWrap() {
        IndexWrapMode m = IndexWrapMode.WRAP;

        objectEquals(3, m.handle(3, 5));
        objectEquals(0, m.handle(3, 3));
        objectEquals(4, m.handle(-1, 5));
        objectEquals(4, m.handle(10, 6));
    }

    @Test
    public void testIndexReflect() {
        IndexWrapMode m = IndexWrapMode.REFLECT;

        objectEquals(3, m.handle(3, 5));
        objectEquals(2, m.handle(3, 3));
        objectEquals(1, m.handle(-1, 5));
        objectEquals(3, m.handle(8, 6));
    }

    @Test
    public void testTimeClamp() {
        TimeWrapMode m = TimeWrapMode.CLAMP;

        objectEquals(3.0, m.handle(3.0, 10.0));
        objectEquals(0.0, m.handle(0.0, 10.0));
        objectEquals(10.0, m.handle(10.0, 10.0));

        objectEquals(0.0, m.handle(-0.1, 10.0));
        objectEquals(0.0, m.handle(-7.0, 10.0));

        objectEquals(10.0, m.handle(10.1, 10.0));
        objectEquals(10.0, m.handle(17.0, 10.0));

        objectEquals(0.0, m.handle(-0.25 - 10.0*12.0, 10.0));
        objectEquals(10.0, m.handle(17.0 + 10.0*12.0, 10.0));
    }

    @Test
    public void testTimeWrap() {
        TimeWrapMode m = TimeWrapMode.WRAP;

        objectEquals(3.0, m.handle(3.0, 10.0));
        objectEquals(0.0, m.handle(0.0, 10.0));
        objectEquals(9.9999, m.handle(9.9999, 10.0));

        objectEquals(9.9, m.handle(-0.1, 10.0));
        objectEquals(3.0, m.handle(-7.0, 10.0));

        objectEquals(0.25, m.handle(10.25, 10.0));
        objectEquals(7.0, m.handle(17.0, 10.0));

        objectEquals(9.75, m.handle(-0.25 - 10.0*12.0, 10.0));
        objectEquals(7.0, m.handle(17.0 + 10.0*12.0, 10.0));
    }

    @Test
    public void testTimeReflect() {
        TimeWrapMode m = TimeWrapMode.REFLECT;

        objectEquals(3.0, m.handle(3.0, 10.0));
        objectEquals(0.0, m.handle(0.0, 10.0));
        objectEquals(9.9999, m.handle(9.9999, 10.0));

        objectEquals(0.25, m.handle(-0.25, 10.0));
        objectEquals(7.0, m.handle(-7.0, 10.0));

        objectEquals(9.75, m.handle(10.25, 10.0));
        objectEquals(3.0, m.handle(17.0, 10.0));

        objectEquals(0.25, m.handle(-0.25 - 10.0*12.0, 10.0));
        objectEquals(3.0, m.handle(17.0 + 10.0*12.0, 10.0));

        objectEquals(9.75, m.handle(-0.25 - 10.0*11.0, 10.0));
        objectEquals(7.0, m.handle(17.0 + 10.0*11.0, 10.0));
    }

}
