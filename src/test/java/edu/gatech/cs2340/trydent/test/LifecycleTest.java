package edu.gatech.cs2340.trydent.test;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.gatech.cs2340.trydent.ContinuousEvent;
import edu.gatech.cs2340.trydent.Time;
import edu.gatech.cs2340.trydent.TrydentEngine;
import edu.gatech.cs2340.trydent.internal.TrydentInternalException;

/**
 * Tests for continuous event timings and engine start/stop behavior.
 *
 */
public class LifecycleTest {

    @Test
    public void testStartUpdateStop() {
        final List<String> messages = new LinkedList<String>();

        ContinuousEvent event = new ContinuousEvent() {

            @Override
            public void onStart() {
                messages.add("onStart");
            }

            @Override
            public void onUpdate() {
                messages.add("onUpdate");
                stop();
            }

            @Override
            public void onStop() {
                messages.add("onStop");
                TrydentEngine.quit();
            }

        };

        TrydentEngine.start();

        try {
            TrydentEngine.waitUntilEngineStops();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String text = messages.toString();
        String expected = "[onStart, onUpdate, onStop]";

        assertTrue("Expected \"" + expected + "\", got \"" + text + "\"", text.equals(expected));
    }

    @Test
    public void startUpdateQuit() {
        final List<String> messages = new LinkedList<String>();

        ContinuousEvent event = new ContinuousEvent() {

            @Override
            public void onStart() {
                messages.add("onStart");
            }

            @Override
            public void onUpdate() {
                messages.add("onUpdate");
                TrydentEngine.quit();
            }

            @Override
            public void onStop() {
                messages.add("onStop");
            }

        };

        TrydentEngine.start();

        try {
            TrydentEngine.waitUntilEngineStops();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String text = messages.toString();
        String expected = "[onStart, onUpdate, onStop]";

        assertTrue("Expected \"" + expected + "\", got \"" + text + "\"", text.equals(expected));
    }

    @Test
    public void testStartUpdate3Stop() {
        final List<String> messages = new LinkedList<String>();

        ContinuousEvent event = new ContinuousEvent() {

            int counter = -100;

            @Override
            public void onStart() {
                messages.add("onStart");
                counter = 0;

            }

            @Override
            public void onUpdate() {
                messages.add("onUpdate");
                counter++;
                if (counter >= 3)
                    stop();
            }

            @Override
            public void onStop() {
                messages.add("onStop");
                TrydentEngine.quit();
            }

        };

        TrydentEngine.start();

        try {
            TrydentEngine.waitUntilEngineStops();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String text = messages.toString();
        String expected = "[onStart, onUpdate, onUpdate, onUpdate, onStop]";

        assertTrue("Expected \"" + expected + "\", got \"" + text + "\"", text.equals(expected));
    }

    @Test
    public void testBasicTiming() {
        final List<String> messages = new LinkedList<String>();

        ContinuousEvent event = new ContinuousEvent() {

            @Override
            public void onStart() {
                // This is event is added before the engine starts, so this
                // should run at the dawn of time, t=0, dt=0.
                messages.add("onStart");
                messages.add("t0=" + Time.getTime());
                messages.add("delta t0=" + Time.getTimePassed());
            }

            @Override
            public void onUpdate() {
                // Run until time is non-zero.
                if (Time.getRealTimeSinceStartup() >= 0.1)
                    stop();
            }

            @Override
            public void onStop() {
                messages.add("onStop");
                messages.add("t1=" + Time.getTime());
                TrydentEngine.quit();
            }

        };

        TrydentEngine.start();

        try {
            TrydentEngine.waitUntilEngineStops();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] expected = {
            "onStart", "t0=0[.]0", "delta t0=0[.]0", "onStop", "t1=(([1-9].*)|(0[.][0]*[1-9]+[0-9]*))"
        };

        assertTrue("expected # messages = " + expected.length + " got " + messages.size(),
                expected.length == messages.size());

        for (int i = 0; i < expected.length; i++) {
            assertTrue("Message #" + i + ": \"" + messages.get(i) + "\" should match \"" + expected[i] + "\"", messages
                    .get(i).matches(expected[i]));
        }
    }

    @Test
    public void testRunOnce() {
        final List<String> messages = new LinkedList<String>();

        new ContinuousEvent() {
            int frame = 0;
            @Override
            public void onStart() {
                frame++;
            }

            @Override
            public void onUpdate() {
                frame++;

                if (frame == 5) {
                    TrydentEngine.runOnce(() -> {
                            messages.add("runOnce");
                        }
                    );
                } else if (frame > 10) {
                    TrydentEngine.quit();
                }
            }

            @Override
            public void onStop() {

            }

        };

        TrydentEngine.start();

        try {
            TrydentEngine.waitUntilEngineStops();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String text = messages.toString();
        String expected = "[runOnce]";

        assertTrue("Expected \"" + expected + "\", got \"" + text + "\"", text.equals(expected));
    }

    @Test
    public void testRunContinuously() {
        final List<String> messages = new LinkedList<String>();

        new ContinuousEvent() {
            int frame = 0;
            Runnable runner;
            boolean[] keepRunning = new boolean[1];

            @Override
            public void onStart() {
                keepRunning[0] = true;

                runner = new Runnable() {
                    int runCount = 0;
                    @Override
                    public void run() {
                        runCount++;
                        if (runCount >= 1 && runCount <= 3) {
                            messages.add("run" + runCount);
                        }
                        keepRunning[0] = true;
                    }
                };
            }

            @Override
            public void onUpdate() {
                frame++;

                if (frame == 5) {
                    TrydentEngine.runContinuously(runner);
                } else if (frame == 10) {
                    keepRunning[0] = false;
                    if (!TrydentEngine.stopRunnable(runner)) {
                        throw new TrydentInternalException("Not able to stop the runner!");
                    }
                } else if (frame > 10) {
                    if (!keepRunning[0]) {
                        TrydentEngine.quit();
                    }
                    keepRunning[0] = false;
                }
            }

            @Override
            public void onStop() {

            }

        };

        TrydentEngine.start();

        try {
            TrydentEngine.waitUntilEngineStops();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String text = messages.toString();
        String expected = "[run1, run2, run3]";

        assertTrue("Expected \"" + expected + "\", got \"" + text + "\"", text.equals(expected));
    }

}
