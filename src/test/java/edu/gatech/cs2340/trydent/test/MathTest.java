package edu.gatech.cs2340.trydent.test;

import static edu.gatech.cs2340.trydent.test.TestUtil.objectEquals;
import static edu.gatech.cs2340.trydent.test.TestUtil.stringEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.gatech.cs2340.trydent.math.IllegalComponentException;
import edu.gatech.cs2340.trydent.math.MathTools;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Scale;
import edu.gatech.cs2340.trydent.math.Vector;
import edu.gatech.cs2340.trydent.math.VectorMismatchException;

public class MathTest {

    @Test
    public void testWrapAngle() {
        // Test identity wraps.
        for (int i = 0; i < 360; i += 10) {
            double theta = i;
            objectEquals(theta, MathTools.wrapAngle(theta));
        }
        // Overflow
        objectEquals(300.0, MathTools.wrapAngle(-60.0));
        // Underflow
        objectEquals(10.0, MathTools.wrapAngle(370.0));
        // Fractional overflow
        objectEquals(10.25, MathTools.wrapAngle(370.25));
        // Fractional underflow
        objectEquals(299.75, MathTools.wrapAngle(-60.25));
        // Extreme values
        objectEquals(275.0, MathTools.wrapAngle(275.0 + 360.0 * 11.0));
        objectEquals(275.0, MathTools.wrapAngle(275.0 - 360.0 * 11.0));
    }

    @Test
    public void testLerpAngle() {
        // Simple cases, where values should be same as normal lerp.
        objectEquals(5.0, MathTools.degreeLerp(2, 8, 0.5));
        objectEquals(2.0, MathTools.degreeLerp(2, 8, 0.0));
        objectEquals(8.0, MathTools.degreeLerp(2, 8, 1.0));

        // Lerping on negative values, which will be wrapped.
        objectEquals(355.0, MathTools.degreeLerp(-2, -8, 0.5));

        // Wrapping from negative to positive.
        objectEquals(10.0, MathTools.degreeLerp(-10, 30, 0.5));

        // Wrapping from positive to negative.
        objectEquals(350.0, MathTools.degreeLerp(10, -30, 0.5));

        // Lerping before the 180 degree divergence line.
        objectEquals(170.0/2.0, MathTools.degreeLerp(0, 170.0, 0.5));

        // Lerping after the 180 degree divergence line.
        objectEquals(360.0 - (170.0/2.0), MathTools.degreeLerp(0, 190.0, 0.5));
    }

    @Test
    public void testInstantiation() {
        stringEquals("(0.0, 0.0)", new Position());
        stringEquals("(3.7, 4.8)", new Position(3.7, 4.8));

        stringEquals("<0.0, 0.0>", new Vector());
        stringEquals("<0.1, 0.7>", new Vector(0.1, 0.7));

        stringEquals("<1.0, 1.0>", new Scale());
        stringEquals("<3.0, 4.5>", new Scale(3.0, 4.5));
        stringEquals("<5.5, 5.5>", new Scale(5.5));

        stringEquals("<2.5, 3.5>", new Vector(new Position(1.0, 2.0), new Position(1.0 + 2.5, 2.0 + 3.5)));
    }

    @Test
    public void testAdd() {
        stringEquals("(2.0, 6.0)", new Position(1.0, 3.0).add(1.0, 3.0));
        stringEquals("(9.0, 12.5)", new Position(5.0, 5.0).add(new Vector(4.0, 7.5)));
        stringEquals("<71.0, 70.0>", new Vector(20.5, 19.5).add(10.0, new Vector(5.05, 5.05)));

        try {
            new Position(1, 2).add(1);
            assertTrue("Expected vector mismatch exception!", false);
        } catch (VectorMismatchException e) {
        }

        try {
            new Position(1, 2).add(1, 2, 3);
            assertTrue("Expected vector mismatch exception!", false);
        } catch (VectorMismatchException e) {
        }
    }

    @Test
    public void testCopy() {
        stringEquals("<3.0, 4.0>", new Scale(3.0, 4.0).copy());
        stringEquals("(4.0, 5.0)", new Position(4.0, 5.0).copy());
        stringEquals("<5.0, 7.5>", new Vector(5.0, 7.5).copy());
    }

    @Test
    public void testDot() {
        stringEquals("1.0", new Vector(1.0, 0.0).dot(1.0, 0.0));
        stringEquals("6.0", new Vector(0.0, 3.0).dot(0.0, 2.0));
        stringEquals("12.0", new Vector(2.0, 3.0).dot(3.0, 2.0));
        stringEquals("50.0", new Vector(5.0, 5.0).dot(5.0, 5.0));

        try {
            new Position(1, 2).dot(1);
            assertTrue("Expected vector mismatch exception!", false);
        } catch (VectorMismatchException e) {
        }

        try {
            new Position(1, 2).dot(1, 2, 3);
            assertTrue("Expected vector mismatch exception!", false);
        } catch (VectorMismatchException e) {
        }
    }

    @Test
    public void testFill() {
        stringEquals("(0.0, 0.0)", new Position(5.7, 6.8).fill(0.0));
        stringEquals("(-6.25, -6.25)", new Position().fill(-6.25));
    }

    @Test
    public void testGetComponent() {
        stringEquals("1.0", new Position(1.0, 2.0).getComponent(0));
        stringEquals("2.0", new Position(1.0, 2.0).getComponent(1));
        stringEquals("2", new Position(1.0, 2.0).getComponentCount());
        stringEquals("1.0", new Position(1.0, 2.0).getX());
        stringEquals("2.0", new Position(1.0, 2.0).getY());

        try {
            new Position(1, 2).getComponent(3);
            assertTrue("getComponent(3) should throw an error!", false);
        } catch (IllegalComponentException x) {
        }

        try {
            new Position(1, 2).getComponent(-1);
            assertTrue("getComponent(-1) should throw an error!", false);
        } catch (IllegalComponentException x) {
        }
    }

