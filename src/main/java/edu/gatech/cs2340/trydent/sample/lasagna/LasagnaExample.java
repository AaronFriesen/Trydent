package edu.gatech.cs2340.trydent.sample.lasagna;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import edu.gatech.cs2340.trydent.Audio;
import edu.gatech.cs2340.trydent.ContinuousEvent;
import edu.gatech.cs2340.trydent.GameObject;
import edu.gatech.cs2340.trydent.Keyboard;
import edu.gatech.cs2340.trydent.Time;
import edu.gatech.cs2340.trydent.TrydentEngine;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Vector;

/**
 * A lasagna game.
 *
 */
public class LasagnaExample extends ContinuousEvent {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int CELLS = 30;
    private static final int BW = WIDTH / CELLS;
    private static final int BH = HEIGHT / CELLS;
    private static final String BG_MUSIC = "edu/gatech/cs2340/trydent/sample/song.mp3";
    private static final double INITIAL_VELOCITY_FACTOR = 2.5;
    private static final int SNAKE_SIZE = 25;

    private GameObject player;
    private List<GameObject> doges1;
    private List<GameObject> doges2;
    private List<GameObject> snake;
    private List<GameObject> paddles;
    private double startTime;
    private double time1 = 14;
    private double time2 = 28;
    private double time3 = 42;
    private double time4 = 56;
    private double velocityFactor;

    private boolean dead;
    private boolean started;

    public static void main(String[] args) {
        TrydentEngine.setWindowSize(WIDTH, HEIGHT);
        TrydentEngine.setWindowTitle("Lasagna Game (Instructions: arrow keys, space, r)");
        TrydentEngine.start();

        new LasagnaExample();
    }

    Label l;
    @Override
    public void onStart() {
        TrydentEngine.setBackgroundColor(Color.BLACK);
        doges1 = new ArrayList<>();
        doges2 = new ArrayList<>();
        snake = new ArrayList<>();
        paddles = new ArrayList<>();
        restart();
    }

    private void restart() {
        doges1.forEach(doge -> doge.destroy());
        doges1.clear();
        doges2.forEach(doge -> doge.destroy());
        doges2.clear();
        snake.forEach(part -> part.destroy());
        snake.clear();
        paddles.forEach(paddle -> paddle.destroy());
        paddles.clear();

        if(player != null) {
            player.destroy();
        }

        dead = false;
        started = false;
        velocityFactor = INITIAL_VELOCITY_FACTOR;

        player = new GameObject(new Rectangle(BW, BH, Color.GRAY));
        player.setLocalPosition(new Position((WIDTH - BW) / 2, (HEIGHT - BH) / 2));

        initDoges(player, doges1, 0, 4, Color.RED);
    }

    private void start() {
        startTime = Time.getRealTimeSinceStartup();
        Audio.resumeAudio();
        Audio.setMasterVolume(0.15);
        Audio.setMusic(getClass().getClassLoader().getResource(BG_MUSIC).toString());
        started = true;
    }

    private void die() {
        dead = true;
        Audio.pauseAudio();
        player.setFill(Color.WHITE);
    }

    @Override
    public void onUpdate() {
        if(dead) {
            if(Keyboard.isKeyDown(KeyCode.R)) {
                restart();
            }
            return;
        } else if(!started) {
            if(Keyboard.isKeyDown(KeyCode.SPACE)) {
                start();
            }
            return;
        }
        keyboardInput();

        if(Time.getRealTimeSinceStartup() - startTime > time1 && doges2.size() == 0) {
            initDoges(player, doges2, Math.PI/8, 8, Color.BLUE);
        } else if(Time.getRealTimeSinceStartup() - startTime > time2 && snake.size() == 0) {
            initSnake(snake);
        } else if(Time.getRealTimeSinceStartup() - startTime > time3 && paddles.size() == 0) {
            initPaddles(player, paddles);
        } else if(Time.getRealTimeSinceStartup() - startTime > time4 && INITIAL_VELOCITY_FACTOR == velocityFactor) {
            velocityFactor *= 2;
        }

        dogeAI(player, doges1, velocityFactor);
        dogeAI(player, doges2, velocityFactor);
        snakeAI(player, snake);
        paddleAI(player, paddles);
        for(GameObject doge : doges1) {
            if(intersectBlock(player.getPosition(), doge.getPosition())) {
                die();
            }
        }
        for(GameObject doge : doges2) {
            if(intersectBlock(player.getPosition(), doge.getPosition())) {
                die();
            }
        }
        for(GameObject part : snake) {
            if(intersectBlock(player.getPosition(), part.getPosition())) {
                die();
            }
        }
        if(paddleIntersect(paddles)) {
            die();
        }
    }

    private void keyboardInput() {
        if(Keyboard.isKeyDown(KeyCode.UP)) {
            player.translate(0, -1);
        }
        if(Keyboard.isKeyDown(KeyCode.DOWN)) {
            player.translate(0, 1);
        }
        if(Keyboard.isKeyDown(KeyCode.LEFT)) {
            player.translate(-1, 0);
        }
        if(Keyboard.isKeyDown(KeyCode.RIGHT)) {
            player.translate(1, 0);
        }
        clampGameObject(player);
    }

