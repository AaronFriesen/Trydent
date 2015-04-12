package edu.gatech.cs2340.trydent.sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import edu.gatech.cs2340.trydent.Behavior;
import edu.gatech.cs2340.trydent.GameObject;
import edu.gatech.cs2340.trydent.Time;
import edu.gatech.cs2340.trydent.TrydentEngine;
import edu.gatech.cs2340.trydent.animation.Animation;
import edu.gatech.cs2340.trydent.animation.AnimationEvent;
import edu.gatech.cs2340.trydent.animation.AnimationListener;
import edu.gatech.cs2340.trydent.animation.KeyframeAnimation;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.curve.IndexWrapMode;
import edu.gatech.cs2340.trydent.math.curve.Interpolation;
import edu.gatech.cs2340.trydent.math.curve.TimeWrapMode;

/**
 * Example testing the display and orientation of animating rectangles.
 *
 * @author Garrett Malmquist
 *
 */
public class AnimationListenerExample implements Runnable {

    private static int width = 720;
    private static int height = 480;

    public static void main(String[] args) {
        TrydentEngine.start();
        TrydentEngine.setWindowTitle("TrydentEngine - Animation Listener Example");
        TrydentEngine.setWindowSize(width, height);
        TrydentEngine.runOnce(new AnimationListenerExample());
    }

    @Override
    public void run() {
        Time.setTimeRate(2.0);

        GameObject center = new GameObject();

        final Rectangle rectFx = new Rectangle(100, 20, Color.GREEN.brighter());
        final GameObject obj1 = new GameObject(rectFx);
        final Text textFx = new Text(20, 16, "text");
        GameObject obj1Text = new GameObject(textFx);
        obj1Text.setParent(obj1);

        obj1.setParent(center);

        center.setPosition(new Position(width/2, height/2));

        Animation anim1 = KeyframeAnimation.create()
                .setInterpolation(Interpolation.STRAIGHT)
                .addKeyframe()
                .moveBy(new Position(100, 0)).addKeyframe()
                .moveBy(new Position(0, 100)).addKeyframe()
                .moveBy(new Position(-100, 0)).addKeyframe()
                .moveBy(new Position(0, -100)).addKeyframe()
                .setTotalDuration(3.0)
                .build();

        Animation anim2 = KeyframeAnimation.create()
                .setInterpolation(Interpolation.SMOOTH)
                .addKeyframe()
                .moveBy(new Position(-100, 0)).addKeyframe()
                .moveBy(new Position(0, -100)).addKeyframe()
                .moveBy(new Position(100, 0)).addKeyframe()
                .setAnimationCircular(true)
                .setTotalDuration(3.0)
                .build();

        obj1.addAnimationListener(new AnimationListener() {
            int loopCount = 0;

            @Override
            public void animationEnded(AnimationEvent event) {
                textFx.setText("ended");

                new Behavior(obj1) {
                    double time;
                    GameObject g;
                    int state = 0;

                    @Override
                    public void onStart() {
                        time = Time.getTime();
                        g = getGameObject();
                    }

                    @Override
                    public void onUpdate() {
                        double time = Time.getTime() - this.time;
                        if (state == 0) {
                            if (time > 1.0) {
                                g.loopAnimation(anim2);
                                state++;
                            }
                        } else if (state == 1) {
                            if (time > 2.0) {
                                g.setAnimationPaused(true);
                                state++;
                            }
                        } else if (state == 2) {
                            if (time > 5.0) {
                                g.setAnimationPaused(false);
                                state++;
                            }
                        } else if (state == 3) {
                            if (time > 7.0) {
                                g.playAnimation(anim1);
                                state++;
                            }
                        } else if (state == 4) {
                            stop();
                        }
                    }

                };
            }

            @Override
            public void animationInterrupted(AnimationEvent event) {
                textFx.setText("interrupted");
                GameObject g = new GameObject(new Rectangle(50, 50, Color.ORANGE));
                g.setPosition(new Position(Math.random()*width, Math.random()*height));
            }

            @Override
            public void animationLooped(AnimationEvent event) {
                textFx.setText("loop " + ++loopCount);
                if (loopCount == 3) {
                    obj1.loopAnimation(anim2);
                }
                if (loopCount == 5) {
                    new Behavior(obj1) {

                        double start = Time.getTime();
                        @Override
                        public void onStart() {
                            getGameObject().stopAnimation();
                        }

                        @Override
                        public void onUpdate() {
                            if (Time.getTime() - start > 3.0) {
                                getGameObject().playAnimation(anim1);
                                stop();
                            }
                        }

                    };
                }
            }

            @Override
            public void animationPaused(AnimationEvent event) {
                textFx.setText("paused");
            }

            @Override
            public void animationStarted(AnimationEvent event) {
                textFx.setText("started");
                if (event.animation == anim1) {
                    rectFx.setFill(Color.GREEN.brighter());
                } else {
                    rectFx.setFill(Color.CYAN);
                }
            }

            @Override
            public void animationStopped(AnimationEvent event) {
                textFx.setText("stopped");

            }

            @Override
            public void animationUnpaused(AnimationEvent event) {
                textFx.setText("unpaused");
            }

        });

        obj1.loopAnimation(anim1);
        anim1.setIndexWrap(IndexWrapMode.WRAP);
        anim1.setTimeWrap(TimeWrapMode.WRAP);

    }
}