    @Test
    public void testLerp() {
        Position a = new Position(-5.0, -3.0);
        Position b = new Position(5.0, 3.0);

        stringEquals(a.toString(), a.copy().lerp(0, b));
        stringEquals(b.toString(), a.copy().lerp(1, b));
        stringEquals("(0.0, 0.0)", a.copy().lerp(0.5, b));
    }

    @Test
    public void testMagnitude() {
        stringEquals("0.0", new Position().magnitudeSquared());
        stringEquals("1.0", new Position(1.0, 0.0).magnitudeSquared());
        stringEquals("1.0", new Position(0.0, 1.0).magnitudeSquared());
        stringEquals("49.0", new Position(7.0, 0.0).magnitudeSquared());
        stringEquals("49.0", new Position(0.0, 7.0).magnitudeSquared());
        stringEquals("25.0", new Position(3.0, 4.0).magnitudeSquared());

        stringEquals("0.0", new Position().magnitude());
        stringEquals("1.0", new Position(1.0, 0.0).magnitude());
        stringEquals("1.0", new Position(0.0, 1.0).magnitude());
        stringEquals("7.0", new Position(7.0, 0.0).magnitude());
        stringEquals("7.0", new Position(0.0, 7.0).magnitude());
        stringEquals("5.0", new Position(3.0, 4.0).magnitude());
    }

    @Test
    public void testNormalize() {
        stringEquals("<0.0, 0.0>", new Vector().normalize());

        Vector[] tests = {
            new Vector(1.0, 2.0), new Vector(5.0, 5.0), new Vector(1.0, 0.0), new Vector(0.0, 4.0)
        };

        for (Vector v : tests) {
            double mag = v.normalize().magnitude();
            assertTrue("|" + v + "| = " + mag, Math.abs(mag - 1.0) <= 1e-6);
        }

        stringEquals("<1.0, 0.0>", new Vector(64.0, 0.0).normalize());
        stringEquals("<1.0, 0.0>", new Vector(4.0, 0.0).normalize());
        stringEquals("<0.0, -1.0>", new Vector(0.0, -25.0).normalize());
        stringEquals("<0.0, 1.0>", new Vector(0.0, 100.0).normalize());
    }

    @Test
    public void testRotate() {
        stringEquals("(0.0, 5.0)", new Position(5.0, 0.0).rotate90());
        stringEquals("(5.0, 0.0)", new Position(0.0, -5.0).rotate90());
        stringEquals("(-5.0, 8.0)", new Position(8.0, 5.0).rotate90());

        stringEquals("(5.0, 0.0)", new Position(5.0, 0.0).rotate2D(360));
        stringEquals("(-5.0, 0.0)", new Position(5.0, 0.0).rotate2D(180));

        stringEquals("(5.0, 2.0)", new Position(5.0, 2.0).rotate2D(360));
        stringEquals("(-5.0, 2.0)", new Position(5.0, -2.0).rotate2D(180));

        stringEquals("0.5", Math.round(new Position(1.0, 0.0).rotate2D(30).getY() * 1e4) / 1e4);
    }

    @Test
    public void testScale() {
        stringEquals("(0.0, 0.0)", new Position().scale(27));
        stringEquals("(0.0, 0.0)", new Position(-6.0, 28.0).scale(0));
        stringEquals("(1.0, 1.0)", new Position(0.25, 0.5).scale(4, 2));
        stringEquals("(3.0, 6.0)", new Position(1.5, 24).scale(2.0, 0.25));
        stringEquals("(-7.25, -0.125)", new Position(7.25 * 2, 0.125 * 2).scale(-0.5));
        stringEquals("(1.0, 1.0)", new Position(4.0, 4.0).scale(new Scale(0.25)));
    }

    @Test
    public void testSetComponent() {
        Position p = new Position();

        p.setX(4);
        p.setY(-8.5);
        stringEquals("(4.0, -8.5)", p);

        p.setComponent(0, 2);
        p.setComponent(1, 9);
        stringEquals("(2.0, 9.0)", p);

        try {
            new Position(1, 2).setComponent(3, 10);
            assertTrue("seComponent(3, x) should throw an error!", false);
        } catch (IllegalComponentException x) {
        }

        try {
            new Position(1, 2).setComponent(-1, 10);
            assertTrue("setComponent(-1, x) should throw an error!", false);
        } catch (IllegalComponentException x) {
        }
    }

    @Test
    public void testSubtract() {
        stringEquals("(0.0, 0.0)", new Position(1.0, 3.0).subtract(1.0, 3.0));
        stringEquals("(1.0, -2.5)", new Position(5.0, 5.0).subtract(new Vector(4.0, 7.5)));
        stringEquals("<3.0, 7.0>",
                new Vector(20.5, 19.5).subtract(2.5, new Vector((20.5 - 3.0) / 2.5, (19.5 - 7.0) / 2.5)));

        try {
            new Position(1, 2).subtract(1);
            assertTrue("Expected vector mismatch exception!", false);
        } catch (VectorMismatchException e) {
        }

        try {
            new Position(1, 2).subtract(1, 2, 3);
            assertTrue("Expected vector mismatch exception!", false);
        } catch (VectorMismatchException e) {
        }
    }

}