    private static void initPaddles(GameObject player, List<GameObject> paddles) {
        paddles.add(new GameObject(new Rectangle(BW, 5 * BH, Color.GOLD)));
        paddles.add(new GameObject(new Rectangle(5 * BW, BH, Color.GOLD)));
        paddles.add(new GameObject(new Rectangle(BW, 5 * BH, Color.GOLD)));
        paddles.add(new GameObject(new Rectangle(5 * BW, BH, Color.GOLD)));
        paddleAI(player, paddles);
    }

    private static void initSnake(List<GameObject> snake) {
        for(int i = 0; i < SNAKE_SIZE; i++) {
            Color color = i == 0 ? Color.LIGHTGREEN : Color.DARKGREEN;
            GameObject part = new GameObject(new Rectangle(BW, BH, color));
            part.setPosition(new Position((WIDTH - BW) / 2, -BW * (i + 2)));
            snake.add(part);
        }
    }

    private static void initDoges(GameObject player, List<GameObject> doges,
            double initialTheta, int num, Color color) {
        double radius = num * Math.sqrt(BW * BW + BH * BH);
        Position center = player.getPosition();
        for(int i = 0; i < num; i++) {
            double theta = i * 2.0 * Math.PI / num + initialTheta;
            GameObject doge = new GameObject(new Rectangle(BW, BH, color));
            doge.setPosition(new Position(
                    center.getX() - radius * Math.cos(theta),
                    center.getY() - radius * Math.sin(theta)));
            doge.addFeature(new Vector(Math.cos(theta), Math.sin(theta)).rotate90());
            doges.add(doge);
        }
    }

    private static void paddleAI(GameObject player, List<GameObject> paddles) {
        if(!paddles.isEmpty()) {
            Position center = player.getPosition();
            paddles.get(0).setPosition(new Position(WIDTH - 3 * BW, center.getY() - 2 * BH));
            paddles.get(1).setPosition(new Position(center.getX() - 2 * BW, 2 * BW));
            paddles.get(2).setPosition(new Position(2 * BW, center.getY() - 2 * BH));
            paddles.get(3).setPosition(new Position(center.getX() - 2 * BW, HEIGHT - 3 * BW));
        }
    }

    private boolean paddleIntersect(List<GameObject> paddles) {
        if(paddles.isEmpty()) return false;
        Position playerPosition = player.getPosition();
        return intersect(playerPosition, paddles.get(0).getPosition(), BW, 5 * BH)
                || intersect(playerPosition, paddles.get(1).getPosition(), 5 * BW, BH)
                || intersect(playerPosition, paddles.get(2).getPosition(), BW, 5 * BH)
                || intersect(playerPosition, paddles.get(3).getPosition(), 5 * BW, BH);
    }

    private static void snakeAI(GameObject player, List<GameObject> snake) {
        double snakeVelocity = 1.0;
        if(!snake.isEmpty()) {
            GameObject head = snake.get(0);
            Vector toPlayer = new Vector(head.getPosition(), player.getPosition());
            toPlayer.scale(snakeVelocity/toPlayer.magnitude());
            head.translate(toPlayer);
            for(int i = 1; i < snake.size(); i++) {
                GameObject prev = snake.get(i - 1);
                GameObject curr = snake.get(i);
                Vector toPrev = new Vector(curr.getPosition(), prev.getPosition());
                toPrev.scale(snakeVelocity / toPrev.magnitude() / Math.sqrt(Math.sqrt(i)));
                curr.translate(toPrev);
            }
        }
    }

    private static void dogeAI(GameObject player, List<GameObject> doges, double velocityFactor) {
        double weightGoal = 1.0 / 50.0;
        double weightCurrent = 1.0 / 1.0;
        double weightOther = 1.0 / 1.0 / doges.size();
        double velocity = velocityFactor / doges.size();
        Position playerPosition = player.getPosition();
        for(int i = 0; i < doges.size(); i++) {
            GameObject doge = doges.get(i);
            Position dogePosition = doge.getPosition();
            Vector dogeVelocity = doge.getFeature(Vector.class);
            dogeVelocity.scale(weightCurrent / dogeVelocity.magnitude());

            Vector toPlayer = new Vector(dogePosition, playerPosition);
            toPlayer.scale(weightGoal / toPlayer.magnitude());
            dogeVelocity.add(toPlayer);
            for(int j = 0; j < doges.size(); j++) {
                if(i != j) {
                    GameObject other = doges.get(j);
                    Vector fromOther = new Vector(other.getPosition(), dogePosition);
                    fromOther.scale(weightOther / fromOther.magnitudeSquared());
                    dogeVelocity.add(fromOther);
                }
            }
            dogeVelocity.scale(velocity / dogeVelocity.magnitude());
            doge.translate(dogeVelocity);
        }
    }

    private static boolean intersectBlock(Position a, Position b) {
        return intersect(a, b, BW, BH);
    }

    private static boolean intersect(Position a, Position b, double width, double height) {
        return Math.min(a.getX() + BW, b.getX() + width) > Math.max(a.getX(), b.getX())
                && Math.min(a.getY() + BH, b.getY() + height) > Math.max(a.getY(), b.getY());
    }

    private static void clampGameObject(GameObject obj) {
        Position objPosition = obj.getPosition();
        if(objPosition.getX() < 0) {
            objPosition.setX(0);
        } else if(objPosition.getX() > WIDTH - BW) {
            objPosition.setX(WIDTH - BW);
        }
        if(objPosition.getY() < 0) {
            objPosition.setY(0);
        } else if(objPosition.getY() > HEIGHT - BH) {
            objPosition.setY(HEIGHT - BH);
        }
        obj.setPosition(objPosition);
    }

}
