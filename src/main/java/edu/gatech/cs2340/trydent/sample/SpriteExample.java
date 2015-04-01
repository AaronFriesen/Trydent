package edu.gatech.cs2340.trydent.sample;

import javafx.scene.paint.Color;
import edu.gatech.cs2340.trydent.Behavior;
import edu.gatech.cs2340.trydent.GameObject;
import edu.gatech.cs2340.trydent.Sprite;
import edu.gatech.cs2340.trydent.Time;
import edu.gatech.cs2340.trydent.TrydentEngine;
import edu.gatech.cs2340.trydent.math.Position;

/**
 * Example to show some possible ways to use sprites.
 */
public class SpriteExample implements Runnable {

    public static void main(String[] args) {
        TrydentEngine.start();
        TrydentEngine.setWindowTitle("TrydentEngine - Sprite Example");
        TrydentEngine.setWindowSize(720, 480);
        TrydentEngine.setBackgroundColor(Color.WHITE);
        TrydentEngine.runOnce(new SpriteExample());
    }

    @Override
    public void run() {
        String prefix = "edu/gatech/cs2340/trydent/sample/sprites/";
        GameObject pikachu = new Sprite(prefix + "pikapika.gif");
        pikachu.setLocalPosition(new Position(50, 50));

        Sprite pacman = new Sprite(
            new String[]{
                prefix + "wakawaka1.png",
                prefix + "wakawaka2.png",
                prefix + "wakawaka3.png",
                prefix + "wakawaka2.png"
            },
            0.05,
            "pacman"
        );

        final double sideLength = 200;
        GameObject pacmanCenter = new GameObject();
        pacmanCenter.setLocalPosition(new Position(320, 240));
        pacman.setParent(pacmanCenter);
        final double offsetX = -pacman.getWidth()/2;
        final double offsetY = -pacman.getHeight()/2;

        new Behavior(pacman) {
            @Override
            public void onUpdate() {
                double distance = (Time.getTime()*200) % (4*sideLength);
                int curSide = (int) (distance / sideLength);
                int curRotation = 90*curSide;
                getGameObject().setLocalPosition(new Position(
                    -sideLength/2 + distance%sideLength + offsetX,
                    -sideLength/2 + offsetY
                ));
                pacmanCenter.setLocalRotation(curRotation);
            }
        };
    }

}
