package edu.gatech.cs2340.trydent.internal;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * A static class for ease of using mouse events.
 *
 */
public class MouseImpl {
    private static double x;
    private static double y;

    private static Set<MouseButton> down = new HashSet<>();
    private static Set<MouseButton> downBuffer = new HashSet<>();
    private static Set<MouseButton> pressed = new HashSet<>();
    private static Set<MouseButton> pressedBuffer = new HashSet<>();
    private static Set<MouseButton> released = new HashSet<>();
    private static Set<MouseButton> releasedBuffer = new HashSet<>();

    private static final Object POSITION_LOCK = new Object();
    private static final Object UPDATE_LOCK = new Object();

    /**
     * Gets the absolute position of the mouse with left as zero.
     * @return the x position
     */
    public static double getMouseX() {
        synchronized(POSITION_LOCK) {
            return x;
        }
    }

    /**
     * Gets the absolute position of the mouse with top as zero.
     * @return the y position
     */
    public static double getMouseY() {
        synchronized(POSITION_LOCK) {
            return y;
        }
    }

    /**
     * Determines if a mouse button is down.
     * @param button which button to check
     * @return whether the button is currently down
     */
    public static boolean isMouseDown(MouseButton button) {
        return down.contains(button);
    }

    /**
     * Determines if a mouse button is pressed.
     * @param button which button to check
     * @return whether the button is down starting at this frame
     */
    public static boolean isMouseDownOnce(MouseButton button) {
        return pressed.contains(button);
    }

    /**
     * Determines if a mouse button is released.
     * @param button which button to check
     * @return whether the button is up starting at this frame
     */
    public static boolean isMouseUpOnce(MouseButton button) {
        return released.contains(button);
    }

    static void newFrame() {
        synchronized(UPDATE_LOCK) {
            down.clear();
            down.addAll(downBuffer);
            pressed.clear();
            pressed.addAll(pressedBuffer);
            released.clear();
            released.addAll(releasedBuffer);

            pressedBuffer.clear();
            releasedBuffer.clear();
        }
    }

    static void pressed(MouseEvent event) {
        synchronized(POSITION_LOCK) {
            x = event.getSceneX();
            y = event.getSceneY();
        }
        synchronized(UPDATE_LOCK) {
            downBuffer.add(event.getButton());
            pressedBuffer.add(event.getButton());
        }
    }

    static void released(MouseEvent event) {
        synchronized(POSITION_LOCK) {
            x = event.getSceneX();
            y = event.getSceneY();
        }
        synchronized(UPDATE_LOCK) {
            downBuffer.remove(event.getButton());
            releasedBuffer.add(event.getButton());
        }
    }

    static void moved(MouseEvent event) {
        synchronized(POSITION_LOCK) {
            x = event.getSceneX();
            y = event.getSceneY();
        }
    }

    static void dragged(MouseEvent event) {
        synchronized(POSITION_LOCK) {
            x = event.getSceneX();
            y = event.getSceneY();
        }
    }

}
