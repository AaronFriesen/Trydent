package edu.gatech.cs2340.trydent.sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import edu.gatech.cs2340.trydent.Behavior;
import edu.gatech.cs2340.trydent.GameObject;
import edu.gatech.cs2340.trydent.Time;
import edu.gatech.cs2340.trydent.TrydentEngine;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Scale;

public class RectangleExample implements Runnable {

    public static void main(String[] args) {        
        TrydentEngine.start();
        TrydentEngine.setWindowTitle("TrydentEngine - Rectangle Example");
        TrydentEngine.setWindowSize(720, 480);
        TrydentEngine.runLater(new RectangleExample());
    }

    @Override
    public void run() {
        GameObject obj = new GameObject(new Rectangle(100, 20, Color.GREEN));
        obj.setLocalPosition(new Position(100, 100));
        
        GameObject sub = new GameObject("Sub", new Rectangle(100, 50, Color.BLUE));
        sub.setLocalPosition(new Position(100, 50));
        
        GameObject tiny = new GameObject("Tiny", new Rectangle(50, 25, Color.YELLOW));
        tiny.setLocalPosition(new Position(100 + 100 + 10, 50));

        sub.setLocalScale(new Scale(0.5, 0.5));        
        tiny.setParent(sub);

        sub.setLocalRotation(45);
        
        new Behavior(sub) {
            @Override
            public void onUpdate() {
                getGameObject().setPosition(new Position(100 + 100 * Math.cos(Time.getTime()), 0));
            }
        };
        
        new Behavior(tiny) {
            @Override
            public void onUpdate() {
                getGameObject().setLocalPosition(new Position(110, 0));
                getGameObject().setRotation(20);
            }
        };
    }
}
