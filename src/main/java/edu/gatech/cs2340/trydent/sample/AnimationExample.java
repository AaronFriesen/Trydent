package edu.gatech.cs2340.trydent.sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import edu.gatech.cs2340.trydent.Animation;
import edu.gatech.cs2340.trydent.GameObject;
import edu.gatech.cs2340.trydent.TrydentEngine;
import edu.gatech.cs2340.trydent.animation.KeyframeAnimation;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Scale;
import edu.gatech.cs2340.trydent.math.curve.IndexWrapMode;
import edu.gatech.cs2340.trydent.math.curve.TimeWrapMode;

/**
 * Example testing the display and orientation of animating rectangles.
 *
 * @author Garrett Malmquist
 *
 */
public class AnimationExample implements Runnable {

    private static int width = 720;
    private static int height = 480;

    public static void main(String[] args) {
        TrydentEngine.start();
        TrydentEngine.setWindowTitle("TrydentEngine - Animation Example");
        TrydentEngine.setWindowSize(width, height);
        TrydentEngine.runLater(new AnimationExample());
    }

    private void createAnimation(Position pos, Color color, IndexWrapMode indexWrap, TimeWrapMode timeWrap, boolean circular) {
        GameObject obj1 = new GameObject(new Rectangle(100, 20, color));
        obj1.setPosition(pos);

        GameObject obj2 = new GameObject(new Rectangle(100, 20, color));
        obj2.setPosition(obj1.getPosition());
        obj2.translate(100, 100);
        obj2.rotate(30);
        obj2.scale(0.4, 2.0);

        GameObject obj3 = new GameObject(new Rectangle(100, 20, color));
        obj3.setPosition(obj2.getPosition());
        obj3.translate(300, 100);


        GameObject moving = new GameObject(new Rectangle(100, 20, color.brighter().brighter().brighter()));
        moving.setOrientation(obj1.getOrientation());

        Animation anim = KeyframeAnimation.create()
                .setOrientation(obj1.getOrientation())
                .addKeyframe(1)
                .setOrientation(obj2.getOrientation())
                .addKeyframe(1)
                .setOrientation(obj3.getOrientation())
                .addKeyframe() // final keyframe has no duration
                .setCircular(circular) // false by default
                .build();

       moving.loopAnimation(anim);
       anim.setIndexWrap(indexWrap);
       anim.setTimeWrap(timeWrap);
    }

    private void createSpinny(Position pos) {
        GameObject obj = new GameObject();
        GameObject square = new GameObject(new Rectangle(50, 50, Color.ORANGE));
        square.setParent(obj);
        square.translate(-25, -25);

        Animation anim = KeyframeAnimation.create()
                .setPosition(pos)
                .addKeyframe()
                .rotateBy(90)
                .setScale(new Scale(1.5))
                .addKeyframe()
                .setScale(new Scale(1))
                .rotateBy(90)
                .addKeyframe()
                .rotateBy(90)
                .setScale(new Scale(1.5))
                .addKeyframe()
                .setScale(new Scale(1))
                .setTotalDuration(2)
                .setCircular(true)
                .build();

        obj.loopAnimation(anim);
    }

    private void createWheely(Position pos, int size) {
        GameObject obj = new GameObject();
        GameObject square = new GameObject(new Rectangle(size, size, Color.ORANGE));
        square.setParent(obj);
        square.translate(-size/2, -size/2);

        Animation anim = KeyframeAnimation.create()
                .setPosition(pos)
                .addKeyframe()
                .rotateBy(90)
                .moveBy(new Position(0, -50))
                .addKeyframe()
                .rotateBy(90)
                .moveBy(new Position(-50, 0))
                .addKeyframe()
                .rotateBy(90)
                .moveBy(new Position(0, 50))
                .addKeyframe()
                .setTotalDuration(2)
                .setCircular(true)
                .build();

        obj.loopAnimation(anim);
    }

    @Override
    public void run() {
        createAnimation(new Position(50, 10), Color.GREEN, IndexWrapMode.WRAP, TimeWrapMode.WRAP, false);
        createAnimation(new Position(50, 110), Color.DARKRED, IndexWrapMode.REFLECT, TimeWrapMode.REFLECT, false);
        createAnimation(new Position(50, 210), Color.DARKBLUE, IndexWrapMode.WRAP, TimeWrapMode.REFLECT, false);

        createSpinny(new Position(width*3/4, height*1/4));
        createWheely(new Position(width*2/4, height*1/4), 10);
    }
}
