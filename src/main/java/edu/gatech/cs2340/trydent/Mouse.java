package edu.gatech.cs2340.trydent;

import javafx.scene.input.MouseButton;
import edu.gatech.cs2340.trydent.internal.MouseImpl;

/**
 * A static class for ease of using mouse events.
 *
 */
public class Mouse {
    /**
     * Gets the absolute position of the mouse with left as zero.
     * @return the x position
     */
    public static double getMouseX() {
        return MouseImpl.getMouseX();
    }

    /**
     * Gets the absolute position of the mouse with top as zero.
     * @return the y position
     */
    public static double getMouseY() {
        return MouseImpl.getMouseY();
    }

    /**
     * Determines if a mouse button is down.
     * @param button which button to check
     * @return whether the button is currently down
     */
    public static boolean isMouseDown(MouseButton button) {
        return MouseImpl.isMouseDown(button);
    }

    /**
     * Determines if a mouse button is pressed.
     * @param button which button to check
     * @return whether the button is down starting at this frame
     */
    public static boolean isMouseDownOnce(MouseButton button) {
        return MouseImpl.isMouseDownOnce(button);
    }

    /**
     * Determines if a mouse button is released.
     * @param button which button to check
     * @return whether the button is up starting at this frame
     */
    public static boolean isMouseUpOnce(MouseButton button) {
        return MouseImpl.isMouseUpOnce(button);
    }

}
