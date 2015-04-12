package edu.gatech.cs2340.trydent.sample.breakout;

import javafx.scene.paint.Color;
import edu.gatech.cs2340.trydent.Behavior;
import edu.gatech.cs2340.trydent.GameObject;
import edu.gatech.cs2340.trydent.math.geom.Rectangle;

public class Brick extends Behavior {

    private static final Color[] COLORS = {
        Color.RED, Color.MAROON, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.VIOLET
    };

    private int hitpoints;
    private int maxHP;

    public Brick(GameObject object, int hitpoints) {
        super(object);
        this.hitpoints = hitpoints;
        this.maxHP = hitpoints;
    }

    public Rectangle getBounds() {
        Rectangle r = getGameObject().getFeature(Rectangle.class);
        r.setTopLeft(getGameObject().getPosition());
        return r;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public int getMaxHitpoints() {
        return maxHP;
    }

    public void hit() {
        hitpoints--;
    }

    @Override
    public void onUpdate() {
        if (hitpoints <= 0) {
            getGameObject().setFill(Color.BLACK);
            getGameObject().destroy();
        } else {
            int c = (hitpoints < 0 ? 0 : hitpoints) % COLORS.length;
            getGameObject().setFill(COLORS[c]);
        }
    }

}
