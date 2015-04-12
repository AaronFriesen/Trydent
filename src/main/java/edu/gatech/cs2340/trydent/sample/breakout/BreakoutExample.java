package edu.gatech.cs2340.trydent.sample.breakout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import edu.gatech.cs2340.trydent.Behavior;
import edu.gatech.cs2340.trydent.ContinuousEvent;
import edu.gatech.cs2340.trydent.GameObject;
import edu.gatech.cs2340.trydent.Keyboard;
import edu.gatech.cs2340.trydent.Time;
import edu.gatech.cs2340.trydent.TrydentEngine;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Vector;
import edu.gatech.cs2340.trydent.math.geom.Rectangle;

/**
 * Main driver class for the Breakout example.
 */
public class BreakoutExample extends ContinuousEvent {

    private static final int RESOLUTION = 2;
    private static final int WIDTH = 240 * RESOLUTION;
    private static final int HEIGHT = 160 * RESOLUTION;

    public static void main(String[] args) {
        TrydentEngine.setWindowSize(WIDTH, HEIGHT);
        TrydentEngine.start();

        new BreakoutExample();
    }

    private int level = 1;
    private int blockWidth = 16;
    private int blockHeight = 8;

    private List<Brick> bricks;

    private GameObject paddle;
    private GameObject ball;

    public BreakoutExample() {
    }

    @Override
    public void onStart() {
        TrydentEngine.setBackgroundColor(Color.BLACK);

        bricks = new ArrayList<>();

        initBall();
        initPaddle();
        initBricks();

    }

    private void reset() {
        for (Brick b : bricks) {
            b.getGameObject().destroy();
        }
        paddle.destroy();
        ball.destroy();

        initBall();
        initPaddle();
        initBricks();
    }

    @Override
    public void onUpdate() {
        if (Keyboard.isKeyDownOnce(KeyCode.ESCAPE)) {
            reset();
            return;
        }

        double speed = WIDTH / 2;
        double delta = speed * Time.getTimePassed();

        if (Keyboard.isKeyDown(KeyCode.LEFT) || Keyboard.isKeyDown(KeyCode.A)) {
            paddle.translate(-delta, 0);
        } else if (Keyboard.isKeyDown(KeyCode.RIGHT) || Keyboard.isKeyDown(KeyCode.D)) {
            paddle.translate(+delta, 0);
        }

        double pwidth = blockWidth * RESOLUTION * 2;

        Position p = paddle.getPosition();
        p.setX(Math.min(Math.max(p.getX(), pwidth / 2), WIDTH - pwidth / 2));
        paddle.setPosition(p);
    }

    private void initBall() {
        Rectangle rect = new Rectangle(0, 0, blockHeight * 0.75 * RESOLUTION, blockHeight * 0.75 * RESOLUTION);
        rect.setCenter(new Position(0, 0));
        ball = new GameObject(rect);
        ball.setFill(Color.WHITE);
        ball.setPosition(new Position(WIDTH / 2, HEIGHT * 0.8));
        ball.addFeature(rect);

        new Behavior(ball) {

            private Vector velocity = new Vector(0, 100);

            private Position lastPaddle = null;

            @Override
            public void onUpdate() {
                GameObject g = this.getGameObject();

                if (g == null || g.isDestroyed()) {
                    this.stop();
                    return;
                }

                Position paddlePos = paddle.getPosition().copy();
                Vector paddleVelocity = new Vector(0, 0);
                if (lastPaddle != null) {
                    double dt = Time.getTimePassed();
                    if (dt < 0 || dt > 0.1)
                        dt = 0.1;
                    paddleVelocity = paddlePos.copy().subtract(lastPaddle).scale(10).toVector();
                    paddleVelocity.setY(0);
                }
                lastPaddle = paddlePos;

                Vector delta = velocity.copy().scale(Time.getTimePassed());
                g.translate(delta);

                Rectangle bounds = g.getFeature(Rectangle.class);
                bounds.setCenter(ball.getPosition());

                Rectangle paddleBounds = paddle.getFeature(Rectangle.class);
                paddleBounds.setCenter(paddle.getPosition());

                if (bounds.intersects(paddleBounds)) {
                    velocity.setY(-Math.abs(velocity.getY()));
                    velocity.add(paddleVelocity);
                    velocity.add(bounds.getCenter().subtract(paddleBounds.getCenter()).projectOffAxis(Vector.AXIS_Y));
                    return;
                }

                if (bounds.getLeft() < 0) {
                    velocity.setX(Math.abs(velocity.getX()));
                }

                if (bounds.getRight() > WIDTH) {
                    velocity.setX(-Math.abs(velocity.getX()));
                }

                if (bounds.getTop() < 0) {
                    velocity.setY(Math.abs(velocity.getY()));
                }

                Set<Brick> deadbricks = new HashSet<>();

                for (Brick brick : bricks) {
                    if (brick.getGameObject().isDestroyed()) {
                        deadbricks.add(brick);
                        continue;
                    }

                    if (bounds.intersects(brick.getBounds())) {
                        brick.hit();
                        velocity.scale(-1);
                    }
                }

                bricks.removeAll(deadbricks);
            }

        };
    }

    private void initPaddle() {
        Rectangle rect = new Rectangle(0, 0, blockWidth * RESOLUTION * 2, blockHeight * RESOLUTION / 2);
        rect.setCenter(new Position(0, 0));
        paddle = new GameObject(rect);
        paddle.setFill(Color.WHITE);
        paddle.setPosition(new Position(WIDTH / 2, HEIGHT * 0.9));
        paddle.addFeature(rect);
    }

    private void initBricks() {
        bricks.clear();

        int pad = 2;
        int rows = 4;
        int cols = (WIDTH / RESOLUTION / (blockWidth + pad));
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = pad * 2 + (blockWidth + pad) * col;
                double y = pad * 2 + (blockHeight + pad) * row;
                double w = blockWidth;
                double h = blockHeight;

                x *= RESOLUTION;
                y *= RESOLUTION;
                w *= RESOLUTION;
                h *= RESOLUTION;

                Rectangle rect = new Rectangle(0, 0, w, h);
                GameObject g = new GameObject(rect);
                g.setPosition(new Position(x, y));
                g.addFeature(rect);
                bricks.add(new Brick(g, level));
            }
        }
    }

}
